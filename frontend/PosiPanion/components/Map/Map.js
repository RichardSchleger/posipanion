import React, {useEffect, useState} from 'react';

import RNLocation from 'react-native-location';
import CurrentLocation from './CurrentLocation';
import {useLocation} from '../hooks';
import {AppRegistry, StatusBar, StyleSheet, View} from 'react-native';
import {useTimingReducer} from './reducer';

import MapView, {PROVIDER_GOOGLE} from 'react-native-maps';
import MenuButton from '../MenuButton/MenuButton';

RNLocation.configure({
  distanceFilter: 0, // Meters
  desiredAccuracy: {
    ios: 'best',
    android: 'highAccuracy',
  },
  // Android only
  // androidProvider: 'auto',
  interval: 1000, // Milliseconds
  fastestInterval: 1000, // Milliseconds
  maxWaitTime: 1000, // Milliseconds
  // iOS Only
  activityType: 'fitness',
  allowsBackgroundLocationUpdates: true,
  headingFilter: 0, // Degrees
  headingOrientation: 'portrait',
  pausesLocationUpdatesAutomatically: false,
  showsBackgroundLocationIndicator: true,
});

export default function Map() {
  const [state, dispatch] = useTimingReducer();
  useLocation(state, dispatch);

  const [showMenuButton, setShowMenuButton] = useState(true);

  const onPress = e => {
    e.preventDefault();
    setShowMenuButton(false);
  };

  return (
    <View style={styles.container}>
      <StatusBar barStyle="dark-content" />
      <CurrentLocation state={state} dispatch={dispatch} />
      <MenuButton show={showMenuButton} onPress={onPress} />
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

AppRegistry.registerComponent('PosiPanion', () => Map);
