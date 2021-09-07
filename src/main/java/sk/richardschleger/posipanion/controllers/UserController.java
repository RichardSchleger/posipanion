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
import sk.richardschleger.posipanion.entities.TrackPoint;
import sk.richardschleger.posipanion.entities.UserDetails;
import sk.richardschleger.posipanion.keys.TrackKey;
import sk.richardschleger.posipanion.keys.TrackPointKey;
import sk.richardschleger.posipanion.models.LocationModel;
import sk.richardschleger.posipanion.services.TrackPointService;
import sk.richardschleger.posipanion.services.TrackService;
import sk.richardschleger.posipanion.services.UserDetailsService;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    
    private final UserDetailsService userDetailsService;

    private final TrackService trackService;

    private final TrackPointService trackPointService;

    public UserController(UserDetailsService userDetailsService,
                          TrackService trackService,
                          TrackPointService trackPointService){
        this.userDetailsService = userDetailsService;
        this.trackService = trackService;
        this.trackPointService = trackPointService;
    }

    @PutMapping("/start/{id}")
    public void startExistingTrack(@PathVariable("id") UUID trackId){
        if(trackId != null){
            if(trackService.getTrackById(trackId) != null){
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();
        
                UserDetails userDetails = userDetailsService.getUserDetailsByEmail(email);
                userDetails.setSelectedTrackId(trackId);

                UUID uuid = UUID.randomUUID();
                Track currentTrack = new Track();
                TrackKey key = new TrackKey(email, uuid);
                currentTrack.setKey(key);

                userDetails.currentTrackId(uuid);

                trackService.saveTrack(currentTrack);
                userDetailsService.saveUserDetails(userDetails);
            }
        }
    }

    @PutMapping("/start")
    public void startNewTrack(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserDetails userDetails = userDetailsService.getUserDetailsByEmail(email);

        UUID uuid = UUID.randomUUID();
        Track currentTrack = new Track();
        TrackKey key = new TrackKey(email, uuid);
        currentTrack.setKey(key);

        userDetails.currentTrackId(uuid);

        trackService.saveTrack(currentTrack);
        userDetailsService.saveUserDetails(userDetails);

    }

    @PostMapping("/location")
    public void saveLocation(@RequestBody LocationModel location){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserDetails userDetails = userDetailsService.getUserDetailsByEmail(email);

        Track track = trackService.getTrackById(userDetails.getCurrentTrackId());
        if(track != null){
            UUID uuid = UUID.randomUUID();
            TrackPointKey key = new TrackPointKey(userDetails.getCurrentTrackId(), uuid);
            TrackPoint trackPoint = new TrackPoint();
            trackPoint.setKey(key);
            trackPoint.setLatitude(location.getLatitude());
            trackPoint.setLongitude(location.getLongitude());
            trackPoint.setElevation(location.getElevation());
            trackPoint.setOrder(trackPointService.getNumberOfPointsForTrackId(userDetails.getCurrentTrackId()));
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
