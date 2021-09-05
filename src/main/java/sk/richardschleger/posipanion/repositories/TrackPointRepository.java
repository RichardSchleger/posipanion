package sk.richardschleger.posipanion.repositories;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.query.Param;

import sk.richardschleger.posipanion.entities.TrackPoint;
import sk.richardschleger.posipanion.keys.TrackPointKey;

public interface TrackPointRepository extends CassandraRepository<TrackPoint, TrackPointKey> {

    int countByTrackId(@Param("trackid") UUID trackId);

}
