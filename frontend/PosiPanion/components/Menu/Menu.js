import React, {useEffect, useRef, useState} from 'react';

import {FontAwesomeIcon} from '@fortawesome/react-native-fontawesome';
import {faCog, faUserFriends} from '@fortawesome/free-solid-svg-icons';

import {
  Animated,
  Dimensions,
  Pressable,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import MapMenu from './MapMenu';
import RideMenu from './RideMenu';
import ConfigMenu from './ConfigMenu';

export default function Menu({show, setRefresh, friends, showUserDetail}) {
  const offsetY = useRef(new Animated.Value(0)).current;
  const [menuShown, setMenuShown] = useState('map');
  const [newRide, setNewRide] = useState(true);

  const [selectedTrackIndex, setSelectedTrackIndex] = useState(-1);

  useEffect(() => {
    if (show) {
      if (menuShown === 'map') {
        slideIntoMapMenuView();
      } else if (menuShown === 'ride' && newRide) {
        slideIntoNewRideMenuView();
      } else if (menuShown === 'ride' && !newRide) {
        slideIntoMapMenuView();
      } else if (menuShown === 'config') {
        slideIntoConfigMenuView();
      }
    } else {
      slideOutOfView();
    }
  }, [offsetY, show, newRide, menuShown]);

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

  const slideIntoConfigMenuView = () => {
    Animated.timing(offsetY, {
      toValue: -(Dimensions.get('window').height / 2 - 50),
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
    setMenuShown('map');
    slideIntoMapMenuView();
  };

  const showRideMenu = e => {
    e.preventDefault();
    setMenuShown('ride');
    if (newRide) {
      slideIntoNewRideMenuView();
    } else if (!newRide) {
      slideIntoMapMenuView();
    }
  };

  const showConfig = e => {
    e.preventDefault();
    setMenuShown('config');
    slideIntoConfigMenuView();
  };

  const button_config = {
    position: 'absolute',
    width: Dimensions.get('window').width / 7,
    height: Dimensions.get('window').width / 7,
    left: '5%',
    top:
      menuShown === 'map'
        ? '84%'
        : menuShown === 'ride' && newRide
        ? '30%'
        : '84%',
  };

  const button_friends = {
    position: 'absolute',
    width: Dimensions.get('window').width / 7,
    height: Dimensions.get('window').width / 7,
    left: '25%',
    top:
      menuShown === 'map'
        ? '84%'
        : menuShown === 'ride' && newRide
        ? '30%'
        : '84%',
  };

  const button_start_ride = {
    position: 'absolute',
    width: '55%',
    height: Dimensions.get('window').width / 7,
    left: '45%',
    top:
      menuShown === 'map'
        ? '84%'
        : menuShown === 'ride' && newRide
        ? '30%'
        : '84%',
  };

  return (
    <Animated.View
      style={[styles.menuContainer, {transform: [{translateY: offsetY}]}]}>
      {(menuShown === 'map' || menuShown === 'ride') && (
        <View style={styles.menu}>
          <Pressable
            style={[
              styles.button,
              styles.button_map,
              menuShown === 'map' ? styles.button_active : '',
            ]}
            onPress={showMapMenu}>
            <Text
              style={[
                styles.button_text,
                menuShown === 'map' ? styles.button_text_active : '',
              ]}>
              MAPA
            </Text>
          </Pressable>
          <Pressable
            style={[
              styles.button,
              styles.button_ride,
              menuShown === 'ride' ? styles.button_active : '',
            ]}
            onPress={showRideMenu}>
            <Text
              style={[
                styles.button_text,
                menuShown === 'ride' ? styles.button_text_active : '',
              ]}>
              JAZDA
            </Text>
          </Pressable>
          {menuShown === 'map' && (
            <MapMenu friends={friends} showUserDetail={showUserDetail} />
          )}
          {menuShown === 'ride' && (
            <RideMenu
              newRide={newRide}
              setNewRide={setNewRide}
              slideIntoNewRideMenuView={slideIntoNewRideMenuView}
              slideIntoExisitngRideMenuView={slideIntoMapMenuView}
              selectedTrackIndex={selectedTrackIndex}
              setSelectedTrackIndex={setSelectedTrackIndex}
            />
          )}
          <Pressable
            style={[styles.button, button_config]}
            onPress={showConfig}>
            <FontAwesomeIcon style={styles.icon} icon={faCog} size={40} />
          </Pressable>
          <Pressable style={[styles.button, button_friends]}>
            <FontAwesomeIcon
              style={styles.icon}
              icon={faUserFriends}
              size={40}
            />
          </Pressable>
          {menuShown === 'ride' && (
            <Pressable
              style={
                !newRide && selectedTrackIndex === -1
                  ? [styles.disabled_button, button_start_ride]
                  : [styles.button, button_start_ride]
              }
              disabled={!newRide && selectedTrackIndex === -1}>
              <Text
                style={
                  !newRide && selectedTrackIndex === -1
                    ? styles.disabled_button_text
                    : styles.button_text
                }>
                SPUSTIŤ
              </Text>
            </Pressable>
          )}
        </View>
      )}
      {menuShown === 'config' && (
        <View style={styles.menu}>
          <ConfigMenu showMapMenu={showMapMenu} setLoginRefresh={setRefresh} />
        </View>
      )}
    </Animated.View>
  );
}

const styles = StyleSheet.create({
  menuContainer: {
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
  },

  menu: {
    height: '100%',
    width: '100%',
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

  disabled_button: {
    borderColor: '#cccccc',
    borderStyle: 'solid',
    borderWidth: 3,
    backgroundColor: '#f3f3f3',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 50,
    color: 'white',
  },

  disabled_button_text: {
    fontSize: 20,
    color: '#cccccc',
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
