package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TsuspensionOfGrader;


@Repository
public interface TsuspensionOfGraderRepository extends JpaRepository<TsuspensionOfGrader,String> {

    boolean existsByComplainantContactNumber(String contactNumber);

    @Query("SELECT t FROM TsuspensionOfGrader t WHERE t.status = :status AND t.pendingAtUser = :roleId")
    Page<TsuspensionOfGrader> findComplaintsByUser(@Param("status") Status status, @Param("roleId") Integer roleId, Pageable pageable);

    @Query("SELECT t FROM TsuspensionOfGrader t WHERE t.status = :status AND t.ceoInitialRemark IS NOT NULL AND t.oicLegalRemark IS NOT NULL AND t.pendingAtUser != :roleId")
    Page<TsuspensionOfGrader> findForwardedComplaintsForOicLegal(@Param("status") Status status, @Param("roleId") Integer roleId, Pageable pageable);

    @Query("SELECT t FROM TsuspensionOfGrader t WHERE t.status = :status AND t.ceoInitialRemark IS NOT NULL AND t.oicLegalRemark IS NOT NULL AND t.approverRemark IS NOT NULL AND t.pendingAtUser != :roleId")
    Page<TsuspensionOfGrader> findForwardedComplaintsForApprover(@Param("status") Status status, @Param("roleId") Integer roleId, Pageable pageable);


    @Query("SELECT c FROM TsuspensionOfGrader c ORDER BY c.createdAt DESC")
    Page<TsuspensionOfGrader> findByIsDeleted(Pageable pageable,boolean isDeleted);

    List<TsuspensionOfGrader> getByIsDeleted(boolean b);

    @Query("SELECT t FROM TsuspensionOfGrader t WHERE t.status = :status")
    Page<TsuspensionOfGrader> findResolvedComplaints(@Param("status") Status status, Pageable pageable);

}
