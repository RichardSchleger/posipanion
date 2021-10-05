package sk.richardschleger.posipanion.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import sk.richardschleger.posipanion.entities.UserDetails;

public interface UserDetailsRepository extends CrudRepository<UserDetails, Integer> {
    
    Optional<UserDetails> findByUserId(@Param("user_id") int id);

}
