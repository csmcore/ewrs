package app.ewarehouse.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ApplicationCertificateOfCollateral;

@Repository
public interface AOCCollateralDetailsRepository extends JpaRepository<ApplicationCertificateOfCollateral, String> {

	@Query("Select applicationId from ApplicationCertificateOfCollateral order by applicationId desc limit 1")
	String getId();

	//List<ApplicationCertificateOfCollateral> findByCreatedBy(Integer userId);

	Page<ApplicationCertificateOfCollateral> findByCreatedByAndDeletedFlagFalseAndIsDraftFalseAndPaymentSuccessTrue(
			Integer userId, Pageable pageable);

	@Query(value = """
			    SELECT
			        app.vchapplicationId AS applicationId,
			        app.vchFullName AS fullName,
			        app.vchMobileNumber AS mobileNo,
			        app.vchEmail AS email,
			        app.vchPostalCode AS postalCode,
			        app.vchPostalAddress AS postalAddress,
			        app.vchTown AS town,
			        app.vchTelephoneNumber AS telephoneNo,
			        app.vchWebsite AS website,
			        app.vchEntityType AS entityType,
			        app.vchCompanyName AS companyName,
			        app.vchCompanyRegistrationNumber AS companyRegNo,
			        app.vchKraPin AS kraPin,
			        app.vchpaymentMode AS paymentMode,
			        app.bitPaymentSuccess AS paymentSuccess,
			        app.actionStatus AS actionStatus,
			        app.bitDeletedFlag AS deletedFlag,
			        app.isInspector AS inspector,
			        app.isCertificatecommittee AS certificateCommittee,
			        app.isDefer AS defer,
			        status.vchStatusDescription AS vchStatusDescription,
			        app.isInspectRemark AS isInspectRemark,
			        app.isCcRemark AS isCcRemark,
			        app.bitDeletedFlag AS bitForwardToOicStatus
			    FROM t_application_of_certificate_of_compliance_collarter app
			    LEFT JOIN m_con_cm_status status
			        ON app.actionStatus = status.intStatusId
			         WHERE app.actionStatus NOT IN (104, 107)
			         ORDER BY app.vchapplicationId DESC
			""", countQuery = """
			    SELECT COUNT(*) FROM t_application_of_certificate_of_compliance_collarter  WHERE actionStatus NOT IN (104, 107)
			""", nativeQuery = true)
	Page<Object[]> getAllDataPagination(Pageable pageable);

	@Query(value = """
			    SELECT
			        app.vchapplicationId AS applicationId,
			        app.vchFullName AS fullName,
			        app.vchMobileNumber AS mobileNo,
			        app.vchEmail AS email,
			        app.vchPostalCode AS postalCode,
			        app.vchPostalAddress AS postalAddress,
			        app.vchTown AS town,
			        app.vchTelephoneNumber AS telephoneNo,
			        app.vchWebsite AS website,
			        app.vchEntityType AS entityType,
			        app.vchCompanyName AS companyName,
			        app.vchCompanyRegistrationNumber AS companyRegNo,
			        app.vchKraPin AS kraPin,
			        app.vchpaymentMode AS paymentMode,
			        app.bitPaymentSuccess AS paymentSuccess,
			        app.actionStatus AS actionStatus,
			        app.bitDeletedFlag AS deletedFlag,
			        app.isInspector AS inspector,
			        app.isCertificatecommittee AS certificateCommittee,
			        app.isDefer AS defer,
			        status.vchStatusDescription AS vchStatusDescription,
			       app.isInspectRemark AS isInspectRemark,
			      app.isCcRemark AS isCcRemark,
			      app.bitDeletedFlag AS bitForwardToOicStatus
			    FROM t_application_of_certificate_of_compliance_collarter app
			    LEFT JOIN m_con_cm_status status
			        ON app.actionStatus = status.intStatusId
			        WHERE app.actionStatus  IN (104)
			        ORDER BY app.vchapplicationId DESC
			""", countQuery = """
			    SELECT COUNT(*) FROM t_application_of_certificate_of_compliance_collarter WHERE actionStatus IN (104)
			""", nativeQuery = true)
	Page<Object[]> getAllApproveDataPagination(Pageable pageable);

	@Query(value = """
			    SELECT
			        app.vchapplicationId AS applicationId,
			        app.vchFullName AS fullName,
			        app.vchMobileNumber AS mobileNo,
			        app.vchEmail AS email,
			        app.vchPostalCode AS postalCode,
			        app.vchPostalAddress AS postalAddress,
			        app.vchTown AS town,
			        app.vchTelephoneNumber AS telephoneNo,
			        app.vchWebsite AS website,
			        app.vchEntityType AS entityType,
			        app.vchCompanyName AS companyName,
			        app.vchCompanyRegistrationNumber AS companyRegNo,
			        app.vchKraPin AS kraPin,
			        app.vchpaymentMode AS paymentMode,
			        app.bitPaymentSuccess AS paymentSuccess,
			        app.actionStatus AS actionStatus,
			        app.bitDeletedFlag AS deletedFlag,
			        app.isInspector AS inspector,
			        app.isCertificatecommittee AS certificateCommittee,
			        app.isDefer AS defer,
			        status.vchStatusDescription AS vchStatusDescription,
			        app.isInspectRemark AS isInspectRemark,
			        app.isCcRemark AS isCcRemark,
			        app.bitDeletedFlag AS bitForwardToOicStatus
			    FROM t_application_of_certificate_of_compliance_collarter app
			    LEFT JOIN m_con_cm_status status
			        ON app.actionStatus = status.intStatusId
			         WHERE app.actionStatus  IN (107)
			         ORDER BY app.vchapplicationId DESC
			""", countQuery = """
			    SELECT COUNT(*) FROM t_application_of_certificate_of_compliance_collarter where actionStatus  IN (107)
			""", nativeQuery = true)
	Page<Object[]> getAllRejectDataPagination(Pageable pageable);

	ApplicationCertificateOfCollateral findByApplicationId(String vchApplicationId);

	@Query(value = """
			    SELECT
			        app.vchapplicationId AS applicationId,
			         app.vchFullName AS fullName,
			         app.vchMobileNumber AS mobileNo,
			         app.vchEmail AS email,
			         app.vchPostalCode AS postalCode,
			         app.vchPostalAddress AS postalAddress,
			         app.vchTown AS town,
			         app.vchTelephoneNumber AS telephoneNo,
			         app.vchWebsite AS website,
			         app.vchEntityType AS entityType,
			         app.vchCompanyName AS companyName,
			         app.vchCompanyRegistrationNumber AS companyRegNo,
			         app.vchKraPin AS kraPin,
			         app.vchpaymentMode AS paymentMode,
			         app.bitPaymentSuccess AS paymentSuccess,
			         app.actionStatus AS actionStatus,
			         app.bitDeletedFlag AS deletedFlag,
			         app.isInspector AS inspector,
			         app.isCertificatecommittee AS certificateCommittee,
			         app.isDefer AS defer,
			         status.vchStatusDescription AS vchStatusDescription,
			         app.isInspectRemark AS isInspectRemark,
			         app.isCcRemark AS isCcRemark,
			         tctac.bitForwardToOicStatus as bitForwardToOicStatus
			    FROM t_application_of_certificate_of_compliance_collarter app
			    LEFT JOIN m_con_cm_status status
			        ON app.actionStatus = status.intStatusId
			    INNER JOIN t_oic_collateral_take_action_cc tctac
			        ON app.vchapplicationId = tctac.vchapplicationId
			    WHERE app.actionStatus NOT IN (104, 107)
			      AND tctac.intCertificateCommitteeId = :userId
			       ORDER BY app.vchapplicationId DESC
			""", countQuery = """
			    SELECT COUNT(*)
			    FROM t_application_of_certificate_of_compliance_collarter app
			    INNER JOIN t_oic_collateral_take_action_cc tctac
			        ON app.vchapplicationId = tctac.vchapplicationId
			    WHERE app.actionStatus NOT IN (104, 107)
			      AND tctac.intCertificateCommitteeId = :userId
			      ORDER BY app.vchapplicationId DESC
			""", nativeQuery = true)
	Page<Object[]> getAllDataPaginationCC(Pageable pageable, @Param("userId") Integer userId);

	@Query(value = """
			    SELECT
			        app.vchapplicationId AS applicationId,
			         app.vchFullName AS fullName,
			         app.vchMobileNumber AS mobileNo,
			         app.vchEmail AS email,
			         app.vchPostalCode AS postalCode,
			         app.vchPostalAddress AS postalAddress,
			         app.vchTown AS town,
			         app.vchTelephoneNumber AS telephoneNo,
			         app.vchWebsite AS website,
			         app.vchEntityType AS entityType,
			         app.vchCompanyName AS companyName,
			         app.vchCompanyRegistrationNumber AS companyRegNo,
			         app.vchKraPin AS kraPin,
			         app.vchpaymentMode AS paymentMode,
			         app.bitPaymentSuccess AS paymentSuccess,
			         app.actionStatus AS actionStatus,
			         app.bitDeletedFlag AS deletedFlag,
			         app.isInspector AS inspector,
			         app.isCertificatecommittee AS certificateCommittee,
			         app.isDefer AS defer,
			         status.vchStatusDescription AS vchStatusDescription,
			         app.isInspectRemark AS isInspectRemark,
			         app.isCcRemark AS isCcRemark,
			         tctac.bitForwardToOicStatus as bitForwardToOicStatus
			    FROM t_application_of_certificate_of_compliance_collarter app
			    LEFT JOIN m_con_cm_status status
			        ON app.actionStatus = status.intStatusId
			    INNER JOIN t_oic_collateral_take_action_cc tctac
			        ON app.vchapplicationId = tctac.vchapplicationId
			    WHERE app.actionStatus IN (104)
			      AND tctac.intCertificateCommitteeId = :userId
			       ORDER BY app.vchapplicationId DESC
			""", countQuery = """
			    SELECT COUNT(*)
			    FROM t_application_of_certificate_of_compliance_collarter app
			    INNER JOIN t_oic_collateral_take_action_cc tctac
			        ON app.vchapplicationId = tctac.vchapplicationId
			    WHERE app.actionStatus IN (104)
			      AND tctac.intCertificateCommitteeId = :userId
			      ORDER BY app.vchapplicationId DESC
			""", nativeQuery = true)
	Page<Object[]> getAllApproveDataPaginationCC(Pageable pageable, Integer userId);

	@Query(value = """
			    SELECT
			        app.vchapplicationId AS applicationId,
			         app.vchFullName AS fullName,
			         app.vchMobileNumber AS mobileNo,
			         app.vchEmail AS email,
			         app.vchPostalCode AS postalCode,
			         app.vchPostalAddress AS postalAddress,
			         app.vchTown AS town,
			         app.vchTelephoneNumber AS telephoneNo,
			         app.vchWebsite AS website,
			         app.vchEntityType AS entityType,
			         app.vchCompanyName AS companyName,
			         app.vchCompanyRegistrationNumber AS companyRegNo,
			         app.vchKraPin AS kraPin,
			         app.vchpaymentMode AS paymentMode,
			         app.bitPaymentSuccess AS paymentSuccess,
			         app.actionStatus AS actionStatus,
			         app.bitDeletedFlag AS deletedFlag,
			         app.isInspector AS inspector,
			         app.isCertificatecommittee AS certificateCommittee,
			         app.isDefer AS defer,
			         status.vchStatusDescription AS vchStatusDescription,
			         app.isInspectRemark AS isInspectRemark,
			         app.isCcRemark AS isCcRemark,
			         tctac.bitForwardToOicStatus as bitForwardToOicStatus
			    FROM t_application_of_certificate_of_compliance_collarter app
			    LEFT JOIN m_con_cm_status status
			        ON app.actionStatus = status.intStatusId
			    INNER JOIN t_oic_collateral_take_action_cc tctac
			        ON app.vchapplicationId = tctac.vchapplicationId
			    WHERE app.actionStatus IN  (107)
			      AND tctac.intCertificateCommitteeId = :userId
			       ORDER BY app.vchapplicationId DESC
			""", countQuery = """
			    SELECT COUNT(*)
			    FROM t_application_of_certificate_of_compliance_collarter app
			    INNER JOIN t_oic_collateral_take_action_cc tctac
			        ON app.vchapplicationId = tctac.vchapplicationId
			    WHERE app.actionStatus IN (107)
			      AND tctac.intCertificateCommitteeId = :userId
			      ORDER BY app.vchapplicationId DESC
			""", nativeQuery = true)
	Page<Object[]> getAllRejectDataPaginationCC(Pageable pageable, Integer userId);

	// Inspector
	@Query(value = """
			    SELECT
			        app.vchapplicationId AS applicationId,
			         app.vchFullName AS fullName,
			         app.vchMobileNumber AS mobileNo,
			         app.vchEmail AS email,
			         app.vchPostalCode AS postalCode,
			         app.vchPostalAddress AS postalAddress,
			         app.vchTown AS town,
			         app.vchTelephoneNumber AS telephoneNo,
			         app.vchWebsite AS website,
			         app.vchEntityType AS entityType,
			         app.vchCompanyName AS companyName,
			         app.vchCompanyRegistrationNumber AS companyRegNo,
			         app.vchKraPin AS kraPin,
			         app.vchpaymentMode AS paymentMode,
			         app.bitPaymentSuccess AS paymentSuccess,
			         app.actionStatus AS actionStatus,
			         app.bitDeletedFlag AS deletedFlag,
			         app.isInspector AS inspector,
			         app.isCertificatecommittee AS certificateCommittee,
			         app.isDefer AS defer,
			         status.vchStatusDescription AS vchStatusDescription,
			         app.isInspectRemark AS isInspectRemark,
			         app.isCcRemark AS isCcRemark
			    FROM t_application_of_certificate_of_compliance_collarter app
			    LEFT JOIN m_con_cm_status status
			        ON app.actionStatus = status.intStatusId
			    INNER JOIN t_oic_collateral_take_action_inspector tctac
			        ON app.vchapplicationId = tctac.vchapplicationId
			    WHERE app.actionStatus NOT IN (104, 107)
			      AND tctac.intInspectorTeamLeadId = :userId
			""", countQuery = """
			    SELECT COUNT(*)
			    FROM t_application_of_certificate_of_compliance_collarter app
			    INNER JOIN t_oic_collateral_take_action_inspector tctac
			        ON app.vchapplicationId = tctac.vchapplicationId
			    WHERE app.actionStatus NOT IN (104, 107)
			      AND tctac.intInspectorTeamLeadId = :userId
			""", nativeQuery = true)
	Page<Object[]> getAllInspectorDataPagination(Pageable pageable, @Param("userId") Integer userId);

	@Query(value = """
			    SELECT
			        app.vchapplicationId AS applicationId,
			         app.vchFullName AS fullName,
			         app.vchMobileNumber AS mobileNo,
			         app.vchEmail AS email,
			         app.vchPostalCode AS postalCode,
			         app.vchPostalAddress AS postalAddress,
			         app.vchTown AS town,
			         app.vchTelephoneNumber AS telephoneNo,
			         app.vchWebsite AS website,
			         app.vchEntityType AS entityType,
			         app.vchCompanyName AS companyName,
			         app.vchCompanyRegistrationNumber AS companyRegNo,
			         app.vchKraPin AS kraPin,
			         app.vchpaymentMode AS paymentMode,
			         app.bitPaymentSuccess AS paymentSuccess,
			         app.actionStatus AS actionStatus,
			         app.bitDeletedFlag AS deletedFlag,
			         app.isInspector AS inspector,
			         app.isCertificatecommittee AS certificateCommittee,
			         app.isDefer AS defer,
			         status.vchStatusDescription AS vchStatusDescription,
			         app.isInspectRemark AS isInspectRemark,
			         app.isCcRemark AS isCcRemark
			    FROM t_application_of_certificate_of_compliance_collarter app
			    LEFT JOIN m_con_cm_status status
			        ON app.actionStatus = status.intStatusId
			    INNER JOIN t_oic_collateral_take_action_inspector tctac
			        ON app.vchapplicationId = tctac.vchapplicationId
			    WHERE app.actionStatus IN (104)
			      AND tctac.intInspectorTeamLeadId = :userId
			""", countQuery = """
			    SELECT COUNT(*)
			    FROM t_application_of_certificate_of_compliance_collarter app
			    INNER JOIN t_oic_collateral_take_action_inspector tctac
			        ON app.vchapplicationId = tctac.vchapplicationId
			    WHERE app.actionStatus IN (104)
			      AND tctac.intInspectorTeamLeadId = :userId
			""", nativeQuery = true)
	Page<Object[]> getAllInspectorApproveDataPagination(Pageable pageable, Integer userId);

	@Query(value = """
			    SELECT
			        app.vchapplicationId AS applicationId,
			         app.vchFullName AS fullName,
			         app.vchMobileNumber AS mobileNo,
			         app.vchEmail AS email,
			         app.vchPostalCode AS postalCode,
			         app.vchPostalAddress AS postalAddress,
			         app.vchTown AS town,
			         app.vchTelephoneNumber AS telephoneNo,
			         app.vchWebsite AS website,
			         app.vchEntityType AS entityType,
			         app.vchCompanyName AS companyName,
			         app.vchCompanyRegistrationNumber AS companyRegNo,
			         app.vchKraPin AS kraPin,
			         app.vchpaymentMode AS paymentMode,
			         app.bitPaymentSuccess AS paymentSuccess,
			         app.actionStatus AS actionStatus,
			         app.bitDeletedFlag AS deletedFlag,
			         app.isInspector AS inspector,
			         app.isCertificatecommittee AS certificateCommittee,
			         app.isDefer AS defer,
			         status.vchStatusDescription AS vchStatusDescription,
			         app.isInspectRemark AS isInspectRemark,
			         app.isCcRemark AS isCcRemark
			    FROM t_application_of_certificate_of_compliance_collarter app
			    LEFT JOIN m_con_cm_status status
			        ON app.actionStatus = status.intStatusId
			    INNER JOIN t_oic_collateral_take_action_inspector tctac
			        ON app.vchapplicationId = tctac.vchapplicationId
			    WHERE app.actionStatus IN  (107)
			      AND tctac.intInspectorTeamLeadId = :userId
			""", countQuery = """
			    SELECT COUNT(*)
			    FROM t_application_of_certificate_of_compliance_collarter app
			    INNER JOIN t_oic_collateral_take_action_inspector tctac
			        ON app.vchapplicationId = tctac.vchapplicationId
			    WHERE app.actionStatus IN (107)
			      AND tctac.intInspectorTeamLeadId = :userId
			""", nativeQuery = true)
	Page<Object[]> getAllInspectorRejectDataPagination(Pageable pageable, Integer userId);

	@Query(value="""
			   SELECT 
			      c.actionStatus AS actionStatus, 
			      COUNT(c.vchapplicationId) AS idCount 
			   FROM 
			      t_application_of_certificate_of_compliance_collarter c
			   WHERE 
			      c.intCreatedBy = :userId 
			      AND c.actionStatus NOT IN (107) 
			      AND c.bitDeletedFlag = 0 
			      AND c.isDraft = 0 
			      AND c.bitPaymentSuccess = 1
			   GROUP BY 
			      c.actionStatus;
			""", nativeQuery=true)
			List<Map<String, Object>> getCollateralCountByUserId(Integer userId);

	List<ApplicationCertificateOfCollateral> findByCreatedByAndPaymentSuccessFalse(Integer userId);

	@Query(value="""
			   SELECT 
			      COUNT(c.vchapplicationId) AS idCount 
			   FROM 
			      t_application_of_certificate_of_compliance_collarter c
			   WHERE 
			      c.intCreatedBy = :userId  
			      AND c.bitDeletedFlag = 0 ;
			""", nativeQuery=true)
	Integer getCountByUserId(Integer userId);
	
	

}
