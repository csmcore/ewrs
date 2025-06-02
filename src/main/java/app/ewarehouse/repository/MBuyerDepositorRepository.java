package app.ewarehouse.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.MBuyerDepositor;

@Repository
public interface MBuyerDepositorRepository extends JpaRepository<MBuyerDepositor, String> {

    MBuyerDepositor findByVchEntityIdAndBitDeletedFlag(String vchEntityId, boolean bitDeletedFlag);

    List<MBuyerDepositor> findAllByBitDeletedFlagOrderByDtmCreatedOnDesc(Boolean bitDeletedFlag);

    Page<MBuyerDepositor> findAllByBitDeletedFlagOrderByDtmCreatedOnDesc(Boolean bitDeletedFlag, Pageable pageable);
}
