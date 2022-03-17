import React, {useEffect, useState} from 'react';
import {
  Dimensions,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import axios from 'axios';
import API from '../Api/API';
import AuthService from '../AuthService/AuthService';
import {FontAwesomeIcon} from '@fortawesome/react-native-fontawesome';
import {faSearch} from '@fortawesome/free-solid-svg-icons';
import CurrentFriends from '../Friends/CurrentFriends';

export default function FriendsMenu({}) {
  const [pendingFriends, setPendingFriends] = useState([]);
  const [confirmedFriends, setConfirmedFriends] = useState([]);
  const [showCurrentFriends, setShowCurrentFriends] = useState(true);
  const [searchText, setSearchText] = useState('');
  const [foundUsers, setFoundUsers] = useState([]);

  useEffect(() => {
    const fetchPendingFriends = async () => {
      const token = await AuthService.getToken();
      return await axios
        .get(API.url + 'user/friends/pending', {
          headers: {Authorization: 'Bearer ' + token},
        })
        .then(response => response.data)
        .catch(async error => {
          if (error.response.status === 606) {
            if (await AuthService.refreshToken()) {
              return fetchPendingFriends();
            }
          }
        });
    };

    const fetchConfirmedFriends = async () => {
      const token = await AuthService.getToken();
      return await axios
        .get(API.url + 'user/friends/confirmed', {
          headers: {Authorization: 'Bearer ' + token},
        })
        .then(response => response.data)
        .catch(async error => {
          if (error.response.status === 606) {
            if (await AuthService.refreshToken()) {
              return fetchPendingFriends();
            }
          }
        });
    };

    const getData = async () => {
      const fetchedPendingFriends = await fetchPendingFriends();
      const fetchedConfirmedFriends = await fetchConfirmedFriends();

      setPendingFriends(fetchedPendingFriends);
      setConfirmedFriends(fetchedConfirmedFriends);
    };
    getData();
  }, []);

  const fetchUsersByText = async () => {
    const token = await AuthService.getToken();
    if (searchText !== '') {
      return await axios
        .get(
          API.url +
            'user/find/' +
            searchText
              .toLowerCase()
              .normalize('NFD')
              .replace(/[\u0300-\u036f]/g, ''),
          {
            headers: {Authorization: 'Bearer ' + token},
          },
        )
        .then(response => response.data)
        .catch(async error => {
          if (error.response.status === 606) {
            if (await AuthService.refreshToken()) {
              return fetchUsersByText();
            }
          }
        });
    }
  };

  const searchUsers = async e => {
    e.preventDefault();
    setShowCurrentFriends(false);
    const fetchedUsers = await fetchUsersByText();
    console.log(fetchedUsers);
    setFoundUsers(fetchedUsers);
  };

  return (
    <View style={styles.friendsMenu}>
      <Pressable style={styles.search_icon} onPress={searchUsers}>
        <FontAwesomeIcon style={styles.icon} icon={faSearch} size={40} />
      </Pressable>
      <TextInput
        style={styles.search_bar}
        value={searchText}
        onChangeText={text => {
          setSearchText(text);
        }}
      />
      {showCurrentFriends && (
        <View style={styles.currentFriends}>
          <CurrentFriends
            pendingFriends={pendingFriends}
            confirmedFriends={confirmedFriends}
          />
        </View>
      )}
      {!showCurrentFriends && (
        <ScrollView style={styles.currentFriends}>
          {foundUsers.map((user, index) => (
            <View style={styles.friendContainer} key={'foundUser' + index}>
              <Text>{user.firstName + ' ' + user.surname}</Text>
            </View>
          ))}
        </ScrollView>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  friendsMenu: {
    top: 0,
    height: '90%',
    width: '100%',
    padding: 0,
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

  button_text: {
    fontSize: 20,
    color: '#109CF1',
  },

  icon: {
    color: '#109CF1',
    width: '',
  },

  search_bar: {
    top: '2%',
    borderBottomColor: '#109CF1',
    borderBottomWidth: 3,
    color: '#109CF1',
    fontSize: 24,
    paddingBottom: 5,
  },

  search_icon: {
    position: 'absolute',
    top: '2%',
    right: '5%',
    zIndex: 10,
  },

  currentFriends: {
    height: '80%',
    width: '100%',
    top: '6%',
    margin: 0,
    padding: 0,
  },

  friendContainer: {
    width: '100%',
    height: Dimensions.get('window').height / 15,
    backgroundColor: '#EEEEEE',
    marginBottom: 5,
    marginLeft: 0,
    borderRadius: 5,
    display: 'flex',
    justifyContent: 'center',
  },
});
