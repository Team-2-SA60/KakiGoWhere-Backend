package team2.kakigowherebackend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team2.kakigowherebackend.model.ItineraryDetail;

@Repository
public interface ItineraryDetailRepository extends JpaRepository<ItineraryDetail, Long> {

    @Query("SELECT id " + "FROM ItineraryDetail id " + "JOIN id.itinerary i " + "WHERE i.id = :id")
    List<ItineraryDetail> findDetailsByItineraryId(@Param("id") Long id);
}
