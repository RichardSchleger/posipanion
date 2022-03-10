import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-native-fontawesome';
import {faSearch} from '@fortawesome/free-solid-svg-icons';
import {
  Dimensions,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import Pressable from 'react-native/Libraries/Components/Pressable/Pressable';

export default function MapMenu({friends, showUserDetail}) {
  const [shownFriends, setShownFriends] = useState([]);
  const [filterText, setFilterText] = useState('');

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

  return (
    <View style={styles.mapMenu}>
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
            <Pressable
              style={styles.friend_container}
              key={'friend_' + index}
              onPress={event => {
                showUserDetail(event, friend.id);
              }}>
              <Text>{friend.firstName + ' ' + friend.surname}</Text>
            </Pressable>
          ))}
        </ScrollView>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  mapMenu: {
    top: '10%',
    height: '65%',
    width: '95%',
    marginLeft: '2.5%',
    padding: 0,
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
  },

  friends_container: {
    top: 25,
    height: '75%',
    width: '100%',
    marginLeft: 0,
    paddingLeft: 0,
  },

  friend_container: {
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
});
