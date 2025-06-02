package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.TrevocationOfCertificateOfCompliance;

@Repository
public interface TrevocationOfCertificateOfComplianceRepository extends JpaRepository<TrevocationOfCertificateOfCompliance,String> {

    TrevocationOfCertificateOfCompliance getSuspensionByComplaintNumber(String complaintNumber);

        @Query("FROM TrevocationOfCertificateOfCompliance where isDeleted = false")
    List<TrevocationOfCertificateOfCompliance> findAllByIsDeleted();

        @Query("SELECT c FROM TrevocationOfCertificateOfCompliance c where c.isDeleted = false ORDER BY c.createdAt DESC")
    Page<TrevocationOfCertificateOfCompliance> findDataByIsDeleted(Pageable pageable);
}
