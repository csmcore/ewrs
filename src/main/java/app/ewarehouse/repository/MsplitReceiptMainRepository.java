package app.ewarehouse.repository;

import app.ewarehouse.entity.MsplitReceiptMain;
import app.ewarehouse.entity.enmReceiptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MsplitReceiptMainRepository extends JpaRepository<MsplitReceiptMain ,String> {
    List<MsplitReceiptMain> findAllByBitDeleteFlagAndStatus(boolean bitDeleteFlag, enmReceiptStatus enmReceiptStatus);

    Page<MsplitReceiptMain> findAllByBitDeleteFlagAndStatusOrderByDtmCreatedAtDesc(boolean bitDeleteFlag, enmReceiptStatus enmReceiptStatus, Pageable pageable);

//    @Query("SELECT m FROM MsplitReceiptMain m JOIN TsplitReceipt s ON m.splits.splitReceipt = s.splitReceipt. WHERE s.splitReceiptNo = :splitReceiptNo AND m.bitDeleteFlag = :bitDeleteFlag")
    @Query(value = "SELECT m.* FROM m_split_receipt_main m inner join t_split_receipt t " +
            "on t.vchSplitIdReceipt = m.vchSplitApplicationId where t.vchSplitReceiptNumber=:splitReceiptNo and m.bitDeleteFlag=:bitDeleteFlag", nativeQuery = true)
    Optional<MsplitReceiptMain> getBySplitReceiptNoAndBitDeleteFlag(@Param("splitReceiptNo") String splitReceiptNo, @Param("bitDeleteFlag") boolean bitDeleteFlag);
}
