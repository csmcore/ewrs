package app.ewarehouse.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.ewarehouse.master.entity.FormConfiguration;
import app.ewarehouse.master.entity.TypeMaster;
import java.util.List;


public interface IFormConfigurationRepository extends JpaRepository<FormConfiguration, Integer> {
	
	   @Query("SELECT COUNT(m.id) FROM FormConfiguration m " +
	           "INNER JOIN FormConfigurationCopy c ON m.id = c.id " +
	           "WHERE " +
	           "(m.typeMaster = :typeMaster AND m.documentName = :docName AND m.id <> :id) " +
	           "OR " +
	           "(c.typeMaster = :typeMaster AND c.documentName = :docName AND c.id <> :id)")
	    long duplicateCheckDocumentNameInUpdate(@Param("docName") String docName ,@Param("typeMaster") TypeMaster typeMaster,@Param("id") Integer id );

	   @Query("SELECT COUNT(m.id) FROM FormConfiguration m " +
	           "INNER JOIN FormConfigurationCopy c ON m.id = c.id " +
	           "WHERE " +
	           "(m.typeMaster = :typeMaster AND m.documentName = :docName) " +
	           "OR " +
	           "(c.typeMaster = :typeMaster AND c.documentName = :docName )")
	    long duplicateCheckDocumentNameInNewRecord(@Param("docName") String docName ,@Param("typeMaster") TypeMaster typeMaster );
	   
	   
	   @Query("SELECT f FROM FormConfiguration f WHERE f.typeMaster = :typeMaster AND f.finalStatus= 2 AND f.deletedFlag=false")
	   List<FormConfiguration> findByTypeMasterAndApprova(@Param("typeMaster") TypeMaster typeMaster);
	   

}
