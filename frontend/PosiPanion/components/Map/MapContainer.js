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

  const [locationCache, setLocationCache] = useState([]);

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
    console.log(rideActive);
  }, [rideActive]);

  useEffect(() => {
    const sendLocation = async () => {
      const token = await AuthService.getToken();

      axios
        .post(
          API.url + 'user/location',
          {
            latitude: positionState.position.lat,
            longitude: positionState.position.lng,
            elevation: positionState.position.alti,
            timestamp: new Date(),
          },
          {
            headers: {Authorization: 'Bearer ' + token},
          },
        )
        .then(() => {
          setRideActive(c => {
            const temp = {...c};
            temp.currentRide.waypoints.push({
              latitude: positionState.position.lat,
              longitude: positionState.position.lng,
            });
            return temp;
          });
        })
        .catch(async error => {
          if (error.response.status === 606) {
            if (await AuthService.refreshToken()) {
              return sendLocation();
            }
          } else {
            setLocationCache(c => {
              const temp = {...c};
              temp.push({
                latitude: positionState.position.lat,
                longitude: positionState.position.lng,
              });
              return temp;
            });
          }
        });
    };

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
      setRideActive(currentRide);
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
