package app.ewarehouse.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ApplicationFeeConfig;

@Repository
public interface ApplicationFeeConfigRepository extends JpaRepository<ApplicationFeeConfig, Integer> {

	Optional<ApplicationFeeConfig> findByApplicationType(String applicationType);

	@Query("SELECT a FROM ApplicationFeeConfig a order by a.applicationFeeId desc")
	Page<ApplicationFeeConfig> findAllApplicationFeeData(Pageable pageable);

	@Query("SELECT a FROM ApplicationFeeConfig a " + "WHERE (:search IS NULL OR "
			+ "LOWER(a.applicationType) LIKE LOWER(CONCAT('%', :search, '%')) " + ")" +
			" order by a.applicationFeeId desc")
	Page<ApplicationFeeConfig> findByFilters(String search, Pageable pageable);
	
	
	@Query("SELECT a.paymentValue FROM ApplicationFeeConfig a WHERE a.applicationTypeAliasName = :aliasName")
	Double findPaymentValueByAliasName(@Param("aliasName") String applicationTypeAliasName);


}
