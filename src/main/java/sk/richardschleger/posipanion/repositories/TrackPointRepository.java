package sk.richardschleger.posipanion.repositories;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.query.Param;

import sk.richardschleger.posipanion.entities.CurrentTrackPoint;
import sk.richardschleger.posipanion.keys.TrackPointKey;

public interface TrackPointRepository extends CassandraRepository<CurrentTrackPoint, TrackPointKey> {

    int countByUserId(@Param("user_id") int userId);

    List<CurrentTrackPoint> findByUserId(@Param("user_id") int userId);

    long deleteByUserId(@Param("user_id") int userId);

}
