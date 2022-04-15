import axios from 'axios';
import API from '../Api/API';
import AsyncStorage from '@react-native-async-storage/async-storage';

import firebase from '@react-native-firebase/app';
import messaging from '@react-native-firebase/messaging';

const login = (e, email, password, setRefresh, setOpacity) => {
  axios
    .post(API.url + 'authenticate', {
      username: email,
      password: password,
    })
    .then(async r => {
      messaging()
        .getToken(firebase.app().options.messagingSenderId)
        .then(token => {
          axios
            .post(
              API.url + 'fcmtoken',
              {
                token: token,
              },
              {
                headers: {Authorization: 'Bearer ' + r.data.token},
              },
            )
            .then(async () => {
              try {
                await AsyncStorage.setItem('AuthToken', r.data.token);
                setRefresh(c => !c);
              } catch (error) {
                // error
              }
            })
            .catch(error => {
              console.log(error);
            });
        });
    })
    .catch(error => {
      console.log(error);
      setOpacity(1);
    });
};

const googleLogin = (idToken, setRefresh) => {
  axios
    .post(API.url + 'google/authenticate', {
      idToken: idToken,
    })
    .then(async r => {
      messaging()
        .getToken(firebase.app().options.messagingSenderId)
        .then(token => {
          axios
            .post(
              API.url + 'fcmtoken',
              {
                token: token,
              },
              {
                headers: {Authorization: 'Bearer ' + r.data.token},
              },
            )
            .then(async () => {
              try {
                await AsyncStorage.setItem('AuthToken', r.data.token);
                setRefresh(c => !c);
              } catch (error) {
                // error
              }
            })
            .catch(error => {
              console.log(error);
            });
        });
    })
    .catch(error => {
      console.log(error);
    });
};

const getToken = async () => {
  try {
    const value = await AsyncStorage.getItem('AuthToken');
    if (value !== null) {
      return value;
    }
  } catch (e) {
    // error
  }
};

const logout = async setRefresh => {
  try {
    await AsyncStorage.removeItem('AuthToken');
    setRefresh(c => !c);
  } catch (error) {
    return false;
  }
};

const refreshToken = async () => {
  const token = await getToken();
  return axios
    .get(API.url + 'refreshtoken', {
      headers: {
        Authorization: 'Bearer ' + token,
        isRefreshToken: true,
      },
    })
    .then(async res => {
      try {
        await AsyncStorage.setItem('AuthToken', res.data.token);
        return true;
      } catch (error) {
        return false;
      }
    })
    .catch(e => {
      //error
    });
};

export default {login, googleLogin, getToken, logout, refreshToken};
