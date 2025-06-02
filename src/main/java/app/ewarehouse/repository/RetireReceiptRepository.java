package app.ewarehouse.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import app.ewarehouse.entity.RetireReceipt;

public interface RetireReceiptRepository extends JpaRepository<RetireReceipt, Integer> {
}
