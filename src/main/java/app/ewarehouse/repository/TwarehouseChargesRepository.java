package app.ewarehouse.repository;

import app.ewarehouse.entity.TvalidateCommodity;
import app.ewarehouse.entity.TwarehouseCharges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwarehouseChargesRepository extends JpaRepository<TwarehouseCharges,Integer> {

    TwarehouseCharges findByTxtConformityIdAndBitDeleteFlag(String txtConformityId,boolean bitDeleteFlag);
}
