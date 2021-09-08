package sk.richardschleger.posipanion.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import sk.richardschleger.posipanion.entities.CurrentTrackPoint;
import sk.richardschleger.posipanion.repositories.TrackPointRepository;

@Service
public class TrackPointService {
    
    private final TrackPointRepository trackPointRepository;

    public TrackPointService(TrackPointRepository trackPointRepository) {
        this.trackPointRepository = trackPointRepository;
    }
    
    public int getNumberOfPointsForTrackId(UUID trackId){
        return trackPointRepository.countByTrackId(trackId);
    }

    public void saveTrackPoint(CurrentTrackPoint trackPoint){
        trackPointRepository.save(trackPoint);
    }
}
