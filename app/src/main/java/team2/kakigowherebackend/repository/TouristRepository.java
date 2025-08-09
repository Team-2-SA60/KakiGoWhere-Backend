package team2.kakigowherebackend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team2.kakigowherebackend.model.Tourist;

@Repository
public interface TouristRepository extends JpaRepository<Tourist, Long> {
    @Query(
            "SELECT DISTINCT t FROM Tourist t LEFT JOIN FETCH t.interestCategories WHERE t.email ="
                    + " :email")
    Optional<Tourist> findByEmail(@Param("email") String email);

    Optional<Tourist> findById(long touristId);

    boolean existsByEmail(String email);
}
