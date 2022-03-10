import React from 'react';
import {View, StyleSheet, Text} from 'react-native';

export default function ActiveRideDetails({ride}) {
  if (ride) {
    return (
      <View style={styles.container}>
        <Text style={styles.text}>
          {Math.round(
            (ride.distance / 1000 / (ride.movingTime / 3600000)) * 10,
          ) /
            10 +
            ' km/h'}
        </Text>
        <Text style={styles.text}>
          {Math.round((ride.distance / 1000) * 10) / 10 + ' km'}
        </Text>
      </View>
    );
  } else {
    return <View />;
  }
}

const styles = StyleSheet.create({
  container: {
    height: '100%',
    backgroundColor: 'blue',
  },
  text: {
    color: '#109CF1',
    fontSize: 24,
  },
});
