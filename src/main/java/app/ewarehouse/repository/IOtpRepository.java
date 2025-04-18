package app.ewarehouse.repository;



import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.OTP;

@Repository
public interface IOtpRepository extends JpaRepository<OTP, Integer> {
	
	OTP findByEmailAndFlag(String email, int flag);

	OTP findByMobileAndFlag(String mobile, int flag);
	
	List<OTP> findByFlagAndEmail(int flag, String email);
	
	List<OTP> findByFlagAndMobile(int flag, String mobile);
	
	@Query(value = "select count(*) from otp where email = ?1 and DATE_FORMAT(edt, 'dd-mm-yyyy') = DATE_FORMAT(now(), 'dd-mm-yyyy')", nativeQuery = true)
	int getOtpCount(String emailid);
	
	@Query(value = "select count(*) from otp where email = ?1 and DATE_FORMAT(edt, 'dd-mm-yyyy') = DATE_FORMAT(now(), 'dd-mm-yyyy') AND flag=0", nativeQuery = true)
	int getCurrentOtpCount(String emailid);
	
	@Query(value = "select edt from otp where email = ?1  AND flag=0 order by edt desc limit 1", nativeQuery = true)
	Date currentOTPTime(String userId);
	
	@Query(value = "select edt from otp where email = ?1 order by edt desc limit 1", nativeQuery = true)
	Date maxOTP(String userId);
	
}
