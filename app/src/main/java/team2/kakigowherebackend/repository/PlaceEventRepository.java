package team2.kakigowherebackend.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team2.kakigowherebackend.model.PlaceEvent;

public interface PlaceEventRepository extends JpaRepository<PlaceEvent, Long> {

    List<PlaceEvent> findByPlace_Id(Long placeId);

    // Search across event name/ place name - IS NULL/ '' returns all
    @Query(
            """
            SELECT e FROM PlaceEvent e
            JOIN e.place p
            WHERE (:kw IS NULL OR :kw = ''
            OR LOWER(e.name) LIKE LOWER(CONCAT('%', :kw, '%'))
            OR LOWER(p.name) LIKE LOWER(CONCAT('%', :kw, '%')))
            ORDER BY e.startDate DESC, e.id DESC
            """)
    Page<PlaceEvent> search(@Param("kw") String keyword, Pageable pageable);

    boolean existsByNameAndStartDateAndEndDateAndPlaceId(
            String name, LocalDate startDate, LocalDate endDate, Long placeId);
}
