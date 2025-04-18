package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Mrole;
import app.ewarehouse.entity.Tmenulinks;
@Repository
public interface TmenulinksRepository extends JpaRepository<Tmenulinks, Integer> {
	Tmenulinks findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

	@Query("From Tmenulinks where bitDeletedFlag=:bitDeletedFlag")
	List<Tmenulinks> findAllByBitDeletedFlag(Boolean bitDeletedFlag);
	
	
	
	String searchLink="""
			SELECT menuLinks FROM Tmenulinks menuLinks WHERE bitDeletedFlag=:bitDeletedFlag AND menuLinks.selLinkType=:search
			""";
	@Query(searchLink)
	List<Tmenulinks> filterByLinksById(@Param("search") Integer search,@Param("bitDeletedFlag")Boolean bitDeletedFlag);


	
	
	@Query("From Tmenulinks where bitDeletedFlag=:bitDeletedFlag order by txtSerialNo")
	List<Tmenulinks> findAllByBitDeletedFlagOrderedBySlNo(boolean bitDeletedFlag);

	@Query("From Tmenulinks where selParentLink=0 AND bitDeletedFlag=:bitDeletedFlag  AND intApplicableFor=1")
	List<Tmenulinks> findModuleByIntParentId(Boolean bitDeletedFlag);

	@Query("From Tmenulinks where selParentLink=:intId AND bitDeletedFlag=:bitDeletedFlag")
	List<Tmenulinks> findByIntParentLinkIdAndIntId(Integer intId, Boolean bitDeletedFlag);

	@Query(value = "SELECT TM.intId, TM.intLinkType, TM.vchLinkName, TM.intParentLinkId, TM.vchUrl,TM.vchViewUrl,(SELECT l.vchLinkName\n"
			+ "     FROM m_admin_menulinks AS l\n"
			+ "     WHERE l.intId = TM.intParentLinkId AND l.bitDeletedFlag = 0\n"
			+ "     LIMIT 1) AS moduleName FROM m_admin_menulinks AS TM WHERE TM.bitDeletedFlag = 0 AND TM.intParentLinkId > 0\n"
			+ "    AND TM.intApplicableFor <= 1 ORDER BY \r\n"
			+ "    TM.intslNo;\n", nativeQuery = true)
	List<Object[]> getAllDataSupAd();

	@Query(value = "SELECT TM.intId, TM.vchLinkName, TM.vchUrl,TM.vchViewUrl , TM.intLinkType, TM.intParentLinkId,TR.intId trIntId, TR.vchLinkId, TR.intRole,\n"
			+ "           (SELECT l.vchLinkName FROM m_admin_menulinks l WHERE l.intId = TM.intParentLinkId AND l.bitDeletedFlag = 0 LIMIT 1) AS moduleName,TR.tinManageRight,TR.tinEditRight,TR.tinPublishRight,TR.tinAddRight,TR.tinDeleteRight,TR.tinAllRight \n"
			+ "             FROM m_admin_menulinks TM \n"
			+ "             JOIN m_admin_rolepermission TR ON TR.vchLinkId = TM.intId \n"
			+ "            WHERE TM.bitDeletedFlag = 0 AND TM.intApplicableFor <= 1 AND TR.bitDeletedFlag = 0 AND TR.intRole =:roleId ORDER BY \r\n"
			+ "    TM.intslNo", nativeQuery = true)
	List<Object[]> getDataFromLinksandPermission(@Param("roleId") Integer roleId);

	List<Tmenulinks> findAllByBitDeletedFlagAndIntApplicableForAndSelLinkTypeOrderByDtmCreatedOnDesc(
			Boolean bitDeletedFlag, Integer intApplicableFor, Integer intLinkType);

	@Query(value = "SELECT TM.intId, TM.vchLinkName, TM.vchUrl,TM.vchViewUrl , TM.intLinkType, TM.intParentLinkId,TR.intId trIntId, TR.vchLinkId, TR.intRole,\n"
			+ "           (SELECT l.vchLinkName FROM m_admin_menulinks l WHERE l.intId = TM.intParentLinkId AND l.bitDeletedFlag = 0 LIMIT 1) AS moduleName,TR.tinManageRight,TR.tinEditRight,TR.tinPublishRight,TR.tinAddRight,TR.tinDeleteRight,TR.tinAllRight \n"
			+ "             FROM m_admin_menulinks TM \n"
			+ "             JOIN m_admin_rolepermission TR ON TR.vchLinkId = TM.intId \n"
			+ "            WHERE TM.bitDeletedFlag = 0 AND TM.intApplicableFor <= 1 AND TR.bitDeletedFlag = 0 AND TR.intUserId =:userId ORDER BY \r\n"
			+ "    TM.intslNo", nativeQuery = true)
	List<Object[]> getDataListByUserId(@Param("userId") Integer userId);

	@Query("Select txtLinkName From Tmenulinks Where intId=:intProcessId AND bitDeletedFlag =false ")
	String getVchLinkNameByUsingintProcessId(@Param("intProcessId") Integer intProcessId);

	@Query("From Tmenulinks Where selParentLink != 0 AND bitDeletedFlag = false And intForApproval=1 order by txtLinkName ASC")
	List<Tmenulinks> getAllFormData();

	
	String IconEncFileNameQuery="""
					SELECT vchIconEnc,vchIconFileType FROM m_admin_menulinks where intParentLinkId=0 and
					intLinkType=1 and vchLinkName=:vchLinkName
			""";
	
	@Query(value =IconEncFileNameQuery , nativeQuery = true)
	List<Object[]> getIconEncString(@Param("vchLinkName") String vchLinkName);

	 Tmenulinks findByTxtLinkNameAndSelParentLink(String txtLinkName, Integer selParentLink);
	
}
