package sk.richardschleger.posipanion.repositories;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

import sk.richardschleger.posipanion.entities.UserStrava;

public interface UserStravaRepositiry extends CassandraRepository<UserStrava, UUID>{
    
}
