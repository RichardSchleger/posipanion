package sk.richardschleger.posipanion.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sk.richardschleger.posipanion.entities.ActiveUser;
import sk.richardschleger.posipanion.entities.CurrentTrackPoint;
import sk.richardschleger.posipanion.entities.User;
import sk.richardschleger.posipanion.keys.TrackPointKey;
import sk.richardschleger.posipanion.models.LocationModel;
import sk.richardschleger.posipanion.services.ActiveUserService;
import sk.richardschleger.posipanion.services.TrackPointService;
import sk.richardschleger.posipanion.services.TrackService;
import sk.richardschleger.posipanion.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;

    private final TrackService trackService;

    private final TrackPointService trackPointService;

    private final ActiveUserService activeUserService;

    public UserController(UserService userService, 
                          TrackService trackService, 
                          TrackPointService trackPointService,
                          ActiveUserService activeUserService) {
        this.userService = userService;
        this.trackService = trackService;
        this.trackPointService = trackPointService;
        this.activeUserService = activeUserService;
    }

    @PutMapping("/start/{id}")
    public void startExistingTrack(@PathVariable("id") Integer trackId, @RequestBody LocationModel location){
        if(trackId != null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());
            if(user != null){
                ActiveUser activeUser = new ActiveUser();
                activeUser.setSelected_track(trackService.getTrackById(trackId));
                activeUser.setUser(user);
                activeUser.setLastKnownLatitude(location.getLatitude());
                activeUser.setLastKnownLongitude(location.getLongitude());
                activeUserService.saveActiveUser(activeUser);

                CurrentTrackPoint trackPoint = new CurrentTrackPoint();
                TrackPointKey key = new TrackPointKey();
                key.setUserId(user.getId());
                trackPoint.setKey(key);
                trackPoint.setLatitude(location.getLatitude());
                trackPoint.setLongitude(location.getLongitude());
                trackPoint.setElevation(location.getElevation());
                trackPoint.setTimestamp(new Date().getTime());
                trackPointService.saveTrackPoint(trackPoint);
            }
        }
    }

    @PutMapping("/start")
    public void startNewTrack(@RequestBody LocationModel location){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        if(user != null){
            ActiveUser activeUser = new ActiveUser();
            activeUser.setUser(user);
            activeUser.setLastKnownLatitude(location.getLatitude());
            activeUser.setLastKnownLongitude(location.getLongitude());
            activeUserService.saveActiveUser(activeUser);

            CurrentTrackPoint trackPoint = new CurrentTrackPoint();
            TrackPointKey key = new TrackPointKey();
            key.setUserId(user.getId());
            trackPoint.setKey(key);
            trackPoint.setLatitude(location.getLatitude());
            trackPoint.setLongitude(location.getLongitude());
            trackPoint.setElevation(location.getElevation());
            trackPoint.setTimestamp(new Date().getTime());
            trackPointService.saveTrackPoint(trackPoint);
        }
    }

    @PostMapping("/location")
    public void saveLocation(@RequestBody LocationModel location){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        if(user != null){
            CurrentTrackPoint trackPoint = new CurrentTrackPoint();
            TrackPointKey key = new TrackPointKey();
            key.setUserId(user.getId());
            trackPoint.setKey(key);
            trackPoint.setLatitude(location.getLatitude());
            trackPoint.setLongitude(location.getLongitude());
            trackPoint.setElevation(location.getElevation());
            trackPoint.setTimestamp(new Date().getTime());
            trackPointService.saveTrackPoint(trackPoint);
        }
    }

    @PostMapping("/fall")
    public void fallDetected(@RequestBody LocationModel location){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<String> tokens = new ArrayList<>();

        MulticastMessage message = MulticastMessage.builder()
            .putData("latitude", String.valueOf(location.getLatitude()))
            .putData("longitude", String.valueOf(location.getLatitude()))
            .setNotification(Notification.builder()
                .setTitle("Detekovaný možný pád!")
                .setBody(email)
                .build()
            )
            .addAllTokens(tokens)
            .build();
        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            System.out.println(response.getSuccessCount() + " messages were sent successfully");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

}
