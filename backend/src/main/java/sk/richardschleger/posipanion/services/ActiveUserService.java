package sk.richardschleger.posipanion.services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import sk.richardschleger.posipanion.entities.ActiveUser;
import sk.richardschleger.posipanion.repositories.ActiveUserRepository;

@Service
public class ActiveUserService {
    
    private final ActiveUserRepository activeUserRepository;

    public ActiveUserService(ActiveUserRepository activeUserRepository) {
        this.activeUserRepository = activeUserRepository;
    }

    @Transactional
    public void saveActiveUser(ActiveUser user){
        activeUserRepository.save(user);
    }

    @Transactional
    public ActiveUser getActiveUserByUserId(int id){
        return activeUserRepository.findByUserId(id).orElse(null);
    }

    @Transactional
    public void removeActiveUser(ActiveUser user){
        activeUserRepository.delete(user);
    }

}
