package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintMgmtInspScheduleDocs;
import app.ewarehouse.entity.Supporting_document;

@Repository
public interface ComplaintMgmtInspScheduleDocsRepository extends JpaRepository<ComplaintMgmtInspScheduleDocs, Integer> {

	
	List<ComplaintMgmtInspScheduleDocs> findByParentIdAndBitDeletedFlag(Integer intId, boolean deletedFlag);
}
