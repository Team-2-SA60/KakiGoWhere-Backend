package team2.kakigowherebackend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team2.kakigowherebackend.model.Tourist;

@Repository
public interface TouristRepository extends JpaRepository<Tourist, Long> {
    Optional<Tourist> findByEmail(String email);

    Optional<Tourist> findById(long touristId);
}
