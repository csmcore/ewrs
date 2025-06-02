package app.ewarehouse.repository;


import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.dto.RoutineComplianceInspDTO;
import app.ewarehouse.entity.RoutineCompliance;
import app.ewarehouse.entity.RoutineComplianceInspection;

@Repository
public interface RoutineComplianceInspectionRepository extends JpaRepository<RoutineComplianceInspection, Long> {
	
	@Query(value = """
		    SELECT distinct r.routine_com_id as routineComId, r.company_id as companyId, r.warehouse_id as warehouseId, r.facility_type as facilityType, r.inspection_type as inspectionType, 
		           r.inspection_time as inspectionTime, r.start_date as startDate, r.end_date as endDate, r.requested_by as requestedBy, r.remarks as remarks, r.inspectionPlan as inspectionPlan 
		           , r.status as status, 
		           c.vchCompanyName as companyName, w.vchWarehouseName as warehouseName,ms.statuspresent as statuspresent,r.compliance_officer as complianceOfficer,
		           r.officer_office as officerOffice,r.other_facilityType as otherFacilityType,r.int_compliance_edit as intComplianceEdit
		    FROM t_routine_compliance_inspection r
		    JOIN m_company_warehouse_details c ON r.company_id = c.vchCompanyId
		    JOIN m_company_warehouse_details w ON r.warehouse_id = w.vchWarehouseId
		    join m_status_master ms on ms.statusid=r.status
		     ORDER BY r.routine_com_id desc
		    """, 
		    countQuery = """
		    SELECT COUNT(r.routine_com_id) 
		    FROM t_routine_compliance_inspection r
		    JOIN m_company_warehouse_details c ON r.company_id = c.vchCompanyId
		    JOIN m_company_warehouse_details w ON r.warehouse_id = w.vchWarehouseId
		    """,
		    nativeQuery = true)
		Page<Object[]> getRoutineCompData(Pageable pageable);

//	Page<RoutineCompliance> findByFilters(Date fromDate, Date toDate, String search, Integer userId, Integer integer,
//			Integer roleId, int oicCompliance, Pageable sortedPageable);
	
//	@Query("SELECT DISTINCT r.routineComId AS routineComId, " +
//		       "r.companyId AS companyId, " +
//		       "r.warehouseId AS warehouseId, " +
//			   "r.startDate AS startDate, " +
//		       "r.endDate AS endDate, " +
//			   "r.inspectionTime AS inspectionTime, " +
//			//   "r.complianceOfficer AS complianceOfficer, " +
//			   "r.officerOffice AS officerOffice, " +
//		       "r.facilityType AS facilityType, " +
//		       "r.inspectionType AS inspectionType, " +
//		       "r.otherFacilityType AS otherFacilityType, " +
//		       "r.requestedBy AS requestedBy, " +
//		       "r.remarks AS remarks, " +
//		       "r.inspectionPlan AS inspectionPlan, " +
//		       "r.status AS status, " +
//		       "c.companyName AS companyName, " +
//		       "w.warehouseName AS warehouseName, " +
//		       "ms.statusPresent AS statusPresent, " +
//		       "r.complianceOfficer AS complianceOfficer " +
//		       "FROM RoutineComplianceInspection r " +
//		       "Inner JOIN CompanyWarehouseDetails c ON r.companyId = c.companyId " +
//		       "Inner JOIN CompanyWarehouseDetails w ON r.warehouseId = w.warehouseId " +
//		       "Inner JOIN StatusMaster ms ON ms.statusId = r.status " +
//		       "WHERE (:userId IS NULL OR r.complianceOfficer = :userId) " +
//		       "AND (:search IS NULL OR " +
//		       "LOWER(w.warehouseName) LIKE LOWER(CONCAT('%', :search, '%')) " +
//		       "OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :search, '%')))")
//	Page<RoutineComplianceInspDTO> findByFiltersData(String search, Integer userId,
//			Pageable sortedPageable);
	
	@Query(value = "SELECT distinct  r.routine_com_id AS routineComId, " +
            "r.company_id AS companyId, " +
            "r.warehouse_id AS warehouseId, " +
            "r.facility_type AS facilityType, " +
            "r.inspection_type AS inspectionType, " +
            "r.inspection_time AS inspectionTime, " +
            "r.start_date AS startDate, " +
            "r.end_date AS endDate, " +
            "r.requested_by AS requestedBy, " +
            "r.remarks AS remarks, " +
            "r.inspectionPlan AS inspectionPlan, " +
            "r.status AS status, " +
            "c.vchCompanyName AS companyName, " +
            "w.vchWarehouseName AS warehouseName, " +
            "ms.statusPresent AS statusPresent, " +
            "r.compliance_officer AS complianceOfficer, " +
            "r.officer_office AS officerOffice, " +
            "r.other_facilityType AS otherFacilityType, " +
            "r.int_compliance_edit as intComplianceEdit " +
           // "r.other_facilityType AS stm_updated_at " +
            "FROM t_routine_compliance_inspection r " +
            "INNER JOIN m_company_warehouse_details c ON r.company_id = c.vchCompanyId " +
            "INNER JOIN m_company_warehouse_details w ON r.warehouse_id = w.vchWarehouseId " +
            "INNER JOIN m_status_master ms ON ms.statusid = r.status " +
            "WHERE (:userId IS NULL OR r.compliance_officer = :userId) " +
            "AND (:search IS NULL OR " +
            "LOWER(w.vchWarehouseName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(c.vchCompanyName) LIKE LOWER(CONCAT('%', :search, '%')))"+
            "ORDER BY r.routine_com_id desc", 
    countQuery = "SELECT COUNT(DISTINCT r.routine_com_id) FROM t_routine_compliance_inspection r " +
                 "INNER JOIN m_company_warehouse_details c ON r.company_id = c.vchCompanyId " +
                 "INNER JOIN m_company_warehouse_details w ON r.warehouse_id = w.vchWarehouseId " +
                 "INNER JOIN m_status_master ms ON ms.statusid = r.status " +
                 "WHERE (:userId IS NULL OR r.compliance_officer = :userId) " +
                 "AND (:search IS NULL OR " +
                 "LOWER(w.vchWarehouseName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                 "OR LOWER(c.vchCompanyName) LIKE LOWER(CONCAT('%', :search, '%')))" +
                 "ORDER BY r.routine_com_id desc",  
    nativeQuery = true)
Page<Object[]> findByFiltersData(@Param("search") String search, 
												@Param("userId") Integer userId, 
                                              Pageable page);
	
	RoutineComplianceInspection findWarehouseAndCompanyByRoutineComId(Long routineComId);

	RoutineComplianceInspection findByRoutineComId(Long routinComplianceId);

	boolean existsByWarehouseId(String warehouseId);

	



}
