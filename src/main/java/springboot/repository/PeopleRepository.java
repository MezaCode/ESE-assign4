package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.model.People;


import java.util.List;

public interface PeopleRepository extends JpaRepository<People, Long> {

}
