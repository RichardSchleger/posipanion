import React from 'react';
import {Dimensions, StyleSheet, TouchableHighlight, View} from 'react-native';

import {FontAwesomeIcon} from '@fortawesome/react-native-fontawesome';
import {faBars} from '@fortawesome/free-solid-svg-icons';

export default function MenuButton({show, onPress}) {
  return (
    <TouchableHighlight
      style={[styles.menuButton, {display: show ? 'flex' : 'none'}]}
      onPress={onPress}>
      <FontAwesomeIcon icon={faBars} style={styles.icon} size={55} />
    </TouchableHighlight>
  );
}

const styles = StyleSheet.create({
  menuButton: {
    position: 'absolute',
    bottom: '2%',
    right: '2%',
    width: Dimensions.get('window').width / 5,
    height: Dimensions.get('window').width / 5,
    borderRadius: Dimensions.get('window').width / 10,
    backgroundColor: '#109CF1',
    zIndex: 100,
    alignItems: 'center',
    justifyContent: 'center',
  },

  icon: {
    color: 'white',
  },
});
