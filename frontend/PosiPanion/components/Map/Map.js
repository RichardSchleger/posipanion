/**
 * @format
 * @flow
 */

import React, {useEffect, useState} from 'react';
import {StyleSheet, Text, View, Dimensions} from 'react-native';
import MapView, {Marker, Polyline, PROVIDER_GOOGLE} from 'react-native-maps';
import ActiveUserDetail from '../ActiveUserDetail/ActiveUserDetail';

const Map = ({
  users,
  detail,
  showUserDetail,
  rideActive,
  shown,
  menuShown,
  mapview,
}) => {
  const [firstRun, setFirstRun] = useState(true);

  const [regionMarkers, setRegionMarkers] = useState([]);

  useEffect(() => {
    if (users && users.length > 0) {
      if (mapview && mapview.current) {
        if (firstRun) {
          mapview.current.fitToElements(true);
          setFirstRun(c => !c);
        }
      }
    }
  }, [mapview, users, menuShown]);

  useEffect(() => {
    let allMarkers = [];
    if (
      detail &&
      detail.currentRide &&
      detail.currentRide.waypoints.length > 0
    ) {
      allMarkers = [...allMarkers, ...detail.currentRide.waypoints];
    }
    if (detail && detail.track && detail.track.waypoints.length > 0) {
      allMarkers = [...allMarkers, ...detail.track.waypoints];
    }
    if (
      rideActive &&
      rideActive.currentRide &&
      rideActive.currentRide.waypoints.length > 0
    ) {
      allMarkers = [...allMarkers, ...rideActive.currentRide.waypoints];
    }
    if (
      rideActive &&
      rideActive.track &&
      rideActive.track.waypoints.length > 0
    ) {
      allMarkers = [...allMarkers, ...rideActive.track.waypoints];
    }
    if (allMarkers.length > 0) {
      setRegionMarkers(() => [
        allMarkers.find(
          m =>
            m.latitude ===
            Math.max.apply(
              Math,
              allMarkers.map(function (o) {
                return o.latitude;
              }),
            ),
        ),
        allMarkers.find(
          m =>
            m.longitude ===
            Math.max.apply(
              Math,
              allMarkers.map(function (o) {
                return o.longitude;
              }),
            ),
        ),
        allMarkers.find(
          m =>
            m.latitude ===
            Math.min.apply(
              Math,
              allMarkers.map(function (o) {
                return o.latitude;
              }),
            ),
        ),
        allMarkers.find(
          m =>
            m.longitude ===
            Math.min.apply(
              Math,
              allMarkers.map(function (o) {
                return o.longitude;
              }),
            ),
        ),
      ]);
    } else {
      setRegionMarkers([]);
    }
  }, [detail, rideActive]);

  useEffect(() => {
    if(regionMarkers.length === 0) {
      setFirstRun(true);
    }else{
      if(firstRun){
        if(mapview && mapview.current){
          mapview.current.fitToElements(true);
          setFirstRun(c => !c);
        }
      }
    }
    
  }, [regionMarkers]);

  const container = {
    ...StyleSheet.absoluteFillObject,
    height: detail
      ? '80%'
      : rideActive && shown && menuShown === 'activeRide'
      ? Dimensions.get('window').height - 350
      : '100%',
    position: 'absolute',
    top: detail ? '20%' : 0,
  };

  return [
    <ActiveUserDetail detail={detail} key={'active_user_detail'} />,
    <View style={container} key={'map_container'}>
      <MapView ref={mapview} style={styles.map}>
        {users &&
          detail === null &&
          menuShown !== 'activeRide' &&
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
        {regionMarkers.map((marker, index) => (
          <Marker
            key={'marker_' + index}
            coordinate={{
              latitude: marker.latitude,
              longitude: marker.longitude,
            }}
            opacity={0}
          />
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
        {rideActive && rideActive.track && (
          <Polyline
            coordinates={rideActive.track.waypoints}
            strokeColor={'red'}
            strokeWidth={3}
          />
        )}
        {rideActive && rideActive.currentRide && (
          <Polyline
            coordinates={rideActive.currentRide.waypoints}
            strokeColor={'#109CF1'}
            strokeWidth={3}
          />
        )}
        {rideActive && (
          <Marker
            key={'user_detail_marker'}
            anchor={{x: 0.5, y: 0.5}}
            coordinate={{
              latitude: rideActive.lastKnownLatitude,
              longitude: rideActive.lastKnownLongitude,
            }}>
            <View style={styles.marker}>
              <Text style={styles.markerText}>
                {rideActive.firstName[0].toUpperCase()}
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
