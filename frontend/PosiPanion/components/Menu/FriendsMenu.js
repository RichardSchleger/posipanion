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
import {
  faArrowLeft,
  faPlus,
  faSearch,
  faUserPlus,
} from '@fortawesome/free-solid-svg-icons';
import CurrentFriends from '../Friends/CurrentFriends';

export default function FriendsMenu({showMapMenu}) {
  const [pendingFriends, setPendingFriends] = useState([]);
  const [confirmedFriends, setConfirmedFriends] = useState([]);
  const [showCurrentFriends, setShowCurrentFriends] = useState(true);
  const [searchText, setSearchText] = useState('');
  const [foundUsers, setFoundUsers] = useState([]);
  const [refresh, setRefresh] = useState(false);

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

    if (showCurrentFriends) {
      getData();
    }
  }, [showCurrentFriends, refresh]);

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
    setFoundUsers(fetchedUsers);
  };

  const handleBack = e => {
    e.preventDefault();
    if (showCurrentFriends) {
      showMapMenu(e);
    } else {
      setShowCurrentFriends(true);
    }
  };

  const showNewFriend = e => {
    e.preventDefault();
    setShowCurrentFriends(false);
  };

  const sendFriendRequest = async (e, id) => {
    e.preventDefault();
    if (!id) {
      return;
    }

    const token = await AuthService.getToken();
    axios
      .post(
        API.url + 'friend/request/send/' + id,
        {},
        {
          headers: {Authorization: 'Bearer ' + token},
        },
      )
      .then(() => {
        setShowCurrentFriends(true);
      })
      .catch(async error => {
        if (error.response.status === 606) {
          if (await AuthService.refreshToken()) {
            return sendFriendRequest(e, id);
          }
        }
      });
  };

  const button_back = {
    position: 'absolute',
    width: showCurrentFriends ? '49%' : '100%',
    height: '10%',
    left: showCurrentFriends ? '51%' : 0,
    top: '87%',
  };

  return (
    <View style={styles.friendsMenu}>
      {!showCurrentFriends && (
        <Pressable style={styles.search_icon} onPress={searchUsers}>
          <FontAwesomeIcon style={styles.icon} icon={faSearch} size={40} />
        </Pressable>
      )}
      {!showCurrentFriends && (
        <TextInput
          style={styles.search_bar}
          value={searchText}
          onChangeText={text => {
            setSearchText(text);
          }}
        />
      )}
      {showCurrentFriends && (
        <View style={styles.currentFriends}>
          <CurrentFriends
            pendingFriends={pendingFriends}
            confirmedFriends={confirmedFriends}
            setRefresh={setRefresh}
          />
        </View>
      )}
      {!showCurrentFriends && (
        <View style={styles.newFriends}>
          <ScrollView>
            {foundUsers.map((user, index) => (
              <View style={styles.friendContainer} key={'foundUser' + index}>
                <Text>{user.firstName + ' ' + user.surname}</Text>
                <Pressable
                  style={[styles.button, styles.addFriendButton]}
                  onPress={event => {
                    sendFriendRequest(event, user.id);
                  }}>
                  <FontAwesomeIcon
                    style={styles.icon}
                    icon={faPlus}
                    size={25}
                  />
                </Pressable>
              </View>
            ))}
          </ScrollView>
        </View>
      )}
      <Pressable
        style={[styles.button, styles.new_friend_button]}
        onPress={showNewFriend}>
        <Text style={styles.button_text}>
          <FontAwesomeIcon icon={faUserPlus} style={styles.button_text} />{' '}
          Pridať priateľa
        </Text>
      </Pressable>
      <Pressable style={[styles.button, button_back]} onPress={handleBack}>
        <Text style={styles.button_text}>
          <FontAwesomeIcon icon={faArrowLeft} style={styles.button_text} /> Späť
        </Text>
      </Pressable>
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

  new_friend_button: {
    position: 'absolute',
    width: '49%',
    height: '10%',
    left: 0,
    top: '87%',
  },

  addFriendButton: {
    width: Dimensions.get('window').width / 10,
    height: Dimensions.get('window').width / 10,
  },

  button_text: {
    fontSize: 16,
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
    fontSize: 20,
    paddingBottom: 5,
  },

  search_icon: {
    position: 'absolute',
    top: '2%',
    right: '5%',
    zIndex: 10,
  },

  currentFriends: {
    height: '87%',
    width: '100%',
    top: 0,
    margin: 0,
    padding: 0,
  },

  newFriends: {
    height: '72%',
    width: '100%',
    top: '5%',
    margin: 0,
    padding: 0,
  },

  friendContainer: {
    width: '100%',
    height: Dimensions.get('window').height / 15,
    backgroundColor: '#EEEEEE',
    color: '#000000',
    marginBottom: 5,
    marginLeft: 0,
    borderRadius: 5,
    paddingLeft: 10,
    paddingRight: 10,
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
});
