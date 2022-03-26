package sk.richardschleger.posipanion.services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import sk.richardschleger.posipanion.entities.FcmToken;
import sk.richardschleger.posipanion.repositories.FcmTokenRepository;

@Service
public class FcmTokenService {
    
    private final FcmTokenRepository fcmTokenRepository;

    public FcmTokenService(FcmTokenRepository fcmTokenRepository){
        this.fcmTokenRepository = fcmTokenRepository;
    }

    @Transactional
    public void save(FcmToken token){
        fcmTokenRepository.save(token);
    }

    @Transactional
    public FcmToken getFcmTokenByTokenAndUserId(String token, int userId){
        return fcmTokenRepository.findByTokenAndUserId(token, userId).orElse(null);
    }

}
