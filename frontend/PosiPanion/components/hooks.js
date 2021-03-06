import {useEffect, useState} from 'react';
import {
  AppState,
  DeviceEventEmitter,
  NativeModules,
  BackHandler,
} from 'react-native';
import RNLocation from 'react-native-location';
import {Actions} from './Map/reducer';

RNLocation.configure({
  distanceFilter: 0, // Meters
  desiredAccuracy: {
    ios: 'best',
    android: 'highAccuracy',
  },
  // Android only
  // androidProvider: 'auto',
  interval: 5000, // Milliseconds
  fastestInterval: 5000, // Milliseconds
  maxWaitTime: 5000, // Milliseconds
  // iOS Only
  activityType: 'fitness',
  allowsBackgroundLocationUpdates: true,
  headingFilter: 0, // Degrees
  headingOrientation: 'portrait',
  pausesLocationUpdatesAutomatically: false,
  showsBackgroundLocationIndicator: true,
});

export const useLocation = (state, dispatch) => {
  const [listener, setListener] = useState(null);

  return useEffect(() => {
    const checkPermission = () => {
      RNLocation.getCurrentPermission().then(currentPermission => {
        if (
          currentPermission === 'notDetermined' ||
          currentPermission === 'denied' ||
          currentPermission === 'restricted'
        ) {
          RNLocation.requestPermission({
            ios: 'whenInUse',
            android: {
              detail: 'fine',
              rationale: {
                title: 'Povoľte prosím prístup k vašej polohe',
                message:
                  'Pre správne fungovanie aplikácie je potrebný prístup k vašej polohe.',
                buttonPositive: 'OK',
                buttonNegative: 'Zrušiť',
              },
            },
          }).then(granted => {
            if (granted) {
              dispatch({type: Actions.LocationRequested, granted});
            } else {
              BackHandler.exitApp();
            }
          });
        }

        const granted =
          currentPermission !== 'denied' && currentPermission !== 'restricted';
        dispatch({type: Actions.LocationRequested, granted});
      });
    };

    if (typeof state.granted === 'undefined') {
      checkPermission();
    }

    const handleAppStateChange = nextAppState => {
      if (nextAppState === 'active') {
        checkPermission();
      }
    };

    setListener(AppState.addEventListener('change', handleAppStateChange));

    return () => {
      if (listener) {
        listener.remove();
        setListener(null);
      }
    };
  }, [dispatch, state.granted]);
};

export const useNativeLocationTracking = (state, dispatch) => {

  return useEffect(() => {
    let subscription;
    if (!state.running) {
      NativeModules.LocationManager.stopBackgroundLocation();
      typeof subscription !== 'undefined' && subscription.remove();
      return;
    }

    if (state.granted) {
      subscription = DeviceEventEmitter.addListener(
        NativeModules.LocationManager.JS_LOCATION_EVENT_NAME,
        e => {
          dispatch({
            type: Actions.UpdatePosition,
            position: {
              lat: e.latitude,
              lng: e.longitude,
              alti: e.altitude,
              timestamp: e.timestamp,
            },
          });
        },
      );

      NativeModules.LocationManager.startBackgroundLocation();
    }

    return () => {
      NativeModules.LocationManager.stopBackgroundLocation();
      typeof subscription !== 'undefined' && subscription.remove();
    };
  }, [state.granted, state.running, dispatch]);
};

export const useLocationTracking = (state, dispatch) => {
  return useEffect(() => {
    let unsubscribe;
    if (!state.running) {
      typeof unsubscribe !== 'undefined' && unsubscribe();
      return;
    }

    if (state.granted) {
      unsubscribe = RNLocation.subscribeToLocationUpdates(locations => {
        locations.forEach(l => {
          dispatch({
            type: Actions.UpdatePosition,
            position: {
              accuracy: l.accuracy,
              lat: l.latitude,
              lng: l.longitude,
              alti: l.altitude,
              timestamp: l.timestamp,
            },
          });
        });
      });
    }

    return () => {
      typeof unsubscribe !== 'undefined' && unsubscribe();
    };
  }, [state.granted, state.running, dispatch]);
};
