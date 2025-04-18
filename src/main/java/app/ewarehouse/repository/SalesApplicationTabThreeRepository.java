package app.ewarehouse.repository;

import app.ewarehouse.entity.SalesApplicationTabThree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesApplicationTabThreeRepository extends JpaRepository<SalesApplicationTabThree, Integer> {
    Optional<SalesApplicationTabThree> findByApplicationId(String applicationId);
}
