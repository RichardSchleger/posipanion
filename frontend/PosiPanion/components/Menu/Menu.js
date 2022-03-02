import React, {useEffect, useRef, useState} from 'react';

import {FontAwesomeIcon} from '@fortawesome/react-native-fontawesome';
import {
  faCog,
  faUserFriends,
  faSearch,
  faDoorOpen,
} from '@fortawesome/free-solid-svg-icons';

import {
  Animated,
  Dimensions,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function Menu({show, refresh, setRefresh, friends}) {
  const offsetY = useRef(new Animated.Value(0)).current;
  const [shownFriends, setShownFriends] = useState([]);
  const [filterText, setFilterText] = useState('');
  const [mapMenuShown, setMapMenuShown] = useState(true);

  useEffect(() => {
    const slideIntoView = () => {
      Animated.timing(offsetY, {
        toValue: -((Dimensions.get('window').height / 3) * 2 - 50),
        duration: 500,
        useNativeDriver: true,
      }).start();
    };

    const slideOutOfView = () => {
      Animated.timing(offsetY, {
        toValue: 0,
        duration: 500,
        useNativeDriver: true,
      }).start();
    };

    if (show) {
      slideIntoView();
    } else {
      slideOutOfView();
    }
  }, [offsetY, show]);

  useEffect(() => {
    const filterFriendList = text => {
      if (text === '') {
        setShownFriends(friends);
      } else {
        setShownFriends(
          friends.filter(friend =>
            (friend.firstName + ' ' + friend.surname)
              .toLowerCase()
              .normalize('NFD')
              .replace(/[\u0300-\u036f]/g, '')
              .includes(
                text
                  .toLowerCase()
                  .normalize('NFD')
                  .replace(/[\u0300-\u036f]/g, ''),
              ),
          ),
        );
      }
    };

    filterFriendList(filterText);
  }, [filterText, friends]);

  const showMapMenu = e => {
    e.preventDefault();
    setMapMenuShown(true);
  };

  const showRideMenu = e => {
    e.preventDefault();
    setMapMenuShown(false);
  };

  const handleLogout = async e => {
    e.preventDefault();
    try {
      await AsyncStorage.removeItem('AuthToken');
      setRefresh(!refresh);
    } catch (error) {
      // saving error
    }
  };

  return (
    <Animated.View style={[styles.menu, {transform: [{translateY: offsetY}]}]}>
      <Pressable
        style={[
          styles.button,
          styles.button_map,
          mapMenuShown ? styles.button_active : '',
        ]}
        onPress={showMapMenu}>
        <Text
          style={[
            styles.button_text,
            mapMenuShown ? styles.button_text_active : '',
          ]}>
          MAPA
        </Text>
      </Pressable>
      <Pressable
        style={[
          styles.button,
          styles.button_ride,
          mapMenuShown ? '' : styles.button_active,
        ]}
        onPress={showRideMenu}>
        <Text
          style={[
            styles.button_text,
            mapMenuShown ? '' : styles.button_text_active,
          ]}>
          JAZDA
        </Text>
      </Pressable>
      <FontAwesomeIcon
        style={[styles.icon, styles.search_icon]}
        icon={faSearch}
        size={40}
      />
      <TextInput
        style={styles.search_bar}
        value={filterText}
        onChangeText={text => {
          setFilterText(text);
        }}
      />
      <View style={styles.friends_container}>
        <ScrollView>
          {shownFriends.map((friend, index) => (
            <View style={styles.friend_container} key={'friend_' + index}>
              <Text>{friend.firstName + ' ' + friend.surname}</Text>
            </View>
          ))}
        </ScrollView>
      </View>
      <Pressable style={[styles.button, styles.button_config]}>
        <FontAwesomeIcon style={styles.icon} icon={faCog} size={40} />
      </Pressable>
      <Pressable style={[styles.button, styles.button_friends]}>
        <FontAwesomeIcon style={styles.icon} icon={faUserFriends} size={40} />
      </Pressable>
      <Pressable
        style={[styles.button, styles.button_logout]}
        onPress={handleLogout}>
        <FontAwesomeIcon style={styles.icon} icon={faDoorOpen} size={40} />
      </Pressable>
    </Animated.View>
  );
}

const styles = StyleSheet.create({
  menu: {
    position: 'absolute',
    bottom: -(Dimensions.get('window').height / 3) * 2,
    width: Dimensions.get('window').width,
    height: (Dimensions.get('window').height / 3) * 2,
    borderRadius: Dimensions.get('window').width / 10,
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 5,
    backgroundColor: '#FFFFFF',
    zIndex: 100,
    display: 'flex',
    paddingTop: 30,
    paddingLeft: 10,
    paddingRight: 10,
  },

  button: {
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 3,
    backgroundColor: '#FFFFFF',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 50,
  },

  button_map: {
    width: '45%',
    height: '10%',
    position: 'absolute',
    left: '5%',
    top: '4%',
  },

  button_ride: {
    width: '45%',
    height: '10%',
    position: 'absolute',
    left: '55%',
    top: '4%',
  },

  button_config: {
    position: 'absolute',
    width: Dimensions.get('window').width / 7,
    height: Dimensions.get('window').width / 7,
    left: '5%',
    top: '84%',
  },

  button_friends: {
    position: 'absolute',
    width: Dimensions.get('window').width / 7,
    height: Dimensions.get('window').width / 7,
    left: '25%',
    top: '84%',
  },

  button_logout: {
    position: 'absolute',
    width: Dimensions.get('window').width / 7,
    height: Dimensions.get('window').width / 7,
    left: '45%',
    top: '84%',
  },

  icon: {
    color: '#109CF1',
    width: '',
  },

  button_active: {
    borderColor: '#109CF1',
    borderStyle: 'solid',
    borderWidth: 3,
    backgroundColor: '#109CF1',
  },

  button_text: {
    fontSize: 20,
    color: '#109CF1',
  },

  button_text_active: {
    color: '#FFFFFF',
  },

  search_bar: {
    top: '12%',
    borderBottomColor: '#109CF1',
    borderBottomWidth: 3,
    color: '#109CF1',
    fontSize: 24,
    paddingBottom: 5,
  },

  search_icon: {
    position: 'absolute',
    top: '19%',
    right: '5%',
  },

  friends_container: {
    top: '32%',
    height: '50%',
    width: '100%',
    left: '2.5%',
    flex: 1,
    position: 'absolute',
  },

  friend_container: {
    width: '100%',
    height: Dimensions.get('window').height / 15,
    backgroundColor: '#EEEEEE',
    marginBottom: 5,
    borderRadius: 5,
    display: 'flex',
    justifyContent: 'center',
    paddingLeft: 10,
  },
});
