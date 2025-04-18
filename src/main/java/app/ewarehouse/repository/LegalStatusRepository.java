package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.LegalStatus;

@Repository
public interface LegalStatusRepository extends JpaRepository<LegalStatus, Integer> {
    LegalStatus findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

    List<LegalStatus> findAllByBitDeletedFlag(Boolean bitDeletedFlag);
}
