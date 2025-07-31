package team2.kakigowherebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team2.kakigowherebackend.model.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query(
            "SELECT p FROM Place p LEFT JOIN FETCH p.interestCategories WHERE p.googleId ="
                    + " :googleId")
    Place findByGoogleId(@Param("googleId") String kmlId);
}
