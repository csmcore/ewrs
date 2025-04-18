package app.ewarehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.LoginTrail;
@Repository
public interface LoginTrailRepository extends JpaRepository<LoginTrail, Integer> {

	@Query("SELECT l FROM LoginTrail l " +
		       "WHERE (:search IS NULL OR " +
		       "LOWER(l.user.txtFullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		       "LOWER(l.ipAddress) LIKE LOWER(CONCAT('%', :search, '%')))")
	Page<LoginTrail> findLoginTrailByFiltes(String search, Pageable pageable);

	@Query("SELECT l FROM LoginTrail l")
	Page<LoginTrail> findAllLoginTrail(Pageable pageable);

}
