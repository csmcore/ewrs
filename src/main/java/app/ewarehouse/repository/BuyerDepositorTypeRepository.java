package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.BuyerDepositorType;

@Repository
public interface BuyerDepositorTypeRepository extends JpaRepository<BuyerDepositorType, Integer> {
    BuyerDepositorType findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

    List<BuyerDepositorType> findAllByBitDeletedFlag(Boolean bitDeletedFlag);
}

