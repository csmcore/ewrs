package app.ewarehouse.repository;

import app.ewarehouse.entity.SalesApplicationTabTwo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesApplicationTabTwoRepository extends JpaRepository<SalesApplicationTabTwo,Integer> {

    Optional<SalesApplicationTabTwo> findByApplicationId(String applicationId);
}
