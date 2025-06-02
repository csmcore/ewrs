package app.ewarehouse.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.TSetAuthority;
@Repository
public interface TSetAuthorityRepository extends JpaRepository<TSetAuthority, Integer> {

	@Query(value="select coalesce( GROUP_CONCAT(distinct intPrimaryAuth)  ,0) AS intPrimaryAuth,coalesce( GROUP_CONCAT(distinct intATAProcessId) ,0) as intATAProcessId from t_set_authority where tinStageNo=:stageNo and intProcessId=:processId and bitDeletedFlag = 0 ",nativeQuery = true)
	List<Object[]> getAuthorityDetailsByProcessIdAndStageNo(Integer processId, Integer stageNo);
	
	@Query(value="select NVL( LISTAGG( intPrimaryAuth, ', ') WITHIN GROUP (ORDER BY intPrimaryAuth)  ,0) intPrimaryAuth, SUBSTR(NVL( LISTAGG( intATAProcessId, ', ') WITHIN GROUP (ORDER BY intATAProcessId)  ,0),1,1 )intATAProcessId from t_set_authority where tinStageNo=:stageNo and intProcessId=:processId and bitDeletedFlag = 0 ",nativeQuery = true)
	List<Object[]> getAuthorityDetailsByProcessIdAndStageNoOracle(Integer processId, Integer stageNo);
	
	@Query(value="select FC.formDetails, pn.vchTableName from m_dyn_form_configuration as FC join m_process_name as pn on FC.itemId = pn.intProcessId where FC.deletedFlag =0 and pn.bitDeletedFlag = 0 and FC.itemId =:processId ; ",nativeQuery = true)
	List<Object[]> getApplicationDetail(Integer processId);

	@Query(value=" SELECT group_concat(intPrimaryAuth) as FORWARD_TO_AUTH FROM t_set_authority where intProcessId =:processId and intProjectId =:projectId and tinStageNo =:stageNo and bitDeletedFlag = 0  ",nativeQuery = true)
	String getIntProcessIdAndTinStageNoAndIntProjectId(Integer processId, Integer stageNo, Integer projectId);
	
	@Query(value=" SELECT intATAProcessId FROM t_set_authority where intProcessId =:processId and intProjectId =:projectId and tinStageNo =:stageNo and bitDeletedFlag = 0  ",nativeQuery = true)
	String getintATAProcessId(Integer processId, Integer stageNo, Integer projectId);

	@Query(value="select max(tinStageNo) FROM t_set_authority where intProcessId=:processId and bitDeletedFlag=0",nativeQuery = true)
	Integer getMaxStageNo(Integer processId);

	@Query(value="select TS.intATAProcessId,TS.tinCalcStatus,TS.jsnCalcDetails,TS.intPrimaryAuth FROM t_set_authority as TS where intProcessId=:processId and bitDeletedFlag=0 and tinStageNo=:stageNo " ,nativeQuery = true)
	List<Object[]> authorityResult(Integer processId,Integer stageNo);
	
	@Query(value="select TS.intATAProcessId,TS.tinCalcStatus,TS.jsnCalcDetails,TS.intPrimaryAuth FROM t_set_authority as TS where intProcessId=:processId and bitDeletedFlag=0 and tinStageNo=:stageNo and vchDynFilter=:dynFilterValue and vchDynFilterCtrlId=:dynFilterCtrlId " ,nativeQuery = true)
	List<Object[]> authorityResultComponentBasis(Integer processId,Integer stageNo,String dynFilterValue,String dynFilterCtrlId);
	
	@Query(value=" SELECT  LISTAGG(intPrimaryAuth,',') as FORWARD_TO_AUTH FROM t_set_authority where intProcessId =:processId and intProjectId =:projectId and tinStageNo =:stageNo and bitDeletedFlag = 0  ",nativeQuery = true)
	String getIntProcessIdAndTinStageNoAndIntProjectIdOracle(Integer processId, Integer stageNo, Integer projectId);

	@Query(value=" Select group_concat(MU.vchEmailId) as authEmailId from m_admin_user_master as MU where MU.intRoleId in(Select Distinct(intPrimaryAuth) from t_set_authority where intProcessId=:intProcessId and bitDeletedFlag=0) and MU.bitDeletedFlag=0",nativeQuery=true)
	String getAllMailsForCc(Integer intProcessId);

	
	@Query(value="select count(*) FROM t_set_authority where intProcessId=:processId and vchDynFilter=:dynFilterValue and vchDynFilterCtrlId=:dynFilterctrlId and bitDeletedFlag=0",nativeQuery = true)
	Integer countFilterDataByCtrlId(Integer processId,String dynFilterValue,String dynFilterctrlId);
	@Query(value="select * from t_set_authority where intProcessId=:intProcessId and intRoleId=:previousAuthority and bitDeletedFlag=0;",nativeQuery = true)
	TSetAuthority getDetailsByProcessId(Integer intProcessId, Integer previousAuthority);

//	@Query(value="SELECT TS.intATAProcessId, \n"
//			+ "       TS.tinCurrentNode, \n"
//			+ "       TS.tinCalcStatus, \n"
//			+ "       TS.jsnCalcDetails,\n"
//			+ "       GROUP_CONCAT(TS.intPrimaryAuth) AS intPrimaryAuth\n"
//			+ "FROM t_set_authority AS TS \n"
//			+ "WHERE TS.tinStageNo = :actualStageNo \n"
//			+ "  AND TS.intProcessId = :processId \n"
//			+ "  AND TS.bitDeletedFlag = 0\n"
//			+ "GROUP BY TS.intATAProcessId, \n"
//			+ "         TS.tinCurrentNode, \n"
//			+ "         TS.tinCalcStatus, \n"
//			+ "         TS.jsnCalcDetails;\n"
//			+ "",nativeQuery = true)
//	List<Map<String, Object>> getAllDatByStageNoAndIntProcessd(@Param("actualStageNo") Integer actualStageNo,@Param("processId") Integer processId);

	
	
	@Query(value="SELECT TS.intATAProcessId, \n"  
			+ "       TS.tinCurrentNode, \n"
			+ "       TS.tinCalcStatus, \n"
			+ "       TS.jsnCalcDetails,\n"
			+ "       GROUP_CONCAT(TS.intPrimaryAuth) AS intPrimaryAuth\n"
			+ "FROM t_set_authority AS TS \n"
			+ "WHERE TS.tinStageNo = :actualStageNo \n"
			+ "  AND TS.intProcessId = :processId \n"
			+ "  AND TS.intLabelId=:intLabelId AND TS.bitDeletedFlag = 0\n"
			+ "GROUP BY TS.intATAProcessId, \n"
			+ "         TS.tinCurrentNode, \n"
			+ "         TS.tinCalcStatus, \n"
			+ "         TS.jsnCalcDetails;\n"
			+ "",nativeQuery = true)
	List<Map<String, Object>> getAllDatByStageNoAndIntProcessd(@Param("actualStageNo") Integer actualStageNo,@Param("processId") Integer processId,@Param("intLabelId") Integer intLabelId);   

	
	@Query(value="SELECT TS.intATAProcessId, \n"  
			+ "       TS.tinCurrentNode, \n"
			+ "       TS.tinCalcStatus, \n"
			+ "       TS.jsnCalcDetails,\n"
			+ "       TS.vchCtrlName,\n"
			+ "       GROUP_CONCAT(TS.intPrimaryAuth) AS intPrimaryAuth\n"
			+ "FROM t_set_authority AS TS \n"
			+ "WHERE TS.tinStageNo = :actualStageNo \n"
			+ "  AND TS.intProcessId = :processId \n"
			+ "  AND TS.vchCtrlName = :conditionAction \n"
			+ "  AND TS.intLabelId=:intLabelId AND TS.bitDeletedFlag = 0\n"
			+ "GROUP BY TS.intATAProcessId, \n"
			+ "         TS.tinCurrentNode, \n"
			+ "         TS.tinCalcStatus, \n"
			+ "         TS.jsnCalcDetails;\n"
			+ "",nativeQuery = true)
	List<Map<String, Object>> getAllDatByStageNoAndIntProcessdBasedOnCondition(@Param("actualStageNo") Integer actualStageNo,@Param("processId") Integer processId,@Param("intLabelId") Integer intLabelId,@Param("conditionAction") String conditionAction);   

	
	
	
	
	@Query("From TSetAuthority where intProcessId=:intId and intPrimaryAuth=:roleId and bitDeletedFlag=0")
	List<TSetAuthority> getfindByOnlineServiceIdAndRoleId(@Param("intId")Integer intId,@Param("roleId") Integer roleId);
	  
    @Query(" From TSetAuthority where intProcessId=:intProcessId And tinStageNo=:tinStageNo And intRoleId=:intRoleId And bitDeletedFlag=0")
    TSetAuthority getByProcessIdAndStageNo(Integer intProcessId,Integer tinStageNo,Integer intRoleId);
	 @Query(value="select vchAuthTypes from t_set_authority where intProcessId=:intProcessId And intRoleId=:intRoleId And tinStageNo=:tinStageNo And bitDeletedFlag=0 And intLabelId=:labelId",nativeQuery = true)
	String getVchAuthsByProcessIdAndRoleId(Integer intProcessId, Integer intRoleId,Integer tinStageNo,Integer labelId );
	
	 @Query(value="select vchAuthTypes from t_set_authority where intProcessId=:intProcessId And intRoleId=:intRoleId  And bitDeletedFlag=0 limit 1",nativeQuery = true)
		String getVchAuthsByProcessIdAndRoleIdup(Integer intProcessId, Integer intRoleId );
	



}
