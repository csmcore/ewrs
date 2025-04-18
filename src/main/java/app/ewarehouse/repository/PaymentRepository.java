package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.DepositorPaymentDetails;
@Repository
public interface PaymentRepository extends JpaRepository<DepositorPaymentDetails, Integer>{

}
