package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ApplicationOfConformityLocationDetails;
import app.ewarehouse.entity.OperatorLicenseFormOneC;

@Repository
public interface OperatorLicenseFormOneCRepository extends JpaRepository<OperatorLicenseFormOneC, Long> {

	OperatorLicenseFormOneC findByWarehouseLocationAndDeletedFlagFalse(ApplicationOfConformityLocationDetails locationData);
}
