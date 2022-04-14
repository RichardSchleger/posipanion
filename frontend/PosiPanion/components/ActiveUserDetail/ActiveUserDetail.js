import React from 'react';
import {View, StyleSheet, Text, Dimensions} from 'react-native';
import Converter from '../Converter/Converter';

export default function ActiveUserDetail({detail}) {
  if (detail) {
    return (
      <View style={styles.container}>
        <Text style={styles.text}>
          {detail.firstName + ' ' + detail.surname}
        </Text>
        <View style={styles.textContainer}>
          <Text style={styles.textLabel}>Priemerná rýchlosť:</Text>
          <Text style={styles.text}>
            {Converter.mAndMsTokmh(
              detail.currentRide.distance,
              detail.currentRide.movingTime,
            ) + ' km/h'}
          </Text>
        </View>
        <View style={styles.textContainer}>
          <Text style={styles.textLabel}>Prejdená vzdialenosť:</Text>
          <Text style={styles.text}>
            {Math.round((detail.currentRide.distance / 1000) * 10) / 10 + ' km'}
          </Text>
        </View>
        <View style={styles.textContainer}>
          <Text style={styles.textLabel}>Trvanie:</Text>
          <Text style={styles.text}>
            {Converter.msToTime(detail.currentRide.movingTime)}
          </Text>
        </View>
        {detail.percentage != 0 && (
          <Text style={styles.text}>{detail.percentage + ' %'}</Text>
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
