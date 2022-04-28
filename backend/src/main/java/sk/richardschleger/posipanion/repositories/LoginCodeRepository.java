package sk.richardschleger.posipanion.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import sk.richardschleger.posipanion.entities.LoginCode;

public interface LoginCodeRepository extends CrudRepository<LoginCode, Integer> {

    Optional<LoginCode> findByCode(String code);

    Optional<LoginCode> findByUserId(int id);

}
