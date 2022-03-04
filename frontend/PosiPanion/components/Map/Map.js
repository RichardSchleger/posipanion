/**
 * @format
 * @flow
 */

import React, {useEffect, useState} from 'react';
import {StyleSheet, Text, View} from 'react-native';
import MapView, {Marker, Polyline, PROVIDER_GOOGLE} from 'react-native-maps';

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

const Map = ({users, detail, showUserDetail}) => {
  const [firstRun, setFirstRun] = useState(true);
  const mapview = React.createRef();

  useEffect(() => {
    if (users && users.length > 0) {
      if (mapview && mapview.current) {
        if (firstRun) {
          if (users && users.length > 0) {
            if (mapview && mapview.current) {
              mapview.current.fitToElements(true);
            }
          }
          setFirstRun(c => !c);
        }
      }
    }
  }, [firstRun, mapview, users]);

  useEffect(() => {
    mapview.current.fitToElements(true);
  }, [detail]);
  return (
    <View style={styles.container}>
      <MapView
        ref={mapview}
        provider={PROVIDER_GOOGLE} // remove if not using Google Maps
        style={styles.map}>
        {users &&
          detail === null &&
          users.map((user, index) => (
            <Marker
              key={'user_' + index}
              anchor={{x: 0.5, y: 0.5}}
              coordinate={{
                latitude: user.lat,
                longitude: user.lng,
              }}
              onPress={event => {
                showUserDetail(event, user.id);
              }}>
              <View style={styles.marker}>
                <Text style={styles.markerText}>
                  {user.firstName[0].toUpperCase()}
                </Text>
              </View>
            </Marker>
          ))}
        {detail &&
          detail.track &&
          detail.track.waypoints.map((waypoint, index) => (
            <Marker
              key={'trackpoint_' + index}
              coordinate={{
                latitude: waypoint.latitude,
                longitude: waypoint.longitude,
              }}
              opacity={0}
            />
          ))}
        {detail && detail.track && (
          <Polyline
            coordinates={detail.track.waypoints}
            strokeColor={'red'}
            strokeWidth={3}
          />
        )}
        {detail &&
          detail.currentRide &&
          detail.currentRide.waypoints.map((waypoint, index) => (
            <Marker
              key={'journeypoint_' + index}
              coordinate={{
                latitude: waypoint.latitude,
                longitude: waypoint.longitude,
              }}
              opacity={0}
            />
          ))}
        {detail && detail.currentRide && (
          <Polyline
            coordinates={detail.currentRide.waypoints}
            strokeColor={'#109CF1'}
            strokeWidth={3}
          />
        )}
        {detail && (
          <Marker
            key={'user_detail_marker'}
            anchor={{x: 0.5, y: 0.5}}
            coordinate={{
              latitude: detail.lastKnownLatitude,
              longitude: detail.lastKnownLongitude,
            }}>
            <View style={styles.marker}>
              <Text style={styles.markerText}>
                {detail.firstName[0].toUpperCase()}
              </Text>
            </View>
          </Marker>
        )}
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
  trackLine: {
    color: 'red',
  },
  currentRideLine: {
    color: 'blue',
  },
});

export default Map;
