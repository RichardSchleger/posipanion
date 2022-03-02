import axios from 'axios';
import API from '../Api/API';
import AsyncStorage from '@react-native-async-storage/async-storage';

const login = (e, email, password, setRefresh, setOpacity) => {
  axios
    .post(API.url + 'authenticate', {
      username: email,
      password: password,
    })
    .then(async r => {
      try {
        await AsyncStorage.setItem('AuthToken', r.data.token);
        setRefresh(c => !c);
      } catch (error) {
        // error
      }
    })
    .catch(error => {
      console.log(error);
      setOpacity(1);
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

export default {login, getToken};
