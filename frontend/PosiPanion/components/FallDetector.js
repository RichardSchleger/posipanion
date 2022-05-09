import React, {useEffect, useState, useRef} from 'react';
import {Dimensions, Pressable} from 'react-native';
import PushNotification from 'react-native-push-notification';

import {
  accelerometer,
  gyroscope,
  setUpdateIntervalForType,
  SensorTypes,
} from 'react-native-sensors';

import {map, filter} from 'rxjs/operators';
import {StyleSheet, Text, View} from 'react-native';
import DistanceCalculator from './DistanceCalculator/DistanceCalculator';
import axios from 'axios';
import AuthService from './AuthService/AuthService';
import API from './Api/API';

setUpdateIntervalForType(SensorTypes.accelerometer, 25);
setUpdateIntervalForType(SensorTypes.gyroscope, 25);

export default function FallDetector({positionState}) {
  const [fallText, setFallText] = useState('');
  const [firstPositionUpdate, setFirstPositionUpdate] = useState(true);
  const [lastPosition, setLastPosition] = useState(null);
  const [fallTimeout, setFallTimeout] = useState(null);

  useEffect(() => {
    if (possibleFallDetected.current) {
      if (firstPositionUpdate) {
        setFirstPositionUpdate(false);
        setLastPosition(positionState);
      } else {
        possibleFallDetected.current = false;
        const distance = DistanceCalculator.calculateDistanceBetweenLatLonEle(
          lastPosition.position.lat,
          lastPosition.position.lng,
          lastPosition.position.alti,
          positionState.position.lat,
          positionState.position.lng,
          positionState.position.alti,
        );
        console.log(distance);
        if (distance < 5) {
          PushNotification.localNotification({
            channelId: 'posipanion',
            title: 'Detekovaný možný pád',
            message:
              'Bol detekovaný možný pád, máte 30 sekúnd na prípadné zrušenie notifikácie priateľov.',
          });
          setFallText('Detekovaný možný pád!');
          const t = setTimeout(() => {
            setFallText('');
            setFirstPositionUpdate(true);
          }, 30000);
          setFallTimeout(t);
        } else {
          setFirstPositionUpdate(true);
        }
      }
    }
  }, [positionState]);

  const accValue = useRef(0);
  const possibleFallDetected = useRef(false);

  const accelerometerSubscription = accelerometer
    .pipe(
      map(
        ({x, y, z}) =>
          Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) / 9.81,
      ),
      filter(value => value > 1.5),
    )
    .subscribe(acc => {
      accValue.current = acc;
    }, error => {
      console.log(error);
    });

  const gyroscopeSubscription = gyroscope
    .pipe(
      map(
        ({x, y, z}) =>
          Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) / 9.81,
      ),
      filter(gyro => gyro > 1),
    )
    .subscribe(gyr => {
      if (
        !possibleFallDetected.current &&
        1.45 * accValue.current + gyr > 7.35
      ) {
        possibleFallDetected.current = true;
      }
    }, error => {
      console.log(error);
    });

  const cancelFall = e => {
    e.preventDefault();
    setFallText('');
    setFirstPositionUpdate(true);
    possibleFallDetected.current = false;
    if (fallTimeout) {
      clearTimeout(fallTimeout);
      setFallTimeout(null);
    }
  };

  const confirmFall = e => {
    e.preventDefault();
    cancelFall(e);
    sendFallNotification(e);
  };

  const sendFallNotification = async e => {
    const token = await AuthService.getToken();
    axios
      .post(
        API.url + 'user/fall',
        {
          latitude: positionState.position.lat,
          longitude: positionState.position.lng,
          elevation: positionState.position.alti,
          timestamp: new Date(),
        },
        {
          headers: {
            Authorization: 'Bearer ' + token,
          },
        },
      )
      .then(res => {
        console.log(res);
      })
      .catch(async error => {
        console.log(error);
        if (
          error &&
          error.response &&
          error.response.status &&
          error.response.status === 606
        ) {
          if (await AuthService.refreshToken()) {
            return sendFallNotification(e);
          }
        }
      });
  };

  const bg = {
    position: 'absolute',
    top: 0,
    left: 0,
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height,
    backgroundColor: 'rgba(0,0,0,0.5)',
    display: fallText ? 'flex' : 'none',
    justifyContent: 'center',
    alignItems: 'center',
  };

  return (
    <View style={bg}>
      <View style={styles.container}>
        <Text style={styles.text}>{fallText}</Text>
        <View style={styles.buttonContainer}>
          <Pressable style={styles.button_cancel} onPress={cancelFall}>
            <Text style={styles.cancel_button_text}>Zrušiť</Text>
          </Pressable>
          <Pressable style={styles.button_confirm} onPress={confirmFall}>
            <Text style={styles.confirm_button_text}>Potvrdiť</Text>
          </Pressable>
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    width: '90%',
    height: '30%',
    backgroundColor: '#FFFFFF',
    borderRadius: Dimensions.get('window').width / 10,
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 5,
    zIndex: 200,
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  },

  text: {
    fontSize: 32,
    color: '#109CF1',
    fontWeight: 'bold',
    textAlign: 'center',
  },

  buttonContainer: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-evenly',
    alignItems: 'center',
    width: '100%',
    height: '23%',
    marginTop: 15,
  },

  button_cancel: {
    width: '45%',
    height: '100%',
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 3,
    backgroundColor: '#109CF1',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 50,
  },

  button_confirm: {
    width: '45%',
    height: '100%',
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 3,
    backgroundColor: '#FFFFFF',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 50,
  },

  cancel_button_text: {
    fontSize: 16,
    color: '#FFFFFF',
  },

  confirm_button_text: {
    fontSize: 16,
    color: '#109CF1',
  },
});
