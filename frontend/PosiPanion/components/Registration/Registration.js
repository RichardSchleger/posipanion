import React, {useState} from 'react';
import {
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
  Keyboard,
} from 'react-native';
import API from '../Api/API';
import axios from 'axios';

export default function Registration({
  setRegistrationShown,
  setRegistrationSuccessful,
}) {
  const [name, setName] = useState('');
  const [surname, setSurname] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [repeatPassword, setRepeatPassword] = useState('');

  const [userAlreadyExists, setUserAlreadyExists] = useState(false);
  const [invalidEmail, setInvalidEmail] = useState(false);

  //https://stackoverflow.com/questions/43676695/email-validation-react-native-returning-the-result-as-invalid-for-all-the-e
  const validateEmail = text => {
    let reg = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w\w+)+$/;
    return reg.test(text);
  };

  const hideRegistration = e => {
    e.preventDefault();
    setRegistrationShown(false);
    setName('');
    setSurname('');
    setEmail('');
    setPassword('');
    setRepeatPassword('');
  };

  const handleRegistration = e => {
    e.preventDefault();

    Keyboard.dismiss();

    const validEmail = validateEmail(email);
    setInvalidEmail(!validEmail);
    if (validEmail) {
      axios
        .post(API.url + 'register', {
          name: name,
          surname: surname,
          email: email,
          password: password,
        })
        .then(() => {
          setRegistrationSuccessful(true);
          setRegistrationShown(false);
        })
        .catch(error => {
          if (error.response.status === 601) {
            setUserAlreadyExists(true);
          }
        });
    }
  };

  return (
    <View style={styles.container}>
      <View>
        <Text style={styles.titleText}>PosiPanion</Text>
        <Text style={styles.subtitleText}>Position of your companion!</Text>
      </View>
      <View>
        {userAlreadyExists && (
          <Text style={styles.wrongRegistrationText}>
            Účet s rovnakým emailom už existuje!
          </Text>
        )}
        <Text style={styles.inputLabel}>Meno</Text>
        <TextInput
          style={styles.input}
          value={name}
          onChangeText={text => setName(text)}
        />
        <Text style={styles.inputLabel}>Priezvisko</Text>
        <TextInput
          style={styles.input}
          value={surname}
          onChangeText={text => setSurname(text)}
        />
        <Text style={styles.inputLabel}>
          Email{'  '}
          {invalidEmail && <Text style={{fontSize: 14}}>Neplatný email!</Text>}
        </Text>
        <TextInput
          style={styles.input}
          value={email}
          onChangeText={text => setEmail(text)}
        />
        <Text style={styles.inputLabel}>
          Heslo{'  '}
          <Text style={{fontSize: 14}}>Minimálna dĺžka je 8 znakov.</Text>
        </Text>
        <TextInput
          secureTextEntry={true}
          style={styles.input}
          value={password}
          onChangeText={text => setPassword(text)}
        />
        <Text style={styles.inputLabel}>Zopakujte heslo</Text>
        <TextInput
          secureTextEntry={true}
          style={styles.input}
          value={repeatPassword}
          onChangeText={text => setRepeatPassword(text)}
        />
        <Pressable
          style={
            password.length < 8 ||
            password !== repeatPassword ||
            name === '' ||
            surname === ''
              ? styles.disabledButton
              : styles.loginButton
          }
          onPress={handleRegistration}
          disabled={
            password.length < 8 ||
            password !== repeatPassword ||
            name === '' ||
            surname === ''
          }>
          <Text
            style={
              password.length < 8 ||
              password !== repeatPassword ||
              name === '' ||
              surname === ''
                ? styles.disabledButtonText
                : styles.buttonText
            }>
            ZAREGISTROVAŤ
          </Text>
        </Pressable>
        <Pressable onPress={hideRegistration}>
          <Text style={styles.backText}>Naspäť na prihlásenie</Text>
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
  wrongRegistrationText: {
    color: '#109CF1',
    fontSize: 18,
    textAlign: 'center',
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
  backText: {
    marginTop: 10,
    fontSize: 14,
    color: '#109CF1',
    textAlign: 'center',
  },
  disabledButton: {
    borderColor: '#cccccc',
    backgroundColor: '#f3f3f3',
    borderStyle: 'solid',
    borderWidth: 2,
    width: '90%',
    marginLeft: '5%',
    height: 50,
    marginTop: 15,
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10,
  },
  disabledButtonText: {
    fontSize: 20,
    color: '#cccccc',
  },
});
