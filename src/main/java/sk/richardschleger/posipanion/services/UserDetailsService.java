package sk.richardschleger.posipanion.services;

import org.springframework.stereotype.Service;

import sk.richardschleger.posipanion.entities.UserDetails;
import sk.richardschleger.posipanion.repositories.UserDetailsRepository;

@Service
public class UserDetailsService {
    
    private final UserDetailsRepository userDetailsRepository;

    public UserDetailsService(UserDetailsRepository userDetailsRepository){
        this.userDetailsRepository = userDetailsRepository;
    }

    public UserDetails getUserDetailsByUserId(int id){
        return userDetailsRepository.findByUserId(id).orElse(null);
    }

    public void saveUserDetails(UserDetails userDetails){
        userDetailsRepository.save(userDetails);
    }

}
