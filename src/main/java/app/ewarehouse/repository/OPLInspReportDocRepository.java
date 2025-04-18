package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.InspReportDoc;

@Repository
public interface OPLInspReportDocRepository extends JpaRepository<InspReportDoc, Integer> {
	
	
	
	List<InspReportDoc> findByVchInsObserIdAndBitDeletedFlagFalse(String Id);
	
}