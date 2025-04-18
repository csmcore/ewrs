package app.ewarehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintReinstatement;

@Repository
public interface ReinstatementRepository extends JpaRepository<ComplaintReinstatement, String> {

	@Query("Select reinstatementId from ComplaintReinstatement order by reinstatementId desc limit 1")
	String getId();

	Page<ComplaintReinstatement> findByUserId(int userId, Pageable pageable);

	Page<ComplaintReinstatement> findByIsCerLicCheck(String string, Pageable pageable);

	ComplaintReinstatement findByReinstatementId(String reinsatementId);
	
	//NoUses
	ComplaintReinstatement findTopByComplaintIdOrderByReinstatementDateDesc(Integer id);
	
	Page<ComplaintReinstatement> findByUserIdOrderByCreatedOnDesc(int userId, Pageable pageable);

	ComplaintReinstatement findTopByComplaintIdOrderByCreatedOnDesc(Integer id);

	@Query(value = "SELECT vchCompanyId FROM m_company_warehouse_details WHERE vchWarehouseId = :warehouseId AND bitDeletedFlag = false", nativeQuery = true)
	String findCompanyIdByWarehouseId(String warehouseId);
	

}
