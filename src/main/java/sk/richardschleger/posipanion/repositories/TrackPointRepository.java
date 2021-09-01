package sk.richardschleger.posipanion.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;

import sk.richardschleger.posipanion.entities.TrackPoint;
import sk.richardschleger.posipanion.keys.TrackPointKey;

public interface TrackPointRepository extends CassandraRepository<TrackPoint, TrackPointKey> {

}
