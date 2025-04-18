package app.ewarehouse.repository;

import java.util.Date;
import java.util.List;

import app.ewarehouse.entity.RoutineCompliance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.DisputeDeclaration;
import app.ewarehouse.entity.Status;

@Repository
public interface DisputeDeclarationRepository extends JpaRepository<DisputeDeclaration, String> {

    @Query("SELECT d FROM DisputeDeclaration d")
    Page<DisputeDeclaration> findAllDisputeDeclarations(Pageable pageable);

    @Query("SELECT d FROM DisputeDeclaration d " +
            "LEFT JOIN d.ceoApproval ca " +
            "LEFT JOIN d.disputeCategory dc " +
            "WHERE (:userId IS NULL OR d.intCreatedBy = :userId) " +
            "AND (:fromDate IS NULL OR d.updatedAt >= :fromDate) " +
            "AND (:toDate IS NULL OR d.updatedAt <= :toDate) " +
            "AND (:leastStage IS NULL OR :leastStage <= d.intCurrentStage) " +
            "AND (:intCurrentRole IS NOT NULL OR d.intCurrentRole = :intCurrentRole OR :intCurrentRole = :complaintRole) " +
//            "AND (:intCurrentRole IS NULL OR d.intCurrentRole = :intCurrentRole OR :intCurrentRole = :complaintRole) " +
            "AND (:search IS NULL OR " +
            "LOWER(d.disputeId) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(d.disputantName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(ca.vchRemark) LIKE LOWER(CONCAT('%', :search, '%')) " +  // Reference the alias for ceoApproval
            "OR LOWER(dc.disputeCategoryName) LIKE LOWER(CONCAT('%', :search, '%')) " +  // Reference the alias for disputeCategory
            "OR LOWER(CAST(d.contactNumber AS string)) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(d.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<DisputeDeclaration> findByFilters(
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("search") String search,
            @Param("userId") Integer userId,
            @Param("leastStage") Integer leastStage,
            @Param("intCurrentRole") Integer intCurrentRole,
            @Param("complaintRole") Integer complaintRole,
            Pageable pageable);
}

