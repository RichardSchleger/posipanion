import React, {useEffect, useState} from 'react';
import type {Node} from 'react';

import AuthService from './components/AuthService/AuthService';
import MapContainer from './components/Map/MapContainer';
import {useTimingReducer} from './components/Map/reducer';
import {useLocation} from './components/hooks';

// import PushNotificationIOS from '@react-native-community/push-notification-ios';
import PushNotification from 'react-native-push-notification';
import Firebase from '@react-native-firebase/app';
import messaging from '@react-native-firebase/messaging';
import Login from './components/Login/Login';

const App: () => Node = () => {
  const [state, dispatch] = useTimingReducer();

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loginRefresh, setLoginRefresh] = useState(false);

  useEffect(() => {
    Firebase.initializeApp(this)
      .then(r => console.log(r))
      .catch(e => console.log(e));
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
            channelId: 'posipanion_channel',
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
      channelId: 'posipanion_channel',
      channelName: 'PosiPanion Channel',
    });
  };

  if (isLoggedIn) {
    return (
      // <SafeAreaView style={backgroundStyle}>
      //   <FallDetector />
      //   <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      //   <ScrollView
      //     contentInsetAdjustmentBehavior="automatic"
      //     style={backgroundStyle}>
      //     <Header />
      //     <View
      //       style={{
      //         backgroundColor: isDarkMode ? Colors.black : Colors.white,
      //       }}>
      //       <Section title="Step One">
      //         Edit <Text style={styles.highlight}>App.js</Text> to change this
      //         screen and then come back to see your edits.
      //       </Section>
      //       <Section title="See Your Changes">
      //         <ReloadInstructions />
      //       </Section>
      //       <Section title="Debug">
      //         <DebugInstructions />
      //       </Section>
      //       <Section title="Learn More">
      //         Read the docs to discover what to do next:
      //       </Section>
      //       <LearnMoreLinks />
      //     </View>
      //   </ScrollView>
      // </SafeAreaView>
      <MapContainer refresh={loginRefresh} setRefresh={setLoginRefresh} />
    );
  } else {
    return <Login refresh={loginRefresh} setRefresh={setLoginRefresh} />;
  }
};

// const styles = StyleSheet.create({
//   sectionContainer: {
//     marginTop: 32,
//     paddingHorizontal: 24,
//   },
//   sectionTitle: {
//     fontSize: 24,
//     fontWeight: '600',
//   },
//   sectionDescription: {
//     marginTop: 8,
//     fontSize: 18,
//     fontWeight: '400',
//   },
//   highlight: {
//     fontWeight: '700',
//   },
// });

export default App;
