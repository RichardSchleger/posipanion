import React from 'react';
import {View, StyleSheet, Text} from 'react-native';
import Converter from '../Converter/Converter';

export default function ActiveRideDetails({ride}) {

  if (ride) {
    return (
      <View style={styles.container}>
        <View style={styles.textContainer}>
          <Text style={styles.textLabel}>Aktuálna rýchlosť:</Text>
          <Text style={styles.text}>
            {(ride.currentRide.currentSpeed
              ? ride.currentRide.currentSpeed
              : 0) + ' km/h'}
          </Text>
        </View>
        <View style={styles.textContainer}>
          <Text style={styles.textLabel}>Priemerná rýchlosť:</Text>
          <Text style={styles.text}>
            {Converter.mAndMsTokmh(
              ride.currentRide.distance,
              ride.currentRide.movingTime,
            ) + ' km/h'}
          </Text>
        </View>
        <View style={styles.textContainer}>
          <Text style={styles.textLabel}>Prejdená vzdialenosť:</Text>
          <Text style={styles.text}>
            {Math.round((ride.currentRide.distance / 1000) * 10) / 10 + ' km'}
          </Text>
        </View>
        <View style={styles.textContainer}>
          <Text style={styles.textLabel}>Trvanie:</Text>
          <Text style={styles.text}>
            {Converter.msToTime(ride.currentRide.movingTime)}
          </Text>
        </View>
      </View>
    );
  } else {
    return <View />;
  }
}

const styles = StyleSheet.create({
  container: {
    top: '10%',
    height: '39%',
    width: '95%',
    marginLeft: '2.5%',
    padding: 0,
  },

  textContainer: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: '3%',
  },

  text: {
    color: '#109CF1',
    fontSize: 24,
  },

  textLabel: {
    color: '#109CF1',
    fontSize: 20,
  },
});
