package app.ewarehouse.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.tika.utils.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.InspectorListDto;
import app.ewarehouse.dto.UserDataResponseDto;
import app.ewarehouse.dto.UserDetailsResponseDto;
import app.ewarehouse.entity.County;
import app.ewarehouse.entity.Mdepartment;
import app.ewarehouse.entity.Mdesignation;
import app.ewarehouse.entity.Memployeetype;
import app.ewarehouse.entity.Mgroups;
import app.ewarehouse.entity.Mrole;
import app.ewarehouse.entity.SubCounty;
import app.ewarehouse.entity.TempUser;
import app.ewarehouse.entity.Tmenulinks;
import app.ewarehouse.entity.Trolepermission;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.master.entity.WardMaster;
import app.ewarehouse.master.repository.WardMasterRepo;
import app.ewarehouse.repository.CountyRepository;
import app.ewarehouse.repository.MdepartmentRepository;
import app.ewarehouse.repository.MdesignationRepository;
import app.ewarehouse.repository.MemployeetypeRepository;
import app.ewarehouse.repository.MgroupsRepository;
import app.ewarehouse.repository.MroleRepository;
import app.ewarehouse.repository.SubCountyRepository;
import app.ewarehouse.repository.TempUserRepository;
import app.ewarehouse.repository.TmenulinksRepository;
import app.ewarehouse.repository.TrolepermissionRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.LogInService;
import app.ewarehouse.service.TuserService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.EmailUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;

@Service
public class TuserServiceImpl implements TuserService {
	private static final String MESSAGE = "message";

	private static final Logger logger = LoggerFactory.getLogger(TuserServiceImpl.class);

	private TuserRepository tuserRepository;
	EntityManager entityManager;

	private TempUserRepository tempUserRepo;

	LogInService service;

	private ObjectMapper om;
	@Autowired
	WardMasterRepo wradrepo;
	@Autowired
	SubCountyRepository subCountyRepo;
	@Autowired
	CountyRepository countyrepo;
	@Autowired
	private TrolepermissionRepository trolepermissionRepository;
	@Autowired 
	private TmenulinksRepository tmenulinksRepository;
	
	@Autowired
	private MdepartmentRepository mdepartmentRepository;
	
	@Autowired
	private MroleRepository mroleRepository;
	
	@Autowired
	private MdesignationRepository mdesignationRepository;
	
	@Autowired
	private MgroupsRepository mgroupsRepository;
	
	@Autowired
	private MemployeetypeRepository memployeetypeRepository;
	
	@Autowired
	private CountyRepository countyRepository;
	
	@Autowired
	private SubCountyRepository subCountyRepository;
	
	@Autowired
	private WardMasterRepo wardMasterrepository;
	
	@PersistenceContext
	private EntityManager em;
	

	public TuserServiceImpl(TuserRepository tuserRepository, EntityManager entityManager,
			TempUserRepository tempUserRepo, LogInService service, ObjectMapper om) {
		super();
		this.tuserRepository = tuserRepository;
		this.entityManager = entityManager;
		this.tempUserRepo = tempUserRepo;
		this.service = service;
		this.om = om;
	}

	JSONObject selGender = new JSONObject("{1:Male,2:Female}");
	JSONObject chkPrevilege = new JSONObject("{2:Admin}");

	Integer parentId = 0;
	Object dynamicValue = null;

	@Override
	public JSONObject save(String data) {
		JSONObject json = new JSONObject();
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
			om.setDateFormat(df);
			Tuser tuser = om.readValue(data, Tuser.class);

			List<String> fileUploadList = new ArrayList<String>();
			fileUploadList.add(tuser.getFilePhoto());
			if (!Objects.isNull(tuser.getIntId()) && tuser.getIntId() > 0) {
				Tuser getEntity = tuserRepository.findByIntIdAndBitDeletedFlag(tuser.getIntId(), false);
				getEntity.setTxtFullName(tuser.getTxtFullName());
				getEntity.setSelGender(tuser.getSelGender());
				getEntity.setFilePhoto(tuser.getFilePhoto());
				getEntity.setTxtMobileNo(tuser.getTxtMobileNo());
				getEntity.setTxtEmailId(tuser.getTxtEmailId());
				getEntity.setTxtAlternateMobileNumber(tuser.getTxtAlternateMobileNumber());
				getEntity.setTxtDateOfJoining(tuser.getTxtDateOfJoining());
				getEntity.setTxtrAddress(tuser.getTxtrAddress());
				getEntity.setSelRole(tuser.getSelRole());
				getEntity.setSelDesignation(tuser.getSelDesignation());
				getEntity.setSelEmployeeType(tuser.getSelEmployeeType());
				getEntity.setSelDepartment(tuser.getSelDepartment());
				getEntity.setSelGroup(tuser.getSelGroup());
				getEntity.setSelCounty(tuser.getSelCounty());
				getEntity.setSelSubCounty(tuser.getSelSubCounty());
				getEntity.setSelWard(tuser.getSelWard());
				getEntity.setWarehouseId(tuser.getWarehouseId());
				getEntity.setSelHierarchy(tuser.getSelHierarchy());
				getEntity.setTxtUserId(tuser.getTxtUserId());
				getEntity.setSelIcmRoleId(tuser.getSelIcmRoleId());
				getEntity.setSelCLCRoleId(tuser.getSelCLCRoleId());
				getEntity.setSelWRSCRoleId(tuser.getSelWRSCRoleId());
				// getEntity.setTxtPassword(tuser.getEnPassword());
				getEntity.setIntReportingAuth(tuser.getIntReportingAuth());
				getEntity.setChkPrevilege(tuser.getChkPrevilege());
				Tuser updateData = tuserRepository.save(getEntity);
				parentId = updateData.getIntId();
				

				
				json.put("status", 202);
			} else {
				Tuser user = tuserRepository.getByUserId(tuser.getTxtUserId(), false);
				if (user == null) {
					tuser.setTxtFullName(tuser.getTxtFullName().trim());
					tuser.setTxtEmailId(tuser.getTxtEmailId().trim());
//					tuser.setTxtPassword(tuser.getTxtPassword().trim());
					tuser.setTxtPassword(tuser.getEnPassword());
					Tuser saveData = tuserRepository.save(tuser);
					parentId = saveData.getIntId();
					
					Tmenulinks tmenu = tmenulinksRepository.findByTxtLinkNameAndSelParentLink("Register Complaint", 243);
					 
					 Trolepermission tRolePermission=new Trolepermission();
						tRolePermission.setSelPermissionFor(1);
						tRolePermission.setSelSelectRole(tuser.getSelRole());
						tRolePermission.setSelSelectUser(saveData.getIntId());
						tRolePermission.setVchLinkId(tmenu.getIntId().toString());
						tRolePermission.setIntadd(1);
						tRolePermission.setIntEditRight(1);
						tRolePermission.setIntViewManageRight(1);
						tRolePermission.setIntDelete(1);
						tRolePermission.setPublish(1);
						tRolePermission.setIntall(1);
					//	trolepermissionRepository.save(tRolePermission);
					
					json.put("status", 200);
				} else {
					json.put("status", 401);
				}
			}
			for (String fileUpload : fileUploadList) {
				if (!"".equals(fileUpload)) {
					File f = new File("src/storage/tempfile/" + fileUpload);
					if (f.exists()) {
						File src = new File("src/storage/tempfile/" + fileUpload);
						File dest = new File("src/storage/manage-users/" + fileUpload);
						try {
							Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
							Files.delete(src.toPath());
						} catch (IOException e) {
							System.out.println("Iniside Error");
						}
					}
				}
			}
			json.put("id", parentId);
		} catch (Exception e) {
			logger.error("TuserServiceImpl:save()", e);
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		Tuser entity = tuserRepository.findByIntIdAndBitDeletedFlag(id, false);
		dynamicValue = (selGender.has(entity.getSelGender().toString()))
				? selGender.get(entity.getSelGender().toString())
				: "--";
		entity.setSelGenderVal(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select vchRoleName from m_admin_role where intId=" + entity.getSelRole());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelRoleVal(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select vchDesgName from m_admin_designation where intId=" + entity.getSelDesignation());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelDesignationVal(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select vchEmployeeType from m_admin_employee_type where intId=" + entity.getSelEmployeeType());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelEmployeeTypeVal(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select vchdeptName from m_admin_department where intId=" + entity.getSelDepartment());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelDepartmentVal(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select vchGroupName from m_admin_groups where intId=" + entity.getSelGroup());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelGroupVal(dynamicValue.toString());

		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select county_name from t_county_master where county_id=" + entity.getSelCounty());
		} catch (Exception ex) {
			dynamicValue = "--";
		}

		entity.setSelCountyVal(dynamicValue.toString());

		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select vchSubCountyName from t_sub_county where intId=" + entity.getSelSubCounty());
		} catch (Exception ex) {
			dynamicValue = "--";
		}

		entity.setSelSubCountyVal(dynamicValue.toString());

		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select wardName from m_master_ward where intWardMasterId=" + entity.getSelWard());
		} catch (Exception ex) {
			dynamicValue = "--";
		}

		entity.setSelWardVal(dynamicValue.toString());

		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select county_name from t_county_master where county_id" + entity.getSelHierarchy());
		} catch (Exception ex) {
			dynamicValue = "--";
		}

		entity.setSelHierarchyVal(dynamicValue.toString());

		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select vchFullName from m_admin_user where intId=" + entity.getIntReportingAuth());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setIntReportingAuthVal(dynamicValue.toString());

		dynamicValue = (chkPrevilege.has(entity.getChkPrevilege().toString()))
				? chkPrevilege.get(entity.getChkPrevilege().toString())
				: "--";
		entity.setChkPrevilegeVal(dynamicValue.toString());

		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		JSONObject jsonData = new JSONObject(formParams);
		List<Tuser> tuserResp = null;
		String warehouseId = null;
		String viewPage = null;

		if (jsonData.has("warehouseId")) {
			warehouseId = jsonData.get("warehouseId").toString();
		}
		if (jsonData.has("viewPage")) {
			viewPage = jsonData.get("viewPage").toString();
		}

		if (!StringUtils.isBlank(warehouseId) && !"0".equals(warehouseId) && "view_warehouse_users".equals(viewPage)) {
			tuserResp = tuserRepository.findAllByWareHouseIdAndBitDeletedFlag(warehouseId, false);
		} else if (!StringUtils.isBlank(warehouseId) && "0".equals(warehouseId)
				&& "view_warehouse_users".equals(viewPage)) {
			tuserResp = tuserRepository.findAllWhUserByBitDeletedFlag(false);
		} else {
			tuserResp = tuserRepository.findAllByBitDeletedFlag(false);
		}

		Set<Integer> roleIds = new HashSet<>();
		Set<Integer> designationIds = new HashSet<>();
		Set<Integer> employeeTypeIds = new HashSet<>();
		Set<Integer> departmentIds = new HashSet<>();
		Set<Integer> groupIds = new HashSet<>();
		Set<Integer> hierarchyIds = new HashSet<>();
		Set<Integer> countyIds = new HashSet<>();
		Set<Integer> subCountyIds = new HashSet<>();
		Set<Integer> wardIds = new HashSet<>();
		Set<Integer> reportingAuthIds = new HashSet<>();
		Set<String> previlegeIds = new HashSet<>();

		for (Tuser entity : tuserResp) {
			roleIds.add(entity.getSelRole());
			designationIds.add(entity.getSelDesignation());
			employeeTypeIds.add(entity.getSelEmployeeType());
			departmentIds.add(entity.getSelDepartment());
			groupIds.add(entity.getSelGroup());
			hierarchyIds.add(entity.getSelHierarchy());
			countyIds.add(entity.getSelCounty());
			subCountyIds.add(entity.getSelSubCounty());
			wardIds.add(entity.getSelWard());
			reportingAuthIds.add(entity.getIntReportingAuth());
			previlegeIds.add(entity.getChkPrevilege().toString());
		}

		Map<Integer, String> roleMap = fetchRoles(roleIds);
		Map<Integer, String> designationMap = fetchDesignations(designationIds);
		Map<Integer, String> employeeTypeMap = fetchEmployeeTypes(employeeTypeIds);
		Map<Integer, String> departmentMap = fetchDepartments(departmentIds);
		Map<Integer, String> groupMap = fetchGroups(groupIds);
		Map<Integer, String> hierarchyMap = fetchHierarchies(hierarchyIds);
		Map<Integer, String> countyMap = fetchCounties(countyIds);
		Map<Integer, String> subCountyMap = fetchSubCounties(subCountyIds);
		Map<Integer, String> wardMap = fetchWards(wardIds);
		Map<Integer, String> reportingAuthMap = fetchReportingAuth(reportingAuthIds);

		for (Tuser entity : tuserResp) {
			entity.setSelGenderVal(selGender.has(entity.getSelGender().toString())
					? (String) selGender.get(entity.getSelGender().toString())
					: "--");
			entity.setSelRoleVal(roleMap.getOrDefault(entity.getSelRole(), "--"));
			entity.setSelDesignationVal(designationMap.getOrDefault(entity.getSelDesignation(), "--"));
			entity.setSelEmployeeTypeVal(employeeTypeMap.getOrDefault(entity.getSelEmployeeType(), "--"));
			entity.setSelDepartmentVal(departmentMap.getOrDefault(entity.getSelDepartment(), "--"));
			entity.setSelGroupVal(groupMap.getOrDefault(entity.getSelGroup(), "--"));
			entity.setSelHierarchyVal(hierarchyMap.getOrDefault(entity.getSelHierarchy(), "--"));
			entity.setSelCountyVal(countyMap.getOrDefault(entity.getSelCounty(), "--"));
			entity.setSelSubCountyVal(subCountyMap.getOrDefault(entity.getSelSubCounty(), "--"));
			entity.setSelWardVal(wardMap.getOrDefault(entity.getSelWard(), "--"));
			entity.setIntReportingAuthVal(reportingAuthMap.getOrDefault(entity.getIntReportingAuth(), "--"));
			dynamicValue = (chkPrevilege.has(entity.getChkPrevilege().toString()))
					? chkPrevilege.get(entity.getChkPrevilege().toString())
					: "--";
			entity.setChkPrevilegeVal(dynamicValue.toString());
		}

		return new JSONArray(tuserResp);
	}

	private Map<Integer, String> fetchRoles(Set<Integer> roleIds) {
		String stringRoleIds = roleIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String query = "SELECT intId, vchRoleName FROM m_admin_role WHERE intId IN (" + stringRoleIds + ")";

		List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);
		Map<Integer, String> roleMap = new HashMap<>();
		Integer roleId = 0;
		String roleName = null;
		for (Object[] result : results) {
			roleId = (Integer) result[0];
			roleName = (String) result[1];
			roleMap.put(roleId, roleName);
		}
		return roleMap;
	}

	private Map<Integer, String> fetchDesignations(Set<Integer> designationIds) {
		String stringDesgIds = designationIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String query = "SELECT intId, vchDesgName FROM m_admin_designation WHERE intId IN(" + stringDesgIds + ")";
		List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);
		Map<Integer, String> designationMap = new HashMap<>();
		Integer desgnId = 0;
		String desgName = null;
		for (Object[] result : results) {
			desgnId = (Integer) result[0];
			desgName = (String) result[1];
			designationMap.put(desgnId, desgName);
		}
		return designationMap;
	}

	private Map<Integer, String> fetchEmployeeTypes(Set<Integer> employeeTypeIds) {

		String stringEmpIds = employeeTypeIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String query = "SELECT intId, vchEmployeeType FROM m_admin_employee_type WHERE intId IN (" + stringEmpIds + ")";
		List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);
		Map<Integer, String> employeeTypeMap = new HashMap<>();
		Integer empId = 0;
		String empName = null;
		for (Object[] result : results) {
			empId = (Integer) result[0];
			empName = (String) result[1];
			employeeTypeMap.put(empId, empName);
		}
		return employeeTypeMap;
	}

	private Map<Integer, String> fetchDepartments(Set<Integer> departmentIds) {
		String stringDeptIds = departmentIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String query = "SELECT intId, vchdeptName FROM m_admin_department WHERE intId IN (" + stringDeptIds + ")";
		List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);
		Map<Integer, String> departmentMap = new HashMap<>();
		Integer deptId = 0;
		String deptName = null;
		for (Object[] result : results) {
			deptId = (Integer) result[0];
			deptName = (String) result[1];
			departmentMap.put(deptId, deptName);
		}
		return departmentMap;
	}

	private Map<Integer, String> fetchGroups(Set<Integer> groupIds) {
		String stringGroupIds = groupIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String query = "SELECT intId, vchGroupName FROM m_admin_groups WHERE intId IN (" + stringGroupIds + ") ";
		List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);
		Map<Integer, String> groupMap = new HashMap<>();
		Integer groupId = 0;
		String groupName = null;
		for (Object[] result : results) {
			groupId = (Integer) result[0];
			groupName = (String) result[1];
			groupMap.put(groupId, groupName);
		}
		return groupMap;
	}

	private Map<Integer, String> fetchHierarchies(Set<Integer> hierarchyIds) {
		String stringHierarchyIds = hierarchyIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String query = "SELECT intStateId, vchStateName FROM m_states WHERE intStateId IN (" + stringHierarchyIds
				+ ") ";
		List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);
		Map<Integer, String> hierarchyMap = new HashMap<>();
		Integer stateId = 0;
		String stateName = null;
		for (Object[] result : results) {
			stateId = (Integer) result[0];
			stateName = (String) result[1];
			hierarchyMap.put(stateId, stateName);
		}
		return hierarchyMap;
	}

	private Map<Integer, String> fetchReportingAuth(Set<Integer> reportingAuthIds) {
		String stringReportingIds = reportingAuthIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String query = "SELECT intId, vchFullName FROM m_admin_user WHERE intId IN (" + stringReportingIds + ") ";
		List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);
		Map<Integer, String> reportingAuthMap = new HashMap<>();
		Integer userId = 0;
		String userName = null;
		for (Object[] result : results) {
			userId = (Integer) result[0];
			userName = (String) result[1];
			reportingAuthMap.put(userId, userName);
		}
		return reportingAuthMap;
	}

	private Map<Integer, String> fetchCounties(Set<Integer> countyIds) {
		String stringCountyIds = countyIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String query = "select county_id,county_name from t_county_master where county_id IN (" + stringCountyIds
				+ ") ";
		List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);
		Map<Integer, String> countyMap = new HashMap<>();
		Integer userId = 0;
		String userName = null;
		for (Object[] result : results) {
			userId = (Integer) result[0];
			userName = (String) result[1];
			countyMap.put(userId, userName);
		}
		return countyMap;
	}

	private Map<Integer, String> fetchSubCounties(Set<Integer> subCountyIds) {
		String stringSubCountyIds = subCountyIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String query = "select intId,vchSubCountyName from t_sub_county where intId IN (" + stringSubCountyIds + ") ";
		List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);
		Map<Integer, String> subCountyMap = new HashMap<>();
		Integer userId = 0;
		String userName = null;
		for (Object[] result : results) {
			userId = (Integer) result[0];
			userName = (String) result[1];
			subCountyMap.put(userId, userName);
		}
		return subCountyMap;
	}

	private Map<Integer, String> fetchWards(Set<Integer> wardIds) {
		String stringWardIds = wardIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String query = "select intWardMasterId,wardName from m_master_ward where intWardMasterId IN (" + stringWardIds + ") ";
		List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);
		Map<Integer, String> wardMap = new HashMap<>();
		Integer userId = 0;
		String userName = null;
		for (Object[] result : results) {
			userId = (Integer) result[0];
			userName = (String) result[1];
			wardMap.put(userId, userName);
		}
		return wardMap;
	}

	@Override
	public JSONObject deleteById(Integer id) {
		JSONObject json = new JSONObject();
		try {
			Tuser entity = tuserRepository.findByIntIdAndBitDeletedFlag(id, false);
			entity.setBitDeletedFlag(true);
			tuserRepository.save(entity);
			json.put("status", 200);
		} catch (Exception e) {
			logger.error("TuserServiceImpl:deleteById()", e);
			json.put("status", 400);
		}
		return json;
	}

	public static JSONArray fillselRoleList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select intId,vchRoleName from m_admin_role  where bitDeletedFlag=0";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("intId", data[0]);
			jsonObj.put("vchRoleName", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselDesignationList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select intId,vchDesgName from m_admin_designation  where bitDeletedFlag=0";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("intId", data[0]);
			jsonObj.put("vchDesgName", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselEmployeeTypeList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select intId,vchEmployeeType from m_admin_employee_type where bitDeletedFlag=0";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("intId", data[0]);
			jsonObj.put("vchEmployeeType", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselDepartmentList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select intId,vchdeptName from m_admin_department  where bitDeletedFlag=0";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("intId", data[0]);
			jsonObj.put("vchdeptName", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselGroupList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select intId,vchGroupName from m_admin_groups where bitDeletedFlag=0";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("intId", data[0]);
			jsonObj.put("vchGroupName", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillUserList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select intId,vchFullName from m_admin_user  where bitDeletedFlag=0 ";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("intId", data[0]);
			jsonObj.put("vchFullName", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselHierarchyList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select intStateId,vchStateName from m_states";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("intStateId", data[0]);
			jsonObj.put("vchStateName", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	@Override
	public JSONArray findUserList(String data) {
		JSONObject json = new JSONObject(data);
		JSONArray mainJSONFile = new JSONArray();
		Integer selDepartment = json.getInt("intDepartment");
		Integer selRole = json.getInt("intRoleId");
		Integer selDesignation = json.getInt("intDesignantion");
		List<Tuser> userList = tuserRepository.findBySelDepartmentAndSelRoleAndSelDesignationAndBitDeletedFlag(
				selDepartment, selRole, selDesignation, false);
		for (Tuser users : userList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("intId", users.getIntId());
			jsonObj.put("vchFullName", users.getTxtFullName());
			mainJSONFile.put(jsonObj);
		}

		return mainJSONFile;
	}

	@Override
	public List<Tuser> findByMobileOrEmail(String mobile, String email) {
		return tuserRepository.getByTxtEmailId(email);
	}

	@Override
	public UserDataResponseDto getUserDataByEmailId(String email) {
		TempUser tempEntity = tempUserRepo.findByEmail(email);
		if (tempEntity != null) {
			UserDataResponseDto dto = new UserDataResponseDto();
			dto.setApplicantName(tempEntity.getApplicantName());
			dto.setMobileNo(tempEntity.getMobile());
			dto.setEmail(tempEntity.getEmail());
			dto.setPostalAddress(tempEntity.getPostalAddress());
			dto.setPostalCode(tempEntity.getPostalCode());
			dto.setTown(tempEntity.getTown());
			return dto;
		}
		return null;
	}

	@Override
	public List<InspectorListDto> getInspectors() {
		List<Tuple> list = tuserRepository.getInspectors();
		List<InspectorListDto> result = new ArrayList<>();
		for (Tuple tuple : list) {
			InspectorListDto dto = new InspectorListDto();
			dto.setId((Integer) tuple.get("intId")); 
			dto.setName((String) tuple.get("vchFullName"));
			dto.setRoleId((Integer) tuple.get("intRoleId"));
			result.add(dto);
		}
		/*
		 * List<Tuple> list1 = tuserRepository.getCECMInspectors(); for (Tuple tuple :
		 * list1) { InspectorListDto dto = new InspectorListDto(); dto.setId((Integer)
		 * tuple.get("intId")); dto.setName((String) tuple.get("vchFullName"));
		 * result.add(dto); }
		 */
		return result;
	}

	@Override
	public List<InspectorListDto> getInspectorsByComplaintType(Integer complaintType) {
//		if(complaintType == 8 || complaintType == 9 || complaintType == 10 || complaintType == 11) {
//			List<Tuple> list = tuserRepository.getCECMInspectors();
//			List<InspectorListDto> result = new ArrayList<>();
//			for(Tuple tuple : list) {
//				InspectorListDto dto = new InspectorListDto();
//				dto.setId((Integer)tuple.get("intId"));
//				dto.setName((String)tuple.get("vchFullName"));
//				result.add(dto);
//			}
//			return result;
//		}else {
		List<Tuple> list = tuserRepository.getInspectors();
		List<InspectorListDto> result = new ArrayList<>();
		for (Tuple tuple : list) {
			InspectorListDto dto = new InspectorListDto();
			dto.setId((Integer) tuple.get("intId"));
			dto.setName((String) tuple.get("vchFullName"));
			result.add(dto);
		}
		return result;
//		}
	}

	@Override
	public List<InspectorListDto> getCollateral() {
		List<Tuple> list = tuserRepository.getCollateral();
		List<InspectorListDto> result = new ArrayList<>();
		for (Tuple tuple : list) {
			InspectorListDto dto = new InspectorListDto();
			dto.setId((Integer) tuple.get("intId"));
			dto.setName((String) tuple.get("vchFullName"));
			result.add(dto);
		}
		return result;
	}

	@Override
	public List<InspectorListDto> getWareHouseWorker(String vchWareHouse, Integer roleId) {

	//	List<Tuple> list = tuserRepository.getWareHouseWorkerByRole(roleId);

		List<Tuple> list = tuserRepository.getWareHouseEmpByRole(roleId);

		List<InspectorListDto> result = new ArrayList<>();
		for (Tuple tuple : list) {
			InspectorListDto dto = new InspectorListDto();
			dto.setId((Integer) tuple.get("intId"));
			dto.setName((String) tuple.get("vchFullName"));
			dto.setWareHouseId((String) tuple.get("vchWarehouse"));
			result.add(dto);
		}
		return result;
	}

	@Override
	public UserDetailsResponseDto getUserDetails(Integer userId) {
		
		UserDetailsResponseDto  userDto=null;
		try {
			
			userDto = tuserRepository.findById(userId).map(user -> {
				UserDetailsResponseDto dto = new UserDetailsResponseDto();
				dto.setUid(user.getIntId());
				dto.setFullName(user.getTxtFullName());
				dto.setContactNumber(user.getTxtMobileNo());
				dto.setEmail(user.getTxtEmailId());
				dto.setAddress(user.getTxtrAddress());
				County county = countyrepo.findByIdAndBitDeletedFlag(user.getSelCounty(), false);
				SubCounty subcouty = subCountyRepo.findByIntIdAndBitDeletedFlag(user.getSelSubCounty(), false).get();
				WardMaster wared_master = wradrepo.findByIntWardMasterIdAndBitDeletedFlag(user.getSelWard(), false);
				dto.setCountyId(county.getId().toString());
				dto.setCountyName(county.getName());
				dto.setSubCountyId(subcouty.getIntId().toString());
				dto.setSubCountyName(subcouty.getTxtSubCountyName());
				dto.setWardId(wared_master.getIntWardMasterId().toString());
				dto.setWardName(wared_master.getWardName());
				return dto;
			}).orElse(null);
		}
		catch(Exception e) {
			logger.error("TuserServiceImpl:updateUserStatus()", e);
		}
		return userDto;
	}

	@Override
	public JSONObject changePassword(String s) {

		JSONObject json = new JSONObject(s);

		JSONObject response = new JSONObject();
		Tuser mUser = tuserRepository.findByIntIdAndBitDeletedFlag(json.getInt("userId"), false);

		if (mUser.getTxtPassword().equals(json.getString("oldPasswordHash"))) {

			if (!mUser.getTxtPassword().equals(json.getString("newPasswordHash"))) {

				mUser.setTxtPassword(json.getString("newPasswordHash"));
				tuserRepository.save(mUser);
				response.put("status", 200);
				response.put("msg", "success");

			} else {

				response.put("status", 404);
				response.put("msg", "New Password Should not be Same As Current Password");
			}

		} else {

			response.put("status", 400);
			response.put("msg", "Current Password not Matched");

		}

		return response;

	}

	@Override
	public JSONObject updateUserStatus(Integer id) {
		JSONObject json = new JSONObject();
		try {
			tuserRepository.changeUserStatus(id);
			Tuser data = tuserRepository.findById(id).orElse(null);
			if (data != null) {
				json.put("status", 200);
				json.put("userStatus", data.getBitIsTwoFactorAuthEnabled());
			} else {
				json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			json.put(MESSAGE, e);
			logger.error("TuserServiceImpl:updateUserStatus()", e);
		}
		return json;
	}

	@Override
	public void updateBlockStatus(Integer userId, boolean isBlocked) {
		Optional<Tuser> userOptional = tuserRepository.findById(userId);
		if (userOptional.isPresent()) {
			Tuser user = userOptional.get();
			user.setLocked(isBlocked);
			tuserRepository.save(user);
		} else {
			throw new RuntimeException("User not found");
		}
	}

	@Override
	@Transactional
	public JSONObject saveActLogEnableDisable(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			JSONObject jsObj = new JSONObject(decodedData);

			int rowsUpdated = tuserRepository.updateActivityLog(jsObj.getBoolean("activityLogStatus"));
			int ssActionStatusUpdate = tuserRepository.updateActionStatus(jsObj.getBoolean("activityLogStatus"));
			if (rowsUpdated > 0 && ssActionStatusUpdate > 0) {
				json.put("status", 200);
				json.put(MESSAGE, "Activity log status updated successfully.");
			} else {
				json.put("status", 404);
				json.put(MESSAGE, "No rows were updated.");
			}
		} catch (Exception e) {
			logger.error("TuserServiceImpl:saveActLogEnableDisable()", e);
			json.put("status", 500);
			json.put(MESSAGE, "Error updating activity log status.");
		}

		return json;
	}

	@Override
	@Transactional
	public JSONObject saveIsTwoFactorAuthEnabled(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			JSONObject jsObj = new JSONObject(decodedData);
			System.err.println(jsObj.getBoolean("twoFactorAuthenticationStatus"));
			int rowsUpdated = tuserRepository
					.updateTwoFactorAuthEnabled(jsObj.getBoolean("twoFactorAuthenticationStatus"));
			if (rowsUpdated > 0) {
				json.put("status", 200);
				json.put(MESSAGE, "TwoFactor Authentication status updated successfully.");
			} else {
				json.put("status", 404);
				json.put(MESSAGE, "No rows were updated.");
			}
		} catch (Exception e) {
			logger.error("TuserServiceImpl:saveIsTwoFactorAuthEnabled()", e);
			json.put("status", 500);
			json.put(MESSAGE, "Error updating activity log status.");
		}

		return json;
	}

	@Override
	public JSONObject getActivityLogStatus() {

		JSONObject jsObject = new JSONObject();

		Boolean actionStatus = tuserRepository.getActionStatus();
		jsObject.put("actionStatus", actionStatus);
		return jsObject;
	}

	@Override
	public String getLastChangePswd() {
		Date lastChangedPswd = tuserRepository.getLastChangedPswd();
		if (lastChangedPswd != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
			return formatter.format(lastChangedPswd);
		}
		return "N/A";
	}

	@Override
	public JSONObject updateActivityLogStatusByUser(Integer id) {
		JSONObject json = new JSONObject();
		try {
			tuserRepository.updateActivityLogStatusByUser(id);
			Tuser data = tuserRepository.findById(id).orElse(null);
			if (data != null) {
				json.put("status", 200);
				json.put("activityLogUserStatus", data.getBitIsSaveActivityLogEnabled());
			} else {
				json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			json.put(MESSAGE, e);
			logger.error("TuserServiceImpl:updateActivityLogStatusByUser()", e);
		}
		return json;
	}

	@Override
	public Integer findRelatedIdById(String str) {
		return tuserRepository.findRelatedIdById(str);

	}

	@Override
	public String getLastLoginTime(Integer userId) {
		LocalDateTime lastLoginTime = tuserRepository.getLastLoginTime(userId);
		if (lastLoginTime != null) {
			Date date = java.util.Date.from(lastLoginTime.atZone(ZoneId.systemDefault()).toInstant());
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
			return formatter.format(date);
		}
		return "--";
	}

	@Override
	public JSONObject changeUserPassword(String str) {

		JSONObject json = new JSONObject(str);

		JSONObject response = new JSONObject();
		Tuser mUser = tuserRepository.findByIntIdAndBitDeletedFlag(json.getInt("userId"), false);

		if (mUser != null) {

			if (json.getString("newPasswordHash") != null) {

				mUser.setTxtPassword(json.getString("newPasswordHash"));
				tuserRepository.save(mUser);
				response.put("status", 200);
				response.put("msg", "success");

			}
		} else {
			response.put("status", 400);
			response.put("msg", "Internal Issue!!!!!");
		}
		return response;
	}

	@Override
	public String sendEmailForIpAndOsBasis(String data) {
		JSONObject response = new JSONObject();
		try {
			// Decode and parse the input data
			String decodedData = CommonUtil.inputStreamDecoder(data);
			JSONObject obj = new JSONObject(decodedData);

			Integer userId = obj.optInt("userId");
			String currentIp = obj.optString("vchIpAddress");
			JSONObject osJson = obj.optJSONObject("vchOs");
			String currentPlatform = osJson != null ? osJson.optString("platform") : "";
			String email = obj.optString("appEmailid");
			// String name = obj.optString("name");

			// Validate email address
			if (email == null || email.isEmpty()) {
				return "Email address is missing or empty.";
			}

			// Fetch the last two login attempts for the user
			List<Tuple> lastTwoLogins = tuserRepository.findDistinctCountsForLogin(userId);

			if (lastTwoLogins != null && lastTwoLogins.size() >= 2) {
				Tuple lastLogin = lastTwoLogins.get(0); // Most recent login
				Tuple secondLastLogin = lastTwoLogins.get(1); // Second most recent login

				// Extract IP and OS details from the last two logins
				String lastLoginIp = lastLogin.get("vchIpAddress", String.class);
				String lastLoginOs = lastLogin.get("vchOs", String.class);
				String secondLastLoginIp = secondLastLogin.get("vchIpAddress", String.class);
				String secondLastLoginOs = secondLastLogin.get("vchOs", String.class);

				// Compare IP and OS between the last two logins
				boolean isIpOrOsDifferent = !lastLoginIp.equals(secondLastLoginIp)
						|| !lastLoginOs.equals(secondLastLoginOs);

				if (isIpOrOsDifferent) {
					// Integer uId = findRelatedIdById(name);
					List<Map<String, Object>> totalRes = service.getUserWiseNotificationStatus(userId);

					// Check the status for the specific notification
					boolean shouldSendMail = totalRes.stream()
							.anyMatch(notification -> "Email me if a new os and ip is used to sign in"
									.equals(notification.get("notificationSubCategoryName"))
									&& Boolean.TRUE.equals(notification.get("notificSubcatStatus")));

					if (shouldSendMail) {
						// Prepare the email content
						String emailContent = "Suspicious login detected for user: " + userId + "\nLast Login IP: "
								+ lastLoginIp + "\nLast Login OS: " + lastLoginOs + "\nSecond Last Login IP: "
								+ secondLastLoginIp + "\nSecond Last Login OS: " + secondLastLoginOs + "\nCurrent IP: "
								+ currentIp + "\nCurrent OS: " + currentPlatform;

						String subject = "Suspicious Login Attempt Detected!";

						// Send the email
						try {
							EmailUtil.sendMail(subject, emailContent, email);
							response.put("status", "Login detected with different IP/OS. Email notification sent");
						} catch (Exception e) {
							response.put("status", "Login detected with different IP/OS, but email could not be sent.");
						}
					} else {
						response.put("status",
								"Login detected with different IP/OS, but notification status is false. No email sent.");
					}
				} else {
					response.put("status", "Login details match with last two logins. No email sent.");
				}
			} else {
				response.put("status", "Not enough login details found for user.");
			}
		} catch (Exception e) {
			logger.info("Inside logindetails method of LoginController", e.getMessage());
			response.put("failure", "An error occurred while processing the request.");
		}
		return response.toString();
	}

	@Override
	public Integer getUserIdByName(String userName) {
		return tuserRepository.findRelatedIdById(userName);
	}

	@Override
	public Map<String, Object> getIpCheck(String ipAdress, Integer userId) {
		List<Tuple> prevLoginIP = tuserRepository.findLastLoginIPFDetails(userId);
		Boolean isTwoFactor = tuserRepository.findBitIsTwoFactorAuthEnabledByIntId(userId);
		Map<String, Object> result = new HashMap<>();
		String lastLoginIp = null;
		boolean isStatus = false;
		if (prevLoginIP.size() != 0) {
			Tuple lastLogin = prevLoginIP.get(0); // Most recent login

			// Extract IP last login
			lastLoginIp = lastLogin.get("vchIpAddress", String.class);
			Timestamp dateTime = lastLogin.get("dtmDateTime", Timestamp.class);
			Integer userIsLogout = tuserRepository.userIsLogout(dateTime, lastLoginIp);
			// Compare IP and OS between the last two logins
			if (!lastLoginIp.equals(ipAdress) && userIsLogout == 0) {
				isStatus = true;
			}

		}
		result.put("isStatus", isStatus);
		result.put("lastLoginIp", lastLoginIp);
		result.put("isTwoFactor", isTwoFactor);

		return result;

	}

	@Override
	public List<Map<String, Object>> getIcMembers(Integer roleId) {

		List<Tuple> list = null;
		if (roleId == 12) {
			list = tuserRepository.getClCMembers(roleId);
		} else if (roleId == 52) {
			list = tuserRepository.getIcMembers(roleId);
		}

		List<Map<String, Object>> result = new ArrayList<>();
		for (Tuple tuple : list) {
			Map<String, Object> dto = new HashMap<>();
			dto.put("userId", tuple.get("userId"));
			dto.put("roleId", tuple.get("roleId"));
			dto.put("fullName", tuple.get("fullName"));
			dto.put("icMemberRoleId", tuple.get("icMemberRoleId"));
			result.add(dto);
		}
		return result;
	}

	@Override
	public List<InspectorListDto> getNotSuspendedUser(Integer countyId, Integer subCountyId, Integer roleId) {
		List<Tuple> list = tuserRepository.getNotSuspendedUser(countyId, subCountyId, roleId);
		List<InspectorListDto> result = new ArrayList<>();
		for (Tuple tuple : list) {
			InspectorListDto dto = new InspectorListDto();
			dto.setId((Integer) tuple.get("intId"));
			dto.setName((String) tuple.get("vchFullName"));
			result.add(dto);
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getCommitteeMembers() {
		List<Tuple> committeeMembersList = tuserRepository.getCommitteeMembersList();

		List<Map<String, Object>> result = new ArrayList<>();
		for (Tuple tuple : committeeMembersList) {
			Map<String, Object> dto = new HashMap<>();
			dto.put("userId", tuple.get("intid"));
			dto.put("committeemember", tuple.get("committeemember"));
			dto.put("roleId", tuple.get("roleId"));
			result.add(dto);
		}
		return result;
	}

	@Override
	public List<InspectorListDto> getCCMembers() {
		List<Tuple> list = tuserRepository.getCCMembers();
		List<InspectorListDto> result = new ArrayList<>();
		for (Tuple tuple : list) {
			InspectorListDto dto = new InspectorListDto();
			dto.setId((Integer) tuple.get("intId"));
			dto.setName((String) tuple.get("vchFullName"));
			result.add(dto);
		}
		return result;
	}

	@Override
	public boolean checkIfUserExists(String emailId) {
		return tuserRepository.existsByTxtUserId(emailId);
	}
	
	
	@Override
	@Transactional
	public JSONObject uploadUserDetailList(MultipartFile file) throws IOException {
		JSONObject json = new JSONObject();
         Tuser tuser=null;
         List<Tuser> tusers = new ArrayList<>();
		// Fetch all departments from DB and map them (Assume repository or service layer is used)
        List<Mdepartment> departmentList = mdepartmentRepository.findAllByBitDeletedFlag(false); // Fetch departments
        Map<String, Integer> departmentMap = departmentList.stream()
            .collect(Collectors.toMap(Mdepartment::getTxtDepartmentName, Mdepartment::getIntId));
        
        List<Mrole> roleList = mroleRepository.findAllByBitDeletedFlag(false);
        Map<String, Integer> roleMap = new HashMap<>();
                   for (Mrole role : roleList) {
                        roleMap.put(role.getTxtRoleName(), role.getIntId());
                              }
        
        List<Mdesignation> designationList = mdesignationRepository.findAllByBitDeletedFlag(false);
        Map<String, Integer> designationMap = designationList.stream()
                .collect(Collectors.toMap(Mdesignation::getTxtDesignationName, Mdesignation::getIntId));
        
        List<Memployeetype> employeeTypeList = memployeetypeRepository.findAllByBitDeletedFlag(false);
        Map<String, Integer> employeeTypeMap = employeeTypeList.stream()
                .collect(Collectors.toMap(Memployeetype::getTxtEmployeeType, Memployeetype::getIntId));
        
        List<Mgroups> groupList = mgroupsRepository.findAllByBitDeletedFlag(false);
        Map<String, Integer> groupMap = groupList.stream()
                .collect(Collectors.toMap(Mgroups::getTxtGroupName, Mgroups::getIntId));
        
        List<County> countyList=countyRepository.findByBitDeletedFlagFalse();
        Map<String, Integer> countryMap = countyList.stream()
                .collect(Collectors.toMap(County::getName, County::getId));
        
        List<SubCounty> subCountyList=subCountyRepository.findAllByBitDeletedFlag(false);
        Map<Integer, Map<String, Integer>> subCountyMap = subCountyList.stream()
        	    .collect(Collectors.groupingBy(
        	        subCounty -> subCounty.getCounty().getId(),  // Extract countyId from County entity
        	        Collectors.toMap(SubCounty::getTxtSubCountyName, SubCounty::getIntId) // Map subCountyName to subCountyId
        	    ));
        
        List<WardMaster> wardList=wardMasterrepository.findAll();
        Map<Integer, Map<String, Integer>> wardMap = wardList.stream()
        	    .collect(Collectors.groupingBy(
        	        WardMaster::getIntSubCountyId, 
        	        Collectors.toMap(WardMaster::getWardName, WardMaster::getIntWardMasterId) // Map subCountyName to subCountyId
        	    ));
        
     // Fetch all active committee members from the database
       
        List<Tuple> committeeMembersList = tuserRepository.getCommitteeMembersList();

     // Create a mapping of committee names to their corresponding IDs
        Map<String, Integer> committeeMap = committeeMembersList.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get("committeemember", String.class),  // Fetch committee member name
                        tuple -> tuple.get("roleId", Integer.class)           // Fetch role ID
                ));

     // Fetching reporting authorities from the database
        String query = "SELECT intId, vchFullName FROM m_admin_user WHERE bitDeletedFlag = 0";
        List<Object[]> dataList = CommonUtil.getDynResultList(em, query);

         Map<String,Integer> reportingAuthorityMap = new HashMap<>();
		Integer authorityId = 0;
		String authorityName = null;
		for (Object[] result : dataList) {
			authorityId = (Integer) result[0];
			authorityName = (String) result[1];
			reportingAuthorityMap.put(authorityName,authorityId);
		}
       
		try (InputStream inputStream = file.getInputStream();
		         Workbook workbook = WorkbookFactory.create(inputStream)) {
		        Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

		        Iterator<Row> iterator = sheet.iterator();
		        int rowCount = 0;
		        // Skip the first two rows (assuming the header and user list heading)
		        while (iterator.hasNext() && rowCount < 2) {
		            iterator.next();
		            rowCount++;
		        }

		        while (iterator.hasNext()) {
		            Row currentRow = iterator.next();
		            int currentRowNum = currentRow.getRowNum() + 1; 
		           
		         // Validate Full Name (Required)
		            Cell fullNameCell = currentRow.getCell(0);
		            if (fullNameCell == null || fullNameCell.getStringCellValue().trim().isEmpty()) {
		                
		            	throw new RuntimeException("Row " + currentRowNum + ": Full Name is required.");
		               
		            }
		            String fullName = fullNameCell != null ? fullNameCell.getStringCellValue().trim() : "";

		            // Validate Gender (Male or Female)
		            Cell genderCell = currentRow.getCell(1);
		            String genderValue = genderCell != null ? genderCell.getStringCellValue().trim() : "";
		            Integer gender = null;
		            if (genderValue.equalsIgnoreCase("Male")) {
		                gender = 1;
		            } else if (genderValue.equalsIgnoreCase("Female")) {
		                gender = 2;
		            } else {
		            	throw new RuntimeException("Row " + currentRowNum + ": '" + genderValue + "' is not a valid gender. Please enter 'Male' or 'Female'");
		                
		            }

		         // Validate Mobile Number
		            String mobileNo = "";
		            Cell mobileCell = currentRow.getCell(2);
		            if (mobileCell == null) {
		                
		            	throw new RuntimeException("Row " + currentRowNum + ": Mobile number is required.");
		               
		            }
		            if (mobileCell.getCellType() == CellType.NUMERIC) {
		                mobileNo = String.valueOf((long) mobileCell.getNumericCellValue());
		            } else {
		                mobileNo = mobileCell.getStringCellValue().trim();
		            }
		            if (!mobileNo.matches("\\d{9}")) {
		                
		            	throw new RuntimeException( "Row " + currentRowNum + ": Mobile number must contain exactly 9 digits.");
		                
		            }
		            mobileNo = "+254" + mobileNo;

		            // Validate Email ID
		            String emailId = (currentRow.getCell(3) != null && currentRow.getCell(3).getCellType() == CellType.STRING) 
		            	    ? currentRow.getCell(3).getStringCellValue().trim() 
		            	    : "";

		            	if (emailId.isEmpty()) {
		            	    throw new RuntimeException("Row " + currentRowNum + ": Email is required.");
		            	  
		            	}

		            	if (!emailId.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
		            	    
		            		throw new RuntimeException("Row " + currentRowNum + ": Invalid email format '" + emailId + "'. Please enter a valid email address.");

		            	   
		            	}
		            	else {
		            		Tuser userdetails = tuserRepository.getByUseremailId(emailId, false);
		            		if(userdetails!=null) {
		            			throw new RuntimeException("Row " + currentRowNum + ": Email ID '" + emailId + "' already exists.");
		            		}
		            	}

		            // Validate Alternate Mobile Number
		            String alternateMobileNumber = "";
		            Cell alternateMobileCell = currentRow.getCell(4);
		            if (alternateMobileCell != null) {
		                if (alternateMobileCell.getCellType() == CellType.NUMERIC) {
		                    alternateMobileNumber = String.valueOf((long) alternateMobileCell.getNumericCellValue());
		                } else {
		                    alternateMobileNumber = alternateMobileCell.getStringCellValue().trim();
		                }
		                if (!alternateMobileNumber.isEmpty() && !alternateMobileNumber.matches("\\d{9}")) {
		                    
		                	throw new RuntimeException("Row " + currentRowNum + ": Alternate Mobile number must contain exactly 9 digits.");
			               
		                }
		                else if (!alternateMobileNumber.isEmpty()) { 
		                		    alternateMobileNumber = "+254" + alternateMobileNumber;
		                }
		            }

		            // Validate Date of Joining
		            Date dateOfJoining = null;
		            Cell dateCell = currentRow.getCell(5);

		            if (dateCell != null) {
		                if (dateCell.getCellType() == CellType.NUMERIC) {
		                    dateOfJoining = dateCell.getDateCellValue();
		                } else if (dateCell.getCellType() == CellType.STRING) {
		                    String dateStr = dateCell.getStringCellValue().trim();
		                    
		                    // Check if the string is empty before validation
		                    if (!dateStr.isEmpty()) {
		                        try {
		                            dateOfJoining = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
		                        } catch (ParseException e) {
		                        	throw new RuntimeException("Row " + currentRowNum + ": Invalid date format '" + dateStr + "'. Use 'dd/MM/yyyy'.");
		                            
		                        }
		                    }
		                }
		            }
          
		            String address = "";
		            Cell addressCell = currentRow.getCell(6);

		            if (addressCell != null && addressCell.getCellType() == CellType.STRING) {
		                address = addressCell.getStringCellValue().trim();
		            }
		         // Validate Department (Mandatory)
		            Cell departmentCell = currentRow.getCell(7);
		            String department = (departmentCell != null && departmentCell.getCellType() == CellType.STRING) 
		                                ? departmentCell.getStringCellValue().trim() 
		                                : "";

		            if (department.isEmpty()) {
		                
		            	throw new RuntimeException("Row " + currentRowNum + ": Department is required.");
		               
		            }
		            Integer departmentId = departmentMap.get(department);
		            if (departmentId == null) {
		                
		            	throw new RuntimeException("Row " + currentRowNum + ": Invalid Department '" + department + "'.");		                
		            }
		         // Validate Role (Mandatory)
		            Cell roleCell = currentRow.getCell(8);
		            String role = (roleCell != null && roleCell.getCellType() == CellType.STRING) 
		                          ? roleCell.getStringCellValue().trim() 
		                          : "";

		            if (role.isEmpty()) {
		                
		            	throw new RuntimeException("Row " + currentRowNum + ": Role is required.");
		              }
		            
		            Integer roleId = roleMap.get(role);
		            if (roleId == null) {
		                
		            	throw new RuntimeException( "Row " + currentRowNum + ": Invalid Role '" + role + "'.");
		               
		            }

		         // Validate Designation (Mandatory)
		            Cell designationCell = currentRow.getCell(9);
		            String designation = (designationCell != null && designationCell.getCellType() == CellType.STRING) 
		                                  ? designationCell.getStringCellValue().trim() 
		                                  : "";

		            if (designation.isEmpty()) {
		                
		            	throw new RuntimeException("Row " + currentRowNum + ": Designation is required.");
		               
		            }

		            Integer designationId = designationMap.get(designation);
		            if (designationId == null) {
		               
		                throw new RuntimeException("Row " + currentRowNum + ": Invalid Designation '" + designation + "'.");
		               
		            }
		            
                    // Validate Employee Type
		            String employeeType = currentRow.getCell(10) != null ? currentRow.getCell(10).getStringCellValue().trim() : "";
		            Integer employeeTypeId = null;
		            if (!employeeType.isEmpty()) {  // Only validate if Employee Type is provided
		            employeeTypeId = employeeTypeMap.get(employeeType);
		            if (employeeTypeId == null) {
			             
                    	throw new RuntimeException( "Row " + currentRowNum + ": Invalid Emplopyee Type '" + employeeType + "'.");
		                
		              }
		            }
                    
                 // Validate Group (Optional)
                    Cell groupCell = currentRow.getCell(11);
                    String group = (groupCell != null && groupCell.getCellType() == CellType.STRING) 
                                   ? groupCell.getStringCellValue().trim() 
                                   : "";
                    Integer groupId=null;
                    // Only validate if a group value is provided
                    if (!group.isEmpty()) {
                         groupId = groupMap.get(group);
                        if (groupId == null) {
                            
                        	throw new RuntimeException("Row " + currentRowNum + ": Invalid Group '" + group + "'.");
                           
                        }
                    }

                 // Validate County (Mandatory)
                    Cell countyCell = currentRow.getCell(12);
                    String county = (countyCell != null && countyCell.getCellType() == CellType.STRING) 
                                    ? countyCell.getStringCellValue().trim() 
                                    : "";

                    if (county.isEmpty()) {
                    	
                     throw new RuntimeException( "Row " + currentRowNum + ": County is required.");
                       
                    }

                    Integer countyId = countryMap.get(county);
                    if (countyId == null) {
                    	throw new RuntimeException("Row " + currentRowNum + ": Invalid County '" + county + "'.");
                       
                    }

                 // Validate Sub-County (Mandatory)
                    Cell subCountyCell = currentRow.getCell(13);
                    String subCounty = (subCountyCell != null && subCountyCell.getCellType() == CellType.STRING)
                                       ? subCountyCell.getStringCellValue().trim()
                                       : "";

                    // Check if Sub-County is missing
                    if (subCounty.isEmpty()) {
                        throw new RuntimeException("Row " + currentRowNum + ": Sub-County is required.");
                      
                    }

                    // Check if Sub-County is valid under the given County
                    Integer subCountyId = subCountyMap.getOrDefault(countyId, Collections.emptyMap()).get(subCounty);
                    if (subCountyId == null) {
                      
                    	  throw new RuntimeException("Row " + currentRowNum + ": Invalid Sub-County '" + subCounty + "' under County '" + county + "'.");
                    }

                 // Validate Ward (Mandatory)
                    Cell wardCell = currentRow.getCell(14);
                    String ward = (wardCell != null && wardCell.getCellType() == CellType.STRING)
                                  ? wardCell.getStringCellValue().trim()
                                  : "";

                    // Check if Ward is missing
                    if (ward.isEmpty()) {
                      
                        throw new RuntimeException( "Row " + currentRowNum + ": Ward is required.");
                    }

                    // Check if Ward is valid under the given Sub-County
                    Integer wardId = wardMap.getOrDefault(subCountyId, Collections.emptyMap()).get(ward);
                    if (wardId == null) {
                        
                    	throw new RuntimeException("Row " + currentRowNum + ": Invalid Ward '" + ward + "' under Sub-County '" + subCounty + "'.");

                    }

                 // Validate User ID (Mandatory)
                    Cell userIdCell = currentRow.getCell(15);
                    String userId = (userIdCell != null && userIdCell.getCellType() == CellType.STRING)
                                    ? userIdCell.getStringCellValue().trim()
                                    : "";

                    // Check if User ID is missing
                    if (userId.isEmpty()) {
                    	throw new RuntimeException("Row " + currentRowNum + ": User ID is required.");
                    }

                 // Default encrypted password value
                    String defaultPassword = "6e95001cee8e57360ffd1ed3ca37035b413a3f198d71df367e3a47a3d822e4df";

                    // Validate Password
                    Cell passwordCell = currentRow.getCell(16);
                    String password = (passwordCell != null && passwordCell.getCellType() == CellType.STRING)
                                     ? passwordCell.getStringCellValue().trim()
                                     : "";

                    // Check if the password is empty
                    if (password.isEmpty()) {
                    	throw new RuntimeException( "Row " + currentRowNum + ": Password is required.");
                        
                    } else {
                        // If password is provided, use the entered value; otherwise, use the default
                        password = !password.isEmpty() ? defaultPassword : password;
                    }
                    String committeeMembers = currentRow.getCell(17) != null ? currentRow.getCell(17).getStringCellValue().trim() : "";
                    // Initialize role ID variables
                    Integer icmRoleId = null;
                    Integer clcRoleId = null;
                    Integer wrscRoleId = null;
                 // If the field is empty, skip validation
                 if (!committeeMembers.isEmpty()) {
                     List<String> selectedCommittees = Arrays.asList(committeeMembers.split("\\s*,\\s*"));

                     // Track invalid committees
                     List<String> invalidCommittees = new ArrayList<>();

                     // Map committee names to their corresponding role IDs
                     for (String committee : selectedCommittees) {
                         Integer roleIds = committeeMap.get(committee); // Get role ID from the map
                         if (roleIds != null) {
                             if ("IC Member".equalsIgnoreCase(committee)) {
                                 icmRoleId = roleIds;
                             } else if ("CLC".equalsIgnoreCase(committee)) {
                                 clcRoleId = roleIds;
                             } else if ("WRSC".equalsIgnoreCase(committee)) {
                                 wrscRoleId = roleIds;
                             }
                         } else {
                             invalidCommittees.add(committee); // Store invalid committees
                         }
                     }

                     // If any invalid committee names exist, throw an exception
                     if (!invalidCommittees.isEmpty()) {
                         throw new RuntimeException("Row " + currentRowNum + ": Invalid Committee Member  " + String.join(", ", invalidCommittees));
                     }
                 }

                 // Validate Privilege
		            String privilegeValue = currentRow.getCell(18) != null ? currentRow.getCell(18).getStringCellValue().trim() : "";
		            Integer privilege = privilegeValue.equalsIgnoreCase("Yes") ? 2 : 3;

		         // Validate Reporting Authority
		            String reportingAuthorityName = currentRow.getCell(19) != null ? currentRow.getCell(19).getStringCellValue().trim() : "";
		            Integer reportingAuthorityId=null;
		            // If Reporting Authority is provided, validate it
		            if (!reportingAuthorityName.isEmpty()) {
		                reportingAuthorityId = reportingAuthorityMap.get(reportingAuthorityName);

		                if (reportingAuthorityId == null) {
		                	throw new RuntimeException("Row " + currentRowNum + ": Invalid Reporting Authority '" + reportingAuthorityName + "'.");

		                }
		            }

		            // Log the data
		            logger.info("Full Name: {}, Gender: {}, Mobile No: {}, Email ID: {}, Alternate Mobile No: {}, Date of Joining: {}, Address: {}, " + 
		                    "Department: {}, Role: {}, Group: {}, County: {}, Sub-County: {}, Ward: {}, User ID: {}, ICM Role ID: {}, CLC Role ID: {}, " + 
		                    "Reporting Auth: {}, Privilege: {}",
		                    fullName, gender, mobileNo, emailId, alternateMobileNumber, dateOfJoining, address, department, role, group, 
		                    county, subCounty, ward, userId, icmRoleId, clcRoleId, reportingAuthorityName, privilege);

					Tuser getEntity = tuserRepository.getByUserId(userId, false);
					if (getEntity == null) {
						    tuser=new Tuser();
						    tuser.setTxtFullName(fullName);
						    tuser.setSelGender(gender);
						    tuser.setTxtMobileNo(mobileNo);
						    tuser.setTxtEmailId(emailId);
						    tuser.setTxtAlternateMobileNumber(alternateMobileNumber.isEmpty() ? null : alternateMobileNumber);
						    tuser.setTxtDateOfJoining(dateOfJoining != null ? dateOfJoining : null);
						    tuser.setTxtrAddress(address != null && !address.trim().isEmpty() ? address : null);
						    tuser.setSelDepartment(departmentId);
						    tuser.setSelRole(roleId);
						    tuser.setSelDesignation(designationId);
						    tuser.setSelEmployeeType(employeeTypeId);
						    tuser.setSelGroup(groupId);
						    tuser.setSelCounty(countyId);
						    tuser.setSelSubCounty(subCountyId);
						    tuser.setSelWard(wardId);
						    tuser.setTxtUserId(userId);
						    tuser.setTxtPassword(password);
						    tuser.setSelIcmRoleId(icmRoleId);
						    tuser.setSelCLCRoleId(clcRoleId);
						    tuser.setSelWRSCRoleId(wrscRoleId);
						    tuser.setChkPrevilege(privilege);
						    tuser.setIntReportingAuth(reportingAuthorityId);
						    tusers.add(tuser);
						   
					} else {
						
						throw new RuntimeException("Row " + currentRowNum + ": User ID '" + userId + "' already exists.");

						}
					 }
		        
		           tuserRepository.saveAll(tusers);
				   json.put("status", 200);
		        }
		            catch (RuntimeException e) {
		            json.put("status", 400);
		        	json.put("error", e.getMessage());
		            return json;
		        } catch (Exception e) {
		        	json.put("status", 500);
		        	json.put("error", "Internal Server Error: " + e.getMessage());
		            return json;
		        }
	         
		return json;
	}            
		

}
