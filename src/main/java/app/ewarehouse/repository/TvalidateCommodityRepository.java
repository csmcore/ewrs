package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.TvalidateCommodity;

@Repository
public interface TvalidateCommodityRepository extends JpaRepository<TvalidateCommodity,String> {
    List<TvalidateCommodity> findAllByBitDeleteFlag(boolean b);
    TvalidateCommodity  findAllByTxtValidateIdAndBitDeleteFlag(String txtValidateId,boolean bitDeleteFlag);

    @Query("From TvalidateCommodity t where t.status = 'Approved' AND t.bitDeleteFlag = :bitDeleteFlag ORDER BY t.dtmCreatedAt DESC")
    Page<TvalidateCommodity> findAllByBitDeleteFlagOrderByDtmCreatedAtDesc(boolean bitDeleteFlag, Pageable pageable);
}
