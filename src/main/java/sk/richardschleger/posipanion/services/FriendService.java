package sk.richardschleger.posipanion.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import sk.richardschleger.posipanion.entities.Friend;
import sk.richardschleger.posipanion.repositories.FriendRepository;

@Service
public class FriendService {
    
    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }


    @Transactional
    public List<Friend> getFriendsByUserId(int id){
        return friendRepository.findByUser1IdOrUser2Id(id, id);
    }
}
