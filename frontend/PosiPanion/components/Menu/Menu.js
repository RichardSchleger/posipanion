import React, {useEffect, useRef, useState} from 'react';

import {
  Animated,
  Button,
  Dimensions,
  Pressable,
  StyleSheet,
  Text,
} from 'react-native';
import {first} from 'rxjs';

export default function Menu({show}) {
  useEffect(() => {
    if (show) {
      slideIntoView();
    } else {
      slideOutOfView();
    }
  }, [show]);

  const offsetY = useRef(new Animated.Value(0)).current;

  const slideIntoView = () => {
    Animated.timing(offsetY, {
      toValue: -((Dimensions.get('window').height / 3) * 2 - 50),
      duration: 500,
    }).start();
  };

  const slideOutOfView = () => {
    Animated.timing(offsetY, {
      toValue: 0,
      duration: 500,
    }).start();
  };

  return (
    <Animated.View style={[styles.menu, {transform: [{translateY: offsetY}]}]}>
      <Pressable
        style={[styles.button, styles.button_map, styles.button_active]}>
        <Text style={[styles.button_text, styles.button_text_active]}>
          MAPA
        </Text>
      </Pressable>
      <Pressable style={[styles.button, styles.button_ride]}>
        <Text style={[styles.button_text]}>JAZDA</Text>
      </Pressable>
    </Animated.View>
  );
}

const styles = StyleSheet.create({
  menu: {
    position: 'absolute',
    bottom: -(Dimensions.get('window').height / 3) * 2,
    width: Dimensions.get('window').width,
    height: (Dimensions.get('window').height / 3) * 2,
    borderRadius: Dimensions.get('window').width / 10,
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 5,
    backgroundColor: '#FFFFFF',
    zIndex: 100,
    display: 'flex',
    paddingTop: 30,
    paddingLeft: 10,
    paddingRight: 10,
  },

  button: {
    width: '45%',
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 3,
    backgroundColor: '#FFFFFF',
    height: '10%',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 50,
  },

  button_map: {
    position: 'absolute',
    left: '5%',
    top: '4%',
  },

  button_ride: {
    position: 'absolute',
    left: '55%',
    top: '4%',
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
