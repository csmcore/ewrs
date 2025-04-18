package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.GraderWeigherForm;

@Repository
public interface GraderWeigherRegRepository extends JpaRepository<GraderWeigherForm, String> {

	int countByEmployeeId(String empId);

}
