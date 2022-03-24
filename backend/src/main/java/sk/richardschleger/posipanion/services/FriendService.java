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
    public List<Friend> getPendingFriendsByUserId(int id){
        return friendRepository.findByUserIdAndConfirmed(id, false);
    }

    @Transactional
    public List<Friend> getConfirmedFriendsByUserId(int id){
        return friendRepository.findByUserIdAndConfirmed(id, true);
    }

    @Transactional
    public List<Friend> getFriendsByUserId(int id){
        return friendRepository.findByUser1IdOrUser2Id(id, id);
    }

    @Transactional
    public void save(Friend friend){
        friendRepository.save(friend);
    }

    @Transactional
    public Friend getFriendById(int id){
        return friendRepository.findById(id).orElse(null);
    }

    @Transactional
    public void delete(Friend friend){
        friendRepository.delete(friend);
    }
}
