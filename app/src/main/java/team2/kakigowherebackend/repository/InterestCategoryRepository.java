package team2.kakigowherebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team2.kakigowherebackend.model.InterestCategory;

@Repository
public interface InterestCategoryRepository extends JpaRepository<InterestCategory, Long> {

    @Query("SELECT i FROM InterestCategory i WHERE i.name = :name")
    InterestCategory findByName(@Param("name") String name);
}
