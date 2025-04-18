package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ApproverStatus;
import app.ewarehouse.entity.ConformityMain;

@Repository
public interface ConformityMainRepository extends JpaRepository<ConformityMain, Long> {
    List<ConformityMain> findByApplicationStatusAndCompany_ProfDetIdOrderByIdDesc(ApproverStatus status, String companyProfDetId);
    
    List<ConformityMain> findByIntApplicationStatus(Integer intApplicationStatus);
    
    
    ConformityMain findByWarehouseId(String warehouseId);
    ConformityMain findByWarehouseIdAndApplicationStatusAndDeletedFlagFalse(String warehouseId,ApproverStatus status);
    
    ConformityMain findByIdAndDeletedFlagFalse(Integer id);
    
    Page<ConformityMain> findByIntApplicationStatusNot(Integer status, Pageable pageable);

    @Query(value = "select tcm.intcnfId , tcm.vchWareHouseId , taocld.vchWarehouseName , taocld.vchWhOperatorName , \r\n"
    		+ "taocld.vchEmail , taocld.vchMobileNumber  , taocld.vchLrNumber , tcoum.county_name , tsc.vchSubCountyName , mmw.wardName,\r\n"
    		+ "mccs.vchStatus , tcm.bitOicOneCC , tcm.bitOicOneInsp , tcm.bitOicTwoCC , tcm.bitOicTwoInsp , tcm.intApplicationStatus\r\n"
    		+ "from t_confirmity_main tcm\r\n"
    		+ "join t_application_of_conformity_location_det taocld on tcm.intlocId = taocld.intWhLocationId\r\n"
    		+ "join t_county_master tcoum on taocld.intCountyId = tcoum.county_id \r\n"
    		+ "join t_sub_county tsc on taocld.intSubCountyId = tsc.intId \r\n"
    		+ "JOIN m_master_ward mmw on taocld.intWardId = mmw.intWardMasterId\r\n"
    		+ "join m_con_cm_status mccs on tcm.intApplicationStatus = mccs.intStatusId \r\n"
    		+ "where tcm.enmApplicationStatus IN ('APPLIED', 'DEFERRED') and tcm.intApplicationStatus NOT IN (104) ORDER BY tcm.intcnfId DESC"
    		,countQuery = "select count(*) from t_confirmity_main tcm where tcm.enmApplicationStatus IN ('APPLIED', 'DEFERRED') \r\n"
    				+ "AND tcm.intApplicationStatus NOT IN (104);"
    		,nativeQuery = true)
    Page<Object[]> getAllPendingData(Pageable pageable);


    @Query(value = "select tcm.intcnfId , tcm.vchWareHouseId , taocld.vchWarehouseName , taocld.vchWhOperatorName , \r\n"
    		+ "taocld.vchEmail , taocld.vchMobileNumber  , taocld.vchLrNumber , tcoum.county_name , tsc.vchSubCountyName , mmw.wardName,\r\n"
    		+ "mccs.vchStatus , tcm.bitOicOneCC , tcm.bitOicOneInsp , tcm.bitOicTwoCC , tcm.bitOicTwoInsp , tcm.intApplicationStatus,\r\n"
    		+ "tctac.bitForwardToOicStatus\r\n"
    		+ "from t_confirmity_main tcm\r\n"
    		+ "join t_application_of_conformity_location_det taocld on tcm.intlocId = taocld.intWhLocationId\r\n"
    		+ "join t_county_master tcoum on taocld.intCountyId = tcoum.county_id \r\n"
    		+ "join t_sub_county tsc on taocld.intSubCountyId = tsc.intId \r\n"
    		+ "JOIN m_master_ward mmw on taocld.intWardId = mmw.intWardMasterId\r\n"
    		+ "join m_con_cm_status mccs on tcm.intApplicationStatus = mccs.intStatusId\r\n"
    		+ "join t_cnf_take_action_cc tctac on tcm.intcnfId = tctac.intcnfId \r\n"
    		+ "where tcm.enmApplicationStatus = 'APPLIED' and tcm.intApplicationStatus NOT IN (104)\r\n"
    		+ "and tctac.intCertificateCommitteeId = :userId ORDER BY tcm.intcnfId DESC"
    		,countQuery = "select count(*) from t_confirmity_main tcm\r\n"
    				+ "join t_cnf_take_action_cc tctac on tcm.intcnfId = tctac.intcnfId \r\n"
    				+ "where tcm.enmApplicationStatus = 'APPLIED'\r\n"
    				+ "AND tcm.intApplicationStatus NOT IN (104) and tctac.intCertificateCommitteeId = :userId;"
    		,nativeQuery = true)
	Page<Object[]> getAllPendingDataForCC(int userId, Pageable pageable);


	@Query(value = "select tcm.intcnfId , tcm.vchWareHouseId , taocld.vchWarehouseName , taocld.vchWhOperatorName , \r\n"
			+ "taocld.vchEmail , taocld.vchMobileNumber  , taocld.vchLrNumber , tcoum.county_name , tsc.vchSubCountyName , mmw.wardName,\r\n"
			+ "mccs.vchStatus , tcm.bitOicOneCC , tcm.bitOicOneInsp , tcm.bitOicTwoCC , tcm.bitOicTwoInsp , tcm.intApplicationStatus, \r\n"
			+ "tctai.bitForwardToOicStatus \r\n"
			+ "from t_confirmity_main tcm\r\n"
			+ "join t_application_of_conformity_location_det taocld on tcm.intlocId = taocld.intWhLocationId\r\n"
			+ "join t_county_master tcoum on taocld.intCountyId = tcoum.county_id \r\n"
			+ "join t_sub_county tsc on taocld.intSubCountyId = tsc.intId \r\n"
			+ "JOIN m_master_ward mmw on taocld.intWardId = mmw.intWardMasterId\r\n"
			+ "join m_con_cm_status mccs on tcm.intApplicationStatus = mccs.intStatusId\r\n"
			+ "join t_cnf_take_action_inspector tctai on tcm.intcnfId = tctai.intcnfId \r\n"
			+ "where tcm.enmApplicationStatus = 'APPLIED' and tcm.intApplicationStatus != 104\r\n"
			+ "and tctai.intInspectorTeamLeadId = :userId ORDER BY tcm.intcnfId DESC"
    		,countQuery = "select count(*) from t_confirmity_main tcm\r\n"
    				+ "join t_cnf_take_action_inspector tctai on tcm.intcnfId = tctai.intcnfId \r\n"
    				+ "where tcm.enmApplicationStatus = 'APPLIED'\r\n"
    				+ "AND tcm.intApplicationStatus NOT IN (104) and tctai.intInspectorTeamLeadId = :userId"
    		,nativeQuery = true)
	Page<Object[]> getAllPendingDataForInspector(int userId, Pageable pageable);

	
	@Query(value = "SELECT tcm.intcnfId, \r\n"
			+ "       tcm.vchWareHouseId, \r\n"
			+ "       taocld.vchWarehouseName, \r\n"
			+ "       taocld.vchWhOperatorName, \r\n"
			+ "       taocld.vchEmail, \r\n"
			+ "       taocld.vchMobileNumber,  \r\n"
			+ "       taocld.vchLrNumber, \r\n"
			+ "       tcoum.county_name, \r\n"
			+ "       tsc.vchSubCountyName, \r\n"
			+ "       mmw.wardName , tcc.certificateNo FROM t_confirmity_main tcm\r\n"
			+ "JOIN t_application_of_conformity_location_det taocld \r\n"
			+ "    ON tcm.intlocId = taocld.intWhLocationId\r\n"
			+ "JOIN t_county_master tcoum \r\n"
			+ "    ON taocld.intCountyId = tcoum.county_id \r\n"
			+ "JOIN t_sub_county tsc \r\n"
			+ "    ON taocld.intSubCountyId = tsc.intId \r\n"
			+ "JOIN m_master_ward mmw\r\n"
			+ "    ON taocld.intWardId = mmw.intWardMasterId \r\n"
			+ "JOIN t_cnf_certificate tcc\r\n"
			+ "	ON tcm.intcnfId = tcc.intcnfId \r\n"
			+ "    WHERE tcm.enmApplicationStatus = 'APPROVED' \r\n"
			+ "      AND tcm.intApplicationStatus = 104 ORDER BY tcm.intcnfId DESC" , nativeQuery = true)
	List<Object[]> findAllApprovedData();
	
	@Query(value = "SELECT tcm.intcnfId, \r\n"
			+ "       tcm.vchWareHouseId, \r\n"
			+ "       taocld.vchWarehouseName, \r\n"
			+ "       taocld.vchWhOperatorName, \r\n"
			+ "       taocld.vchEmail, \r\n"
			+ "       taocld.vchMobileNumber,  \r\n"
			+ "       taocld.vchLrNumber, \r\n"
			+ "       tcoum.county_name, \r\n"
			+ "       tsc.vchSubCountyName, \r\n"
			+ "       mmw.wardName , tcc.certificateNo FROM t_confirmity_main tcm\r\n"
			+ "JOIN t_application_of_conformity_location_det taocld \r\n"
			+ "    ON tcm.intlocId = taocld.intWhLocationId\r\n"
			+ "JOIN t_county_master tcoum \r\n"
			+ "    ON taocld.intCountyId = tcoum.county_id \r\n"
			+ "JOIN t_sub_county tsc \r\n"
			+ "    ON taocld.intSubCountyId = tsc.intId \r\n"
			+ "JOIN m_master_ward mmw\r\n"
			+ "    ON taocld.intWardId = mmw.intWardMasterId \r\n"
			+ "JOIN t_cnf_certificate tcc\r\n"
			+ "	ON tcm.intcnfId = tcc.intcnfId \r\n"
			+ "    WHERE tcm.enmApplicationStatus = 'APPROVED' \r\n"
			+ "      AND tcm.intApplicationStatus = 104 ORDER BY tcm.intcnfId DESC" , nativeQuery = true)
	Page<Object[]> findAllApprovedData(Pageable pageable);

	@Query(value="SELECT \r\n"
			+ "    tcm.intcnfId, \r\n"
			+ "    tcm.vchWareHouseId, \r\n"
			+ "    taocld.vchWarehouseName, \r\n"
			+ "    taocld.vchWhOperatorName, \r\n"
			+ "    taocld.vchEmail, \r\n"
			+ "    taocld.vchMobileNumber,  \r\n"
			+ "    taocld.vchLrNumber, \r\n"
			+ "    tcoum.county_name, \r\n"
			+ "    tsc.vchSubCountyName, \r\n"
			+ "    mmw.wardName, \r\n"
			+ "    tcm.vchDeferCeoRemarks,\r\n"
			+ "    tctao.remarkForDeferring \r\n"
			+ "FROM t_confirmity_main tcm\r\n"
			+ "JOIN t_application_of_conformity_location_det taocld \r\n"
			+ "    ON tcm.intlocId = taocld.intWhLocationId\r\n"
			+ "JOIN t_county_master tcoum \r\n"
			+ "    ON taocld.intCountyId = tcoum.county_id \r\n"
			+ "JOIN t_sub_county tsc \r\n"
			+ "    ON taocld.intSubCountyId = tsc.intId \r\n"
			+ "JOIN m_master_ward mmw\r\n"
			+ "    ON taocld.intWardId = mmw.intWardMasterId\r\n"
			+ "JOIN t_cnf_take_action_oic tctao\r\n"
			+ "	ON tctao.intcnfId = tcm.intcnfId\r\n"
			+ "WHERE \r\n"
			+ "    tcm.enmApplicationStatus = 'DEFERRED'\r\n"
			+ "  AND tcm.intApplicationStatus IN (105 , 106) ORDER BY tcm.intcnfId DESC;",nativeQuery = true)
	List<Object[]> findAllDeferredDataForTakeAction();
	
	
	@Query(value="SELECT \r\n"
			+ "    tcm.intcnfId, \r\n"
			+ "    tcm.vchWareHouseId, \r\n"
			+ "    taocld.vchWarehouseName, \r\n"
			+ "    taocld.vchWhOperatorName, \r\n"
			+ "    taocld.vchEmail, \r\n"
			+ "    taocld.vchMobileNumber,  \r\n"
			+ "    taocld.vchLrNumber, \r\n"
			+ "    tcoum.county_name, \r\n"
			+ "    tsc.vchSubCountyName, \r\n"
			+ "    mmw.wardName, \r\n"
			+ "    tcm.vchDeferCeoRemarks,\r\n"
			+ "    tctao.remarkForDeferring \r\n"
			+ "FROM t_confirmity_main tcm\r\n"
			+ "JOIN t_application_of_conformity_location_det taocld \r\n"
			+ "    ON tcm.intlocId = taocld.intWhLocationId\r\n"
			+ "JOIN t_county_master tcoum \r\n"
			+ "    ON taocld.intCountyId = tcoum.county_id \r\n"
			+ "JOIN t_sub_county tsc \r\n"
			+ "    ON taocld.intSubCountyId = tsc.intId \r\n"
			+ "JOIN m_master_ward mmw\r\n"
			+ "    ON taocld.intWardId = mmw.intWardMasterId\r\n"
			+ "JOIN t_cnf_take_action_oic tctao\r\n"
			+ "	ON tctao.intcnfId = tcm.intcnfId\r\n"
			+ "WHERE \r\n"
			+ "    tcm.enmApplicationStatus = 'DEFERRED'\r\n"
			+ "  AND tcm.intApplicationStatus IN (105 , 106) ORDER BY tcm.intcnfId DESC;",nativeQuery = true)
	Page<Object[]> findAllDeferredDataForTakeAction(Pageable pageable);
	
	
	
	
	@Query(value = "SELECT tcm.intcnfId, \r\n"
			+ "       tcm.vchWareHouseId, \r\n"
			+ "       taocld.vchWarehouseName, \r\n"
			+ "       taocld.vchWhOperatorName, \r\n"
			+ "       taocld.vchEmail, \r\n"
			+ "       taocld.vchMobileNumber,  \r\n"
			+ "       taocld.vchLrNumber, \r\n"
			+ "       tcoum.county_name, \r\n"
			+ "       tsc.vchSubCountyName, \r\n"
			+ "       mmw.wardName , tcm.vchRejectionRemarks FROM t_confirmity_main tcm\r\n"
			+ "JOIN t_application_of_conformity_location_det taocld \r\n"
			+ "    ON tcm.intlocId = taocld.intWhLocationId\r\n"
			+ "JOIN t_county_master tcoum \r\n"
			+ "    ON taocld.intCountyId = tcoum.county_id \r\n"
			+ "JOIN t_sub_county tsc \r\n"
			+ "    ON taocld.intSubCountyId = tsc.intId \r\n"
			+ "JOIN m_master_ward mmw\r\n"
			+ "    ON taocld.intWardId = mmw.intWardMasterId \r\n"
			+ "    WHERE tcm.enmApplicationStatus = 'REJECTED' \r\n"
			+ "      AND tcm.intApplicationStatus = 107 ORDER BY tcm.intcnfId DESC" , nativeQuery = true)
	Page<Object[]> findAllRejectedDataForTakeAction(Pageable pageable);
	
	@Query("SELECT c.id FROM ConformityMain c WHERE c.warehouseId = :warehouseId")
	Integer findIdByWarehouseId(String warehouseId);
    
}





