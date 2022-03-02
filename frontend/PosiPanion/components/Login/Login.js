import React, {useState} from 'react';

import {
  AppRegistry,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';

import AuthService from '../AuthService/AuthService';

export default function Login({setRefresh}) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [opacity, setOpacity] = useState(0);

  const wrongLoginText = {
    fontSize: 18,
    fontWeight: 'normal',
    color: '#109CF1',
    textAlign: 'center',
    marginBottom: 15,
    opacity: opacity,
  };

  return (
    <View style={styles.container}>
      <View>
        <Text style={styles.titleText}>PosiPanion</Text>
        <Text style={styles.subtitleText}>Position of your companion!</Text>
      </View>
      <View>
        <Text nativeID={'wrong_login_text'} style={wrongLoginText}>
          Nesprávny email alebo heslo!
        </Text>
        <Text style={styles.inputLabel}>Email</Text>
        <TextInput
          style={styles.input}
          value={email}
          onChangeText={text => setEmail(text)}
        />
        <Text style={styles.inputLabel}>Heslo</Text>
        <TextInput
          secureTextEntry={true}
          style={styles.input}
          value={password}
          onChangeText={text => setPassword(text)}
        />
        <Pressable
          style={styles.loginButton}
          onPress={e => {
            AuthService.login(e, email, password, setRefresh, setOpacity);
          }}>
          <Text style={styles.buttonText}>PRIHLÁSIŤ</Text>
        </Pressable>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
    height: '100%',
    display: 'flex',
    justifyContent: 'space-evenly',
  },
  map: {
    ...StyleSheet.absoluteFillObject,
  },
  input: {
    width: '90%',
    height: 50,
    backgroundColor: '#dedede',
    marginBottom: 15,
    marginLeft: '5%',
    borderRadius: 5,
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 2,
  },
  inputLabel: {
    fontSize: 18,
    marginLeft: '5%',
    color: '#109CF1',
  },
  loginButton: {
    width: '90%',
    marginLeft: '5%',
    height: 50,
    marginTop: 15,
    backgroundColor: '#109CF1',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10,
  },
  buttonText: {
    color: 'white',
    fontWeight: 'bold',
    fontSize: 18,
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
