import React, {useEffect, useRef, useState} from 'react';

import {FontAwesomeIcon} from '@fortawesome/react-native-fontawesome';
import {faCog, faUserFriends} from '@fortawesome/free-solid-svg-icons';
import Toast from 'react-native-toast-message';

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
import axios from 'axios';
import API from '../Api/API';
import AuthService from '../AuthService/AuthService';
import ActiveRideDetails from '../ActiveRideDetals/ActiveRideDetails';
import FriendsMenu from './FriendsMenu';
import DebugMenu from './DebugMenu';

export default function Menu({
  show,
  setRefresh,
  friends,
  showUserDetail,
  rideActive,
  setRideActive,
  setShownRideActive,
  setFirstRun,
  positionState,
  menuShown,
  setMenuShown,
  setDetail,
  setCode,
  setExpiresAt,
}) {
  const offsetY = useRef(new Animated.Value(0)).current;
  const [newRide, setNewRide] = useState(true);

  const [selectedTrack, setSelectedTrack] = useState(null);

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
      } else if (menuShown === 'activeRide') {
        slideIntoActiveRideMenuView();
      } else if (menuShown === 'friends') {
        slideIntoMapMenuView();
      }
    } else {
      slideOutOfView();
    }
    setSelectedTrack(null);
  }, [offsetY, show, newRide, menuShown]);

  const slideIntoMapMenuView = () => {
    Animated.timing(offsetY, {
      toValue: -530,
      duration: 500,
      useNativeDriver: true,
    }).start();
  };

  const slideIntoNewRideMenuView = () => {
    Animated.timing(offsetY, {
      toValue: -235,
      duration: 500,
      useNativeDriver: true,
    }).start();
  };

  const slideIntoConfigMenuView = () => {
    Animated.timing(offsetY, {
      toValue: -450,
      duration: 500,
      useNativeDriver: true,
    }).start();
  };

  const slideIntoActiveRideMenuView = () => {
    Animated.timing(offsetY, {
      toValue: -385,
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
    if (rideActive) {
      setMenuShown('activeRide');
      setDetail(null);
      setShownRideActive(rideActive);
      setFirstRun(true);
      slideIntoActiveRideMenuView();
    } else {
      setMenuShown('ride');
      if (newRide) {
        slideIntoNewRideMenuView();
      } else if (!newRide) {
        slideIntoMapMenuView();
      }
    }
  };

  const showConfig = e => {
    e.preventDefault();
    setMenuShown('config');
    slideIntoConfigMenuView();
  };

  const showDebug = e => {
    e.preventDefault();
    setMenuShown('debug');
    slideIntoMapMenuView();
  };

  const showFriends = e => {
    e.preventDefault();
    setMenuShown('friends');
    slideIntoMapMenuView();
  };

  const button_config = {
    position: 'absolute',
    width: Dimensions.get('window').width / 7,
    height: Dimensions.get('window').width / 7,
    left: '5%',
    top:
      menuShown === 'map'
        ? '83%'
        : menuShown === 'ride' && newRide
        ? '30%'
        : menuShown === 'activeRide'
        ? '57%'
        : '83%',
  };

  const button_friends = {
    position: 'absolute',
    width: Dimensions.get('window').width / 7,
    height: Dimensions.get('window').width / 7,
    left: '25%',
    top:
      menuShown === 'map'
        ? '83%'
        : menuShown === 'ride' && newRide
        ? '30%'
        : menuShown === 'activeRide'
        ? '57%'
        : '83%',
  };

  const button_start_ride = {
    position: 'absolute',
    width: '55%',
    height: Dimensions.get('window').width / 7,
    left: '45%',
    top:
      menuShown === 'map'
        ? '83%'
        : menuShown === 'ride' && newRide
        ? '30%'
        : menuShown === 'activeRide'
        ? '57%'
        : '83%',
  };

  const fetchProfile = async () => {
    const token = await AuthService.getToken();
    return axios
      .get(API.url + 'user/profile', {
        headers: {Authorization: 'Bearer ' + token},
      })
      .then(response => response.data)
      .catch(async error => {
        if (error.response.status === 606) {
          if (await AuthService.refreshToken()) {
            return fetchProfile();
          }
        }
      });
  };

  const handleRideStart = async e => {
    e.preventDefault();
    const token = await AuthService.getToken();
    axios
      .put(
        API.url + 'user/start' + (selectedTrack ? '/' + selectedTrack.id : ''),
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
      .then(async () => {
        const profile = await fetchProfile();
        const ride = {
          firstName: profile.firstName,
          surname: profile.surname,
          lastKnownLatitude: positionState.position.lat,
          lastKnownLongitude: positionState.position.lng,
          track: selectedTrack,
          currentRide: {
            distance: 0,
            elevation: 0,
            movingTime: 0,
            waypoints: [
              {
                latitude: positionState.position.lat,
                longitude: positionState.position.lng,
              },
            ],
          },
        };
        setRideActive(ride);
        setShownRideActive(ride);

        setMenuShown('activeRide');
      })
      .catch(async error => {
        if (error.response) {
          if (error.response.status === 606) {
            if (await AuthService.refreshToken()) {
              return handleRideStart(e);
            }
          }
        } else {
          Toast.show({
            type: 'error',
            text1: 'Nepodarilo sa spusti?? jazdu',
            text2: '??iadne pripojenie na internet',
          });
        }
        
      });
  };

  const handleRideEnd = async e => {
    e.preventDefault();
    const token = await AuthService.getToken();
    axios
      .put(
        API.url + 'user/end',
        {},
        {
          headers: {Authorization: 'Bearer ' + token},
        },
      )
      .then(() => {
        setRideActive(null);
        setShownRideActive(null);
        setMenuShown('map');
      })
      .catch(async error => {
        if (error.response) {
          if (error.response.status === 606) {
            if (await AuthService.refreshToken()) {
              return handleRideEnd(e);
            }
          }
        } else {
          Toast.show({
            type: 'error',
            text1: 'Nepodarilo sa ukon??i?? jazdu',
            text2: '??iadne pripojenie na internet',
          });
        }
      });
  };

  return (
    <Animated.View
      style={[styles.menuContainer, {transform: [{translateY: offsetY}]}]}>
      {(menuShown === 'map' ||
        menuShown === 'ride' ||
        menuShown === 'activeRide') && (
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
              menuShown === 'ride' || menuShown === 'activeRide'
                ? styles.button_active
                : '',
            ]}
            onPress={showRideMenu}>
            <Text
              style={[
                styles.button_text,
                menuShown === 'ride' || menuShown === 'activeRide'
                  ? styles.button_text_active
                  : '',
              ]}>
              JAZDA
            </Text>
          </Pressable>
          {/* <Pressable style={styles.debug_button} onPress={showDebug}>
            <Text style={styles.darkText}>DEBUG</Text>
          </Pressable> */}
          {menuShown === 'map' && (
            <MapMenu friends={friends} showUserDetail={showUserDetail} />
          )}
          {menuShown === 'ride' && (
            <RideMenu
              newRide={newRide}
              setNewRide={setNewRide}
              slideIntoNewRideMenuView={slideIntoNewRideMenuView}
              slideIntoExisitngRideMenuView={slideIntoMapMenuView}
              selectedTrack={selectedTrack}
              setSelectedTrack={setSelectedTrack}
            />
          )}
          {menuShown === 'activeRide' && (
            <ActiveRideDetails ride={rideActive} />
          )}
          {menuShown !== 'config' && menuShown !== 'friends' && (
            <Pressable
              style={[styles.button, button_config]}
              onPress={showConfig}>
              <FontAwesomeIcon style={styles.icon} icon={faCog} size={40} />
            </Pressable>
          )}
          {menuShown !== 'config' && menuShown !== 'friends' && (
            <Pressable
              style={[styles.button, button_friends]}
              onPress={showFriends}>
              <FontAwesomeIcon
                style={styles.icon}
                icon={faUserFriends}
                size={40}
              />
            </Pressable>
          )}
          {menuShown === 'ride' && (
            <Pressable
              style={
                !newRide && !selectedTrack
                  ? [styles.disabled_button, button_start_ride]
                  : [styles.button, button_start_ride]
              }
              disabled={!newRide && !selectedTrack}
              onPress={handleRideStart}>
              <Text
                style={
                  !newRide && !selectedTrack
                    ? styles.disabled_button_text
                    : styles.button_text
                }>
                SPUSTI??
              </Text>
            </Pressable>
          )}
          {menuShown === 'activeRide' && (
            <Pressable
              style={[styles.button, button_start_ride]}
              onPress={handleRideEnd}>
              <Text style={styles.button_text}>UKON??I??</Text>
            </Pressable>
          )}
        </View>
      )}
      {menuShown === 'config' && (
        <View style={styles.menu}>
          <ConfigMenu showMapMenu={showMapMenu} setLoginRefresh={setRefresh} setCode={setCode} setExpiresAt={setExpiresAt}/>
        </View>
      )}
      {menuShown === 'friends' && (
        <View style={styles.menu}>
          <FriendsMenu showMapMenu={showMapMenu} />
        </View>
      )}
      {menuShown === 'debug' && (
        <View style={styles.menu}>
          <DebugMenu showMapMenu={showMapMenu} />
        </View>
      )}
    </Animated.View>
  );
}

const styles = StyleSheet.create({
  menuContainer: {
    position: 'absolute',
    bottom: -600,
    width: Dimensions.get('window').width,
    height: 600,
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
    fontSize: 16,
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
    fontSize: 16,
    color: '#109CF1',
  },

  button_text_active: {
    color: '#FFFFFF',
  },

  debug_button: {
    position: 'absolute',
    top: 5,
    left: 15,
  },

  darkText: {
    color: '#000000',
  },
});
