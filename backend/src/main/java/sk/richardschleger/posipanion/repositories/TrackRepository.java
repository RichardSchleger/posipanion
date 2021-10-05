package sk.richardschleger.posipanion.repositories;

import org.springframework.data.repository.CrudRepository;

import sk.richardschleger.posipanion.entities.Track;

public interface TrackRepository extends CrudRepository<Track, Integer> {
    
}
