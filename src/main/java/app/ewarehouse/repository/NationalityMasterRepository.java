package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Nationality;
import jakarta.transaction.Transactional;
@Repository
public interface NationalityMasterRepository extends JpaRepository<Nationality, Long> {

	@Modifying
    @Transactional
    @Query("UPDATE Nationality n SET n.bitDeletedFlag = NOT n.bitDeletedFlag WHERE n.intNationalityId = :id")
    void changeNationalityStatus(@Param("id") Long id);
	
	
	List<Nationality> findByBitDeletedFlagFalse();
}
