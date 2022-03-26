package sk.richardschleger.posipanion.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sk.richardschleger.posipanion.entities.Friend;
import sk.richardschleger.posipanion.entities.User;
import sk.richardschleger.posipanion.services.FriendService;
import sk.richardschleger.posipanion.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/friend")
public class FriendController {
    
    private final UserService userService;

    private final FriendService friendService;
    
    public FriendController(UserService userService,
                            FriendService friendService){
        this.userService = userService;
        this.friendService = friendService;
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
            }
        } 
    }

    @PostMapping("/request/confirm/{id}")
    public void confirmFriendRequest(@PathVariable("id") Integer friendId){

        if(friendId != null && friendId != 0){

            Friend friend = friendService.getFriendById(friendId);
            if(friend != null){

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User currentUser = userService.getUserByEmail(authentication.getName());

                if(friend.getUser2().equals(currentUser)){
                    friend.setConfirmed(true);
                    friendService.save(friend);
                }
            }
        }
    }

    @PostMapping("/request/reject/{id}")
    public void rejectFriendRequest(@PathVariable("id") Integer friendId){

        if(friendId != null && friendId != 0){

            Friend friend = friendService.getFriendById(friendId);
            if(friend != null){

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User currentUser = userService.getUserByEmail(authentication.getName());

                if(friend.getUser2().equals(currentUser)){
                    friendService.delete(friend);
                }
            }
        }
    }

    @PostMapping("/remove/{id}")
    public void removeFriend(@PathVariable("id") Integer friendId){

        if(friendId != null && friendId != 0){

            Friend friend = friendService.getFriendById(friendId);
            if(friend != null){

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User currentUser = userService.getUserByEmail(authentication.getName());

                if(friend.getUser1().equals(currentUser) || friend.getUser2().equals(currentUser)){
                    friendService.delete(friend);
                }
            }
        }
    }

}
