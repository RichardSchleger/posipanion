package sk.richardschleger.posipanion.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import sk.richardschleger.posipanion.entities.ActiveUser;

public interface ActiveUserRepository extends CrudRepository<ActiveUser, Integer> {
    
    Optional<ActiveUser> findByUserId(@Param("user_id") int id);

}
