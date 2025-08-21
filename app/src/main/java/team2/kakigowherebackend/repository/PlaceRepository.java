package team2.kakigowherebackend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team2.kakigowherebackend.dto.PlaceNameDTO;
import team2.kakigowherebackend.model.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByGoogleId(String googleId);

    Optional<Place> findById(long placeId);

    Optional<Place> findByName(String name);

    List<Place> findAllByAutoFetch(boolean autoFetch);

    List<Place> findAllByActive(boolean active);

    @Query(
            "SELECT DISTINCT p FROM Place p "
                    + "LEFT JOIN p.interestCategories ic "
                    + "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
                    + " OR LOWER(ic.description) LIKE LOWER(CONCAT('%', :keyword, '%')) "
                    + "ORDER BY p.name ASC")
    Page<Place> getPlacesBySearch(@Param("keyword") String keyword, Pageable pageable);

    @Query(
            """
            SELECT new team2.kakigowherebackend.dto.PlaceNameDTO(p.id, p.name)
            FROM Place p
            ORDER BY p.name ASC
            """)
    List<PlaceNameDTO> findAllNames();
}
