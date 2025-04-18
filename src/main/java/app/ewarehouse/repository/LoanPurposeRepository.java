package app.ewarehouse.repository;

import app.ewarehouse.entity.MLoanPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanPurposeRepository extends JpaRepository<MLoanPurpose, Integer> {
}
