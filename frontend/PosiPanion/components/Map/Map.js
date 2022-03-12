/**
 * @format
 * @flow
 */

import React, {useEffect, useState} from 'react';
import {StyleSheet, Text, View} from 'react-native';
import MapView, {Marker, Polyline, PROVIDER_GOOGLE} from 'react-native-maps';
import ActiveUserDetail from '../ActiveUserDetail/ActiveUserDetail';

const Map = ({users, detail, showUserDetail, rideActive, shown}) => {
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

  const container = {
    ...StyleSheet.absoluteFillObject,
    height: detail ? '80%' : rideActive && shown ? '50%' : '100%',
    position: 'absolute',
    top: detail ? '20%' : 0,
  };

  return [
    <ActiveUserDetail detail={detail} key={'active_user_detail'} />,
    <View style={container} key={'map_container'}>
      <MapView ref={mapview} provider={PROVIDER_GOOGLE} style={styles.map}>
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
    </View>,
  ];
};

const styles = StyleSheet.create({
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
