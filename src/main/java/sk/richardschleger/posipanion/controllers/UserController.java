package sk.richardschleger.posipanion.controllers;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sk.richardschleger.posipanion.entities.Track;
import sk.richardschleger.posipanion.entities.UserDetails;
import sk.richardschleger.posipanion.keys.TrackKey;
import sk.richardschleger.posipanion.services.TrackService;
import sk.richardschleger.posipanion.services.UserDetailsService;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    
    private final UserDetailsService userDetailsService;

    private final TrackService trackService;

    public UserController(UserDetailsService userDetailsService,
                          TrackService trackService){
        this.userDetailsService = userDetailsService;
        this.trackService = trackService;
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

}
