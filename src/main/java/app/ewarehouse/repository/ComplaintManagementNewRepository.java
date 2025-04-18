package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintManagementNewEntity;
import app.ewarehouse.entity.Complaint_managment;

@Repository
public interface ComplaintManagementNewRepository extends JpaRepository<ComplaintManagementNewEntity, Integer> {
	
	ComplaintManagementNewEntity findByIdAndCreatedByAndDeletedFlagFalse(Integer Id,Integer createdBy);
	
	@Query("From ComplaintManagementNewEntity c JOIN c.user u where c.deletedFlag=:bitDeletedFlag  and (:compNO='0' or c.complaintNo = :compNO) and (:complaintAgainstName='0' or u.txtFullName=:complaintAgainstName) and (:intCreatedBy=0 or c.createdBy = :intCreatedBy)")
	List<ComplaintManagementNewEntity> findAllComplaintByDeletedFlag(Boolean bitDeletedFlag,Pageable pageRequest,String compNO,String complaintAgainstName, Integer intCreatedBy);
	
	Integer countByDeletedFlag(Boolean bitDeletedFlag);
	Integer countByDeletedFlagAndCreatedBy(boolean b, int i);
	@Query("Select count(c) From ComplaintManagementNewEntity c  JOIN c.user u where c.deletedFlag=:deletedFlag  and (:complaintNo='0' or c.complaintNo like CONCAT('%',:complaintNo,'%'))  and (:complaintAgainstName='0' or u.txtFullName like CONCAT('%',:complaintAgainstName,'%')) and (:intCreatedBy=0 or c.createdBy = :intCreatedBy)")
	Integer countSearchByDeletedFlagAndMobileAndComplaintNo(@Param("deletedFlag")boolean bitDeletedFlag, @Param("complaintNo")String complaintNo,@Param("complaintAgainstName")String complaintAgainstName,@Param("intCreatedBy")Integer intCreatedBy );
	
	ComplaintManagementNewEntity findByIdAndDeletedFlag(Integer intId, boolean bitDeletedFlag);
	
	@Query("Select complaintNo from ComplaintManagementNewEntity order by id desc limit 1")
	String getId();

	 @Query(value = "SELECT * FROM complaint_managment_new " +
             "WHERE complaint_type = :complaintType " +
             "AND complaint_against = :complainAgainst " +
             "AND applicationStatus IN ('Suspended', 'Under Reinstatement')"+
             "AND complaint_against_userId = :userId", nativeQuery = true)
	 ComplaintManagementNewEntity getSuspendCheck(@Param("complaintType") Integer complaintType,
                      @Param("complainAgainst") Integer complainAgainst,
                      @Param("userId") Integer userId);

	 @Query(value = "SELECT main.vchOplId " +
             "FROM t_operator_licence_application ap " +
             "INNER JOIN t_WRSC_OPL_Main main " +
             "ON ap.intLicenceSno = main.intOplAppSno " +
             "WHERE ap.vchWarehouseId = :vchWarehouse", 
     nativeQuery = true)
	 String getLicenceNo(@Param("vchWarehouse") String vchWarehouse);

	 
//	 @Query(value = "SELECT * FROM complaint_managment_new " +
//             "WHERE complaint_type = :complaintType " +
//             "AND complaint_against = :complainAgainst " +
//             "AND applicationStatus = 'Under Revocation' " +
//             "AND complaint_against_userId = :userId", nativeQuery = true)	 
//	ComplaintManagementNewEntity getRevocationCheck(Integer complaintType, Integer complainAgainst, Integer userId);
//	 
	 @Query(value = "SELECT * FROM complaint_managment_new " +
             "WHERE complaint_type = :complaintType " +
             "AND complaint_against = :complainAgainst " +
             "AND applicationStatus IN ('Under Revocation', 'Revoked')"+
             "AND complaint_against_userId = :userId", nativeQuery = true)	 
	ComplaintManagementNewEntity getRevocationCheck(Integer complaintType, Integer complainAgainst, Integer userId);
	 

	
	
	
}