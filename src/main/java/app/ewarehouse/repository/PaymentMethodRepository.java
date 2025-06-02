package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.PaymentMethod;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {

    PaymentMethod findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);
    
    PaymentMethod findByIntId(Integer intId);

    @Query("From PaymentMethod where bitDeletedFlag=:bitDeletedFlag")
    List<PaymentMethod> findAllByBitDeletedFlag(Boolean bitDeletedFlag);

    @Query(" Select txtPaymentMethod From PaymentMethod where intId=:intId AND bitDeletedFlag=:bitDeletedFlag")
    String  getPaymentMethodByIntId(Integer intId,Boolean bitDeletedFlag);

    @Query("Select count(*) from PaymentMethod where txtPaymentMethod=:txtPaymentMethod AND bitDeletedFlag=:bitDeletedFlag AND intId !=:intId ")
    Integer getCountByPaymentMethodANDbitDeletedFlagNOTIntId(Integer intId,String txtPaymentMethod, boolean bitDeletedFlag);
    @Query("Select count(*) From PaymentMethod where txtPaymentMethod=:txtPaymentMethod AND bitDeletedFlag=:bitDeletedFlag")
    Integer countByPaymentMethodANDBitDeletedFlag(String txtPaymentMethod, Boolean bitDeletedFlag);

    @Query("SELECT p FROM PaymentMethod p " +
            "WHERE (:search IS NULL OR " +
            "LOWER(p.txtPaymentMethod) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.txtDescription) LIKE LOWER(CONCAT('%', :search, '%')))"+
            "order by p.intId desc")
	Page<PaymentMethod> findByFilters(String search, Pageable pageable);

    @Query("SELECT p FROM PaymentMethod p order by p.intId desc")
	Page<PaymentMethod> getAllPaymentTypes(Pageable pageable);
}
