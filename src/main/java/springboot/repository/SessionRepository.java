package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.model.Session;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {

    Optional<Session> findByToken(String token);
}
