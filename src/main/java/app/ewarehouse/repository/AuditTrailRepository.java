package app.ewarehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.AuditTrail;

@Repository
public interface AuditTrailRepository extends JpaRepository<AuditTrail, Integer> {
    
	@Query(value = """
			SELECT us.vchFullName as firstname,adm.vchRoleName,a.vchAction,a.vchRemarks,
			vchIpAddress,a.vchDeviceName
			FROM
			    t_audit_trail a
			LEFT JOIN
			    m_admin_role adm ON a.intRoleId = adm.intId
			LEFT JOIN
			    m_admin_user us ON a.intUserId = us.intId
			WHERE
			    (adm.vchRoleName IS NOT NULL AND LOWER(adm.vchRoleName) LIKE LOWER(CONCAT('%', :search, '%')))
			    OR
			    (us.vchFullName IS NOT NULL AND LOWER(us.vchFullName) LIKE LOWER(CONCAT('%', :search, '%')))
                   OR
			    (a.vchRemarks IS NOT NULL AND LOWER(a.vchRemarks) LIKE LOWER(CONCAT('%', :search, '%')))
			""", nativeQuery = true)
	Page<Object> findAuditTrialFilter(String search, Pageable pageable);
    
	
	
	@Query(value = """
			SELECT  us.vchFullName as firstname,adm.vchRoleName,a.vchAction,a.vchRemarks,  
			vchIpAddress,a.vchDeviceName
			FROM
			    t_audit_trail a
			LEFT JOIN
			    m_admin_role adm ON a.intRoleId = adm.intId
			LEFT JOIN
			     m_admin_user us ON a.intUserId = us.intId
			""", nativeQuery = true)
	Page<Object> findAuditTrialDetails(Pageable pageable);

}
