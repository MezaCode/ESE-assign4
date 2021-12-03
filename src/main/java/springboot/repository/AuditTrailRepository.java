package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.model.AuditTrail;

import java.util.List;
import java.util.Optional;

public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long> {

    Optional<AuditTrail> findByid(int id);
    List<AuditTrail> findBypersonId(long id);

}
