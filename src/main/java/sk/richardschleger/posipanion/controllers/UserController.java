package sk.richardschleger.posipanion.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import sk.richardschleger.posipanion.entities.Track;
import sk.richardschleger.posipanion.entities.User;
import sk.richardschleger.posipanion.entities.UserDetails;
import sk.richardschleger.posipanion.keys.TrackPointKey;
import sk.richardschleger.posipanion.models.LocationModel;
import sk.richardschleger.posipanion.services.TrackPointService;
import sk.richardschleger.posipanion.services.TrackService;
import sk.richardschleger.posipanion.services.UserDetailsService;
import sk.richardschleger.posipanion.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final TrackService trackService;

    private final TrackPointService trackPointService;

    public UserController(UserService userService, 
                          UserDetailsService userDetailsService, 
                          TrackService trackService, 
                          TrackPointService trackPointService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.trackService = trackService;
        this.trackPointService = trackPointService;
    }

    @PutMapping("/start/{id}")
    public void startExistingTrack(@PathVariable("id") UUID trackId){
        if(trackId != null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());
            if(user != null){
        
            }
        }
    }

    @PutMapping("/start")
    public void startNewTrack(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        if(user != null){
    
        }
    }

    @PostMapping("/location")
    public void saveLocation(@RequestBody LocationModel location){

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
