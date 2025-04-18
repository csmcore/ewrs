package app.ewarehouse.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Supporting_document; 
@Repository
public interface Supporting_documentRepository extends JpaRepository<Supporting_document, Integer> {

void deleteAllByIntParentId(Integer parentId);
List<Supporting_document> findByIntParentIdAndBitDeletedFlag(Integer intId, boolean deletedFlag);
}