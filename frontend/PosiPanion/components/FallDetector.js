import React, {useEffect, useState} from 'react';

import {
  accelerometer,
  gyroscope,
  setUpdateIntervalForType,
  SensorTypes,
} from 'react-native-sensors';

import Geolocation from 'react-native-geolocation-service';
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
  const [viewLocation, isViewLocation] = useState([]);
  const [fallText, setFallText] = useState('');

  async function requestLocationPermission() {
    if (Platform.OS === 'ios') {
      Geolocation.requestAuthorization('whenInUse');
    } else {
      try {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
          {
            title: 'PosiPanion',
            message: 'PosiPanion needs access to your location ',
          },
        );
      } catch (err) {
        console.warn(err);
      }
    }
  }

  const getLocation = async () => {
    await requestLocationPermission();
    Geolocation.getCurrentPosition(
      position => {
        console.log(position.coords);
        isViewLocation(position.coords);
      },
      error => {
        console.log(error.code, error.message);
      },
      {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 0,
        distanceFilter: 0,
      },
    );
  };

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
      Geolocation.getCurrentPosition(
        position => {
          console.log(position.coords);
        },
        error => {
          console.warn(error.code, error.message);
        },
        {
          enableHighAccuracy: true,
          timeout: 5000,
          maximumAge: 0,
          distanceFilter: 0,
        },
      );
    } else {
      possibleFall = {};
    }
  };

  accelerometer
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
      Geolocation.getCurrentPosition(
        position => {
          possibleFall.location = position.coords;
        },
        error => {
          console.warn(error.code, error.message);
        },
        {
          enableHighAccuracy: true,
          timeout: 5000,
          maximumAge: 0,
          distanceFilter: 0,
        },
      );
      console.log('TIMEOUT' + possibleFall.timeout);
    });

  gyroscope
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
        <View
          style={{marginTop: 10, padding: 10, borderRadius: 10, width: '40%'}}>
          <Button title="Get Location" onPress={getLocation} />
        </View>
        <Text>Latitude: {viewLocation.latitude} </Text>
        <Text>Longitude: {viewLocation.longitude} </Text>
        <View
          style={{marginTop: 10, padding: 10, borderRadius: 10, width: '40%'}}>
          <Button title="Send Location" />
        </View>
      </View>
    </View>
  );
}
