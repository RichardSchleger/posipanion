import React from 'react';
import {Pressable, StyleSheet, Text, View} from 'react-native';

export default function RideMenu({
  newRide,
  setNewRide,
  slideIntoNewRideMenuView,
  slideIntoExisitngRideMenuView,
}) {
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
    fontSize: 20,
    color: '#109CF1',
  },

  button_text_active: {
    color: '#FFFFFF',
  },
});
