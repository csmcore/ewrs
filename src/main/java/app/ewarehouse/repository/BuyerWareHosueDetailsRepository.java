package app.ewarehouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.BuyerWareHosueDetails;

@Repository
public interface BuyerWareHosueDetailsRepository extends JpaRepository<BuyerWareHosueDetails, Integer> {
    
	//@Query(value="select vchWarehouseId  from m_company_warehouse_details where vchWarehouseId=:wareHouseName and bitDeletedFlag=0 ;",nativeQuery=true)
	//Optional<BuyerWareHosueDetails> findByVchWarehouseId(String wareHouseName);

//	@Query(value="select *  from m_company_warehouse_details where vchWarehouseId=:vchWarehouseId and bitDeletedFlag=:bitDeletedFlag ;",nativeQuery=true)
//	Optional<BuyerWareHosueDetails> findByVchWarehouseIdAndBitDeletedFlagFalse(@Param("vchWarehouseId")String vchWarehouseId,@Param("bitDeletedFlag") boolean bitDeletedFlag);

	Optional<BuyerWareHosueDetails> findByVchWarehouseId(String wareHouseName);
}
