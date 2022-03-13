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

export default function MapContainer({setRefresh}) {
  const [positionState, dispatch] = useTimingReducer();

  const [menuShown, setMenuShown] = useState('map');
  const [locationCache, setLocationCache] = useState([]);

  const mapview = React.createRef();

  useLocation(positionState, dispatch);

  if (Platform.OS === 'ios') {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    useLocationTracking(positionState, dispatch);
  } else {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    useNativeLocationTracking(positionState, dispatch);
  }

  const [friends, setFriends] = useState([]);
  const [showMenu, setShowMenu] = useState(false);
  const [detail, setDetail] = useState(null);
  const [rideActive, setRideActive] = useState(null);

  useEffect(() => {
    if (rideActive && positionState) {
      sendLocation();
    }
  }, [positionState]);

  useEffect(() => {
    const fetchData = async () => {
      const token = await AuthService.getToken();
      return await axios
        .get(API.url + 'user/friends/active', {
          headers: {Authorization: 'Bearer ' + token},
        })
        .then(response => response.data)
        .catch(async error => {
          if (error.response.status === 606) {
            if (await AuthService.refreshToken()) {
              return fetchData();
            }
          }
        });
    };

    const fetchCurrentRide = async () => {
      const token = await AuthService.getToken();
      return await axios
        .get(API.url + 'user/detail', {
          headers: {Authorization: 'Bearer ' + token},
        })
        .then(response => response.data)
        .catch(async error => {
          if (error.response.status === 606) {
            if (await AuthService.refreshToken()) {
              return fetchCurrentRide();
            }
          }
        });
    };

    const getData = async () => {
      const response = await fetchData();
      const currentRide = await fetchCurrentRide();
      if (currentRide) {
        currentRide.currentRide.waypoints =
          currentRide.currentRide.waypoints.sort(
            (a, b) => a.timestamp - b.timestamp,
          );
        setRideActive(currentRide);
      }
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
    };
    getData();
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

      setRideActive(c => {
        let temp = {...c};
        temp.currentRide.waypoints = [
          ...temp.currentRide.waypoints,
          {
            latitude: payload.latitude,
            longitude: payload.longitude,
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
        return temp;
      });

      axios
        .post(API.url + 'user/location', payload, {
          headers: {Authorization: 'Bearer ' + token},
        })
        .then(() => {
          if (!location) {
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
            setLocationCache(c => {
              const temp = [
                ...c,
                {
                  latitude: payload.latitude,
                  longitude: payload.longitude,
                  elevation: payload.elevation,
                  timestamp: payload.timestamp,
                },
              ];
              return temp;
            });
          }
        });
    }
  };

  const sendCachedLocations = () => {
    locationCache.forEach(location => {
      sendLocation(location);
    });

    setLocationCache([]);
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
        })
        .catch(async error => {
          if (error.response.status === 606) {
            if (await AuthService.refreshToken()) {
              return showUserDetail(e, id);
            }
          }
        });
    }
  };

  return (
    <View style={styles.container}>
      <StatusBar barStyle="dark-content" />
      <Map
        users={friends}
        detail={detail}
        showUserDetail={showUserDetail}
        rideActive={rideActive}
        positionState={positionState}
        shown={showMenu}
        menuShown={menuShown}
        mapview={mapview}
      />
      <MenuButton onPress={onPress} />
      <Menu
        show={showMenu}
        setRefresh={setRefresh}
        friends={friends}
        showUserDetail={showUserDetail}
        rideActive={rideActive}
        setRideActive={setRideActive}
        positionState={positionState}
        menuShown={menuShown}
        setMenuShown={setMenuShown}
        mapview={mapview}
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
