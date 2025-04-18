package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.dto.PaymentDTO;
import app.ewarehouse.entity.PaymentData;
import app.ewarehouse.entity.PaymentStatus;


@Repository
public interface PaymentDataRepository extends JpaRepository<PaymentData, Long>{


	PaymentData findByIdAndEnmPaymentStatusAndBitDeletedFlag(Long paymentId, PaymentStatus enmPaymentStatus, Boolean bitDeletedFlag);
	

	PaymentData findByBillRefNumberAndClientIDNumberAndBitDeletedFlagFalse(String billRefNumber, String clientIDNumber);


	 @Query(value = "SELECT new app.ewarehouse.dto.PaymentDTO(p.clientName as applicantName,p.amountExpected as applicationFees, p.billRefNumber as paymentTransactionNo , p.dtmCreatedOn as paymentDateTime, c.paymentMode) "
	            + "FROM PaymentData p "
	            + "INNER JOIN ApplicationCertificateOfCollateral c ON p.depositorId = c.applicationId "
	            + "WHERE enmPaymentStatus='PAID' AND p.depositorId = :applicationId and p.clientIDNumber=:userId")
	 PaymentDTO getPaymentDetailsByApplicationIdandUserId(@Param("applicationId") String applicationId, Integer userId);


	 @Query(value = "SELECT new app.ewarehouse.dto.PaymentDTO(p.clientName as applicantName,p.amountExpected as applicationFees, p.billRefNumber as paymentTransactionNo , p.dtmCreatedOn as paymentDateTime, c.paymentMode) "
	            + "FROM PaymentData p "
	            + "INNER JOIN ApplicationCertificateOfCollateral c ON p.depositorId = c.applicationId "
	            + "WHERE enmPaymentStatus='PAID' AND p.depositorId = :applicationId ")
	PaymentDTO getPaymentDetailsByApplicationId(@Param("applicationId") String applicationId);


	@Query(value = "SELECT p.client_name AS applicantName, " +
               "p.amount_expected AS applicationFees, " +
               "p.bill_ref_number AS paymentTransactionNo, " +
               "p.dtm_created_on AS paymentDateTime, " +
               "p.enm_payment_status AS paymentStatus " +
        "FROM t_payment_ewarehouse p " +
        "WHERE p.int_created_by = :userId " +
        "AND p.vchPaymentInitId = :applicationId " +
        "AND p.bit_deleted_flag = 0",
       nativeQuery = true)
List<Object[]> getPaymentDetailsByUserIdRetired(@Param("applicationId") String applicationId, 
                                                @Param("userId") Integer userId);


}
