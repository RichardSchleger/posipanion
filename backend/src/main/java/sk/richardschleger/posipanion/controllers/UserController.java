package sk.richardschleger.posipanion.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
import sk.richardschleger.posipanion.comparators.FriendModelComparator;
import sk.richardschleger.posipanion.comparators.TrackPointComparator;
import sk.richardschleger.posipanion.datatypes.Pair;
import sk.richardschleger.posipanion.entities.ActiveUser;
import sk.richardschleger.posipanion.entities.CurrentTrackPoint;
import sk.richardschleger.posipanion.entities.Friend;
import sk.richardschleger.posipanion.entities.StravaUser;
import sk.richardschleger.posipanion.entities.Track;
import sk.richardschleger.posipanion.entities.User;
import sk.richardschleger.posipanion.entities.UserDetails;
import sk.richardschleger.posipanion.models.ActiveUserDetailModel;
import sk.richardschleger.posipanion.models.FriendModel;
import sk.richardschleger.posipanion.models.LocationModel;
import sk.richardschleger.posipanion.models.ProfileModel;
import sk.richardschleger.posipanion.models.SaveTrackModel;
import sk.richardschleger.posipanion.models.StravaModel;
import sk.richardschleger.posipanion.models.TrackModel;
import sk.richardschleger.posipanion.models.WayPointModel;
import sk.richardschleger.posipanion.services.ActiveUserService;
import sk.richardschleger.posipanion.services.FirebaseMessageService;
import sk.richardschleger.posipanion.services.FriendService;
import sk.richardschleger.posipanion.services.GpxService;
import sk.richardschleger.posipanion.services.StravaService;
import sk.richardschleger.posipanion.services.StravaUserService;
import sk.richardschleger.posipanion.services.TrackPointService;
import sk.richardschleger.posipanion.services.TrackService;
import sk.richardschleger.posipanion.services.UserDetailsService;
import sk.richardschleger.posipanion.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    private final TrackService trackService;

    private final TrackPointService trackPointService;

    private final ActiveUserService activeUserService;

    private final FriendService friendService;

    private final UserDetailsService userDetailsService;

    private final StravaUserService stravaUserService;

    private final StravaService stravaService;

    private final GpxService gpxService;

    private final FirebaseMessageService firebaseMessageService;

    @Value("${app.upload.path}")
    private String uploadPath;

    public UserController(UserService userService, 
                          TrackService trackService, 
                          TrackPointService trackPointService,
                          ActiveUserService activeUserService,
                          FriendService friendService,
                          UserDetailsService userDetailsService,
                          StravaUserService stravaUserService,
                          StravaService stravaService,
                          GpxService gpxService,
                          FirebaseMessageService firebaseMessageService) {
        this.userService = userService;
        this.trackService = trackService;
        this.trackPointService = trackPointService;
        this.activeUserService = activeUserService;
        this.friendService = friendService;
        this.userDetailsService = userDetailsService;
        this.stravaUserService = stravaUserService;
        this.stravaService = stravaService;
        this.gpxService = gpxService;
        this.firebaseMessageService = firebaseMessageService;
    }

    @PutMapping("/start/{id}")
    public void startExistingTrack(@PathVariable("id") Integer trackId, @RequestBody LocationModel location){
        if(trackId != null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());
            if(user != null){
                ActiveUser activeUser = user.getActiveUser();
                if(activeUser != null){
                    logger.info("User {} already has active ride", user.getEmail());
                    return;
                }

                activeUser = new ActiveUser();
                activeUser.setSelectedTrack(trackService.getTrackById(trackId));
                activeUser.setUser(user);
                activeUser.setLastKnownLatitude(location.getLatitude());
                activeUser.setLastKnownLongitude(location.getLongitude());
                activeUserService.saveActiveUser(activeUser);

                CurrentTrackPoint trackPoint = new CurrentTrackPoint();
                trackPoint.setUserId(user.getId());
                trackPoint.setLatitude(location.getLatitude());
                trackPoint.setLongitude(location.getLongitude());
                trackPoint.setElevation(location.getElevation());
                if(location.getTimestamp() != null){
                    trackPoint.setTimestamp(location.getTimestamp().getTime());
                }else{
                    trackPoint.setTimestamp(new Date().getTime());
                }
                
                trackPointService.saveTrackPoint(trackPoint);
                logger.info("User {} started ride {}", user.getEmail(), trackId);
            }else{
                logger.info("User {} not found", authentication.getName());
            }
        }
    }

    @PutMapping("/start")
    public void startNewTrack(@RequestBody LocationModel location){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        if(user != null){
            ActiveUser activeUser = user.getActiveUser();
            if(activeUser != null){
                logger.info("User {} already has active ride", user.getEmail());
                return;
            }

            activeUser = new ActiveUser();
            activeUser.setUser(user);
            activeUser.setLastKnownLatitude(location.getLatitude());
            activeUser.setLastKnownLongitude(location.getLongitude());
            activeUserService.saveActiveUser(activeUser);

            CurrentTrackPoint trackPoint = new CurrentTrackPoint();
            trackPoint.setUserId(user.getId());
            trackPoint.setLatitude(location.getLatitude());
            trackPoint.setLongitude(location.getLongitude());
            trackPoint.setElevation(location.getElevation());
            if(location.getTimestamp() != null){
                trackPoint.setTimestamp(location.getTimestamp().getTime());
            }else{
                trackPoint.setTimestamp(new Date().getTime());
            }
            trackPointService.saveTrackPoint(trackPoint);
            logger.info("User {} started new ride", user.getEmail());
        }else{
            logger.info("User {} not found", authentication.getName());
        }
    }

    @PutMapping("/end")
    public void endTrack(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        if(user != null){
            ActiveUser activeUser = activeUserService.getActiveUserByUserId(user.getId());
            
            if(activeUser != null){

                if(user.getStravaUser() != null && user.getStravaUser().getStravaUploadActivity()){
                    Pair<GPX, Pair<Double, Double>> gpxPair = gpxService.createGPXForUser(user);
                    GPX gpx = gpxPair.getFirst();

                    String uuid = UUID.randomUUID().toString();
                    String path = uploadPath + uuid + ".gpx";

                    try {
                        GPX.write(gpx, Paths.get(path).toAbsolutePath().normalize());

                        File gpxFile = new File(path);
                        stravaService.uploadActivityToStrava(user.getStravaUser(), gpxFile);

                        try{
                            Files.delete(Path.of(path));
                            logger.info("Successfully deleted gpx file {}", gpxFile.getName());
                        } catch (Exception e) {
                            logger.error("Failed to delete gpx file {}", gpxFile.getName());
                        }
                        

                    } catch (IOException e) {
                        logger.info("Error saving track", e);
                        e.printStackTrace();
                    }
                }

                user.setActiveUser(null);
                activeUserService.removeActiveUser(activeUser);
                trackPointService.removeTrackPointsForUserId(user.getId());
                logger.info("User {} ended ride", user.getEmail());
            }else{
                logger.info("User {} has no active ride", user.getEmail());
            }
        }else{
            logger.info("User {} not found", authentication.getName());
        }
    }

    @PostMapping("/save")
    public void saveTrack(@RequestBody SaveTrackModel trackModel){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        if(user != null){
            ActiveUser activeUser = user.getActiveUser();
            if(activeUser != null){

                Pair<GPX, Pair<Double, Double>> gpxPair = gpxService.createGPXForUser(user);
                GPX gpx = gpxPair.getFirst();

                Pair<Double, Double> distEle = gpxPair.getSecond();
                double dist = distEle.getFirst();
                double ele = distEle.getSecond();

                String uuid = UUID.randomUUID().toString();
                String path = uploadPath + uuid + ".gpx";;
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
                    logger.info("Error saving track", e);
                    e.printStackTrace();
                }

                user.setActiveUser(null);
                activeUserService.removeActiveUser(activeUser);
                trackPointService.removeTrackPointsForUserId(user.getId());
                logger.info("User {} saved ride", user.getEmail());

                if(user.getStravaUser() != null && user.getStravaUser().getStravaUploadActivity()){
                    File gpxFile = new File(path);
                    stravaService.uploadActivityToStrava(user.getStravaUser(), gpxFile);
                }

            }else{
                logger.info("User {} has no active ride", user.getEmail());
            }
        }else{
            logger.info("User {} not found", authentication.getName());
        }
    }

    @PostMapping("/location")
    public void saveLocation(@RequestBody LocationModel location){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        if(user != null){
            ActiveUser activeUser = user.getActiveUser();

            if(activeUser != null){

                CurrentTrackPoint trackPoint = new CurrentTrackPoint();
                trackPoint.setUserId(user.getId());
                trackPoint.setLatitude(location.getLatitude());
                trackPoint.setLongitude(location.getLongitude());
                trackPoint.setElevation(location.getElevation());
                if(location.getTimestamp() != null){
                    trackPoint.setTimestamp(location.getTimestamp().getTime());
                }else{
                    trackPoint.setTimestamp(new Date().getTime());
                }
                trackPointService.saveTrackPoint(trackPoint);

                activeUser.setLastKnownLatitude(location.getLatitude());
                activeUser.setLastKnownLongitude(location.getLongitude());
                activeUserService.saveActiveUser(activeUser);
                logger.info("User {} updated location", user.getEmail());
            }else{
                logger.info("User {} has no active ride", user.getEmail());
            }
            
        }else{
            logger.info("User {} not found", authentication.getName());
        }
    }

    @PostMapping("/wearables/location")
	public void saveWearablesLocation(@RequestParam Map<String, String> body){

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        if(user != null){
            ActiveUser activeUser = user.getActiveUser();

            if(activeUser != null){

                CurrentTrackPoint trackPoint = new CurrentTrackPoint();
                trackPoint.setUserId(user.getId());
                trackPoint.setLatitude(Double.parseDouble(body.get("latitude")));
                trackPoint.setLongitude(Double.parseDouble(body.get("longitude")));
                trackPoint.setElevation(Double.parseDouble(body.get("elevation")));
                trackPoint.setTimestamp(Long.parseLong(body.get("timestamp")));

                trackPointService.saveTrackPoint(trackPoint);

                activeUser.setLastKnownLatitude(Double.parseDouble(body.get("latitude")));
                activeUser.setLastKnownLongitude(Double.parseDouble(body.get("longitude")));
                activeUserService.saveActiveUser(activeUser);
                logger.info("User {} updated location", user.getEmail());
            }else{
                logger.info("User {} has no active ride", user.getEmail());
            }
            
        }else{
            logger.info("User {} not found", authentication.getName());
        }
		
	}

    @PostMapping("/location/cached")
    public void saveCachedLocations(@RequestBody List<LocationModel> locations){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        if(user != null){
        
            ActiveUser activeUser = user.getActiveUser();
            
            if(activeUser != null){

                for (LocationModel location : locations) {
                    CurrentTrackPoint trackPoint = new CurrentTrackPoint();
                    trackPoint.setUserId(user.getId());
                    trackPoint.setLatitude(location.getLatitude());
                    trackPoint.setLongitude(location.getLongitude());
                    trackPoint.setElevation(location.getElevation());
                    if(location.getTimestamp() != null){
                        trackPoint.setTimestamp(location.getTimestamp().getTime());
                    }else{
                        trackPoint.setTimestamp(new Date().getTime());
                    }
                    trackPointService.saveTrackPoint(trackPoint);
                }
                logger.info("User {} sended {} cached locations", user.getEmail(), locations.size());
    
                CurrentTrackPoint trackPoint = trackPointService.getLastTrackPointForUserId(user.getId());
                if(trackPoint != null){
                    activeUser.setLastKnownLatitude(trackPoint.getLatitude());
                    activeUser.setLastKnownLongitude(trackPoint.getLongitude());
                    activeUserService.saveActiveUser(activeUser);
                    logger.info("User {} updated location", user.getEmail());
                }else{
                    logger.info("Could not find last location for user {}", user.getEmail());
                }
                
            }else{
                logger.info("User {} has no active ride", user.getEmail());
            }

        }
    }

    @PostMapping("/fall")
    public void fallDetected(@RequestBody LocationModel location){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userService.getUserByEmail(email);
        if(user != null){
            
            List<User> targetUsers = new ArrayList<>();

            List<Friend> friends = friendService.getFriendsByUserId(user.getId());
            for (Friend friend : friends) {
                User friendUser;
                if (friend.getUser1().equals(user)) {
                    friendUser = friend.getUser2();
                } else {
                    friendUser = friend.getUser1();
                }
                targetUsers.add(friendUser);
            }

            firebaseMessageService.sendMulticast(
                targetUsers, 
                user.getUserDetails().getFirstName() + " " + user.getUserDetails().getSurname() + ": Detekovaný možný pád!", 
                "Bol detekovaný možný pád používateľa " + user.getUserDetails().getFirstName() + " " + user.getUserDetails().getSurname() + "!",
                logger);

        }else{
            logger.info("User {} not found", email);
        }

    }

    @GetMapping("/friends/pending")
    public List<FriendModel> getListOfPendingFriends(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        if(user != null){
             List<FriendModel> friendModelList = new ArrayList<>();
            List<Friend> friends = friendService.getPendingFriendsByUserId(user.getId());
            for (Friend friend : friends) {
                Pair<UserDetails, ActiveUser> userInfo = getUserDetailsAndActiveUser(friend, user);
                UserDetails userDetails = userInfo.getFirst();
                ActiveUser activeUser = userInfo.getSecond();
                FriendModel friendModel = new FriendModel();
                friendModel.setId(userDetails.getUser().getId());
                friendModel.setFriendId(friend.getId());
                friendModel.setFirstName(userDetails.getFirstName());
                friendModel.setSurname(userDetails.getSurname());
                if(activeUser != null){
                    friendModel.setLastKnownLatitude(activeUser.getLastKnownLatitude());
                    friendModel.setLastKnownLongitude(activeUser.getLastKnownLongitude());
                }
                friendModel.setCanConfirm(friend.getUser2().equals(user));
                friendModelList.add(friendModel);
            }
            friendModelList.sort(new FriendModelComparator());
            logger.info("{} pending friends found and returned for user {}", friendModelList.size(), authentication.getName());
            return friendModelList;
        }else{
            logger.info("User {} not found", authentication.getName());
            return new ArrayList<>();
        }

       
    }

    @GetMapping("/friends/confirmed")
    public List<FriendModel> getListOfConfirmedFriends(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        if(user != null){
            List<FriendModel> friendModelList = new ArrayList<>();
            List<Friend> friends = friendService.getConfirmedFriendsByUserId(user.getId());
            for (Friend friend : friends) {
                Pair<UserDetails, ActiveUser> userInfo = getUserDetailsAndActiveUser(friend, user);
                UserDetails userDetails = userInfo.getFirst();
                ActiveUser activeUser = userInfo.getSecond();
                FriendModel friendModel = new FriendModel();
                friendModel.setId(userDetails.getUser().getId());
                friendModel.setFriendId(friend.getId());
                friendModel.setFirstName(userDetails.getFirstName());
                friendModel.setSurname(userDetails.getSurname());
                if(activeUser != null){
                    friendModel.setLastKnownLatitude(activeUser.getLastKnownLatitude());
                    friendModel.setLastKnownLongitude(activeUser.getLastKnownLongitude());
                }
                friendModelList.add(friendModel);
            }
            friendModelList.sort(new FriendModelComparator());
            logger.info("{} confirmed friends found and returned for user {}", friendModelList.size(), authentication.getName());
            return friendModelList;
        }else{
            logger.info("User {} not found", authentication.getName());
            return new ArrayList<>();
        }
        
    }

    @GetMapping("/friends/active")
    public List<FriendModel> getListOfActiveFriends(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        if(user != null){
            List<FriendModel> friendModelList = new ArrayList<>();
            List<Friend> friends = friendService.getConfirmedFriendsByUserId(user.getId());
            for (Friend friend : friends) {
                Pair<UserDetails, ActiveUser> userInfo = getUserDetailsAndActiveUser(friend, user);
                UserDetails userDetails = userInfo.getFirst();
                ActiveUser activeUser = userInfo.getSecond();
                if(activeUser != null){
                    FriendModel friendModel = new FriendModel();
                    friendModel.setId(userDetails.getUser().getId());
                    friendModel.setFriendId(friend.getId());
                    friendModel.setFirstName(userDetails.getFirstName());
                    friendModel.setSurname(userDetails.getSurname());
                    friendModel.setLastKnownLatitude(activeUser.getLastKnownLatitude());
                    friendModel.setLastKnownLongitude(activeUser.getLastKnownLongitude());
                    friendModelList.add(friendModel);
                }
            }
            friendModelList.sort(new FriendModelComparator());
            return friendModelList;
        }else{
            logger.info("User {} not found", authentication.getName());
            return new ArrayList<>();
        }

        
    }

    @GetMapping("/find/{text}")
    public List<FriendModel> findUsers(@PathVariable("text") String text){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByEmail(authentication.getName());
        
        if(currentUser != null){
            List<User> currentFriends = new ArrayList<>();
            friendService.getFriendsByUserId(currentUser.getId()).forEach(friend -> {
                if(!currentFriends.contains(friend.getUser1())){
                    currentFriends.add(friend.getUser1());
                }
                if(!currentFriends.contains(friend.getUser2())){
                    currentFriends.add(friend.getUser2());
                }
            });
    
            List<FriendModel> friendModelList = new ArrayList<>();
    
            List<User> allUsers = userService.getAll();
            allUsers.forEach(user -> {
                if((user.getUserDetails().getFirstName().toLowerCase() + " " + user.getUserDetails().getSurname().toLowerCase()).contains(text) ||
                    (user.getUserDetails().getSurname().toLowerCase() + " " + user.getUserDetails().getFirstName().toLowerCase()).contains(text) || 
                    user.getEmail().toLowerCase().contains(text)){
                        if(!currentUser.equals(user) && !currentFriends.contains(user)){
                            FriendModel model = new FriendModel();
                            model.setId(user.getId());
                            model.setFirstName(user.getUserDetails().getFirstName());
                            model.setSurname(user.getUserDetails().getSurname());
                            friendModelList.add(model);
                        }
                }
            });    

            logger.info("{} users found and returned", friendModelList.size());
            return friendModelList;

        }else{
            logger.info("User {} not found", authentication.getName());
            return new ArrayList<>();
        }

    }

    @GetMapping("/profile")
    public ProfileModel getProfile(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        if(user != null){
            UserDetails userDetails = userDetailsService.getUserDetailsByUserId(user.getId());
            StravaUser stravaUser = stravaUserService.getStravaUserByUserId(user.getId());

            ProfileModel profile = new ProfileModel();
            if(userDetails != null){
                profile.setFirstName(userDetails.getFirstName());
                profile.setSurname(userDetails.getSurname());
            }
            if(stravaUser != null){
                profile.setStravaId(stravaUser.getStravaId());
            }

            logger.info("Profile found and returned for user {}", authentication.getName());
            return profile;
        }else{
            logger.info("User {} not found", authentication.getName());
            return null;
        }

        
    }

    @PostMapping("/profile")
    public void saveProfile(@RequestBody ProfileModel profile){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        if(user != null){
            UserDetails userDetails = userDetailsService.getUserDetailsByUserId(user.getId());

            if(userDetails == null) userDetails = new UserDetails();
            userDetails.setFirstName(profile.getFirstName());
            userDetails.setSurname(profile.getSurname());
            userDetailsService.saveUserDetails(userDetails);
            logger.info("Profile saved for user {}", authentication.getName());
        }else{
            logger.info("User {} not found", authentication.getName());
        }

    }

    @PostMapping(path = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            
            GPX gpx = GPX.reader().read(file.getInputStream());
            List<WayPoint> wayPoints = gpx.getTracks().get(0).getSegments().get(0).getPoints();
            double ele = 0;
            double dist = 0;
            if(wayPoints.size() > 0){
                WayPoint lastPoint = wayPoints.get(0);
                for (WayPoint wayPoint : wayPoints) {
                    if(lastPoint != null && !lastPoint.equals(wayPoint)){

                        double height = 0;
                        if(wayPoint.getElevation().isPresent() && lastPoint.getElevation().isPresent())
                            height = wayPoint.getElevation().get().doubleValue() - lastPoint.getElevation().get().doubleValue();

                        double theta = lastPoint.getLongitude().doubleValue() - wayPoint.getLongitude().doubleValue();
                        double distance = Math.sin(Math.toRadians(lastPoint.getLatitude().doubleValue())) * Math.sin(Math.toRadians(wayPoint.getLatitude().doubleValue())) + Math.cos(Math.toRadians(lastPoint.getLatitude().doubleValue())) * Math.cos(Math.toRadians(wayPoint.getLatitude().doubleValue())) * Math.cos(Math.toRadians(theta));
                        distance = Math.acos(distance);
                        distance = Math.toDegrees(distance);
                        distance = distance * (60 * 1.1515 * 1.609344 * 1000);
                        distance = Math.pow(distance, 2) + Math.pow(height, 2);
                        distance = Math.sqrt(distance);

                        dist += distance;
                        if(height > 0) ele += height;

                        lastPoint = wayPoint;
                    }
                }
            }
            String uuid = UUID.randomUUID().toString();
            String path = uploadPath + uuid + ".gpx";
            GPX.write(gpx, Paths.get(path).toAbsolutePath().normalize());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());

            Track newTrack = new Track();
            newTrack.setDistance(dist);
            newTrack.setElevationGain(ele);
            newTrack.setGpxPath(path);
            newTrack.setName(file.getOriginalFilename());
            newTrack.setUser(user);
            trackService.saveTrack(newTrack);

        } catch (IOException e) {
            logger.info("Error uploading file {}", e.getMessage());
            e.printStackTrace();
        }

    }

    @GetMapping("/detail/{id}")
    public ActiveUserDetailModel getActiveUserDetails(@PathVariable("id") Integer userId){

        if(userId == null){
            logger.info("User id is null");
            return null;
        }

        User user = userService.getUserById(userId);
        if(user == null || user.getActiveUser() == null){
            logger.info("User {} not found or is not active", userId);
            return null;
        }

        ActiveUserDetailModel model = new ActiveUserDetailModel();
        model.setId(user.getId());
        model.setFirstName(user.getUserDetails().getFirstName());
        model.setSurname(user.getUserDetails().getSurname());
        model.setLastKnownLatitude(user.getActiveUser().getLastKnownLatitude());
        model.setLastKnownLongitude(user.getActiveUser().getLastKnownLongitude());

        TrackModel trackModel = new TrackModel();
        List<WayPointModel>wayPointModels = new ArrayList<>();

        List<CurrentTrackPoint> trackPoints = trackPointService.getTrackPointsForUserId(user.getId());
        trackPoints.sort(new TrackPointComparator());
        double ele = 0;
        double dist = 0;
        long movingTime = 0;
        CurrentTrackPoint lastPoint = trackPoints.get(0);
        for (CurrentTrackPoint trackPoint : trackPoints) {
            if(lastPoint != null && !lastPoint.equals(trackPoint)){
                double height = trackPoint.getElevation() - lastPoint.getElevation();
                
                double theta = lastPoint.getLongitude() - trackPoint.getLongitude();
                double distance = Math.sin(Math.toRadians(lastPoint.getLatitude())) * Math.sin(Math.toRadians(trackPoint.getLatitude())) + Math.cos(Math.toRadians(lastPoint.getLatitude())) * Math.cos(Math.toRadians(trackPoint.getLatitude())) * Math.cos(Math.toRadians(theta));
                distance = Math.acos(distance);
                distance = Math.toDegrees(distance);
                distance = distance * (60 * 1.1515 * 1.609344 * 1000);
                distance = Math.pow(distance, 2) + Math.pow(height, 2);
                distance = Math.sqrt(distance);

                dist += distance;
                if(height > 0) ele += height;
                movingTime += trackPoint.getTimestamp() - lastPoint.getTimestamp();

                lastPoint = trackPoint;
            }
            wayPointModels.add(new WayPointModel(trackPoint.getLatitude(), trackPoint.getLongitude(), new Timestamp(trackPoint.getTimestamp())));
        }
        trackModel.setDistance(dist);
        trackModel.setElevation(ele);
        trackModel.setMovingTime(movingTime);
        trackModel.setWaypoints(wayPointModels);
        model.setCurrentRide(trackModel);

        Track selectedTrack = user.getActiveUser().getSelectedTrack();
        if(selectedTrack != null){
            
            trackModel = new TrackModel();
            trackModel.setDistance(selectedTrack.getDistance());
            trackModel.setMovingTime(selectedTrack.getEstimatedMovingTime());
            wayPointModels = new ArrayList<>();
            
            GPX gpx;
            try {
                gpx = GPX.reader().read(selectedTrack.getGpxPath());
                List<WayPoint> wayPoints = gpx.getTracks().get(0).getSegments().get(0).getPoints();
                for (WayPoint wayPoint : wayPoints) {
                    wayPointModels.add(new WayPointModel(wayPoint.getLatitude().doubleValue(), wayPoint.getLongitude().doubleValue()));
                }
            } catch (IOException e) {
                logger.info("Error reading file {}", e.getMessage());
                e.printStackTrace();
            }

            trackModel.setWaypoints(wayPointModels);
            model.setTrack(trackModel);

            model.setPercentage((int) Math.round(model.getCurrentRide().getDistance() / model.getTrack().getDistance() * 100));
        }

        logger.info("User {} details retrieved", user.getEmail());
        return model;
    }

    @GetMapping("/detail")
    public ActiveUserDetailModel getCurrentActiveUserDetails(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        if(user == null || user.getActiveUser() == null){
            logger.info("User {} not found or is not active", user.getEmail());
            return null;
        }

        ActiveUserDetailModel model = new ActiveUserDetailModel();
        model.setId(user.getId());
        model.setFirstName(user.getUserDetails().getFirstName());
        model.setSurname(user.getUserDetails().getSurname());
        model.setLastKnownLatitude(user.getActiveUser().getLastKnownLatitude());
        model.setLastKnownLongitude(user.getActiveUser().getLastKnownLongitude());

        TrackModel trackModel = new TrackModel();
        List<WayPointModel>wayPointModels = new ArrayList<>();

        List<CurrentTrackPoint> trackPoints = trackPointService.getTrackPointsForUserId(user.getId());
        trackPoints.sort(new TrackPointComparator());
        double ele = 0;
        double dist = 0;
        long movingTime = 0;

        if(trackPoints.size() > 0){
            CurrentTrackPoint lastPoint = trackPoints.get(0);
            for (CurrentTrackPoint trackPoint : trackPoints) {
                if(lastPoint != null && !lastPoint.equals(trackPoint)){
                    double height = trackPoint.getElevation() - lastPoint.getElevation();
                    
                    double theta = lastPoint.getLongitude() - trackPoint.getLongitude();
                    double distance = Math.sin(Math.toRadians(lastPoint.getLatitude())) * Math.sin(Math.toRadians(trackPoint.getLatitude())) + Math.cos(Math.toRadians(lastPoint.getLatitude())) * Math.cos(Math.toRadians(trackPoint.getLatitude())) * Math.cos(Math.toRadians(theta));
                    distance = Math.acos(distance);
                    distance = Math.toDegrees(distance);
                    distance = distance * (60 * 1.1515 * 1.609344 * 1000);
                    distance = Math.pow(distance, 2) + Math.pow(height, 2);
                    distance = Math.sqrt(distance);

                    dist += distance;
                    if(height > 0) ele += height;
                    movingTime += trackPoint.getTimestamp() - lastPoint.getTimestamp();

                    lastPoint = trackPoint;
                }
                wayPointModels.add(new WayPointModel(trackPoint.getLatitude(), trackPoint.getLongitude(), new Timestamp(trackPoint.getTimestamp())));
            }
        }
        
        trackModel.setDistance(dist);
        trackModel.setElevation(ele);
        trackModel.setMovingTime(movingTime);
        trackModel.setWaypoints(wayPointModels);
        model.setCurrentRide(trackModel);

        Track selectedTrack = user.getActiveUser().getSelectedTrack();
        if(selectedTrack != null){
            
            trackModel = new TrackModel();
            trackModel.setDistance(selectedTrack.getDistance());
            trackModel.setMovingTime(selectedTrack.getEstimatedMovingTime());
            wayPointModels = new ArrayList<>();
            
            GPX gpx;
            try {
                gpx = GPX.reader().read(selectedTrack.getGpxPath());
                List<WayPoint> wayPoints = gpx.getTracks().get(0).getSegments().get(0).getPoints();
                for (WayPoint wayPoint : wayPoints) {
                    wayPointModels.add(new WayPointModel(wayPoint.getLatitude().doubleValue(), wayPoint.getLongitude().doubleValue()));
                }
            } catch (IOException e) {
                logger.info("Error reading file {}", e.getMessage());
                e.printStackTrace();
            }

            trackModel.setWaypoints(wayPointModels);
            model.setTrack(trackModel);

            model.setPercentage((int) Math.round(model.getCurrentRide().getDistance() / model.getTrack().getDistance() * 100));
        }

        logger.info("User {} details retrieved", user.getEmail());
        return model;
    }

    @PostMapping("/strava")
    public void saveStravaDetails(@RequestBody StravaModel stravaModel){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        StravaUser stravaUser = new StravaUser();
        stravaUser.setUser(user);
        stravaUser.setStravaId(stravaModel.getId());
        stravaUser.setStravaAccessToken(stravaModel.getAccessToken());
        stravaUser.setStravaAccessTokenExpiration(stravaModel.getAccessTokenExpiration());
        stravaUser.setStravaRefreshToken(stravaModel.getRefreshToken());
        stravaUser.setStravaUploadActivity(stravaModel.getUpload());

        stravaUserService.saveStravaUser(stravaUser);
        logger.info("Strava details saved for user {}", user.getEmail());

    }

    @GetMapping("/strava/token")
    public String getStravaToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        StravaUser stravaUser = user.getStravaUser();

        if(stravaUser != null) {
            if(stravaUser.getStravaAccessTokenExpiration().before(new Timestamp(System.currentTimeMillis()))){
                return stravaService.fetchNewStravaTokens(stravaUser);
            }else{
                logger.info("Strava token retrieved for user {}", user.getEmail());
                return stravaUser.getStravaAccessToken();
            }
        } else {
            return "";
        }
    }

    @DeleteMapping("/strava")
    public void deleteStravaUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        StravaUser stravaUser = user.getStravaUser();
        if(stravaUser != null){
            user.setStravaUser(null);
            stravaUserService.deleteStravaUser(stravaUser);
            logger.info("Strava details deleted for user {}", user.getEmail());
        }else{
            logger.info("No Strava details found for user {}", user.getEmail());
        }
    }

    @GetMapping("/tracks")
    public List<TrackModel> getTracksForUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        if(user == null){
            logger.info("User {} not found or is not active", authentication.getName());
            return null;
        }

        List<TrackModel> tracks = new ArrayList<>();
        for (Track track : user.getTracks()) {
            TrackModel trackModel = new TrackModel();
            trackModel.setId(track.getId());
            trackModel.setName(track.getName());
            trackModel.setDistance(track.getDistance());
            trackModel.setElevation(track.getElevationGain());
            trackModel.setMovingTime(track.getEstimatedMovingTime());

            List<WayPointModel>wayPointModels = new ArrayList<>();
            GPX gpx;
            try {
                gpx = GPX.reader().read(track.getGpxPath());
                List<WayPoint> wayPoints = gpx.getTracks().get(0).getSegments().get(0).getPoints();
                for (WayPoint wayPoint : wayPoints) {
                    wayPointModels.add(new WayPointModel(wayPoint.getLatitude().doubleValue(), wayPoint.getLongitude().doubleValue()));
                }
            } catch (IOException e) {
                logger.info("Error reading file {}", e.getMessage());
                e.printStackTrace();
            }
            trackModel.setWaypoints(wayPointModels);

            tracks.add(trackModel);
        }

        logger.info("{} tracks retrieved for user {}", tracks.size(), user.getEmail());
        return tracks;
    }

    @PutMapping("/wearables/start")
    public void startNewWearablesTrack(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        if(user != null){
            ActiveUser activeUser = user.getActiveUser();
            if(activeUser != null){
                logger.info("User {} already has active ride", user.getEmail());
                return;
            }

            activeUser = new ActiveUser();
            activeUser.setUser(user);
            activeUserService.saveActiveUser(activeUser);

            logger.info("User {} started new ride", user.getEmail());
        }else{
            logger.info("User {} not found", authentication.getName());
        }
    }

    private Pair<UserDetails, ActiveUser> getUserDetailsAndActiveUser(Friend friend, User user){
        UserDetails userDetails;
        ActiveUser activeUser;
        if(friend.getUser1().equals(user)){
            userDetails = userDetailsService.getUserDetailsByUserId(friend.getUser2().getId());
            activeUser = activeUserService.getActiveUserByUserId(friend.getUser2().getId());
        }else{
            userDetails = userDetailsService.getUserDetailsByUserId(friend.getUser1().getId());
            activeUser = activeUserService.getActiveUserByUserId(friend.getUser1().getId());
        }
        return new Pair<>(userDetails, activeUser);
    }
    
}
