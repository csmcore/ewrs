package app.ewarehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.BuyerDepositorAndWareHouseOperator;
@Repository
public interface BuyerDepositorAndWareHouseOperatorRepository extends JpaRepository<BuyerDepositorAndWareHouseOperator, String> {

	 @Query(value="SELECT * \r\n"
	 		+ "	FROM t_buyer_depositor_wh_operator_application \r\n"
	 		+ "WHERE intCreatedBy = :id \r\n"
	 		+ "ORDER BY dtmCreatedOn DESC;",nativeQuery=true)
	Page<BuyerDepositorAndWareHouseOperator> findByIntCreatedBy(Integer id, Pageable pageable);
	
    @Query(value="select count(intDepositorWhOperator) from t_buyer_depositor_wh_operator_application where intCreatedBy =:createdBy and bitDeletedFlag=0;",nativeQuery=true)
	Integer getDepositorCount(Integer createdBy);

	List<BuyerDepositorAndWareHouseOperator> findBybitDeletedFlag(boolean bitDeletedFlag);
	
	//Page<BuyerDepositorAndWareHouseOperator> findByWareHouseName(String wareHouseName , Pageable pageable);
    
//	@Query(value="select intDepositorWhOperator,nameOfDepositor from t_buyer_depositor_wh_operator_application where vchWarehouseId=:vchWarehouseId and bitDeletedFlag=0;" ,nativeQuery = true)
//	List<Object[]> findDepositorIdsByWarehouseId(String vchWarehouseId);
	
	@Query(value="select intDepositorWhOperator,nameOfDepositor from t_buyer_depositor_wh_operator_application where bitDeletedFlag=0;" ,nativeQuery = true)
	List<Object[]> findDepositorIdsByWarehouseId();

	Optional<BuyerDepositorAndWareHouseOperator> findByIntDepositorWhOperator(String id);


	BuyerDepositorAndWareHouseOperator findByEmailAddress(String email);

	Page<BuyerDepositorAndWareHouseOperator> findBybitDeletedFlag(Pageable pageable,boolean bitDeletedFlag);
    
//	@Query(value="SELECT tu.vchCompanyName, tu.vchWarehouseName,w.vchWarehouseId,w.vchCompanyId\r\n"
//			+ "			FROM t_buyer_depositor_wh_operator_application w\r\n"
//			+ "			INNER JOIN m_company_warehouse_details tu \r\n"
//			+ "			    ON (w.vchCompanyId = tu.vchCompanyId OR w.vchWarehouseId = tu.vchWarehouseId)\r\n"
//			+ "            WHERE w.vchWarehouseId=:wareId and   tu.bitDeletedFlag = 0;",nativeQuery=true)
	
	@Query(value="SELECT w.vchCompanyName, w.vchWarehouseName,w.vchWarehouseId,w.vchCompanyId\r\n"
			+ "			FROM m_company_warehouse_details w\r\n"
			+ "            WHERE w.vchWarehouseId=:vchWareHouseId and   w.bitDeletedFlag = 0;",nativeQuery=true)
	List<Object[]> findCompanyAndWarehouseById(@Param("vchWareHouseId") String vchWareHouseId);
    
	@Query(value="SELECT * \r\n"
			+ "	FROM t_buyer_depositor_wh_operator_application \r\n"
			+ " WHERE vchWarehouseId = :vchWareHouseId \r\n"
			+ "ORDER BY dtmCreatedOn DESC;",nativeQuery=true)
	Page<BuyerDepositorAndWareHouseOperator> findByWareHouseName_VchWarehouseId(String vchWareHouseId,
			Pageable pageable);

	BuyerDepositorAndWareHouseOperator findByEmailAddressAndInternalUserId(String email, int i);



	

//	Optional<BuyerDepositorAndWareHouseOperator> findById(Integer intDepositorWhOperator);


	
    
	/*
	 * @Query("SELECT new app.ewarehouse.dto.BuyerDepositorViewDto(b.companyName, b.wareHouseName, b.typeOfDepositor, b.intDepositorWhOperator, b.nameOfDepositor, b.countyId, b.subCountyId, b.intWard, b.depositorStatus) FROM BuyerDepositorAndWareHouseOperator b"
	 * ) List<BuyerDepositorAndWareHouseOperatorDto> findAllDepositorViews();
	 */

}
