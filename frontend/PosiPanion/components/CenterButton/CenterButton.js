import React from 'react';
import {Dimensions, StyleSheet, TouchableHighlight} from 'react-native';

import {FontAwesomeIcon} from '@fortawesome/react-native-fontawesome';
import {faLocationCrosshairs} from '@fortawesome/free-solid-svg-icons';

export default function CenterButton({onPress}) {
  return (
    <TouchableHighlight style={styles.centerButton} onPress={onPress}>
      <FontAwesomeIcon
        icon={faLocationCrosshairs}
        style={styles.icon}
        size={30}
      />
    </TouchableHighlight>
  );
}

const styles = StyleSheet.create({
  centerButton: {
    position: 'absolute',
    bottom: '18%',
    right: '2%',
    width: Dimensions.get('window').width / 6,
    height: Dimensions.get('window').width / 6,
    borderRadius: Dimensions.get('window').width / 3,
    backgroundColor: 'white',
    zIndex: 100,
    alignItems: 'center',
    justifyContent: 'center',
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 5,
  },

  icon: {
    color: '#109CF1',
  },
});
