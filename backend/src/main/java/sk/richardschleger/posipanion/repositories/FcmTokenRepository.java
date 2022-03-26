package sk.richardschleger.posipanion.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import sk.richardschleger.posipanion.entities.FcmToken;

public interface FcmTokenRepository extends CrudRepository<FcmToken, Integer> {
    
    Optional<FcmToken> findByTokenAndUserId(@Param("token") String token, @Param("user_id") int userId);

}
