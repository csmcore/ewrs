package app.ewarehouse.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.ewarehouse.entity.Supporting_doc;
 
@Repository
public interface Supporting_docRepository extends JpaRepository<Supporting_doc, Integer> {

void deleteAllByIntParentId(Integer parentId);
List<Supporting_doc> findByIntParentIdAndBitDeletedFlag(Integer intId, boolean deletedFlag);
}