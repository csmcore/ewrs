package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.DraftLetterEntity;
@Repository
public interface DraftLetterRepository extends JpaRepository<DraftLetterEntity, Integer> {
	
	@Query("Select vchGenerateStatus from DraftLetterEntity where intLetterConfigId=:intLetterConfigId and intApplicationId= :intApplicationId")
	List<Integer> getVchGenerateStatusByIntLetterConfigId(Integer intLetterConfigId,Integer intApplicationId);
	@Query("Select txtLetterContent from DraftLetterEntity where intConfigId=:intConfigId and bitDeletedFlag=false")
	String getIntLetterConfigIdByIntConfigId(int intConfigId);
	@Query("Select txtLetterContent from DraftLetterEntity where intLetterConfigId=:intLetterConfigId and intApplicationId=:intServiceId and bitDeletedFlag=false")
	String getLetterContentByIntConfigId(Integer intLetterConfigId,Integer intServiceId);
	DraftLetterEntity findByIntLetterConfigIdAndIntApplicationIdAndBitDeletedFlag(Integer intLetterConfigId,
			Integer intApplicationId, boolean bitDeletedFlag);
	DraftLetterEntity findByIntConfigIdAndBitDeletedFlag(Integer intConfigId, boolean bitDeletedFlag);
	@Query("Select vchGenerateStatus from DraftLetterEntity where intLetterConfigId=:intLetterConfigId and intApplicationId=:intApplicationId and bitDeletedFlag=:bitDeletedFlag")
	Integer findByIntLetterConfigIdAndBitDeletedFlag(Integer intLetterConfigId,Integer intApplicationId, boolean bitDeletedFlag);
	List<DraftLetterEntity> findByIntApplicationIdAndBitDeletedFlag(Integer intApplicationId, boolean bitDeletedFlag);
	
	@Query(value="select count(*) from t_dynamic_letter_config where intLetterConfigId=:intLetterConfigId and intApplicationId=:intApplicationId and bitDeletedFlag=:bitDeletedFlag",nativeQuery=true)
	Integer countByIntLetterConfigIdAndIntServiceIdAndBitDeletedFlag(Integer intLetterConfigId, Integer intApplicationId, boolean bitDeletedFlag);
	@Query(value="select GROUP_CONCAT(DISTINCT intLetterConfigId) from t_dynamic_letter_config where intApplicationId=:intApplicationId and bitDeletedFlag=0",nativeQuery=true)
	String getIntLetterConfigIdByIntApplicationId(Integer intApplicationId );

}
