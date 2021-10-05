package sk.richardschleger.posipanion.comparators;

import java.util.Comparator;

import sk.richardschleger.posipanion.entities.CurrentTrackPoint;

public class TrackPointComparator implements Comparator<CurrentTrackPoint>{

    @Override
    public int compare(CurrentTrackPoint trackPoint1, CurrentTrackPoint trackPoint2) {
        return Long.compare(trackPoint1.getTimestamp(), trackPoint2.getTimestamp());
    }
}
