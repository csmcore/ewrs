package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.ewarehouse.entity.LossReceipt;

public interface LossReceiptRepository extends JpaRepository<LossReceipt, Integer> {
}
