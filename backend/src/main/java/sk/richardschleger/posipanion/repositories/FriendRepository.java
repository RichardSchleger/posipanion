package sk.richardschleger.posipanion.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import sk.richardschleger.posipanion.entities.Friend;

public interface FriendRepository extends CrudRepository<Friend, Integer>{
    
    @Query("from Friend f WHERE (f.user1.id = :id OR f.user2.id = :id) AND f.confirmed = :confirmed")
    List<Friend> findByUserIdAndConfirmed(@Param("id") int id, @Param("confirmed") boolean confirmed);

    List<Friend> findByUser1IdOrUser2Id(@Param("user1_id") int id1, @Param("user2_id") int id2);

}
