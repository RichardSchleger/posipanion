package sk.richardschleger.posipanion.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import sk.richardschleger.posipanion.entities.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    
    Optional<User> findByEmail(String email);

}
