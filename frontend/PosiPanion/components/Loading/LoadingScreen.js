import React from 'react';

import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
} from 'react-native';

export default function LoadingScreen({setRefresh}) {

    return (
      <View style={styles.container}>
        <View>
          <Text style={styles.titleText}>PosiPanion</Text>
          <Text style={styles.subtitleText}>Position of your companion!</Text>
        </View>
      </View>
    )
}

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
    height: '100%',
    display: 'flex',
    justifyContent: 'space-evenly',
  },

  titleText: {
    fontSize: 48,
    fontWeight: 'bold',
    color: '#109CF1',
    textAlign: 'center',
  },
  subtitleText: {
    fontSize: 24,
    fontWeight: 'normal',
    color: '#109CF1',
    textAlign: 'center',
  },
});

AppRegistry.registerComponent('PosiPanion', () => Login);
