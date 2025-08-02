package team2.kakigowherebackend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team2.kakigowherebackend.model.Bookmark;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b FROM Bookmark b JOIN b.tourist t WHERE t.id = :touristId")
    List<Bookmark> findAllByTouristId(@Param("touristId") long touristId);

    @Query(
            "SELECT b FROM Bookmark b "
                    + "JOIN b.place p JOIN b.tourist t "
                    + "WHERE p.id = :placeId AND t.id = :touristId")
    Optional<Bookmark> findBookmarkByPlaceIdAndTouristId(long placeId, long touristId);

    // For saving PlaceStats (temporarily put here)
    @Query(
            "SELECT COUNT(b) FROM Bookmark b "
                    + "JOIN b.place p "
                    + "WHERE p.id = :placeId AND b.bookmarkedDate = :date "
                    + "GROUP BY b")
    long countBookmarksByPlaceAndDate(
            @Param("placeId") long placeId, @Param("date") LocalDate date);
}
