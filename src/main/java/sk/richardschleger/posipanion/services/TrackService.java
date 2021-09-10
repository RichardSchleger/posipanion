package sk.richardschleger.posipanion.services;

import org.springframework.stereotype.Service;

import sk.richardschleger.posipanion.entities.Track;
import sk.richardschleger.posipanion.repositories.TrackRepository;

@Service
public class TrackService {
    
    private final TrackRepository trackRepository;

    public TrackService(TrackRepository trackRepository){
        this.trackRepository = trackRepository;
    }

    public Track getTrackById(int id){
        return trackRepository.findById(id).orElse(null);
    }

    public void saveTrack(Track track){
        trackRepository.save(track);
    }

}
