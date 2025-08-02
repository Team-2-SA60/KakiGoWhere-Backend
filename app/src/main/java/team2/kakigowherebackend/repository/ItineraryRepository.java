package team2.kakigowherebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team2.kakigowherebackend.model.Itinerary;

import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    @Query("SELECT i " +
            "FROM Itinerary i " +
            "JOIN i.tourist t " +
            "WHERE t.email = :email")
    List<Itinerary> findByTouristEmail(@Param("email") String email);
}
