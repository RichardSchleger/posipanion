import React, {useEffect, useState} from 'react';
import {
  Dimensions,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import AuthService from '../AuthService/AuthService';
import axios from 'axios';
import API from '../Api/API';

export default function RideMenu({
  newRide,
  setNewRide,
  slideIntoNewRideMenuView,
  slideIntoExisitngRideMenuView,
  selectedTrack,
  setSelectedTrack,
}) {
  const [tracks, setTracks] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const token = await AuthService.getToken();
      return await axios
        .get(API.url + 'user/tracks', {
          headers: {Authorization: 'Bearer ' + token},
        })
        .then(response => response.data)
        .catch(async error => {
          if (error.response.status === 606) {
            if (await AuthService.refreshToken()) {
              return fetchData();
            }
          }
        });
    };

    const getData = async () => {
      const response = await fetchData();
      setTracks(response);
    };
    getData();
  }, []);

  const showNewRide = e => {
    e.preventDefault();
    setNewRide(true);
    slideIntoNewRideMenuView();
  };

  const showExistingRide = e => {
    e.preventDefault();
    setNewRide(false);
    slideIntoExisitngRideMenuView();
  };

  return (
    <View style={styles.rideMenu}>
      <Pressable
        style={[
          styles.button,
          styles.button_map,
          newRide ? styles.button_active : '',
        ]}
        onPress={showNewRide}>
        <Text
          style={[
            styles.button_text,
            newRide ? styles.button_text_active : '',
          ]}>
          NOVÁ
        </Text>
      </Pressable>
      <Pressable
        style={[
          styles.button,
          styles.button_ride,
          newRide ? '' : styles.button_active,
        ]}
        onPress={showExistingRide}>
        <Text
          style={[
            styles.button_text,
            newRide ? '' : styles.button_text_active,
          ]}>
          NAPLÁNOVANÁ
        </Text>
      </Pressable>
      {!newRide && (
        <View style={styles.tracks_container}>
          <ScrollView>
            {tracks.map((track, index) => (
              <Pressable
                style={
                  selectedTrack && track.id === selectedTrack.id
                    ? [styles.selected_track, styles.track_container]
                    : styles.track_container
                }
                key={'track_' + index}
                onPress={() => {
                  if (
                    !selectedTrack ||
                    (selectedTrack && track.id !== selectedTrack.id)
                  ) {
                    setSelectedTrack(tracks[index]);
                  } else {
                    setSelectedTrack(null);
                  }
                }}>
                <Text style={styles.darkText}>{track.name}</Text>
                <Text style={styles.darkText}>
                  {Math.round((track.distance / 1000) * 10) / 10 + ' km'}
                </Text>
              </Pressable>
            ))}
          </ScrollView>
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  rideMenu: {
    top: '10%',
    left: '-3%',
    height: '65%',
    width: '106%',
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
    height: '15%',
    position: 'absolute',
    left: '5%',
  },

  button_ride: {
    width: '45%',
    height: '15%',
    position: 'absolute',
    left: '55%',
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

  tracks_container: {
    top: 60,
    height: '80%',
    width: '100%',
    marginLeft: 0,
    paddingLeft: 0,
  },

  selected_track: {
    borderWidth: 3,
    borderStyle: 'solid',
    borderColor: '#109CF1',
  },

  track_container: {
    width: '100%',
    height: Dimensions.get('window').height / 15,
    backgroundColor: '#EEEEEE',
    color: '#000000',
    marginBottom: 5,
    marginLeft: 0,
    borderRadius: 5,
    display: 'flex',
    justifyContent: 'center',
    paddingLeft: 10,
  },

  darkText: {
    color: '#000000',
  },
});
