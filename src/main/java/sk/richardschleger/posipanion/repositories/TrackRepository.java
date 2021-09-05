package sk.richardschleger.posipanion.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.query.Param;

import sk.richardschleger.posipanion.entities.Track;
import sk.richardschleger.posipanion.keys.TrackKey;

public interface TrackRepository extends CassandraRepository<Track, TrackKey>{
    
    Optional<Track> findByUserEmail(@Param("useremail") String email);

    Optional<Track> findById(@Param("id") UUID id);

}
