import React from 'react';
import {View, StyleSheet, Text, Dimensions} from 'react-native';

export default function ActiveUserDetail({detail}) {
  if (detail) {
    return (
      <View style={styles.container}>
        <Text style={styles.text}>
          {detail.firstName + ' ' + detail.surname}
        </Text>
        <Text style={styles.text}>
          {Math.round(
            (detail.currentRide.distance /
              1000 /
              (detail.currentRide.movingTime / 3600000)) *
              10,
          ) /
            10 +
            ' km/h'}
        </Text>
        <Text style={styles.text}>
          {Math.round((detail.currentRide.distance / 1000) * 10) / 10 + ' km'}
        </Text>
        {detail.percentage != 0 && (
          <Text style={styles.text}>{detail.percentage + '%'}</Text>
        )}
      </View>
    );
  } else {
    return <View />;
  }
}

const styles = StyleSheet.create({
  container: {
    height: '25%',
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 5,
    top: -Dimensions.get('window').width / 20,
    borderRadius: Dimensions.get('window').width / 20,
    backgroundColor: '#FFFFFF',
    display: 'flex',
    paddingTop: Dimensions.get('window').width / 20,
    paddingLeft: 10,
    paddingRight: 10,
    elevation: 10,
    zIndex: 10,
  },
  text: {
    color: '#109CF1',
    fontSize: 24,
  },
});
