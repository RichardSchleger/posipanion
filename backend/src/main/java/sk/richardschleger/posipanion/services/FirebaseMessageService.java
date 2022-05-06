package sk.richardschleger.posipanion.services;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import sk.richardschleger.posipanion.entities.User;

@Service
public class FirebaseMessageService {
    
    public void sendMulticast(List<User> users, String title, String body, Logger logger) {
        
        List<String> tokens = new ArrayList<>();
        for(User user : users){
            user.getFcmTokens().forEach(token -> tokens.add(token.getToken()));
        }

        MulticastMessage message = MulticastMessage.builder()
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build()
            )
            .addAllTokens(tokens)
            .build();
        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            logger.info("{} messages were sent successfully", response.getSuccessCount());
        } catch (FirebaseMessagingException e) {
            logger.info("Error sending message", e);
            e.printStackTrace();
        }

    }

}
