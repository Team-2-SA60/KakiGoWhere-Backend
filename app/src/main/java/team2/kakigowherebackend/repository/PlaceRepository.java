package team2.kakigowherebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team2.kakigowherebackend.model.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findByGoogleId(String googleId);
}
