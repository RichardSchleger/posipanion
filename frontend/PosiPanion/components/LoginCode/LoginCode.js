import React, {useState, useEffect, useRef} from 'react';
import {Dimensions, StyleSheet, Text, View} from 'react-native';

export default function LoginCode({code, expiresAt, setCode}) {
  const [expiration, setExpiration] = useState(-1);

  const timer = useRef(null);

  useEffect(() => {
    if (timer.current) {
      clearInterval(timer.current);
    }
    if (expiresAt) {
      setExpiration(parseInt((new Date(expiresAt) - new Date()) / 1000));
      timer.current = setInterval(() => {
        setExpiration(c => c - 1);
      }, 1000);
    }
  }, [expiresAt]);

  useEffect(() => {
    if (expiration === 0) {
      clearInterval(timer.current);
      setExpiration(-1);
      setCode('');
    }
  }, [expiration]);

  return (
    <View style={styles.bg}>
      <View style={styles.container}>
        <Text style={styles.expirationText}>
          {'Platnosť: ' + expiration + ' sekúnd'}
        </Text>
        <Text style={styles.codeText}>{code}</Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  bg: {
    position: 'absolute',
    top: 0,
    left: 0,
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height,
    backgroundColor: 'rgba(0,0,0,0.5)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  },

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

  expirationText: {
    fontSize: 18,
    color: '#109CF1',
  },

  codeText: {
    fontSize: 32,
    color: '#109CF1',
    fontWeight: 'bold',
  },
});
