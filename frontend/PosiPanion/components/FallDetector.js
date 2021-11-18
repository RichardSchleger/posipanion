import React, {useEffect, useState} from 'react';

import {
  accelerometer,
  gyroscope,
  setUpdateIntervalForType,
  SensorTypes,
} from 'react-native-sensors';

import {filter, map} from 'rxjs/operators';
import {
  StyleSheet,
  Button,
  Text,
  View,
  SafeAreaView,
  Platform,
  PermissionsAndroid,
} from 'react-native';

setUpdateIntervalForType(SensorTypes.accelerometer, 25);
setUpdateIntervalForType(SensorTypes.gyroscope, 25);

export default function FallDetector() {
  const [fallText, setFallText] = useState('');

  const accelerometerSubscription = accelerometer
    .pipe(
      map(
        ({x, y, z}) =>
          Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) - 9.81,
      ),
      filter(acc => acc > 50),
    )
    .subscribe(acc => {
      console.log('A ' + acc);
      if (possibleFall.timeout != null) {
        console.log('CLEANING TIMEOUT');
        clearTimeout(possibleFall.timeout);
      }
      possibleFall.timeout = setTimeout(() => {
        possibleFallDetected();
      }, 10000);
      possibleFall.a = acc;
      possibleFall.aTime = Date.now();
      console.log('TIMEOUT' + possibleFall.timeout);
    });

  const gyroscopeSubscription = gyroscope
    .pipe(
      map(({x, y, z}) =>
        Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)),
      ),
      filter(acc => acc > 15),
    )
    .subscribe(acc => {
      console.log('G ' + acc);
      possibleFall.g = acc;
      possibleFall.gTime = Date.now();
    });

  let possibleFall = {};
  const possibleFallDetected = () => {
    if (Math.abs(possibleFall.aTime - possibleFall.gTime) < 1000) {
      console.log('POSSIBLE ACCELEROMETER AND GYROSCOPE FALL!!!');
      setFallText('POSSIBLE ACCELEROMETER AND GYROSCOPE FALL!!!');
      setTimeout(() => {
        possibleFall = {};
        setFallText('');
      }, 10000);
      console.log(possibleFall);
    } else {
      possibleFall = {};
    }
  };

  const styles = StyleSheet.create({
    container: {
      alignItems: 'center',
      justifyContent: 'center',
    },
  });

  return (
    <View style={{display: 'flex'}}>
      <View style={styles.container}>
        <Text style={{fontSize: 24, color: 'red'}}>{fallText}</Text>
      </View>
    </View>
  );
}
