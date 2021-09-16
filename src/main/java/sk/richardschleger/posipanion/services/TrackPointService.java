package sk.richardschleger.posipanion.services;

import java.util.List;

import org.springframework.stereotype.Service;

import sk.richardschleger.posipanion.entities.CurrentTrackPoint;
import sk.richardschleger.posipanion.keys.TrackPointKey;
import sk.richardschleger.posipanion.repositories.TrackPointRepository;

@Service
public class TrackPointService {
    
    private final TrackPointRepository trackPointRepository;

    public TrackPointService(TrackPointRepository trackPointRepository) {
        this.trackPointRepository = trackPointRepository;
    }

    public void saveTrackPoint(CurrentTrackPoint trackPoint){
        TrackPointKey key = trackPoint.getKey();
        key.setOrder(trackPointRepository.countByUserId(trackPoint.getKey().getUserId()));
        trackPoint.setKey(key);
        trackPointRepository.save(trackPoint);
    }

    public void removeTrackPointsForUserId(int id){
        trackPointRepository.deleteByUserId(id);
    }

    public List<CurrentTrackPoint> getTrackPointsForUserId(int id){
        return trackPointRepository.findByUserId(id);
    }
}
