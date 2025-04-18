package app.ewarehouse.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.WarehouseApplicant;

@Repository
public interface WarehouseApplicantRepository extends JpaRepository<WarehouseApplicant, String> {

    WarehouseApplicant findByVchApplicantIdAndBitDeletedFlag(String vchApplicantId, boolean bitDeletedFlag);

    List<WarehouseApplicant> findAllByBitDeletedFlag(Boolean bitDeletedFlag);

    Page<WarehouseApplicant> findAllByBitDeletedFlag(Boolean bitDeletedFlag, Pageable pageable);

    @Query("SELECT wa FROM WarehouseApplicant wa WHERE wa.bitDeletedFlag = false AND " +
            "(:fromDate IS NULL OR wa.dtmCreatedOn >= :fromDate) AND " +
            "(:toDate IS NULL OR wa.dtmCreatedOn <= :toDate) AND " +
            "(:status IS NULL OR wa.enmStatus = :status)")
    Page<WarehouseApplicant> findByFilters(@Param("fromDate") Date fromDate,
                                           @Param("toDate") Date toDate,
                                           @Param("status") Status status,
                                           Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE WarehouseApplicant wa SET wa.bitDeletedFlag = true WHERE wa.vchApplicantId = :vchApplicantId")
    void softDeleteWarehouseApplicant(@Param("vchApplicantId") String vchApplicantId);

    @Transactional
    @Modifying
    @Query("UPDATE Company c SET c.bitDeletedFlag = true WHERE c.intCompanyId IN " +
            "(SELECT wa.company.intCompanyId FROM WarehouseApplicant wa WHERE wa.vchApplicantId = :vchApplicantId)")
    void softDeleteCompany(@Param("vchApplicantId") String vchApplicantId);

    @Transactional
    @Modifying
    @Query("UPDATE WarehouseParticulars wp SET wp.bitDeletedFlag = true WHERE wp.intWareHouseParticularsId IN " +
            "(SELECT wa.warehouseParticulars.intWareHouseParticularsId FROM WarehouseApplicant wa WHERE wa.vchApplicantId = :vchApplicantId)")
    void softDeleteWarehouseParticulars(@Param("vchApplicantId") String vchApplicantId);
}
