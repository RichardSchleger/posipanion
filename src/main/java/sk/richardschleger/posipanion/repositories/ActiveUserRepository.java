package sk.richardschleger.posipanion.repositories;

import org.springframework.data.repository.CrudRepository;

import sk.richardschleger.posipanion.entities.ActiveUser;

public interface ActiveUserRepository extends CrudRepository<ActiveUser, Integer> {
    
}
