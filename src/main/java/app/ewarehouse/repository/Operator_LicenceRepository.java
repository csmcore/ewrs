package app.ewarehouse.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.OperatorLicenceEntity;


@Repository
public interface Operator_LicenceRepository extends JpaRepository<OperatorLicenceEntity, Integer>  {

	Optional<OperatorLicenceEntity> findTopByOplIdStartingWithOrderByOplIdDesc(String IdFormat);
	OperatorLicenceEntity findByLicenceSnoAndDeletedFlagFalse(Integer id);
	OperatorLicenceEntity  findByOplIdAndLicenceCertGenAndDeletedFlagFalse(String id,boolean certStatus);
	OperatorLicenceEntity  findByWarehouseIdAndFormOneCIdAndDeletedFlagFalse(String whId,String onecId);
	OperatorLicenceEntity  findByWarehouseIdAndLicenseGenTrueAndLicenceCertGenTrueAndDeletedFlagFalse(String whId);
	
	

	Integer countByDeletedFlagAndCreatedBy(boolean b, int i);
	@Query(value="""
			SELECT vchPaymentStatus FROM t_operator_licence_application
			Where vchWareHouseId=:whid 
			AND vchApprovalStatus IN ('PENDING','APPROVED','DEFERRED') 
			AND bitLicenceCertGen=false 
			AND bitLicenseGen=false 
			AND vchFormStatus='Submitted'
			""",nativeQuery=true)
	String checkPaymentStatus(@Param("whid")String whid);
	
	
	@Query(value="""
			SELECT ola.vchOPlId,loc.vchWarehouseName,loc.vchWhOperatorName,loc.vchEmail,loc.vchMobileNumber,loc.vchLrNumber,
			county.county_name,subcounty.vchSubCountyName,ward.wardName,
			(select mu.vchFullName from m_admin_user as mu Where mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName ,
			ola.vchFormOneCId,payment.id as paymentId ,payment.enmPaymentStatus, ola.vchApplicationStatus,ola.vchApprovalStatus,
			TSA.intOnlineServiceId,TSA.intStageNo,TSA.intPendingAt,TSA.intProcessId,TSA.intLabelId,ola.vchWareHouseId,ola.vchCompanyId 
			FROM t_operator_licence_application ola
			inner join  t_application_of_conformity_location_det loc
			on ola.vchWareHouseId=loc.vchWarehouseId
			inner join t_county_master county on county.county_id=loc.intCountyId
			inner join t_sub_county subcounty on subcounty.intId=loc.intSubCountyId
			inner join m_master_ward ward on ward.intWardMasterId=loc.intWardId
			inner join t_payment_ewarehouse payment on payment.id=ola.intPaymentId
			inner join t_opl_service_approval TSA on TSA.intOnlineServiceId=ola.intLicenceSno
			LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, TSA.intPendingAt) > 0 )
			WHERE ola.bitDeletedFlag = 0
			AND ola.vchApprovalStatus=:approvalStatus
			AND ola.vchPaymentStatus='PAID'
			AND payment.enmPaymentStatus='PAID'
			AND ola.intPaymentId!=0
			AND TSA.bitDeletedFlag = 0
			AND TSA.intProcessId =:processId
			AND TSA.intPendingAt != 0
			AND (:search IS NULL OR LOWER(ola.vchOPlId) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWarehouseName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWhOperatorName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchEmail) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchMobileNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchLrNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(county.county_name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(subcounty.vchSubCountyName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(ward.wardName) LIKE LOWER(CONCAT('%', :search, '%')))
			order by ola.intCreatedBy desc
		""",nativeQuery=true,
			countQuery = """
			SELECT count(1)
			FROM t_operator_licence_application ola
			inner join  t_application_of_conformity_location_det loc on ola.vchWareHouseId=loc.vchWarehouseId
			inner join t_county_master county on county.county_id=loc.intCountyId
			inner join t_sub_county subcounty on subcounty.intId=loc.intSubCountyId
			inner join m_master_ward ward on ward.intWardMasterId=loc.intWardId
			inner join t_payment_ewarehouse payment on payment.id=ola.intPaymentId
			inner join t_opl_service_approval TSA on TSA.intOnlineServiceId=ola.intLicenceSno
			LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, TSA.intPendingAt) > 0 )
			WHERE ola.bitDeletedFlag = 0
			AND ola.vchApprovalStatus=:approvalStatus
			AND ola.vchPaymentStatus='PAID'
			AND payment.enmPaymentStatus='PAID'
			AND ola.intPaymentId!=0
			AND TSA.bitDeletedFlag = 0
			AND TSA.intProcessId =:processId
			AND TSA.intPendingAt != 0
			AND (:search IS NULL OR
			LOWER(ola.vchOPlId) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWarehouseName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWhOperatorName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchEmail) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchMobileNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchLrNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(county.county_name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(subcounty.vchSubCountyName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(ward.wardName) LIKE LOWER(CONCAT('%', :search, '%')))
			order by ola.intCreatedBy desc
			 """)
	Page<Map<String,Object>> getAllApprovalData(@Param("processId")Integer intProcessId,@Param("approvalStatus")String approvalStatus,@Param("search")String search,Pageable pageRequest);
	
	@Query(value="""
			SELECT count(*) as count FROM t_opl_service_approval where bitInspAllocated=true and bitCLCAllocated=true
			""",nativeQuery=true)
	  Integer checkStageOneCompeleted();
	
	//AND TSA.intStageNo NOT IN(2)  AND TSA.intPendingAt = :roleId  	  @Param("roleId") Integer roleId
	
   //  AND TD.intId =:roleId
	
	
	
	
	@Query(value="""
		    SELECT ola.vchOPlId,loc.vchWarehouseName,loc.vchWhOperatorName,loc.vchEmail,loc.vchMobileNumber,loc.vchLrNumber,
			county.county_name,subcounty.vchSubCountyName,ward.wardName,
			(select mu.vchFullName from m_admin_user as mu Where mu.intId=opl_clc.icmUserId and mu.bitDeletedFlag=0) as userName ,
			ola.vchFormOneCId,payment.id as paymentId ,payment.enmPaymentStatus, ola.vchApplicationStatus,ola.vchApprovalStatus,
			opl_clc.icmComplaintAppId as intOnlineServiceId,opl_clc.intStageNo,opl_clc.intPendingAt,opl_clc.intProcessId,opl_clc.intLableId as intLabelId,ola.vchWareHouseId,ola.vchCompanyId,opl_clc.icmStatus 
			FROM t_operator_licence_application ola
			inner join  t_application_of_conformity_location_det loc
			on ola.vchWareHouseId=loc.vchWarehouseId
			inner join t_county_master county on county.county_id=loc.intCountyId
			inner join t_sub_county subcounty on subcounty.intId=loc.intSubCountyId
			inner join m_master_ward ward on ward.intWardMasterId=loc.intWardId
			inner join t_payment_ewarehouse payment on payment.id=ola.intPaymentId
			inner join t_opl_service_approval TSA on TSA.intOnlineServiceId=ola.intLicenceSno
            inner join t_opl_clc opl_clc on opl_clc.icmComplaintAppId=ola.intLicenceSno
			LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId,opl_clc.intPendingAt) > 0 )
			WHERE ola.bitDeletedFlag = 0
			AND ola.vchApprovalStatus=:approvalStatus
			AND ola.vchPaymentStatus='PAID'
			AND payment.enmPaymentStatus='PAID'
			AND ola.intPaymentId!=0
			AND TSA.bitDeletedFlag = 0
			AND TSA.intProcessId =:processId
			AND TSA.intPendingAt != 0
			AND opl_clc.icmRoleId=:roleId 
			AND opl_clc.icmUserId=:userId
			AND TD.intId =:roleId
			AND (:search IS NULL OR LOWER(ola.vchOPlId) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWarehouseName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWhOperatorName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchEmail) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchMobileNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchLrNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(county.county_name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(subcounty.vchSubCountyName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(ward.wardName) LIKE LOWER(CONCAT('%', :search, '%')))
		""",nativeQuery=true,
			countQuery = """
			SELECT count(1)
		    	FROM t_operator_licence_application ola
			inner join  t_application_of_conformity_location_det loc
			on ola.vchWareHouseId=loc.vchWarehouseId
			inner join t_county_master county on county.county_id=loc.intCountyId
			inner join t_sub_county subcounty on subcounty.intId=loc.intSubCountyId
			inner join m_master_ward ward on ward.intWardMasterId=loc.intWardId
			inner join t_payment_ewarehouse payment on payment.id=ola.intPaymentId
			inner join t_opl_service_approval TSA on TSA.intOnlineServiceId=ola.intLicenceSno
            inner join t_opl_clc opl_clc on opl_clc.icmComplaintAppId=ola.intLicenceSno
			LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId,opl_clc.intPendingAt) > 0 )
			WHERE ola.bitDeletedFlag = 0
			AND ola.vchApprovalStatus=:approvalStatus
			AND ola.vchPaymentStatus='PAID'
			AND payment.enmPaymentStatus='PAID'
			AND ola.intPaymentId!=0
			AND TSA.bitDeletedFlag = 0
			AND TSA.intProcessId =:processId
			AND TSA.intPendingAt != 0
			AND opl_clc.icmRoleId=:roleId 
			AND opl_clc.icmUserId=:userId
			AND TD.intId =:roleId
			AND (:search IS NULL OR LOWER(ola.vchOPlId) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWarehouseName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWhOperatorName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchEmail) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchMobileNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchLrNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(county.county_name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(subcounty.vchSubCountyName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(ward.wardName) LIKE LOWER(CONCAT('%', :search, '%')))
			 """)
	Page<Map<String,Object>> getAllReportingAuthData(@Param("processId")Integer intProcessId,@Param("userId") Integer userId,@Param("roleId") Integer roleId,@Param("approvalStatus")String approvalStatus,@Param("search")String search,Pageable pageRequest);
	
	/*
	 * AND opl_clc.intStageNo IN (2) AND opl_clc.intPendingAt=:roleId AND
	 * opl_clc.icmStatus='APPLICATION_ASSIGNED' AND opl_clc.icmRoleId=:roleId AND
	 * opl_clc.icmUserId=:userId
	 * Condition Removed
	 */
	
	
	@Query(value="""
			SELECT ola.intLicenceSno ,ola.vchOPlId as vchOPLAPPId,loc.vchWarehouseName,loc.vchWhOperatorName,loc.vchEmail,loc.vchMobileNumber,loc.vchLrNumber,
			county.county_name,subcounty.vchSubCountyName,ward.wardName,
			(select mu.vchFullName from m_admin_user as mu Where mu.intId=ola.intApprovedBy and mu.bitDeletedFlag=0) as userName ,
			ola.vchFormOneCId,payment.id as paymentId ,payment.enmPaymentStatus, ola.vchApplicationStatus,ola.vchApprovalStatus,
			ola.vchWareHouseId,ola.vchCompanyId,oplmain.bitLicenseGen,ola.bitLicenceCertGen,ola.vchApproverRemarks,oplmain.vchOplId,payment.amountExpected,payment.currency,loc.vchStreetName,loc.vchBuilding,oplmain.vchCertId,oplmain.bitCertGen  
			FROM t_operator_licence_application ola
			inner join  t_application_of_conformity_location_det loc
			on ola.vchWareHouseId=loc.vchWarehouseId
			inner join t_county_master county on county.county_id=loc.intCountyId
			inner join t_sub_county subcounty on subcounty.intId=loc.intSubCountyId
			inner join m_master_ward ward on ward.intWardMasterId=loc.intWardId
			inner join t_payment_ewarehouse payment on payment.id=ola.intPaymentId
			inner join t_WRSC_OPL_Main oplmain on oplmain.intOplAppSno=ola.intLicenceSno
			WHERE ola.bitDeletedFlag = 0
			AND ola.vchPaymentStatus='PAID'
			AND payment.enmPaymentStatus='PAID'
			AND ola.intPaymentId!=0
            AND oplmain.bitLicenseGen=:oplStatus
            AND ola.vchApprovalStatus=:approvalStatus
            AND (:search IS NULL OR LOWER(ola.vchOPlId) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWarehouseName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWhOperatorName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchEmail) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchMobileNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchLrNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(county.county_name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(subcounty.vchSubCountyName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(ward.wardName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(oplmain.vchCertId) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(oplmain.vchOplId) LIKE LOWER(CONCAT('%', :search, '%')))
			order by ola.intCreatedBy desc  
			""",nativeQuery=true,countQuery ="""
			SELECT count(1)
			FROM t_operator_licence_application ola
			inner join  t_application_of_conformity_location_det loc
			on ola.vchWareHouseId=loc.vchWarehouseId
			inner join t_county_master county on county.county_id=loc.intCountyId
			inner join t_sub_county subcounty on subcounty.intId=loc.intSubCountyId
			inner join m_master_ward ward on ward.intWardMasterId=loc.intWardId
			inner join t_payment_ewarehouse payment on payment.id=ola.intPaymentId
			inner join t_WRSC_OPL_Main oplmain on oplmain.intOplAppSno=ola.intLicenceSno
			WHERE ola.bitDeletedFlag = 0
			AND ola.vchPaymentStatus='PAID'
			AND payment.enmPaymentStatus='PAID'
			AND ola.intPaymentId!=0
            AND oplmain.bitLicenseGen=:oplStatus
            AND ola.vchApprovalStatus=:approvalStatus
            AND (:search IS NULL OR LOWER(ola.vchOPlId) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWarehouseName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWhOperatorName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchEmail) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchMobileNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchLrNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(county.county_name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(subcounty.vchSubCountyName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(ward.wardName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(oplmain.vchCertId) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(oplmain.vchOplId) LIKE LOWER(CONCAT('%', :search, '%')))
			order by ola.intCreatedBy desc  
					""")
	Page<Map<String,Object>> getAllApprovedAndRejectApplicationStatus(@Param("approvalStatus")String approvalStatus,@Param("oplStatus")boolean oplStatus,@Param("search")String search,Pageable pageRequest);
	
	
	
	
	
	@Query(value="""
		    SELECT ola.intLicenceSno,ola.vchOPlId as vchOPLAPPId,loc.vchWarehouseName,loc.vchWhOperatorName,loc.vchEmail,loc.vchMobileNumber,loc.vchLrNumber,
			county.county_name,subcounty.vchSubCountyName,ward.wardName,
			(select mu.vchFullName from m_admin_user as mu Where mu.intId=ola.intApprovedBy and mu.bitDeletedFlag=0) as userName ,
			ola.vchFormOneCId,payment.id as paymentId ,payment.enmPaymentStatus, ola.vchApplicationStatus,ola.vchApprovalStatus,
			ola.vchWareHouseId,ola.vchCompanyId,ola.bitLicenceCertGen,ola.vchApproverRemarks,payment.amountExpected,payment.currency,loc.vchStreetName,loc.vchBuilding  
			FROM t_operator_licence_application ola
			inner join  t_application_of_conformity_location_det loc
			on ola.vchWareHouseId=loc.vchWarehouseId
			inner join t_county_master county on county.county_id=loc.intCountyId
			inner join t_sub_county subcounty on subcounty.intId=loc.intSubCountyId
			inner join m_master_ward ward on ward.intWardMasterId=loc.intWardId
			inner join t_payment_ewarehouse payment on payment.id=ola.intPaymentId
			WHERE ola.bitDeletedFlag = 0
			AND ola.vchPaymentStatus='PAID'
			AND payment.enmPaymentStatus='PAID'
			AND ola.intPaymentId!=0
            AND ola.vchApprovalStatus=:approvalStatus
            AND (:search IS NULL OR LOWER(ola.vchOPlId) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWarehouseName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWhOperatorName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchEmail) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchMobileNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchLrNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(county.county_name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(subcounty.vchSubCountyName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(ward.wardName) LIKE LOWER(CONCAT('%', :search, '%')))
			order by ola.intCreatedBy desc  
			""",nativeQuery=true,countQuery ="""
			SELECT count(1)
			FROM t_operator_licence_application ola
			inner join  t_application_of_conformity_location_det loc
			on ola.vchWareHouseId=loc.vchWarehouseId
			inner join t_county_master county on county.county_id=loc.intCountyId
			inner join t_sub_county subcounty on subcounty.intId=loc.intSubCountyId
			inner join m_master_ward ward on ward.intWardMasterId=loc.intWardId
			inner join t_payment_ewarehouse payment on payment.id=ola.intPaymentId
			WHERE ola.bitDeletedFlag = 0
			AND ola.vchPaymentStatus='PAID'
			AND payment.enmPaymentStatus='PAID'
			AND ola.intPaymentId!=0
            AND ola.vchApprovalStatus=:approvalStatus
            AND (:search IS NULL OR LOWER(ola.vchOPlId) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWarehouseName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchWhOperatorName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchEmail) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchMobileNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(loc.vchLrNumber) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(county.county_name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(subcounty.vchSubCountyName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(ward.wardName) LIKE LOWER(CONCAT('%', :search, '%')))  
			order by ola.intCreatedBy desc
					""")
//	Page<Map<String,Object>> getAllDeferredApplicationStatus(@Param("roleId") Integer roleId,@Param("userId") Integer userId,@Param("approvalStatus")String approvalStatus,String search,Pageable pageRequest);
	
	Page<Map<String,Object>> getAllDeferredApplicationStatus(@Param("approvalStatus")String approvalStatus,String search,Pageable pageRequest);
	
	
	
	
	@Query(value="""
			SELECT ola.vchOPlId,loc.vchWarehouseName,loc.vchWhOperatorName,loc.vchEmail,loc.vchMobileNumber,loc.vchLrNumber,
			county.county_name,subcounty.vchSubCountyName,ward.wardName,
			(select mu.vchFullName from m_admin_user as mu Where mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName ,
			ola.vchFormOneCId,payment.id as paymentId ,payment.enmPaymentStatus, ola.vchApplicationStatus,ola.vchApprovalStatus,
			TSA.intOnlineServiceId,TSA.intStageNo,TSA.intPendingAt,TSA.intProcessId,TSA.intLabelId,ola.vchWareHouseId,ola.vchCompanyId,ola.vchPaymentStatus as appPaymentStatus,ola.bitLicenseGen,ola.bitLicenceCertGen,ola.vchCertId 
			FROM t_operator_licence_application ola
			inner join  t_application_of_conformity_location_det loc
			on ola.vchWareHouseId=loc.vchWarehouseId
			inner join t_county_master county on county.county_id=loc.intCountyId
			inner join t_sub_county subcounty on subcounty.intId=loc.intSubCountyId
			inner join m_master_ward ward on ward.intWardMasterId=loc.intWardId
			inner join t_payment_ewarehouse payment on payment.id=ola.intPaymentId
			inner join t_opl_service_approval TSA on TSA.intOnlineServiceId=ola.intLicenceSno
			LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, ola.intCreatedRoleId) > 0 )
			WHERE ola.bitDeletedFlag = 0
			AND ola.vchApprovalStatus=:approvalStatus
            AND ola.intCreatedBy=:userId
            AND ola.intCreatedRoleId=:roleId
			AND TD.intId =:roleId 
			""",nativeQuery=true,countQuery ="""
			SELECT count(1)
			FROM t_operator_licence_application ola
			inner join  t_application_of_conformity_location_det loc
			on ola.vchWareHouseId=loc.vchWarehouseId
			inner join t_county_master county on county.county_id=loc.intCountyId
			inner join t_sub_county subcounty on subcounty.intId=loc.intSubCountyId
			inner join m_master_ward ward on ward.intWardMasterId=loc.intWardId
			inner join t_payment_ewarehouse payment on payment.id=ola.intPaymentId
			inner join t_opl_service_approval TSA on TSA.intOnlineServiceId=ola.intLicenceSno
			LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, ola.intCreatedRoleId) > 0 )
			WHERE ola.bitDeletedFlag = 0
			AND ola.vchApprovalStatus=:approvalStatus
            AND ola.intCreatedBy=:userId
            AND ola.intCreatedRoleId=:roleId
			AND TD.intId =:roleId  
					""")
	Page<Map<String,Object>> getOPLFormDetialsDataValue(@Param("userId")Integer userId,@Param("roleId")Integer roleId,@Param("approvalStatus")String approvalStatus,Pageable pageRequest);
	
	
	
	@Query(value="""
			SELECT ola.vchOPlId as vchOPLAPPId,loc.vchWarehouseName,loc.vchWhOperatorName,loc.vchEmail,loc.vchMobileNumber,loc.vchLrNumber,
			county.county_name,subcounty.vchSubCountyName,ward.wardName,
			(select mu.vchFullName from m_admin_user as mu Where mu.intId=ola.intApprovedBy and mu.bitDeletedFlag=0) as userName ,
			ola.vchFormOneCId,payment.id as paymentId ,payment.enmPaymentStatus, ola.vchApplicationStatus,ola.vchApprovalStatus,
			ola.vchWareHouseId,ola.vchCompanyId,oplmain.bitLicenseGen,ola.bitLicenceCertGen,ola.vchApproverRemarks,oplmain.vchOplId,payment.amountExpected,payment.currency,loc.vchStreetName,loc.vchBuilding,oplmain.vchCertId  
			FROM t_operator_licence_application ola
			inner join  t_application_of_conformity_location_det loc
			on ola.vchWareHouseId=loc.vchWarehouseId
			inner join t_county_master county on county.county_id=loc.intCountyId
			inner join t_sub_county subcounty on subcounty.intId=loc.intSubCountyId
			inner join m_master_ward ward on ward.intWardMasterId=loc.intWardId
			inner join t_payment_ewarehouse payment on payment.id=ola.intPaymentId
			inner join t_WRSC_OPL_Main oplmain on oplmain.intOplAppSno=ola.intLicenceSno
			WHERE ola.bitDeletedFlag = 0
			AND ola.vchPaymentStatus='PAID'
			AND payment.enmPaymentStatus='PAID'
			AND ola.intPaymentId!=0
            AND oplmain.bitLicenseGen=:oplStatus
            AND ola.vchApprovalStatus=:approvalStatus
            AND ola.intCreatedBy=:userId
            AND ola.intCreatedRoleId=:roleId
            
			""",nativeQuery=true,countQuery ="""
			SELECT count(1)
			FROM t_operator_licence_application ola
			inner join  t_application_of_conformity_location_det loc
			on ola.vchWareHouseId=loc.vchWarehouseId
			inner join t_county_master county on county.county_id=loc.intCountyId
			inner join t_sub_county subcounty on subcounty.intId=loc.intSubCountyId
			inner join m_master_ward ward on ward.intWardMasterId=loc.intWardId
			inner join t_payment_ewarehouse payment on payment.id=ola.intPaymentId
			inner join t_WRSC_OPL_Main oplmain on oplmain.intOplAppSno=ola.intLicenceSno
			WHERE ola.bitDeletedFlag = 0
			AND ola.vchPaymentStatus='PAID'
			AND payment.enmPaymentStatus='PAID'
			AND ola.intPaymentId!=0
            AND ola.vchApprovalStatus=:approvalStatus
            AND ola.intCreatedBy=:userId
            AND ola.intCreatedRoleId=:roleId
      
					""")
	Page<Map<String,Object>> getActionTakenApplicationStatus(@Param("userId")Integer userId,@Param("roleId")Integer roleId,@Param("approvalStatus")String approvalStatus,@Param("oplStatus")boolean oplStatus,Pageable pageRequest);
	
	
	
	
	
}
