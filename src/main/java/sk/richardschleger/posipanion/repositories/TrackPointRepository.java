package sk.richardschleger.posipanion.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;

import sk.richardschleger.posipanion.entities.TrackPoint;

import java.util.UUID;

public interface TrackPointRepository extends CassandraRepository<TrackPoint, UUID> {

}
