package app.ewarehouse.repository;

import app.ewarehouse.entity.SalesApplicationTabOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesApplicationTabOneRepository extends JpaRepository<SalesApplicationTabOne, Integer> {
    Optional<SalesApplicationTabOne> findByApplicationId(String applicationId);
}
