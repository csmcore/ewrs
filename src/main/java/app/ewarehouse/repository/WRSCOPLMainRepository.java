package app.ewarehouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.ewarehouse.entity.WRSC_OPL_Main;

@Repository
public interface WRSCOPLMainRepository  extends JpaRepository<WRSC_OPL_Main, Integer> {
	
	Optional<WRSC_OPL_Main> findTopByVchOplIdStartingWithOrderByVchOplIdDesc(String IdFormat);
	Optional<WRSC_OPL_Main> findTopByVchCertIdStartingWithOrderByVchCertIdDesc(String IdFormat);
	
	WRSC_OPL_Main  findByVchOplAppIdAndBitLicenceCertGenAndBitDeletedFlagFalse(String id,boolean certStatus);
	WRSC_OPL_Main  findByVchCertIdAndBitLicenceCertGenAndBitDeletedFlagFalse(String id,boolean certStatus);
	
	
	
	
	
	

}
