/**
 * @format
 * @flow
 */

import type {TimingState, TimingDispatch} from '../../types';

import React, {useEffect, useState} from 'react';
import {StyleSheet, Text, View} from 'react-native';
import MapView, {Marker, Polyline, PROVIDER_GOOGLE} from 'react-native-maps';

type Props = {
  state: TimingState,
  dispatch: TimingDispatch,
};

const Stats = ({state, dispatch}: Props) => {
  const [coordArray, setCoordArray] = useState([]);

  var mapview = null;

  useEffect(() => {
    if (state.position != null) {
      setCoordArray(oldArray => [
        ...oldArray,
        {latitude: state.position.lat, longitude: state.position.lng},
      ]);
      if (mapview) {
        mapview.fitToElements(true);
      }
    }
  }, [state]);

  return (
    // <View style={styles.container}>
    //   <Text>Running: {state.running ? 'Yes' : 'No'}</Text>
    //   <Text>Granted: {state.granted ? 'Yes' : 'No'}</Text>
    //   <Text>
    //     Current Location:{' '}
    //     {state.position ? JSON.stringify(state.position) : 'None'}
    //   </Text>
    // </View>
    <View style={styles.container}>
      <MapView
        ref={map => (mapview = map)}
        provider={PROVIDER_GOOGLE} // remove if not using Google Maps
        style={styles.map}>
        <Polyline
          coordinates={coordArray}
          strokeColor="#000" // fallback for when `strokeColors` is not supported by the map-provider
          strokeWidth={6}
        />
        {coordArray.map(coord => (
          <Marker
            coordinate={{
              latitude: coord.latitude ? coord.latitude : 0,
              longitude: coord.longitude ? coord.longitude : 0,
            }}
            opacity={0}
          />
        ))}
        <Marker
          coordinate={{
            latitude: state.position ? state.position.lat : 0,
            longitude: state.position ? state.position.lng : 0,
          }}
        />
      </MapView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
    height: '100%',
  },
  map: {
    ...StyleSheet.absoluteFillObject,
  },
});

export default Stats;
