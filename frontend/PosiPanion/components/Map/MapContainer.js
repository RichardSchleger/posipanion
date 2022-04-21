import React, {useEffect, useState} from 'react';

import {Platform, StatusBar, StyleSheet, View} from 'react-native';
import {useBackHandler} from '@react-native-community/hooks';

import API from '../Api/API';
import MenuButton from '../MenuButton/MenuButton';
import Menu from '../Menu/Menu';
import Map from './Map';
import axios from 'axios';
import AuthService from '../AuthService/AuthService';
import {useTimingReducer} from './reducer';
import {
  useLocation,
  useLocationTracking,
  useNativeLocationTracking,
} from '../hooks';
import DistanceCalculator from '../DistanceCalculator/DistanceCalculator';
import CenterButton from '../CenterButton/CenterButton';

export default function MapContainer({setRefresh}) {
  const [positionState, dispatch] = useTimingReducer();

  const [menuShown, setMenuShown] = useState('map');
  const [locationCache, setLocationCache] = useState([]);

  const [friends, setFriends] = useState([]);
  const [showMenu, setShowMenu] = useState(false);
  const [detail, setDetail] = useState(null);
  const [rideActive, setRideActive] = useState(null);
  const [shownRideActive, setShownRideActive] = useState(null);

  const [mapRefresh, setMapRefresh] = useState(false);
  const [refreshInterval, setRefreshInterval] = useState(null);

  const [firstRun, setFirstRun] = useState(true);

  const mapview = React.createRef();

  useLocation(positionState, dispatch);

  if (Platform.OS === 'ios') {
    useLocationTracking(positionState, dispatch);
  } else {
    useNativeLocationTracking(positionState, dispatch);
  }

  const lastTimestamp = React.useRef(0);

  useEffect(() => {
    setFirstRun(true);
  },[menuShown]);

  useEffect(() => {
    if (rideActive && positionState) {
      if(positionState.position.timestamp - lastTimestamp.current > 1000) {
        sendLocation();
        lastTimestamp.current = positionState.position.timestamp;
      }
    }
  }, [positionState]);

  useEffect(() => {
    const fetchData = async () => {
      const token = await AuthService.getToken();
      return axios
        .get(API.url + 'user/friends/active', {
          headers: {Authorization: 'Bearer ' + token},
        })
        .then(response => response.data)
        .catch(async error => {
          if (
            error &&
            error.response &&
            error.response.status &&
            error.response.status === 606
          ) {
            if (await AuthService.refreshToken()) {
              return fetchData();
            }
          }
        });
    };

    const fetchDetail = async id => {
      const token = await AuthService.getToken();
      return axios
        .get(API.url + 'user/detail/' + id, {
          headers: {Authorization: 'Bearer ' + token},
        })
        .then(response => response.data)
        .catch(async error => {
          if (
            error &&
            error.response &&
            error.response.status &&
            error.response.status === 606
          ) {
            if (await AuthService.refreshToken()) {
              return fetchDetail(id);
            }
          }
        });
    };

    const getData = async () => {
      const response = await fetchData();
      if (response) {
        setFriends(
          response.map(user => {
            return {
              id: user.id,
              firstName: user.firstName,
              surname: user.surname,
              lat: user.lastKnownLatitude,
              lng: user.lastKnownLongitude,
            };
          }),
        );
      }

      if (detail) {
        const fetchedDetail = await fetchDetail(detail.id);
        if (fetchedDetail) {
          setDetail(fetchedDetail);
        } else {
          setDetail(null);
        }
      }
    };
    getData();
  }, [mapRefresh]);

  useEffect(() => {
    const fetchCurrentRide = async () => {
      const token = await AuthService.getToken();
      return axios
        .get(API.url + 'user/detail', {
          headers: {Authorization: 'Bearer ' + token},
        })
        .then(response => response.data)
        .catch(async error => {
          if (
            error &&
            error.response &&
            error.response.status &&
            error.response.status === 606
          ) {
            if (await AuthService.refreshToken()) {
              return fetchCurrentRide();
            }
          }
        });
    };

    const getData = async () => {
      const currentRide = await fetchCurrentRide();
      if (currentRide) {
        currentRide.currentRide.waypoints =
          currentRide.currentRide.waypoints.sort(
            (a, b) => a.timestamp - b.timestamp,
          );
        setRideActive(currentRide);
        setMenuShown('activeRide');
        setShowMenu(true);
      }
    };
    getData();
  }, []);

  useEffect(() => {
    if (refreshInterval) {
      clearInterval(refreshInterval);
    }
    setRefreshInterval(
      setInterval(() => {
        setMapRefresh(c => !c);
      }, 10000),
    );
  }, []);

  const sendLocation = async location => {
    if (rideActive) {
      const token = await AuthService.getToken();

      let payload;
      if (location) {
        payload = {
          latitude: location.latitude,
          longitude: location.longitude,
          elevation: location.elevation,
          timestamp: location.timestamp,
        };
      } else {
        payload = {
          latitude: positionState.position.lat,
          longitude: positionState.position.lng,
          elevation: positionState.position.alti,
          timestamp: new Date(),
        };
      }
      let temp = {...rideActive};
      if (temp) {
        temp.currentRide.waypoints = [
          ...temp.currentRide.waypoints,
          {
            latitude: payload.latitude,
            longitude: payload.longitude,
            elevation: payload.elevation,
            timestamp: payload.timestamp,
          },
        ].sort((a, b) => a.timestamp - b.timestamp);
        temp.lastKnownLatitude =
          temp.currentRide.waypoints[
            temp.currentRide.waypoints.length - 1
          ].latitude;
        temp.lastKnownLongitude =
          temp.currentRide.waypoints[
            temp.currentRide.waypoints.length - 1
          ].longitude;
        if (temp.currentRide.waypoints.length > 1) {
          const lastPoint =
            temp.currentRide.waypoints[temp.currentRide.waypoints.length - 2];
          const newPoint =
            temp.currentRide.waypoints[temp.currentRide.waypoints.length - 1];

          const dist = DistanceCalculator.calculateDistanceBetweenLatLonEle(
            lastPoint.latitude,
            lastPoint.longitude,
            lastPoint.elevation,
            newPoint.latitude,
            newPoint.longitude,
            newPoint.elevation,
          );
          if (!isNaN(dist)) {
            temp.currentRide.distance += dist;
          }

          const time = newPoint.timestamp - lastPoint.timestamp;
          if (!isNaN(time)) {
            temp.currentRide.movingTime += time;
          }
          temp.currentRide.currentSpeed =
            time !== 0
              ? Math.round((dist / 1000 / (time / 3600000)) * 10) / 10
              : 0;
        }
      }

      setRideActive(temp);
      if (shownRideActive) {
        setShownRideActive(temp);
      } else {
        setShownRideActive(null);
      }

      axios
        .post(API.url + 'user/location', payload, {
          headers: {Authorization: 'Bearer ' + token},
        })
        .then(() => {
          if (locationCache.length > 0) {
            sendCachedLocations();
          }
        })
        .catch(async error => {
          if (
            error &&
            error.response &&
            error.response.status &&
            error.response.status === 606
          ) {
            if (await AuthService.refreshToken()) {
              return sendLocation();
            }
          } else {
            console.log('Adding location to cache');
            setLocationCache(c => {
              return [
                ...c,
                {
                  latitude: payload.latitude,
                  longitude: payload.longitude,
                  elevation: payload.elevation,
                  timestamp: payload.timestamp,
                },
              ];
            });
          }
        });
    }
  };

  const sendCachedLocations = async () => {
    console.log('Sending ' + locationCache.length + ' cached locations');
    const token = await AuthService.getToken();

    axios
      .post(API.url + 'user/location/cached', locationCache, {
        headers: {Authorization: 'Bearer ' + token},
      })
      .then(() => {
        console.log('Cached locations sent');
        setLocationCache([]);
      })
      .catch(async error => {
        console.log(error);
        if (
          error &&
          error.response &&
          error.response.status &&
          error.response.status === 606
        ) {
          if (await AuthService.refreshToken()) {
            return sendCachedLocations();
          }
        }
      });
  };

  const onPress = e => {
    e.preventDefault();
    setShowMenu(true);
  };

  useBackHandler(() => {
    if (showMenu) {
      setShowMenu(false);
      return true;
    } else if (detail) {
      setDetail(null);
      return true;
    }
    return false;
  });

  const showUserDetail = async (e, id) => {
    e.preventDefault();
    if (id) {
      setShowMenu(false);
      const token = await AuthService.getToken();
      axios
        .get(API.url + 'user/detail/' + id, {
          headers: {Authorization: 'Bearer ' + token},
        })
        .then(response => {
          setDetail(response.data);
          setShownRideActive(null);
          setFirstRun(true);
        })
        .catch(async error => {
          if (
            error &&
            error.response &&
            error.response.status &&
            error.response.status === 606
          ) {
            if (await AuthService.refreshToken()) {
              return showUserDetail(e, id);
            }
          }
        });
    }
  };

  const centerMap = e => {
    e.preventDefault();
    if (mapview && mapview.current) {
      mapview.current.fitToElements(true);
    }
  };

  return (
    <View style={styles.container}>
      <Map
        users={friends}
        detail={detail}
        showUserDetail={showUserDetail}
        rideActive={shownRideActive}
        positionState={positionState}
        shown={showMenu}
        menuShown={menuShown}
        mapview={mapview}
        firstRun={firstRun}
        setFirstRun={setFirstRun}
      />
      <MenuButton onPress={onPress} />
      <CenterButton
        onPress={centerMap}
        shown={showMenu}
        menuShown={menuShown}
      />
      <Menu
        show={showMenu}
        setRefresh={setRefresh}
        friends={friends}
        showUserDetail={showUserDetail}
        rideActive={rideActive}
        setRideActive={setRideActive}
        setShownRideActive={setShownRideActive}
        setFirstRun={setFirstRun}
        positionState={positionState}
        menuShown={menuShown}
        setMenuShown={setMenuShown}
        mapview={mapview}
        setDetail={setDetail}
      />
    </View>
  );
}
const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
    height: '100%',
  },
  map: {
    ...StyleSheet.absoluteFillObject,
  },
});
