package sk.richardschleger.posipanion.controllers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import sk.richardschleger.posipanion.comparators.FriendModelComparator;
import sk.richardschleger.posipanion.comparators.TrackPointComparator;
import sk.richardschleger.posipanion.entities.ActiveUser;
import sk.richardschleger.posipanion.entities.CurrentTrackPoint;
import sk.richardschleger.posipanion.entities.Friend;
import sk.richardschleger.posipanion.entities.Track;
import sk.richardschleger.posipanion.entities.User;
import sk.richardschleger.posipanion.entities.UserDetails;
import sk.richardschleger.posipanion.keys.TrackPointKey;
import sk.richardschleger.posipanion.models.FriendModel;
import sk.richardschleger.posipanion.models.LocationModel;
import sk.richardschleger.posipanion.models.SaveTrackModel;
import sk.richardschleger.posipanion.services.ActiveUserService;
import sk.richardschleger.posipanion.services.FriendService;
import sk.richardschleger.posipanion.services.TrackPointService;
import sk.richardschleger.posipanion.services.TrackService;
import sk.richardschleger.posipanion.services.UserDetailsService;
import sk.richardschleger.posipanion.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;

    private final TrackService trackService;

    private final TrackPointService trackPointService;

    private final ActiveUserService activeUserService;

    private final FriendService friendService;

    private final UserDetailsService userDetailsService;

    public UserController(UserService userService, 
                          TrackService trackService, 
                          TrackPointService trackPointService,
                          ActiveUserService activeUserService,
                          FriendService friendService,
                          UserDetailsService userDetailsService) {
        this.userService = userService;
        this.trackService = trackService;
        this.trackPointService = trackPointService;
        this.activeUserService = activeUserService;
        this.friendService = friendService;
        this.userDetailsService = userDetailsService;
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

    @PutMapping("/end")
    public void endTrack(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        if(user != null){
            ActiveUser activeUser = activeUserService.getActiveUserByUserId(user.getId());
            if(activeUser != null){
                activeUserService.removeActiveUser(activeUser);
                trackPointService.removeTrackPointsForUserId(user.getId());
            }
        }
    }

    @PostMapping("/save")
    public void saveTrack(@RequestBody SaveTrackModel trackModel){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        if(user != null){
            ActiveUser activeUser = activeUserService.getActiveUserByUserId(user.getId());
            if(activeUser != null){

                List<WayPoint> wayPoints = new ArrayList<>();
                List<CurrentTrackPoint> trackPoints = trackPointService.getTrackPointsForUserId(user.getId());
                Collections.sort(trackPoints, new TrackPointComparator());
                double ele = 0;
                double dist = 0;
                CurrentTrackPoint lastPoint = null;
                final int R = 6371;
                for (CurrentTrackPoint trackPoint : trackPoints) {
                    if(lastPoint != null){
                        double latDistance = Math.toRadians(lastPoint.getLatitude() - trackPoint.getLatitude());
                        double lonDistance = Math.toRadians(lastPoint.getLongitude()- trackPoint.getLongitude());
                        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                                + Math.cos(Math.toRadians(trackPoint.getLongitude())) * Math.cos(Math.toRadians(lastPoint.getLongitude()))
                                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                        double distance = R * c * 1000; // convert to meters

                        double height = trackPoint.getElevation() - lastPoint.getElevation();

                        distance = Math.pow(distance, 2) + Math.pow(height, 2);
                        distance = Math.sqrt(distance);

                        dist += distance;
                        if(height > 0) ele += height;
                    }
                    wayPoints.add(WayPoint.of(trackPoint.getLatitude(), trackPoint.getLongitude(), trackPoint.getElevation(), trackPoint.getTimestamp()));
                }
                TrackSegment segment = TrackSegment.of(wayPoints);
                GPX gpx = GPX.builder().addTrack(track -> track.addSegment(segment)).build();
                String uuid = UUID.randomUUID().toString();
                String path = "gpx-files/" + uuid + ".gpx";
                try {
                    GPX.write(gpx, Paths.get(path).toAbsolutePath().normalize());

                    Track newTrack = new Track();
                    newTrack.setDistance(dist);
                    newTrack.setElevationGain(ele);
                    newTrack.setGpxPath(path);
                    newTrack.setName(trackModel.getName());
                    newTrack.setEstimatedMovingTime(trackModel.getEstimatedMovingTime());
                    newTrack.setStravaId(trackModel.getStravaId());
                    trackService.saveTrack(newTrack);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                activeUserService.removeActiveUser(activeUser);
                trackPointService.removeTrackPointsForUserId(user.getId());
            }
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

    @GetMapping("/friends")
    public List<FriendModel> getListOfFriends(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        List<FriendModel> friendModelList = new ArrayList<>();
        List<Friend> friends = friendService.getFriendsByUserId(user.getId());
        for (Friend friend : friends) {
            UserDetails userDetails = null;
            ActiveUser activeUser = null;
            if(friend.getUser1().equals(user)){
                userDetails = userDetailsService.getUserDetailsByUserId(friend.getUser2().getId());
                activeUser = activeUserService.getActiveUserByUserId(friend.getUser2().getId());
            }else{
                userDetails = userDetailsService.getUserDetailsByUserId(friend.getUser2().getId());
                activeUser = activeUserService.getActiveUserByUserId(friend.getUser2().getId());
            }
            FriendModel friendModel = new FriendModel();
            friendModel.setFirstName(userDetails.getFirstName());
            friendModel.setSurname(userDetails.getSurname());
            if(activeUser != null){
                friendModel.setLastKnownLatitude(activeUser.getLastKnownLatitude());
                friendModel.setLastKnownLongitude(activeUser.getLastKnownLongitude());
            }
            friendModelList.add(friendModel);
        }
        Collections.sort(friendModelList, new FriendModelComparator());

        return friendModelList;
    }

}
