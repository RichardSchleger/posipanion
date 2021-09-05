package sk.richardschleger.posipanion.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;

import sk.richardschleger.posipanion.entities.User;

public interface UserRepository extends CassandraRepository<User, String>{

}
