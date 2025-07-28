package team2.kakigowherebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team2.kakigowherebackend.model.Tourist;

public interface TouristRepository extends JpaRepository<Tourist, Long> {

}
