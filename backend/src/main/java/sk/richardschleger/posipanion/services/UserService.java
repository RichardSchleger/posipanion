package sk.richardschleger.posipanion.services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import sk.richardschleger.posipanion.entities.User;
import sk.richardschleger.posipanion.repositories.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User getUserById(int id){
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

}
