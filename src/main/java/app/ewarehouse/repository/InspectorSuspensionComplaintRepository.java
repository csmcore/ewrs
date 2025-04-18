package app.ewarehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.InspectorSuspensionComplaint;
import app.ewarehouse.entity.Status;

@Repository
public interface InspectorSuspensionComplaintRepository extends JpaRepository<InspectorSuspensionComplaint, String> {
    @Query("SELECT c FROM InspectorSuspensionComplaint c WHERE c.isDeleted = :booleanDeletedFlag")
    List<InspectorSuspensionComplaint> findAllComplaints(@Param("booleanDeletedFlag") Boolean booleanDeletedFlag);

    @Query("SELECT c FROM InspectorSuspensionComplaint c WHERE c.complaintId = :id and c.isDeleted = :booleanDeletedFlag")
    Optional<InspectorSuspensionComplaint> findComplaintById(@Param("id") String id, @Param("booleanDeletedFlag") Boolean booleanDeletedFlag);

    @Query("SELECT c FROM InspectorSuspensionComplaint c WHERE c.isDeleted = :booleanDeletedFlag")
    Page<InspectorSuspensionComplaint> findAllComplaints(@Param("booleanDeletedFlag") Boolean booleanDeletedFlag, Pageable pageable);

    @Query("FROM InspectorSuspensionComplaint c WHERE c.actionTakenBy in :actionTakenBy AND c.status=:status")
    Page<InspectorSuspensionComplaint> findByStatusAndActionTakenBy(Status status, List<Integer> actionTakenBy,
                                                                    Pageable pageable);

    @Query("FROM InspectorSuspensionComplaint c WHERE c.actionTakenBy in :actionTakenBy")
    Page<InspectorSuspensionComplaint> findByActionTakenBy(List<Integer> actionTakenBy, Pageable pageable);


    @Query("SELECT c FROM InspectorSuspensionComplaint c " +
            "WHERE (LOWER(c.complaintId) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(c.complainantName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(c.complaintType) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(c.remark) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(CAST(c.dateOfIncident AS string)) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(CAST(c.status AS string)) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<InspectorSuspensionComplaint> findByFilters(@Param("search") String search, Pageable pageable);
}
