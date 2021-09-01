package sk.richardschleger.posipanion.repositories;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

import sk.richardschleger.posipanion.entities.UserDetails;

public interface UserDetailsRepository extends CassandraRepository<UserDetails, UUID>{
    
}
