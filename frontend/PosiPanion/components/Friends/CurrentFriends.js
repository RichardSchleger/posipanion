import React from 'react';
import {
  Dimensions,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import {FontAwesomeIcon} from '@fortawesome/react-native-fontawesome';
import {faCheck, faCircle, faTimes} from '@fortawesome/free-solid-svg-icons';
import AuthService from '../AuthService/AuthService';
import axios from 'axios';
import API from '../Api/API';

export default function CurrentFriends({
  pendingFriends,
  confirmedFriends,
  setRefresh,
}) {
  const acceptFriend = async (e, id) => {
    e.preventDefault();
    if (!id) {
      return;
    }

    const token = await AuthService.getToken();
    axios
      .post(
        API.url + 'friend/request/confirm/' + id,
        {},
        {
          headers: {Authorization: 'Bearer ' + token},
        },
      )
      .then(() => {
        setRefresh(c => !c);
      })
      .catch(async error => {
        console.log(error);
        if (error.response.status === 606) {
          if (await AuthService.refreshToken()) {
            return acceptFriend(e, id);
          }
        }
      });
  };

  const rejectFriend = async (e, id) => {
    e.preventDefault();
    if (!id) {
      return;
    }

    const token = await AuthService.getToken();
    axios
      .post(
        API.url + 'friend/request/reject/' + id,
        {},
        {
          headers: {Authorization: 'Bearer ' + token},
        },
      )
      .then(() => {
        setRefresh(c => !c);
      })
      .catch(async error => {
        if (error.response.status === 606) {
          if (await AuthService.refreshToken()) {
            return rejectFriend(e, id);
          }
        }
      });
  };

  const removeFriend = async (e, id) => {
    e.preventDefault();
    if (!id) {
      return;
    }

    const token = await AuthService.getToken();
    axios
      .post(
        API.url + 'friend/remove/' + id,
        {},
        {
          headers: {Authorization: 'Bearer ' + token},
        },
      )
      .then(() => {
        setRefresh(c => !c);
      })
      .catch(async error => {
        if (error.response.status === 606) {
          if (await AuthService.refreshToken()) {
            return rejectFriend(e, id);
          }
        }
      });
  };

  return (
    <ScrollView style={styles.currentFriends}>
      <Text style={styles.friendTitle}>Žiadosti o priateľstvo</Text>
      {pendingFriends.map((friend, index) => (
        <View
          style={styles.pendingFriendContainer}
          key={'pendingFriend' + index}>
          <Text style={styles.darkText}>
            {friend.firstName + ' ' + friend.surname}
          </Text>
          <View style={styles.pendingFriendButtonContainer}>
            {friend.canConfirm && (
              <Pressable
                onPress={event => {
                  acceptFriend(event, friend.friendId);
                }}>
                <FontAwesomeIcon
                  icon={faCheck}
                  size={40}
                  style={styles.acceptFriendIcon}
                />
              </Pressable>
            )}
            <Pressable
              onPress={event => {
                rejectFriend(event, friend.friendId);
              }}>
              <FontAwesomeIcon
                icon={faTimes}
                size={40}
                style={styles.removeFriendIcon}
              />
            </Pressable>
          </View>
        </View>
      ))}
      <View
        style={{
          borderBottomColor: '#109CF1',
          borderBottomWidth: 1,
          marginTop: 5,
          marginBottom: 5,
        }}
      />
      <Text style={styles.friendTitle}>Potvrdení priatelia</Text>
      {confirmedFriends.map((friend, index) => (
        <View
          style={styles.confirmedFriendContainer}
          key={'confirmedFriend' + index}>
          <Text style={styles.darkText}>
            {friend.firstName + ' ' + friend.surname + ' '}
            {friend.lastKnownLatitude && friend.lastKnownLongitude ? (
              <FontAwesomeIcon icon={faCircle} style={styles.activeUserIcon} />
            ) : (
              <FontAwesomeIcon
                icon={faCircle}
                style={styles.inactiveUserIcon}
              />
            )}
          </Text>
          <Pressable
            onPress={event => {
              removeFriend(event, friend.friendId);
            }}>
            <FontAwesomeIcon
              icon={faTimes}
              size={40}
              style={styles.removeFriendIcon}
            />
          </Pressable>
        </View>
      ))}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  currentFriends: {
    top: 0,
    height: '100%',
    width: '100%',
    padding: 0,
  },

  pendingFriendContainer: {
    width: '100%',
    height: Dimensions.get('window').height / 15,
    backgroundColor: '#eeeeee',
    marginBottom: 5,
    marginLeft: 0,
    borderRadius: 5,
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingLeft: 10,
  },

  pendingFriendButtonContainer: {
    display: 'flex',
    flexDirection: 'row',
    height: '100%',
    alignItems: 'center',
  },

  confirmedFriendContainer: {
    width: '100%',
    height: Dimensions.get('window').height / 15,
    backgroundColor: '#eeeeee',
    marginBottom: 5,
    marginLeft: 0,
    borderRadius: 5,
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingLeft: 10,
  },

  activeUserIcon: {
    color: '#40cf4a',
  },

  inactiveUserIcon: {
    color: '#999999',
  },

  removeFriendIcon: {
    color: '#eb3636',
    marginRight: 10,
  },

  acceptFriendIcon: {
    color: '#21af29',
    marginRight: 10,
  },

  friendTitle: {
    fontSize: 16,
    color: '#109CF1',
  },

  darkText: {
    color: '#000000',
  },
});
