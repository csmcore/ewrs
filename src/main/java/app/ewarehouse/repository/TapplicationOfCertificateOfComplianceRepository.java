package app.ewarehouse.repository;

import java.util.List;

import app.ewarehouse.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public interface TapplicationOfCertificateOfComplianceRepository extends JpaRepository<TapplicationOfCertificateOfCompliance,String> {

    Optional<TapplicationOfCertificateOfCompliance> findByTxtApplicationIdAndBitDeletedFlag(String applicationId, boolean bitDeletedFlag);

    TapplicationOfCertificateOfCompliance findByStatusAndIntCreatedByAndBitDeletedFlag(draftStatus status, Integer intCreatedBy,boolean bitDeletedFlag);

    TapplicationOfCertificateOfCompliance findByIntAdminUserIdAndBitDeletedFlag(Integer admin_user_i,boolean bitDeletedFlag);
    
    List<TapplicationOfCertificateOfCompliance> findAllByBitDeletedFlag(boolean bitDeletedFlag);

    @Query("SELECT a FROM TapplicationOfCertificateOfCompliance a " +
            "WHERE a.bitDeletedFlag = false AND a.status = :paymentStatus AND " +
            "(:userId IS NULL OR a.inspector.intId = :userId) AND (" +
            "(:status = :pendingStatus AND ( (:status = a.enmApprovalStatus AND :intCurrentRole = a.intCurrentRole) OR ( (a.intApprovalStage IN :immediateBelowStages OR a.intApprovalStage IN :myStages) AND :onHoldStatus = a.enmApprovalStatus) ) ) OR " +
            "(:status = :forwardedStatus AND a.intApprovalStage > :firstStage AND a.intApprovalStage NOT IN :myStages AND NOT (a.intApprovalStage IN :immediateBelowStages AND :onHoldStatus = a.enmApprovalStatus) ) OR " +
            "(:status IN (:approvedStatus, :rejectedStatus) AND :status = a.enmApprovalStatus)) AND " +
            "(:search IS NULL OR " +
            "LOWER(a.txtApplicationId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.txtFullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.txtMobilePhoneNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.txtEmail) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(CAST(a.enmApprovalStatus AS string)) LIKE LOWER(CONCAT('%', :search, '%')))"
    )
    Page<TapplicationOfCertificateOfCompliance> findByFilters(
            @Param("intCurrentRole") Integer intCurrentRole,
            @Param("firstStage") Integer firstStage,
            @Param("immediateBelowStages") List<Integer> immediateBelowStages,
            @Param("myStages") List<Integer> myStages,
            @Param("status") Status status,
            @Param("pendingStatus") Status pendingStatus,
            @Param("forwardedStatus") Status forwardedStatus,
            @Param("approvedStatus") Status approvedStatus,
            @Param("rejectedStatus") Status rejectedStatus,
            @Param("onHoldStatus") Status onHoldStatus,
            @Param("paymentStatus") draftStatus paymentStatus,
            @Param("userId") Integer userId,
            @Param("search") String search,
            Pageable pageable);


    @Query("SELECT r.enmStatus FROM RoutineCompliance r WHERE r.vchRoutineComplianceId = :id AND r.bitDeleteFlag = false")
    Status findStatusById(@Param("id") String id);

    @Query("SELECT a FROM TapplicationOfCertificateOfCompliance a WHERE a.subCounty.intId = :intId AND a.bitDeletedFlag = :bitDeletedFlag AND a.enmApprovalStatus = :status")
    List<TapplicationOfCertificateOfCompliance> findBySubCounty_intIdAndBitDeletedFlagAndEnmApprovalStatus(Integer intId, boolean bitDeletedFlag, Status status);

    @Query("SELECT a.subCounty FROM TapplicationOfCertificateOfCompliance a WHERE a.bitDeletedFlag = :bitDeletedFlag AND a.enmApprovalStatus = :status")
    List<SubCounty> findSubCountyByEnmApprovalStatusAndBitDeletedFlag(Status status, boolean bitDeletedFlag);
}
