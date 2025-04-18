package app.ewarehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.IssuanceWareHouseRecipt;

@Repository
public interface IssuanceWareHouseReciptRepository extends JpaRepository<IssuanceWareHouseRecipt, Integer>{
	
	@Query(value="SELECT * FROM t_issuance_ware_house_recipt_application where intCreatedBy=:userId AND bitDeletedFlag=:bitDeletedFlag",nativeQuery = true)
	List<IssuanceWareHouseRecipt> findByIntCreatedByAndBitDeletedFlag(Integer userId,boolean bitDeletedFlag);

	@Query(value="SELECT * FROM t_issuance_ware_house_recipt_application where intDepositorWhOperator=:id AND intIssueanceWhId=:id1 AND bitDeletedFlag=:bitDeletedFlag",nativeQuery = true)
	List<IssuanceWareHouseRecipt> findByIdAndBitDeletedFlag(String id,Integer id1, boolean bitDeletedFlag);


	///Page<IssuanceWareHouseRecipt> findBybitDeletedFlag( boolean bitDeletedFlag, Pageable pageable);
	
	@Query(value="SELECT * FROM t_issuance_ware_house_recipt_application where intDepositorWhOperator=:id AND intIssueanceWhId=:id1 AND bitDeletedFlag=:bitDeletedFlag",nativeQuery = true)
	IssuanceWareHouseRecipt findByIdAndIntDepositorWhOperatorAndBitDeletedFlag(String id,Integer id1,boolean bitDeletedFlag);

	//Page<IssuanceWareHouseRecipt> findByIntCreatedBy(Integer userId, Pageable pageable);
    
	@Query(value="SELECT * \r\n"
			+ "	FROM t_issuance_ware_house_recipt_application \r\n"
			+ "WHERE vchWarehouseId = :vchWarehouseId \r\n"
			+ "ORDER BY intIssueanceWhId DESC;",nativeQuery = true)
	Page<IssuanceWareHouseRecipt> findByVchWarehouseId(String vchWarehouseId, Pageable pageable);

	List<IssuanceWareHouseRecipt> findByIntCreatedBy(Integer userId);
	
	@Query(value = "SELECT MAX(CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(wareHouseReciptNo, '-', -2), '-', 1) AS UNSIGNED)) AS largest_middle_number\r\n"
			+ "FROM t_issuance_ware_house_recipt_application;", nativeQuery = true)
    Integer findLatestReceiptId();
	
	@Query(
		    value = "SELECT * FROM t_issuance_ware_house_recipt_application " +
		            "WHERE (paymentStatus = 'Received' OR doPaymentLater=1) AND isRetired=0 AND isTransfer=0 " +
		            "ORDER BY stmUpdatedOn DESC",
		    countQuery = "SELECT COUNT(*) FROM t_issuance_ware_house_recipt_application WHERE paymentStatus = 'Received' AND isRetired=0 AND isTransfer=0",
		    nativeQuery = true
		)
		Page<IssuanceWareHouseRecipt> findAllByPaymentStatusReceived(Pageable pageable);

	@Query(
		    value = "SELECT * FROM t_issuance_ware_house_recipt_application " +
		            "WHERE oicStatus = 'ForwaredToRegistrar' AND isRetired=0 AND isTransfer=0 " +
		            "ORDER BY stmUpdatedOn DESC",
		    countQuery = "SELECT COUNT(*) FROM t_issuance_ware_house_recipt_application WHERE oicStatus = 'ForwaredToRegistrar' AND isRetired=0 AND isTransfer=0",
		    nativeQuery = true
		)
	Page<IssuanceWareHouseRecipt> findAllByOICStatusReceived(Pageable pageable);

	@Query(value="""
			select tm.vchOplId
			from t_WRSC_OPL_Main tm
			inner join t_operator_licence_application  toa on  toa.intLicenceSno=tm.intOplAppSno
			where toa.vchWareHouseId=:warehouseId and toa.vchApprovalStatus='APPROVED' and tm.bitDeletedFlag=0
			""",nativeQuery = true)
	String getOperatorLicenceNo(String warehouseId);
	
	
	@Query(value = "SELECT i.* FROM t_issuance_ware_house_recipt_application i " +
            "JOIN t_buyer_depositor_wh_operator_application b " +
            "ON i.intDepositorWhOperator = b.intDepositorWhOperator " +
            "WHERE b.emailAddress = :emailId and b.bitDeletedFlag=0 and i.bitDeletedFlag=0 ORDER BY i.dtmCreatedOn DESC"
            , nativeQuery = true)
	List<IssuanceWareHouseRecipt> findByEmail(String emailId);

	@Query(
		    value = "SELECT * FROM t_issuance_ware_house_recipt_application " +
		            "WHERE isTransfer =1 " +
		            "ORDER BY dtmCreatedOn DESC",
		    countQuery = "SELECT COUNT(*) FROM t_issuance_ware_house_recipt_application WHERE isTransfer =1",
		    nativeQuery = true
		)
	Page<IssuanceWareHouseRecipt> viewTransferIssuance(Pageable pageable);
	
	
	@Query(
		    value = "SELECT * FROM t_issuance_ware_house_recipt_application " +
		            "WHERE isTransfer =1 AND paymentStatus = 'Received' " +
		            "ORDER BY dtmCreatedOn DESC",
		    countQuery = "SELECT COUNT(*) FROM t_issuance_ware_house_recipt_application WHERE isTransfer =1 AND paymentStatus = 'Received'",
		    nativeQuery = true
		)
	Page<IssuanceWareHouseRecipt> viewTransferIssuanceOIC(Pageable pageable);

	@Query(
		    value = "SELECT * FROM t_issuance_ware_house_recipt_application " +
		            "WHERE isTransfer =1 AND oicStatus = 'ForwaredToRegistrar'" +
		            "ORDER BY dtmCreatedOn DESC",
		    countQuery = "SELECT COUNT(*) FROM t_issuance_ware_house_recipt_application WHERE isTransfer =1 AND oicStatus = 'ForwaredToRegistrar'",
		    nativeQuery = true
		)
	Page<IssuanceWareHouseRecipt> viewTransferIssuanceCEO(Pageable pageable);

	
}
