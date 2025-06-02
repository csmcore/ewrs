package app.ewarehouse.repository;

import app.ewarehouse.entity.CommitteeDesignation;
import app.ewarehouse.entity.LegalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitteeDesignationRepository extends JpaRepository<CommitteeDesignation, Integer> {
    CommitteeDesignation findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

    List<CommitteeDesignation> findAllByBitDeletedFlag(Boolean bitDeletedFlag);
}
