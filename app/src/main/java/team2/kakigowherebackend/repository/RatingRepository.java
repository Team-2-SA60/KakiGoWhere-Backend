package team2.kakigowherebackend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team2.kakigowherebackend.dto.RatingItemDTO;
import team2.kakigowherebackend.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT DISTINCT r FROM Rating r WHERE r.place.id = :placeId")
    List<Rating> findByPlaceId(@Param("placeId") long placeId);

    @Query("SELECT r FROM Rating r WHERE r.place.id = :placeId AND r.tourist.id = :touristId")
    Optional<Rating> findByPlaceIdAndTouristId(
            @Param("placeId") long placeId, @Param("touristId") long touristId);

    @Query(
            """
    SELECT new team2.kakigowherebackend.dto.RatingItemDTO(
        r.id,
        r.tourist.id,
        r.tourist.name,
        r.rating,
        r.comment
    )
    FROM Rating r
    WHERE r.place.id = :placeId
      AND r.tourist.id = :touristId
""")
    Optional<RatingItemDTO> findMyRatingItemDTO(
            @Param("placeId") long placeId, @Param("touristId") long touristId);

    @Query(
            """
        SELECT new team2.kakigowherebackend.dto.RatingItemDTO(
            r.id,
            r.tourist.id,
            r.tourist.name,
            r.rating,
            r.comment
        )
        FROM Rating r
        WHERE r.place.id = :placeId
          AND r.tourist.id <> :touristId
    """)
    List<RatingItemDTO> findOtherRatingItemDTOs(
            @Param("placeId") long placeId, @Param("touristId") long touristId);
}
