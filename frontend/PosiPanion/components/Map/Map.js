/**
 * @format
 * @flow
 */

import React, {useEffect, useState} from 'react';
import {StyleSheet, Text, View} from 'react-native';
import MapView, {Marker, PROVIDER_GOOGLE} from 'react-native-maps';
// import RNLocation from 'react-native-location';

// RNLocation.configure({
//   distanceFilter: 0, // Meters
//   desiredAccuracy: {
//     ios: 'best',
//     android: 'highAccuracy',
//   },
//   // Android only
//   // androidProvider: 'auto',
//   interval: 1000, // Milliseconds
//   fastestInterval: 1000, // Milliseconds
//   maxWaitTime: 1000, // Milliseconds
//   // iOS Only
//   activityType: 'fitness',
//   allowsBackgroundLocationUpdates: true,
//   headingFilter: 0, // Degrees
//   headingOrientation: 'portrait',
//   pausesLocationUpdatesAutomatically: false,
//   showsBackgroundLocationIndicator: true,
// });

const Map = ({users}) => {
  const [firstRun, setFirstRun] = useState(true);
  const mapview = React.createRef();

  useEffect(() => {
    const centerMap = () => {
      if (users && users.length > 0) {
        if (mapview && mapview.current) {
          mapview.current.fitToElements(true);
        }
      }
    };

    if (users && users.length > 0) {
      if (mapview && mapview.current) {
        if (firstRun) {
          centerMap();
          setFirstRun(c => !c);
        }
      }
    }
  }, [firstRun, mapview, users]);

  return (
    <View style={styles.container}>
      <MapView
        ref={mapview}
        provider={PROVIDER_GOOGLE} // remove if not using Google Maps
        style={styles.map}>
        {users &&
          users.map((user, index) => (
            <Marker
              key={'user_' + index}
              anchor={{x: 0.5, y: 0.5}}
              coordinate={{
                latitude: user.lat,
                longitude: user.lng,
              }}>
              <View style={styles.marker}>
                <Text style={styles.markerText}>
                  {user.firstName[0].toUpperCase()}
                </Text>
              </View>
            </Marker>
          ))}
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
  marker: {
    borderColor: '#109CF1',
    borderWidth: 2,
    borderStyle: 'solid',
    borderRadius: 25,
    width: 30,
    height: 30,
    backgroundColor: 'white',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  },
  markerText: {
    fontSize: 18,
    color: '#109CF1',
    fontWeight: 'bold',
  },
});

export default Map;
