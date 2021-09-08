package sk.richardschleger.posipanion.repositories;

import org.springframework.data.repository.CrudRepository;

import sk.richardschleger.posipanion.entities.StravaUser;

public interface StravaUserRepository extends CrudRepository<StravaUser, Integer> {
    
}
