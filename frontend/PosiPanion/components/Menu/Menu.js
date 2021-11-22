import React, {useEffect, useRef, useState} from 'react';

import {Animated, Dimensions, StyleSheet} from 'react-native';
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
    <Animated.View
      style={[
        styles.menu,
        {transform: [{translateY: offsetY}]},
      ]}></Animated.View>
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
    alignItems: 'center',
    justifyContent: 'center',
  },
});
