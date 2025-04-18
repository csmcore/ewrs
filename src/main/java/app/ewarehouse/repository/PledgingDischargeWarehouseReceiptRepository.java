package app.ewarehouse.repository;

import app.ewarehouse.entity.MPledgingDischargeWarehouseReceipt;
import app.ewarehouse.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PledgingDischargeWarehouseReceiptRepository extends JpaRepository<MPledgingDischargeWarehouseReceipt, String> {
    @Query("SELECT p FROM MPledgingDischargeWarehouseReceipt p WHERE p.status = 'Published'")
    Page<MPledgingDischargeWarehouseReceipt> getAll(Pageable pageable);


    @Query("SELECT p FROM MPledgingDischargeWarehouseReceipt p " +
            "LEFT JOIN p.buyer b " +  // Join with the Buyer entity
            "WHERE (:search IS NULL OR " +
            "LOWER(b.txtName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(b.txtPassportNo) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(b.intCentralRegistryIdentifier) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(b.txtEmailAddress) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<MPledgingDischargeWarehouseReceipt> findByFilters(@Param("search") String search, Pageable pageable);

    @Query("SELECT p FROM MPledgingDischargeWarehouseReceipt p WHERE p.status = :status AND p.createdBy = :createdBy")
    MPledgingDischargeWarehouseReceipt findByCreatedByAndStatus(@Param("status")Status status, @Param("createdBy")Integer createdBy);
}
