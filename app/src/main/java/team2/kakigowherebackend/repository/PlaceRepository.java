package team2.kakigowherebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team2.kakigowherebackend.model.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT p " + "FROM Place p " + "WHERE p.kmlId = :kmlId")
    Place findByKmlId(@Param("kmlId") String kmlId);
}
