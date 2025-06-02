package app.ewarehouse.repository;

import java.util.List;

import app.ewarehouse.entity.draftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TsuspensionOfCertificateOfCompliance;

@Repository
public interface TsuspensionOfCertificateOfComplianceRepository extends JpaRepository<TsuspensionOfCertificateOfCompliance, String> {

	@Query("from TsuspensionOfCertificateOfCompliance t ORDER BY t.createdAt DESC ")
	Page<TsuspensionOfCertificateOfCompliance> findAllByIsDeleted(Pageable pageable,boolean isDeleted);

	TsuspensionOfCertificateOfCompliance findByComplaintNumberAndIsDeleted(String complaintNumber,boolean isDeleted);

	boolean existsByComplainantContactNumberAndIsDeleted( @Param("contactNumber") String contactNumber,boolean isDeleted);

	@Query("SELECT s FROM TsuspensionOfCertificateOfCompliance s " +
			"WHERE s.isDeleted = false AND (" +
			"(:status = :pendingStatus AND ( (:status = s.status AND :intCurrentRole = s.intCurrentRole) OR ( (s.intCurrentStage IN :immediateBelowStages OR s.intCurrentStage IN :myStages) AND :onHoldStatus = s.status) ) ) OR " +
			"(:status = :forwardedStatus AND s.intCurrentStage > :firstStage AND s.intCurrentStage NOT IN :myStages AND NOT (s.intCurrentStage IN :immediateBelowStages AND :onHoldStatus = s.status) ) OR " +
			"(:status IN (:approvedStatus, :rejectedStatus) AND :status = s.status)) AND " +
			"(:search IS NULL OR " +
			"LOWER(s.complaintNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			"LOWER(s.complaintType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			"LOWER(CAST(s.status AS string)) LIKE LOWER(CONCAT('%', :search, '%')))")
	Page<TsuspensionOfCertificateOfCompliance> findByFilters(
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
			@Param("search") String search,
			Pageable pageable);

	List<TsuspensionOfCertificateOfCompliance> findByIsDeleted(boolean b);
}
