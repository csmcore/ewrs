package app.ewarehouse.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.DynamicLetterConfig;
@Repository
public interface LetterConfigurationRepository extends JpaRepository<DynamicLetterConfig, Integer> {

	@Query("From DynamicLetterConfig where bitDeletedFlag=:bitDeletedFlag and (:intLetterConfigId = 0 or intLetterConfigId=:intLetterConfigId) and (:txtLetterName='0' or txtLetterName like CONCAT('%',:txtLetterName,'%')) and (:intFormId = 0 or intFormId=:intFormId ) ")
	List<DynamicLetterConfig> findAllByBitDeletedFlagAndIntInsertStatus(boolean bitDeletedFlag,
			Integer intLetterConfigId, String txtLetterName, Integer intFormId);

	DynamicLetterConfig findByIntLetterConfigIdAndBitDeletedFlag(Integer intLetterConfigId, boolean bitDeletedFlag);

	List<DynamicLetterConfig> findByIntFormIdAndBitDeletedFlag(Integer intFormId, boolean bitDeletedFlag);

	@Query(value = "select m.intLetterConfigId,m.vchLetterName,m.txtLetterContent,m.intformId,t.intConfigId,t.vchGenerateStatus From m_dyn_letter_config m \n"
			+ "left join t_dynamic_letter_config t on m.intLetterConfigId=t.intLetterConfigId \n"
			+ "where t.intApplicationId=:serviceId and m.bitDeletedFlag=0", nativeQuery = true)
	List<DynamicLetterConfig> findByLetterData(Integer serviceId);

	@Query(value = "SELECT m.intLetterConfigId, m.vchLetterName, t.vchGenerateStatus " + "FROM m_dyn_letter_config m "
			+ "LEFT JOIN t_dynamic_letter_config t ON m.intLetterConfigId = t.intLetterConfigId "
			+ "AND t.intApplicationId =:intApplicationId "
			+ "WHERE m.intLetterConfigId IN (:authLetters) and m.bitDeletedFlag=0 " + "UNION "
			+ "SELECT m.intLetterConfigId, m.vchLetterName, t.vchGenerateStatus " + "FROM m_dyn_letter_config m "
			+ "RIGHT JOIN t_dynamic_letter_config t ON m.intLetterConfigId = t.intLetterConfigId "
			+ "WHERE t.intApplicationId =:intApplicationId and m.intformId=:intformId and t.bitDeletedFlag=0", nativeQuery = true)
	List<Map<String, Object>> getByDataServiceIdAndAuthLetters(List<Integer> authLetters, Integer intApplicationId,
			Integer intformId);

	@Query(value = "select m.intLetterConfigId,m.vchLetterName,t.vchGenerateStatus From m_dyn_letter_config m \n"
			+ "left join t_dynamic_letter_config t on m.intLetterConfigId=t.intLetterConfigId and t.intApplicationId=:intApplicationId\n"
			+ "where m.intLetterConfigId IN (:authLetters) and m.bitDeletedFlag=0 ", nativeQuery = true)
	List<Map<String, Object>> getDataForDaraftUsingAuthLetters(List<Integer> authLetters, Integer intApplicationId);

	@Query(value = "select m.intLetterConfigId,m.vchLetterName,t.vchGenerateStatus From m_dyn_letter_config m \n"
			+ "right join t_dynamic_letter_config t on m.intLetterConfigId=t.intLetterConfigId\n"
			+ "where t.intApplicationId=:intApplicationId and m.intformId=:intformId and t.bitDeletedFlag=0", nativeQuery = true)
	List<Map<String, Object>> getDataForGenerateUsingServiceId(Integer intApplicationId, Integer intformId);

}
