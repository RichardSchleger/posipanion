package sk.richardschleger.posipanion.repositories;

import java.util.List;


import sk.richardschleger.posipanion.entities.CurrentTrackPoint;

public interface TrackPointRepository{

    List<CurrentTrackPoint> findByUserId(int userId);

    CurrentTrackPoint findLastByUserId(int userId);

    void deleteByUserId(int userId);

    void save(CurrentTrackPoint trackPoint);

}
