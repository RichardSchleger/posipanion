package sk.richardschleger.posipanion.services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import sk.richardschleger.posipanion.entities.StravaUser;
import sk.richardschleger.posipanion.repositories.StravaUserRepository;

@Service
public class StravaUserService {
    
    private final StravaUserRepository stravaUserRepository;

    public StravaUserService(StravaUserRepository stravaUserRepository) {
        this.stravaUserRepository = stravaUserRepository;
    }

    @Transactional
    public StravaUser getStravaUserByUserId(int id){
        return stravaUserRepository.findByUserId(id).orElse(null);
    }

    @Transactional
    public void saveStravaUser(StravaUser user){
        stravaUserRepository.save(user);
    }

}
