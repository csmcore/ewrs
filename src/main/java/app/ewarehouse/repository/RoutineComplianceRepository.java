package app.ewarehouse.repository;

import java.util.List;



import app.ewarehouse.entity.RoutineCompliance;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.Tuser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;



@Repository
public interface RoutineComplianceRepository extends JpaRepository<RoutineCompliance, String> {

    RoutineCompliance findByVchRoutineComplianceIdAndBitDeleteFlag(String intId, boolean bitDeletedFlag);

    List<RoutineCompliance> findAllByBitDeleteFlag(Boolean bitDeletedFlag);

    Page<RoutineCompliance> findAllByBitDeleteFlag(Boolean bitDeletedFlag, Pageable pageable);

//    @Query("SELECT r FROM RoutineCompliance r " +
//            "WHERE r.bitDeleteFlag = false " +
//            "AND (:intOfficerStage = :complianceStage OR " +
//            "r.enmStatus = :approvedStatus OR " +
//            "(r.enmStatus = :onHoldStatus AND :nextOfficerStage = r.intOfficerStage) OR" +
//            ":intOfficerStage = r.intOfficerStage)")
//    Page<RoutineCompliance> findByFilters(@Param("intOfficerStage") Integer intOfficerStage,
//                                          @Param("complianceStage") Integer complianceStage,
//                                          @Param("nextOfficerStage") Integer nextOfficerStage,
//                                          @Param("approvedStatus") Status approvedStatus,
//                                          @Param("onHoldStatus") Status onHoldStatus,
//                                          Pageable pageable);

 //   (a.intApprovalStage IN :immediateBelowStages OR a.intApprovalStage IN :myStages) AND :onHoldStatus = a.enmApprovalStatus

    @Query("SELECT r FROM RoutineCompliance r " +
            "WHERE r.bitDeleteFlag = false " +
            "AND (:userId IS NULL OR r.leadInspector.intId = :userId) " +
//            "AND ( (:intCurrentRole = :complianceRole AND :action IS NULL) OR " +
//            "r.enmStatus = :approvedStatus OR " +
//            "r.enmStatus = :rejectedStatus OR " +
//            //"(r.enmStatus = :onHoldStatus AND :nextOfficerStage = r.intOfficerStage) OR " +
//            "r.intCurrentStage IN :myStages OR " +
//            ":intCurrentRole = r.intCurrentRole) " +
            "AND (:fromDate IS NULL OR r.dtmInspectionDate >= :fromDate) " +
            "AND (:toDate IS NULL OR r.dtmInspectionDate <= :toDate) " +
            "AND (:leastStage <= r.intCurrentStage OR :intCurrentRole = r.intCurrentRole OR :intCurrentRole = :complianceRole) " +
            "AND (:search IS NULL OR " +
            "LOWER(r.vchRoutineComplianceId) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(CAST(r.warehouseParticulars.applicationOfConformity.applicationId AS string)) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(r.warehouseParticulars.applicationOfConformity.particularOfApplicantsId.shop) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(CAST(r.enmStatus AS string)) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<RoutineCompliance> findByFilters(
            //@Param("myStages") List<Integer> myStages,
            //@Param("approvedStatus") Status approvedStatus,
            //@Param("rejectedStatus") Status rejectedStatus,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("search") String search,
            @Param("userId") Integer userId,
            @Param("leastStage") Integer leastStage,
            @Param("intCurrentRole") Integer intCurrentRole,
            @Param("complianceRole") Integer complianceRole,
            //@Param("action") Integer action,
            Pageable pageable);


    @Query("SELECT r.enmStatus FROM RoutineCompliance r WHERE r.vchRoutineComplianceId = :id AND r.bitDeleteFlag = false")
    Status findStatusById(@Param("id") String id);

    @Query("SELECT u FROM Tuser as u WHERE u.selRole=:intRoleId and u.bitDeletedFlag = false")
    List<Tuser> getAllInspectors(@Param("intRoleId")Integer intRoleId);
}
