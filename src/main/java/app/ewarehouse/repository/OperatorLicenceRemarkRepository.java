package app.ewarehouse.repository;

import app.ewarehouse.entity.OperatorLicence;
import org.springframework.data.jpa.repository.JpaRepository;

import app.ewarehouse.entity.OperatorLicenceRemarks;

public interface OperatorLicenceRemarkRepository extends JpaRepository<OperatorLicenceRemarks, Integer> {

    void deleteAllByOperatorLicence(OperatorLicence operatorLicence);
}
