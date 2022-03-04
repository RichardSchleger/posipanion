import React, {useEffect, useState} from 'react';

import {AppRegistry, StatusBar, StyleSheet, View} from 'react-native';
import {useBackHandler} from '@react-native-community/hooks';

import API from '../Api/API';
import MenuButton from '../MenuButton/MenuButton';
import Menu from '../Menu/Menu';
import Map from './Map';
import axios from 'axios';
import AuthService from '../AuthService/AuthService';

export default function MapContainer({refresh, setRefresh}) {
  // const [state, dispatch] = useTimingReducer();
  //
  // useLocation(state, dispatch);

  const [friends, setFriends] = useState([]);
  const [showMenu, setShowMenu] = useState(false);
  const [detail, setDetail] = useState(null);

  useEffect(() => {
    const getData = async () => {
      const response = await fetchData();
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

  const fetchData = async () => {
    const token = await AuthService.getToken();
    return await axios
      .get(API.url + 'user/friends/active', {
        headers: {Authorization: 'Bearer ' + token},
      })
      .then(response => response.data);
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
        .catch(error => console.log(error));
    }
  };

  return (
    <View style={styles.container}>
      <StatusBar barStyle="dark-content" />
      <Map users={friends} detail={detail} showUserDetail={showUserDetail} />
      <MenuButton onPress={onPress} />
      <Menu
        show={showMenu}
        refresh={refresh}
        setRefresh={setRefresh}
        friends={friends}
        showUserDetail={showUserDetail}
      />
    </View>
    // <View style={styles.container}>
    //   <MapView
    //     provider={PROVIDER_GOOGLE} // remove if not using Google Maps
    //     style={styles.map}
    //     region={{
    //       latitude: 37.78825,
    //       longitude: -122.4324,
    //       latitudeDelta: 0.015,
    //       longitudeDelta: 0.0121,
    //     }}></MapView>
    // </View>
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

AppRegistry.registerComponent('PosiPanion', () => MapContainer);
