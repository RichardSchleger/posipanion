import React, {useEffect, useState} from 'react';

import AuthService from './components/AuthService/AuthService';
import MapContainer from './components/Map/MapContainer';
import {useTimingReducer} from './components/Map/reducer';
import {useLocation} from './components/hooks';

// import PushNotificationIOS from '@react-native-community/push-notification-ios';
import PushNotification from 'react-native-push-notification';
import Firebase from '@react-native-firebase/app';
import messaging from '@react-native-firebase/messaging';
import Login from './components/Login/Login';
import {AppRegistry, Platform} from 'react-native';

const App = () => {
  const [state, dispatch] = useTimingReducer();

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loginRefresh, setLoginRefresh] = useState(false);

  useEffect(() => {
    Firebase.initializeApp(this)
      .then(r => console.log(r))
      .catch(e => console.log(e));

    if(Platform.OS === 'ios') {

    } else {
      createChannels();
      messaging().setBackgroundMessageHandler(async remoteMessage => {
        console.log('Message handled in the background!', remoteMessage);
      });
      PushNotification.configure({
        onRegister: function (token) {
          console.log('TOKEN:', token);
        },

        onNotification: function (notification) {
          console.log('NOTIFICATION:', notification);

          if (notification.foreground) {
            PushNotification.localNotification({
              channelId: 'posipanion',
              title: notification.title,
              message: notification.message,
            });
          }
        },

        senderID: '399407603065',

        permissions: {
          alert: true,
          badge: true,
          sound: true,
        },

        popInitialNotification: true,

        requestPermissions: true,
      });
    }
  }, []);

  useEffect(() => {
    const checkLogin = async () => {
      const token = await AuthService.getToken();
      if (token) {
        setIsLoggedIn(true);
      } else {
        setIsLoggedIn(false);
      }
    };
    checkLogin();
  }, [loginRefresh]);

  useLocation(state, dispatch);

  const createChannels = () => {
    PushNotification.createChannel({
      channelId: 'posipanion',
      channelName: 'PosiPanion',
    });
  };

  if (isLoggedIn) {
    return <MapContainer setRefresh={setLoginRefresh} />;
  } else {
    return <Login refresh={loginRefresh} setRefresh={setLoginRefresh} />;
  }
};

export default App;

AppRegistry.registerComponent('PosiPanion', () => App);
