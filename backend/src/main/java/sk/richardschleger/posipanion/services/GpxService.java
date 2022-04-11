package sk.richardschleger.posipanion.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.GPX.Version;
import sk.richardschleger.posipanion.comparators.TrackPointComparator;
import sk.richardschleger.posipanion.datatypes.Pair;
import sk.richardschleger.posipanion.entities.CurrentTrackPoint;
import sk.richardschleger.posipanion.entities.User;

@Service
public class GpxService {

    private final TrackPointService trackPointService;

    public GpxService(TrackPointService trackPointService) {
        this.trackPointService = trackPointService;
    }
    
    public Pair<GPX, Pair<Double, Double>> createGPXForUser(User user){
        List<WayPoint> wayPoints = new ArrayList<>();
        List<CurrentTrackPoint> trackPoints = trackPointService.getTrackPointsForUserId(user.getId());
        trackPoints.sort(new TrackPointComparator());
        double ele = 0;
        double dist = 0;
        CurrentTrackPoint lastPoint = trackPoints.get(0);
        for (CurrentTrackPoint trackPoint : trackPoints) {
            if(lastPoint != null && !lastPoint.equals(trackPoint)){
                double height = trackPoint.getElevation() - lastPoint.getElevation();
        
                double theta = lastPoint.getLongitude() - trackPoint.getLongitude();
                double distance = Math.sin(Math.toRadians(lastPoint.getLatitude())) * Math.sin(Math.toRadians(trackPoint.getLatitude())) + Math.cos(Math.toRadians(lastPoint.getLatitude())) * Math.cos(Math.toRadians(trackPoint.getLatitude())) * Math.cos(Math.toRadians(theta));
                distance = Math.acos(distance);
                distance = Math.toDegrees(distance);
                distance = distance * (60 * 1.1515 * 1.609344 * 1000);
                distance = Math.pow(distance, 2) + Math.pow(height, 2);
                distance = Math.sqrt(distance);

                dist += distance;
                if(height > 0) ele += height;

                lastPoint = trackPoint;
            }
            wayPoints.add(WayPoint.of(trackPoint.getLatitude(), trackPoint.getLongitude(), trackPoint.getElevation(), trackPoint.getTimestamp()));
            lastPoint = trackPoint;
        }
        TrackSegment segment = TrackSegment.of(wayPoints);
        return new Pair<>(GPX.builder(Version.V11, "PosiPanion").addTrack(track -> track.addSegment(segment)).build(), new Pair<>(dist, ele));
    }

}
