package team2.kakigowherebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team2.kakigowherebackend.model.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {

}
