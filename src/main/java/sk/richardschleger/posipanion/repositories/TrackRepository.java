package sk.richardschleger.posipanion.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;

import sk.richardschleger.posipanion.entities.Track;
import sk.richardschleger.posipanion.keys.TrackKey;

public interface TrackRepository extends CassandraRepository<Track, TrackKey>{
    
}
