package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.TreceiveCommodity;

@Repository
public interface TreceiveCommodityRepository extends JpaRepository<TreceiveCommodity ,String> {
    TreceiveCommodity findByTxtReceiveCIdAndBitDeleteFlag(String txtReceiveCId, boolean bitDeleteFlag);

    @Query("FROM TreceiveCommodity t ORDER BY dtmCreatedAt DESC")
    List<TreceiveCommodity> findAllByBitDeleteFlag(boolean bitDeleteFlag);

@Query("SELECT t FROM TreceiveCommodity t " +
        "JOIN t.depositor d " +
        "JOIN t.commodity c " +
        "WHERE t.bitDeleteFlag = false " +
        "AND (:search IS NULL OR " +
        "LOWER(c.seasonality.seasonname) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(t.status) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(d.txtName) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(d.txtEmailAddress) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(t.txtGrade) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR CAST(t.intQuantity AS string) LIKE (CONCAT('%', :search, '%')) " +
        "OR LOWER(t.txtLotNo) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR CAST(t.txtReceiveCId AS string) LIKE CONCAT('%', :search, '%') " +
        "OR CAST(d.txtTelephoneNumber AS string) LIKE CONCAT('%', :search, '%') " +
        "OR CAST(d.intId AS string) LIKE CONCAT('%', :search, '%')) " +
        "AND t.warehouseId = :warehouseId")
    Page<TreceiveCommodity> findByFilters(String search, Pageable pageable, String warehouseId);
}
