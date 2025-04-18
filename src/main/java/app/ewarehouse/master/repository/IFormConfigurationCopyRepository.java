package app.ewarehouse.master.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.ewarehouse.master.entity.FormConfigurationCopy;
import jakarta.transaction.Transactional;

public interface IFormConfigurationCopyRepository extends JpaRepository<FormConfigurationCopy,Integer> {

	 Optional<FormConfigurationCopy> findById(Integer id);

	    @Modifying
	    @Transactional
	    @Query("UPDATE FormConfigurationCopy f SET f.checkerStatus = :status, f.updatedOn = :updatedOn, f.updatedBy = :updatedBy, f.deletedFlag = :deletedFlag WHERE f.idCopy = :idCopy")
	    Integer updateFormConfigurationCopyById(@Param("status") Integer status, 
	                                            @Param("updatedOn") Date updatedOn, 
	                                            @Param("updatedBy") String updatedBy, 
	                                            @Param("deletedFlag") Boolean deletedFlag, 
	                                            @Param("idCopy") Integer idCopy);
}
