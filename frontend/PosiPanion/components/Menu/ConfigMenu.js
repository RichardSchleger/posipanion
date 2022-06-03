import React, {useEffect, useState} from 'react'; import {FontAwesomeIcon} from '@fortawesome/react-native-fontawesome'; 
import {
  faArrowLeft, faDoorOpen, faUpload, } from '@fortawesome/free-solid-svg-icons'; import { Image, Pressable, StyleSheet, 
  Text, TextInput, View,
} from 'react-native'; import AuthService from '../AuthService/AuthService'; import axios from 'axios'; import API from 
'../Api/API'; import {authorize} from 'react-native-app-auth'; import DocumentPicker from 'react-native-document-picker'; 
export default function ConfigMenu({showMapMenu, setLoginRefresh, setCode, setExpiresAt}) {
  const [user, setUser] = useState(null); const [refresh, setRefresh] = useState(false); const [operationText, 
  setOperationText] = useState(''); const [name, setName] = useState(''); const [surname, setSurname] = useState(''); 
  const [editShown, setEditShown] = useState(false); const config = {
    clientId: '68481', clientSecret: 'STRAVA_CLIENT_SECRET', redirectUrl: 
    'com.posipanion://oauthredirect', serviceConfiguration: {
      authorizationEndpoint: 'https://www.strava.com/oauth/mobile/authorize', tokenEndpoint:
        'https://www.strava.com/oauth/token?client_id=68481&client_secret=e6ac42d01e32aaada132ca5f6281034cdac4c54f',
    }, scopes: ['read,read_all,activity:write'],
  }; useEffect(() => {
    const fetchData = async () => {
      const token = await AuthService.getToken(); return axios
        .get(API.url + 'user/profile', {
          headers: {Authorization: 'Bearer ' + token},
        }) .then(response => {
          console.log(response.data); return response.data;
        }) .catch(async error => {
          console.log(error); if (error.response.status === 606) {
            if (await AuthService.refreshToken()) {
              return fetchData();
            }
          }
        });
    }; const getData = async () => {
      const response = await fetchData(); setUser(response);
    }; getData();
  }, [refresh]); const handleBack = e => {
    e.preventDefault(); if (editShown) {
      setEditShown(false);
    } else {
      showMapMenu(e);
    }
  }; const handleConnectToStrava = async e => {
    const authState = await authorize(config); const token = await AuthService.getToken(); const upload = 
    authState.scopes[0].includes('write'); axios
      .post(
        API.url + 'user/strava', {
          id: authState.tokenAdditionalParameters.athlete.id, accessToken: authState.accessToken, accessTokenExpiration: 
          new Date(authState.accessTokenExpirationDate), refreshToken: authState.refreshToken, upload: upload,
        }, {
          headers: {Authorization: 'Bearer ' + token},
        },
      ) .then(() => {
        setRefresh(!refresh); setOperationText('Pripojenie prebehlo úspešne!');
      }) .catch(async error => {
        if (error.response.status === 606) {
          if (await AuthService.refreshToken()) {
            return handleConnectToStrava(e);
          }
        }
      });
  }; const fetchStravaToken = token => {
    return axios
      .get(API.url + 'user/strava/token', {
        headers: {Authorization: 'Bearer ' + token},
      }) .then(res => res.data) .catch(async error => {
        if (error.response.status === 606) {
          if (await AuthService.refreshToken()) {
            return fetchStravaToken(token);
          }
        }
      });
  }; const handleDisconnectFromStrava = async e => {
    e.preventDefault(); const token = await AuthService.getToken(); const stravaAccessToken = await 
    fetchStravaToken(token); if (stravaAccessToken !== '') {
      deleteStravaDetails(token, stravaAccessToken);
    }
  }; const deleteStravaDetails = (token, stravaAccessToken) => {
    axios
      .post(
        'https://www.strava.com/oauth/deauthorize?access_token=' +
          stravaAccessToken,
      ) .then(() => {
        axios
          .delete(API.url + 'user/strava/', {
            headers: {Authorization: 'Bearer ' + token},
          }) .then(() => {
            setRefresh(!refresh); setOperationText('Odpojenie prebehlo úspešne!');
          }) .catch(async error => {
            if (error.response.status === 606) {
              if (await AuthService.refreshToken()) {
                return deleteStravaDetails(token, stravaAccessToken);
              }
            }
          });
      }) .catch(error => {
        console.log(error);
      });
  }; const showEditProfile = e => {
    e.preventDefault(); setName(user.firstName); setSurname(user.surname); setOperationText(''); setEditShown(true);
  }; const handleEditProfile = async e => {
    e.preventDefault(); const token = await AuthService.getToken(); axios
      .post(
        API.url + 'user/profile', {
          firstName: name, surname: surname,
        }, {
          headers: {Authorization: 'Bearer ' + token},
        },
      ) .then(() => {
        setEditShown(false); setOperationText('Profil úspešne upravený!'); setRefresh(!refresh);
      }) .catch(async error => {
        if (error.response.status === 606) {
          if (await AuthService.refreshToken()) {
            return handleEditProfile(e);
          }
        }
      });
  }; const handleLogout = async e => {
    e.preventDefault(); AuthService.logout(setLoginRefresh);
  }; const handleFileUpload = e => {
    e.preventDefault(); DocumentPicker.pickSingle({
      type: DocumentPicker.types.allFiles,
    })
      .then(res => {
        if (res.name.endsWith('.gpx')) {
          sendFile(res);
        } else {
          console.log(res.name + ' is not GPX file'); setOperationText('Nahrávaný súbor nie je GPX!');
        }
      }) .catch(err => {
        console.log(err);
      });
  }; const sendFile = async file => {
    const data = new FormData(); console.log(file); data.append('file', file); const token = await AuthService.getToken(); 
    const xhr = new XMLHttpRequest(); xhr.open('POST', API.url + 'user/uploadFile'); xhr.setRequestHeader('Authorization', 
    'Bearer ' + token); xhr.onload = () => {
      setOperationText('GPX súbor úspešne uložený!');
    }; xhr.onerror = async error => {
      setOperationText('Nepodarilo sa uložiť GPX súbor!'); if (error.response.status === 606) {
        if (await AuthService.refreshToken()) {
          return sendFile(file);
        }
      }
    }; xhr.send(data);
  }; const handleGenerateCode = async e => {
    e.preventDefault(); const token = await AuthService.getToken(); axios
      .get(API.url + 'generatecode', {
        headers: {Authorization: 'Bearer ' + token},
      }) .then(res => {
        setCode(res.data.code); setExpiresAt(res.data.expiresAt);
      }) .catch(async error => {
        if (
          error && error.response && error.response.status && error.response.status === 606
        ) {
          if (await AuthService.refreshToken()) {
            return handleGenerateCode(e);
          }
        }
      });
  }; const operationTextDisplay = {
    display: operationText === '' ? 'none' : 'flex',
  }; return (
    <View style={styles.configMenu}>
      {!editShown && [
        <Text style={styles.profile_text} key={'user_name'}>
          {user && user.firstName}
        </Text>, <Text style={styles.profile_text} key={'user_surname'}>
          {user && user.surname}
        </Text>, <Pressable
          style={[styles.button, styles.button_edit_profile]} onPress={showEditProfile} key={'edit_profile_button'}> <Text 
          style={styles.button_text}>Upraviť profil</Text>
        </Pressable>,
      ]} {editShown && [
        <Text style={styles.profile_input_label} key={'name_label'}>
          Meno
        </Text>, <TextInput
          style={styles.profile_input} value={name} onChangeText={text => setName(text)} key={'name_input'}
        />, <Text style={styles.profile_input_label} key={'surname_label'}>
          Priezvisko
        </Text>, <TextInput
          style={styles.profile_input} value={surname} onChangeText={text => setSurname(text)} key={'surname_input'}
        />, <Pressable
          style={[styles.button, styles.button_edit_profile]} onPress={handleEditProfile} key={'submit_button'}> <Text 
          style={styles.button_text}>Uložiť profil</Text>
        </Pressable>,
      ]} <Text style={[styles.connection_text, operationTextDisplay]}>
        {operationText}
      </Text> {user && user.stravaId === 0 && (
        <Pressable
          style={[styles.strava_button]} onPress={handleConnectToStrava}> <Text style={styles.strava_text}> Pripojiť k 
          </Text> <Image
            style={{
              width: '33%', height: '50%',
            }} source={require('../../images/strava.png')}
          />
        </Pressable>
      )} {user && user.stravaId !== 0 && (
        <Pressable
          style={[styles.strava_button]} onPress={handleDisconnectFromStrava}> <Text style={styles.strava_text}> Odpojiť 
          od </Text> <Image
            style={{
              width: '33%', height: '50%',
            }} source={require('../../images/strava.png')}
          />
        </Pressable>
      )} <Pressable
        style={[styles.button, styles.button_upload]} onPress={handleFileUpload} key={'submit_button'}> <Text 
        style={styles.button_text}>
          <FontAwesomeIcon icon={faUpload} style={styles.button_text} /> Nahrať GPX súbor
        </Text>
      </Pressable> <Pressable
        style={[styles.button, styles.button_generate]} onPress={handleGenerateCode} key={'generate_button'}> <Text 
        style={styles.button_text}>
          Vygenerovať prihlasovací kód pre hodinky
        </Text>
      </Pressable> <Pressable
        style={[styles.button, styles.logout_button]} onPress={handleLogout}> <Text style={styles.button_text}>
          <FontAwesomeIcon icon={faDoorOpen} style={styles.button_text} />{' '} Odhlásenie
        </Text>
      </Pressable> <Pressable
        style={[styles.button, styles.button_back]} onPress={handleBack}> <Text style={styles.button_text}>
          <FontAwesomeIcon icon={faArrowLeft} style={styles.button_text} /> Späť
        </Text>
      </Pressable>
    </View>
  ); } const styles = StyleSheet.create({ configMenu: {
    top: 0, height: '90%', width: '100%', padding: 0,
  }, button: {
    borderColor: '#109CF1', borderStyle: 'solid', borderWidth: 3, backgroundColor: '#FFFFFF', display: 'flex', 
    justifyContent: 'center', alignItems: 'center', borderRadius: 50,
  }, button_text: {
    fontSize: 16, color: '#109CF1',
  }, button_back: {
    position: 'absolute', width: '49%', height: '10%', left: '51%', top: '71%',
  }, logout_button: {
    position: 'absolute', width: '49%', height: '10%', left: 0, top: '71%',
  }, button_edit_profile: {
    position: 'absolute', width: '100%', height: '10%', left: 0, top: '23%',
  }, profile_text: {
    fontSize: 28, color: '#109CF1', textAlign: 'left', paddingLeft: 20,
  }, connection_text: {
    fontSize: 20, color: '#109CF1', textAlign: 'center', top: '17%', width: '100%', position: 'absolute',
  }, strava_button: {
    borderColor: '#fc4c02', borderStyle: 'solid', borderWidth: 3, backgroundColor: '#fc4c02', display: 'flex', 
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', borderRadius: 50, width: '100%', height: '10%', 
    position: 'absolute', top: '35%',
  }, strava_text: {
    fontSize: 16, color: '#FFFFFF', textAlign: 'left',
  }, profile_input: {
    borderBottomColor: '#109CF1', borderBottomWidth: 3, color: '#109CF1', fontSize: 24, paddingBottom: 5, paddingLeft: 20,
  }, profile_input_label: {
    color: '#109CF1', marginBottom: '-5%', fontSize: 12,
  }, button_upload: {
    position: 'absolute', width: '100%', height: '10%', left: 0, top: '47%',
  }, button_generate: {
    position: 'absolute', width: '100%', height: '10%', left: 0, top: '59%',
  },
});
