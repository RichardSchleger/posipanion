import React, {useState} from 'react';

import {
  AppRegistry,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';

import {
  GoogleSignin,
  GoogleSigninButton,
  statusCodes,
} from '@react-native-google-signin/google-signin';

import AuthService from '../AuthService/AuthService';
import Registration from '../Registration/Registration';

export default function Login({setRefresh}) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [opacity, setOpacity] = useState(0);
  const [text, setText] = useState('');

  const [registrationSuccessful, setRegistrationSuccessful] = useState(false);
  const [registrationShown, setRegistrationShown] = useState(false);

  GoogleSignin.configure({
    webClientId:
      '399407603065-k1d2poogobr5o212kv260vkbmn27p61l.apps.googleusercontent.com',
    offlineAccess: true,
  });

  const signIn = async () => {
    try {
      await GoogleSignin.hasPlayServices();
      const userInfo = await GoogleSignin.signIn();
      AuthService.googleLogin(userInfo.idToken, setRefresh);
      console.log(userInfo);
    } catch (error) {
      console.log(error);
      if (error.code === statusCodes.SIGN_IN_CANCELLED) {
        // user cancelled the login flow
      } else if (error.code === statusCodes.IN_PROGRESS) {
        // operation (e.g. sign in) is in progress already
      } else if (error.code === statusCodes.PLAY_SERVICES_NOT_AVAILABLE) {
        // play services not available or outdated
      } else {
        // some other error happened
      }
    }
  };

  const wrongLoginText = {
    fontSize: 18,
    fontWeight: 'normal',
    color: '#109CF1',
    textAlign: 'center',
    marginBottom: 15,
    opacity: opacity,
  };

  const showRegistration = e => {
    e.preventDefault();
    setRegistrationShown(true);
  };

  if (registrationShown) {
    return (
      <Registration
        setRegistrationShown={setRegistrationShown}
        setRegistrationSuccessful={setRegistrationSuccessful}
      />
    );
  } else {
    return (
      <View style={styles.container}>
        <View>
          <Text style={styles.titleText}>PosiPanion</Text>
          <Text style={styles.subtitleText}>Position of your companion!</Text>
        </View>
        <View>
          <Text nativeID={'wrong_login_text'} style={wrongLoginText}>
            {text}
          </Text>
          {registrationSuccessful && (
            <Text style={styles.successfulRegistrationText}>
              Registrácia prebehla úspešne!
            </Text>
          )}
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
              AuthService.login(
                e,
                email,
                password,
                setRefresh,
                setOpacity,
                setText,
              );
            }}>
            <Text style={styles.buttonText}>PRIHLÁSIŤ</Text>
          </Pressable>
          <Pressable onPress={showRegistration}>
            <Text style={styles.registration_text}>
              Nemáte účet? Vytvorte si ho!
            </Text>
          </Pressable>
          <View style={{flexDirection: 'row', padding: 10}}>
            <View
              style={{
                backgroundColor: '#109CF1',
                height: 1,
                flex: 1,
                alignSelf: 'center',
              }}
            />
            <Text
              style={{
                alignSelf: 'center',
                paddingHorizontal: 5,
                fontSize: 18,
                color: '#109CF1',
              }}>
              ALEBO
            </Text>
            <View
              style={{
                backgroundColor: '#109CF1',
                height: 1,
                flex: 1,
                alignSelf: 'center',
              }}
            />
          </View>
          <View style={styles.google_login_container}>
            <GoogleSigninButton
              size={GoogleSigninButton.Size.Wide}
              color={GoogleSigninButton.Color.Dark}
              onPress={signIn}
            />
          </View>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
    height: '100%',
    display: 'flex',
    justifyContent: 'space-evenly',
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
  google_login_container: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: '100%',
    marginTop: '2%',
  },
  registration_text: {
    marginTop: 10,
    fontSize: 14,
    color: '#109CF1',
    textAlign: 'center',
  },
  or_text: {
    marginTop: 10,
    fontSize: 18,
    color: '#109CF1',
    textAlign: 'center',
  },
  successfulRegistrationText: {
    color: '#109CF1',
    fontSize: 18,
    textAlign: 'center',
  },
});

AppRegistry.registerComponent('PosiPanion', () => Login);
