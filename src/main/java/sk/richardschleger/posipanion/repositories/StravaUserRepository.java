package sk.richardschleger.posipanion.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import sk.richardschleger.posipanion.entities.StravaUser;

public interface StravaUserRepository extends CrudRepository<StravaUser, Integer> {
    
    Optional<StravaUser> findByUserId(@Param("user_id") int id);

}
