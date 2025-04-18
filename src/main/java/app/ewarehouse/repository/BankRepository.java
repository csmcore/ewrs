package app.ewarehouse.repository;


import java.util.List;


import app.ewarehouse.entity.Bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
    Bank findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);
    List<Bank> findAllByBitDeletedFlag(Boolean bitDeletedFlag);
}
