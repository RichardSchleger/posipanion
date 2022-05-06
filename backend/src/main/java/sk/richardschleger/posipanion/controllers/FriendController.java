package sk.richardschleger.posipanion.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sk.richardschleger.posipanion.entities.Friend;
import sk.richardschleger.posipanion.entities.User;
import sk.richardschleger.posipanion.services.FirebaseMessageService;
import sk.richardschleger.posipanion.services.FriendService;
import sk.richardschleger.posipanion.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/friend")
public class FriendController {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    private final FriendService friendService;

    private final FirebaseMessageService firebaseMessageService;
    
    public FriendController(UserService userService,
                            FriendService friendService,
                            FirebaseMessageService firebaseMessageService){
        this.userService = userService;
        this.friendService = friendService;
        this.firebaseMessageService = firebaseMessageService;
    }

    @PostMapping("/request/send/{userId}")
    public void sendFriendRequest(@PathVariable("userId") Integer userId){

        if(userId != null && userId != 0){

            User newFriend = userService.getUserById(userId);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userService.getUserByEmail(authentication.getName());

            if(newFriend != null && currentUser != null){

                Friend friend = new Friend();
                friend.setUser1(currentUser);
                friend.setUser2(newFriend);
                friend.setConfirmed(false);

                friendService.save(friend);

                List<User> targetUsers = new ArrayList<>();
                targetUsers.add(newFriend);

                firebaseMessageService.sendMulticast(
                    targetUsers, 
                    "Nová žiadosť o priateľstvo!", 
                    "Používateľ " + currentUser.getUserDetails().getFirstName() + " " + currentUser.getUserDetails().getSurname() + " vám poslal žiadosť o priateľstvo.",
                    logger);

                logger.info("Friend request sent to user with id {} from user {}", userId, currentUser.getEmail());
            }else{
                logger.error("User or new friend not found");
            }
        }else{
            logger.error("UserId is null or 0");
        }
    }

    @PostMapping("/request/confirm/{id}")
    public void confirmFriendRequest(@PathVariable("id") Integer friendId){

        if(friendId != null && friendId != 0){

            Friend friend = friendService.getFriendById(friendId);
            if(friend != null){

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User currentUser = userService.getUserByEmail(authentication.getName());

                if(currentUser != null){
                    if(friend.getUser2().equals(currentUser)){
                        friend.setConfirmed(true);
                        friendService.save(friend);

                        List<User> targetUsers = new ArrayList<>();
                        targetUsers.add(friend.getUser1());
        
                        firebaseMessageService.sendMulticast(
                            targetUsers, 
                            "Vaša žiadosť o priateľstvo bola prijatá!", 
                            "Používateľ " + currentUser.getUserDetails().getFirstName() + " " + currentUser.getUserDetails().getSurname() + " prijal vašu žiadosť o priateľstvo.",
                            logger);

                        logger.info("Friend request confirmed between users {} and {}", friend.getUser1().getEmail(), friend.getUser2().getEmail());
                    }else{
                        logger.error("User {} cannot confirm friendship", currentUser.getEmail());
                    }
                }else{
                    logger.error("User {} not found", authentication.getName());
                }
                
            }else{
                logger.error("Friend connection not found");
            }
        }else{
            logger.error("FriendId is null or 0");
        }
    }

    @PostMapping("/request/reject/{id}")
    public void rejectFriendRequest(@PathVariable("id") Integer friendId){

        if(friendId != null && friendId != 0){

            Friend friend = friendService.getFriendById(friendId);
            if(friend != null){

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User currentUser = userService.getUserByEmail(authentication.getName());

                if(currentUser == null){
                    logger.error("User {} not found", authentication.getName());
                }

                if(friend.getUser1().equals(currentUser) || friend.getUser2().equals(currentUser)){
                    friendService.delete(friend);
                }else{
                    logger.error("User {} cannot reject friendship", currentUser.getEmail());
                }
            }else{
                logger.error("Friend connection not found");
            }
        }else{
            logger.error("FriendId is null or 0");
        }
    }

    @PostMapping("/remove/{id}")
    public void removeFriend(@PathVariable("id") Integer friendId){

        if(friendId != null && friendId != 0){

            Friend friend = friendService.getFriendById(friendId);
            if(friend != null){

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User currentUser = userService.getUserByEmail(authentication.getName());

                if(currentUser == null){
                    logger.error("User {} not found", authentication.getName());
                }

                if(friend.getUser1().equals(currentUser) || friend.getUser2().equals(currentUser)){
                    friendService.delete(friend);
                }else{
                    logger.error("User {} cannot remove friendship", currentUser.getEmail());
                }
            }else{
                logger.error("Friend connection not found");
            }
        }else{
            logger.error("FriendId is null or 0");
        }
    }

}
