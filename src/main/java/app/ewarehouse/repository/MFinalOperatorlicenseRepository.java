package app.ewarehouse.repository;

import app.ewarehouse.entity.CreatedStatus;
import app.ewarehouse.entity.MFinalOperatorLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MFinalOperatorlicenseRepository extends JpaRepository<MFinalOperatorLicense, String> {

        MFinalOperatorLicense findByVchLicenseNoAndBitDeletedFlag(String vchLicenseNo, boolean bitDeletedFlag);

        List<MFinalOperatorLicense> findByApplicationOfConformity_ApplicationIdAndBitDeletedFlagAndEnmStatus(String id, boolean bitDeletedFlag, CreatedStatus createdStatus);

        List<MFinalOperatorLicense> findAllByBitDeletedFlag(boolean bitDeletedFlag);
}
