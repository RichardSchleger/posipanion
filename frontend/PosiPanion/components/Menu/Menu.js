import React, {useEffect, useRef, useState} from 'react';

import {FontAwesomeIcon} from '@fortawesome/react-native-fontawesome';
import {
  faCog,
  faUserFriends,
  faDoorOpen,
} from '@fortawesome/free-solid-svg-icons';

import {Animated, Dimensions, Pressable, StyleSheet, Text} from 'react-native';
import AuthService from '../AuthService/AuthService';
import MapMenu from './MapMenu';
import RideMenu from './RideMenu';

export default function Menu({show, setRefresh, friends, showUserDetail}) {
  const offsetY = useRef(new Animated.Value(0)).current;
  const [mapMenuShown, setMapMenuShown] = useState(true);
  const [newRide, setNewRide] = useState(true);

  useEffect(() => {
    if (show) {
      if (mapMenuShown) {
        slideIntoMapMenuView();
      } else if (newRide) {
        slideIntoNewRideMenuView();
      } else if (!newRide) {
        slideIntoMapMenuView();
      }
    } else {
      slideOutOfView();
    }
  }, [offsetY, show, newRide, mapMenuShown]);

  const slideIntoMapMenuView = () => {
    Animated.timing(offsetY, {
      toValue: -((Dimensions.get('window').height / 3) * 2 - 50),
      duration: 500,
      useNativeDriver: true,
    }).start();
  };

  const slideIntoNewRideMenuView = () => {
    Animated.timing(offsetY, {
      toValue: -(Dimensions.get('window').height / 3 - 50),
      duration: 500,
      useNativeDriver: true,
    }).start();
  };

  const slideOutOfView = () => {
    Animated.timing(offsetY, {
      toValue: 0,
      duration: 500,
      useNativeDriver: true,
    }).start();
  };

  const showMapMenu = e => {
    e.preventDefault();
    setMapMenuShown(true);
    slideIntoMapMenuView();
  };

  const showRideMenu = e => {
    e.preventDefault();
    setMapMenuShown(false);
    if (newRide) {
      slideIntoNewRideMenuView();
    } else if (!newRide) {
      slideIntoMapMenuView();
    }
  };

  const handleLogout = async e => {
    e.preventDefault();
    AuthService.logout(setRefresh);
  };

  const button_config = {
    position: 'absolute',
    width: Dimensions.get('window').width / 7,
    height: Dimensions.get('window').width / 7,
    left: '5%',
    top: mapMenuShown ? '84%' : newRide ? '30%' : '84%',
  };

  const button_friends = {
    position: 'absolute',
    width: Dimensions.get('window').width / 7,
    height: Dimensions.get('window').width / 7,
    left: '25%',
    top: mapMenuShown ? '84%' : newRide ? '30%' : '84%',
  };

  const button_logout = {
    position: 'absolute',
    width: Dimensions.get('window').width / 7,
    height: Dimensions.get('window').width / 7,
    left: '45%',
    top: mapMenuShown ? '84%' : newRide ? '30%' : '84%',
  };

  return (
    <Animated.View style={[styles.menu, {transform: [{translateY: offsetY}]}]}>
      <Pressable
        style={[
          styles.button,
          styles.button_map,
          mapMenuShown ? styles.button_active : '',
        ]}
        onPress={showMapMenu}>
        <Text
          style={[
            styles.button_text,
            mapMenuShown ? styles.button_text_active : '',
          ]}>
          MAPA
        </Text>
      </Pressable>
      <Pressable
        style={[
          styles.button,
          styles.button_ride,
          mapMenuShown ? '' : styles.button_active,
        ]}
        onPress={showRideMenu}>
        <Text
          style={[
            styles.button_text,
            mapMenuShown ? '' : styles.button_text_active,
          ]}>
          JAZDA
        </Text>
      </Pressable>
      {mapMenuShown && (
        <MapMenu friends={friends} showUserDetail={showUserDetail} />
      )}
      {!mapMenuShown && (
        <RideMenu
          newRide={newRide}
          setNewRide={setNewRide}
          slideIntoNewRideMenuView={slideIntoNewRideMenuView}
          slideIntoExisitngRideMenuView={slideIntoMapMenuView}
        />
      )}
      <Pressable style={[styles.button, button_config]}>
        <FontAwesomeIcon style={styles.icon} icon={faCog} size={40} />
      </Pressable>
      <Pressable style={[styles.button, button_friends]}>
        <FontAwesomeIcon style={styles.icon} icon={faUserFriends} size={40} />
      </Pressable>
      <Pressable style={[styles.button, button_logout]} onPress={handleLogout}>
        <FontAwesomeIcon style={styles.icon} icon={faDoorOpen} size={40} />
      </Pressable>
    </Animated.View>
  );
}

const styles = StyleSheet.create({
  menu: {
    position: 'absolute',
    bottom: -(Dimensions.get('window').height / 3) * 2,
    width: Dimensions.get('window').width,
    height: (Dimensions.get('window').height / 3) * 2,
    borderRadius: Dimensions.get('window').width / 10,
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 5,
    backgroundColor: '#FFFFFF',
    zIndex: 100,
    display: 'flex',
    paddingTop: 30,
    paddingLeft: 10,
    paddingRight: 10,
  },

  button: {
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 3,
    backgroundColor: '#FFFFFF',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 50,
  },

  button_map: {
    width: '45%',
    height: '10%',
    position: 'absolute',
    left: '5%',
    top: '4%',
  },

  button_ride: {
    width: '45%',
    height: '10%',
    position: 'absolute',
    left: '55%',
    top: '4%',
  },

  icon: {
    color: '#109CF1',
    width: '',
  },

  button_active: {
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 3,
    backgroundColor: '#109CF1',
  },

  button_text: {
    fontSize: 20,
    color: '#109CF1',
  },

  button_text_active: {
    color: '#FFFFFF',
  },
});
