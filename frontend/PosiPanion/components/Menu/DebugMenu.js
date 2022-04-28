import React, {useEffect, useState} from 'react';
import {
  View,
  Pressable,
  Text,
  StyleSheet,
  Dimensions,
  ScrollView,
} from 'react-native';
import axios from 'axios';
import API from '../Api/API';
import AuthService from '../AuthService/AuthService';
import {FontAwesomeIcon} from '@fortawesome/react-native-fontawesome';
import {
  faArrowLeft,
  faRemove,
} from '@fortawesome/free-solid-svg-icons';

var logs = [];

export default function DebugMenu({showMapMenu}) {

  const button_back = {
    position: 'absolute',
    width: '49%',
    height: '10%',
    left: '51%',
    top: '87%',
  };

  const handleBack = e => {
    e.preventDefault();
    showMapMenu(e);
  };

  const cleanDebug = e => {
    e.preventDefault();
    logs = [];
  }

  return (
    <View style={styles.friendsMenu}>

      <View style={styles.text_container}>
        <ScrollView>
          {logs.map((log, index) => (
            <Text key={'log_' + index} style={styles.darkText}>{log}</Text>
          ))}
        </ScrollView>
      </View>
      <Pressable
        style={[styles.button, styles.new_friend_button]}
        onPress={cleanDebug}>
        <Text style={styles.button_text}>
          <FontAwesomeIcon icon={faRemove} style={styles.button_text} />{' '}
          Zmazať
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

export const log = message => {
  logs.unshift(new Date().toLocaleTimeString('sk-SK', {hour: 'numeric', minute:'numeric', second:'numeric'}) + ": " + message);
};

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

  text_container: {
    top: 0,
    height: '86%',
    width: '100%',
    padding: 0,
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

  darkText: {
    color: '#000000',
  },
});
