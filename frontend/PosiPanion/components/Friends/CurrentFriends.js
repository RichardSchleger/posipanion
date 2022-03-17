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
import {faCircle, faTimes} from '@fortawesome/free-solid-svg-icons';

export default function CurrentFriends({pendingFriends, confirmedFriends}) {
  return (
    <ScrollView style={styles.currentFriends}>
      {pendingFriends.map((friend, index) => (
        <View
          style={styles.pendingFriendContainer}
          key={'pendingFriend' + index}>
          <Text>{friend.firstName + ' ' + friend.surname}</Text>
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
      {confirmedFriends.map((friend, index) => (
        <View
          style={styles.pendingFriendContainer}
          key={'confirmedFriend' + index}>
          <Text>
            {friend.firstName + ' ' + friend.surname + ' '}
            {friend.lastKnownLatitude && friend.lastKnownLongitude ? (
              <FontAwesomeIcon icon={faCircle} style={styles.activeUserIcon} />
            ) : (
              ''
            )}
          </Text>
          <Pressable style={styles.removeFriend}>
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
    backgroundColor: '#EEEEEE',
    marginBottom: 5,
    marginLeft: 0,
    borderRadius: 5,
    display: 'flex',
    justifyContent: 'center',
    paddingLeft: 10,
  },

  confirmedFriendContainer: {
    width: '100%',
    height: Dimensions.get('window').height / 15,
    backgroundColor: '#EEEEEE',
    marginBottom: 5,
    marginLeft: 0,
    borderRadius: 5,
    display: 'flex',
    justifyContent: 'center',
  },

  activeUserIcon: {
    color: '#40cf4a',
  },

  removeFriend: {
    position: 'absolute',
    right: 20,
  },

  removeFriendIcon: {
    color: '#eb3636',
  },
});
