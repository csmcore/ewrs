package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.entity.Tmenulinks;
import app.ewarehouse.entity.Trolepermission;
import app.ewarehouse.repository.TmenulinksRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.TmenulinksService;
import app.ewarehouse.service.TrolepermissionService;
import app.ewarehouse.util.CommonUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TmenulinksServiceImpl implements TmenulinksService {
	private static final String TIN_PUBLISH_RIGHT = "tinPublishRight";
	private static final String TIN_DELETE_RIGHT = "tinDeleteRight";
	private static final String TIN_EDIT_RIGHT = "tinEditRight";
	private static final String TIN_MANAGE_RIGHT = "tinManageRight";
	private static final String TIN_ADD_RIGHT = "tinAddRight";
	@Autowired
	private TmenulinksRepository tmenulinksRepository;
	@Autowired
	EntityManager entityManager;
	@Autowired
	private TrolepermissionService trolepermissionService;
	@Autowired
	private TuserRepository tuserRepository;
	
	@Autowired
    private CacheManager cacheManager;

	JSONObject selLinkType = new JSONObject("{1:Global Link,2:Parent Link,3:Secondary Link,4:Button,5:Tab}");

	Integer parentId = 0;
	Object dynamicValue = null;
	
	@Override
	public JSONObject save(String data) {
		JSONObject json = new JSONObject();
		try {
			ObjectMapper om = new ObjectMapper();
			Tmenulinks tmenulinks = om.readValue(data, Tmenulinks.class);
			List<String> fileUploadList = new ArrayList<String>();
			if (!Objects.isNull(tmenulinks.getIntId()) && tmenulinks.getIntId() > 0) {
				Tmenulinks getEntity = tmenulinksRepository.findByIntIdAndBitDeletedFlag(tmenulinks.getIntId(), false);
				getEntity.setSelLinkType(tmenulinks.getSelLinkType());
				getEntity.setSelParentLink(tmenulinks.getSelParentLink());
				getEntity.setTxtLinkName(tmenulinks.getTxtLinkName());
				getEntity.setTxtURL(tmenulinks.getTxtURL());
				getEntity.setVchViewUrl(tmenulinks.getTxtURL());
				getEntity.setTxtCSSClass(tmenulinks.getTxtCSSClass());
				getEntity.setTxtSerialNo(tmenulinks.getTxtSerialNo());
				if (tmenulinks.getVchIconEnc().length() > 0) {
					getEntity.setVchIconEnc(tmenulinks.getVchIconEnc());
				}

				if (tmenulinks.getVchIconFileType().length() > 0) {
					getEntity.setVchIconFileType(tmenulinks.getVchIconFileType());
				}
				Tmenulinks updateData = tmenulinksRepository.save(getEntity);
				parentId = updateData.getIntId();
				json.put("status", 202);
			} else {
				tmenulinks.setIntApplicableFor(1);
				tmenulinks.setVchViewUrl(tmenulinks.getTxtURL());
				// tmenulinks.setTxtURL(null);
				tmenulinks.setTxtURL(tmenulinks.getTxtURL());
				Tmenulinks saveData = tmenulinksRepository.save(tmenulinks);
				parentId = saveData.getIntId();
				json.put("status", 200);
			}
			json.put("id", parentId);
			clearMenuCache();
			
		} catch (Exception e) {
			log.error("TmenulinksServiceImpl:save()",e);
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		Tmenulinks entity = tmenulinksRepository.findByIntIdAndBitDeletedFlag(id, false);
		dynamicValue = (selLinkType.has(entity.getSelLinkType().toString()))
				? selLinkType.get(entity.getSelLinkType().toString())
				: "--";
		entity.setSelLinkTypeVal(dynamicValue.toString());

		if (entity.getTxtURL() == null) {
			entity.setTxtURL("");
		}

		if (entity.getVchViewUrl() == null) {

			entity.setVchViewUrl("");
		}
		if (entity.getTxtCSSClass() == null) {

			entity.setTxtCSSClass("");
		}

		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select vchLinkName from m_admin_menulinks where intLinkType=" + entity.getSelParentLink());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelParentLinkVal(dynamicValue.toString());

		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		JSONObject jsonData = new JSONObject(formParams);
		String searchValue = jsonData.get("searchValue").toString();

		List<Tmenulinks> tmenulinksResp = null;

		if (searchValue != null && searchValue.length() > 0) {

			Integer searchId = Integer.parseInt(searchValue);
			tmenulinksResp = tmenulinksRepository.filterByLinksById(searchId, false);
		} else {
			tmenulinksResp = tmenulinksRepository.findAllByBitDeletedFlag(false);
		}

		Set<Integer> parentLinkIds = tmenulinksResp.stream().map(Tmenulinks::getSelParentLink)
				.collect(Collectors.toSet());

		Map<Integer, String> parentLinkNamesMap = fetchParentLinkNames(parentLinkIds);

		Map<String, String> selLinkTypeMap = new HashMap<>();
		for (String linkTypeKey : selLinkType.keySet()) {
			selLinkTypeMap.put(linkTypeKey, (String) selLinkType.get(linkTypeKey));
		}

		for (Tmenulinks entity : tmenulinksResp) {
			dynamicValue = (selLinkType.has(entity.getSelLinkType().toString()))
					? selLinkType.get(entity.getSelLinkType().toString())
					: "--";
			entity.setSelLinkTypeVal(dynamicValue.toString());
			try {
				dynamicValue = parentLinkNamesMap.getOrDefault(entity.getSelParentLink(), "--");
				
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelParentLinkVal(dynamicValue.toString());

		}

		return new JSONArray(tmenulinksResp);
	}

	private Map<Integer, String> fetchParentLinkNames(Set<Integer> parentLinkIds) {
		String stringParentLinkIds = parentLinkIds.stream().map(String::valueOf).collect(Collectors.joining(","));

		String query = "SELECT intLinkType, vchLinkName FROM m_admin_menulinks WHERE intLinkType IN ("
				+ stringParentLinkIds + ")";

		List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);

		Map<Integer, String> parentLinkNamesMap = new HashMap<>();

		for (Object[] result : results) {
			Integer linkTypeId = (Integer) result[0];
			String linkName = (String) result[1];
			parentLinkNamesMap.put(linkTypeId, linkName);
		}

		return parentLinkNamesMap;
	}

	@Override
	public JSONArray getAllForOrderScreen(String formParams) {
		List<Tmenulinks> tmenulinksResp = tmenulinksRepository.findAllByBitDeletedFlagOrderedBySlNo(false);
		
		Set<Integer> parentLinkIds = tmenulinksResp.stream().map(Tmenulinks::getSelParentLink)
				.collect(Collectors.toSet());

		Map<Integer, String> parentLinkNamesMap = fetchParentLinkNames(parentLinkIds);

		Map<String, String> selLinkTypeMap = new HashMap<>();
		for (String linkTypeKey : selLinkType.keySet()) {
			selLinkTypeMap.put(linkTypeKey, (String) selLinkType.get(linkTypeKey));
		}
		
		for (Tmenulinks entity : tmenulinksResp) {
			dynamicValue = (selLinkType.has(entity.getSelLinkType().toString()))
					? selLinkType.get(entity.getSelLinkType().toString())
					: "--";
			entity.setSelLinkTypeVal(dynamicValue.toString());
			try {
				dynamicValue = parentLinkNamesMap.getOrDefault(entity.getSelParentLink(), "--");
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelParentLinkVal(dynamicValue.toString());

		}
		return new JSONArray(tmenulinksResp);
	}

	@Override
	public JSONObject deleteById(Integer id) {
		JSONObject json = new JSONObject();
		try {
			Tmenulinks entity = tmenulinksRepository.findByIntIdAndBitDeletedFlag(id, false);
			entity.setBitDeletedFlag(true);
			tmenulinksRepository.save(entity);
			json.put("status", 200);
			clearMenuCache();
		} catch (Exception e) {
			log.error("TmenulinksServiceImpl:deleteById()",e);
			json.put("status", 400);
		}
		return json;
	}

	public static JSONArray fillselParentLinkList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select intId,vchLinkName from m_admin_menulinks where intLinkType=1";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("intId", data[0]);
			jsonObj.put("vchLinkName", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	@Override
	@Cacheable(value = "getMenuLinks",key = "#data")
	public JSONArray getByDataUsing(String data) {
		JSONObject jsob = new JSONObject(data);
		Integer roleId = jsob.getInt("intRoleId");
		Integer userId = jsob.getInt("intUserId");
		JSONArray makeArray = new JSONArray();
		Integer isAdmin = tuserRepository.getcheckPrevilegeByUserId(userId, false);
		List<Trolepermission> rolePermission = trolepermissionService.getRolePermissionListByUserId(userId);

		if (roleId == 0) {
			List<Object[]> menuList = tmenulinksRepository.getAllDataSupAd();
			for (Object[] obj : menuList) {
				JSONObject jsonObject = new JSONObject();
				
				  String moduleName=obj[6].toString();  
				
					  if("Configuration".equals(moduleName)
					  ||"Master".equals(moduleName) ||"Admin Console".equals(moduleName)
					  || "Application Audit".equals(moduleName) ||
					  "Manage Settings".equals(moduleName)) {
						  jsonObject.put("intId", (Integer) obj[0]);
						  jsonObject.put("intLinkType", (Integer) obj[1]);
						  jsonObject.put("vchLinkName", obj[2].toString());
						  jsonObject.put("intParentLinkId", (Integer) obj[3]);
						  jsonObject.put("vchViewUrl", obj[5].toString()); jsonObject.put("moduleName",
						  obj[6].toString()); makeArray.put(jsonObject);
					  
					  }
					  else { 
						  
					  }
			}
		} else {

			if (!rolePermission.isEmpty() && isAdmin != 2) {
			    List<Object[]> menuList1 = tmenulinksRepository.getDataFromLinksandPermission(roleId);
			    List<Object[]> menuList2 = tmenulinksRepository.getDataListByUserId(userId);

			    List<Object[]> combinedList = new ArrayList<>();
			    combinedList.addAll(menuList1);
			    combinedList.addAll(menuList2);
			   
			    Map<String, List<JSONObject>> menuMap = new HashMap<>();

			    for (Object[] obj : combinedList) {
			        String vchLinkName = obj[1].toString();
			        String moduleName = obj[9].toString();

			        String tinAddRight = obj[13].toString();
			        String tinManageRight = obj[10].toString();
			        String tinEditRight = obj[11].toString();
			        String tinDeleteRight = obj[14].toString();
			        String tinPublishRight = obj[12].toString();

			        JSONObject jsonObject = new JSONObject();
			        jsonObject.put("intId", (Integer) obj[0]);
			        jsonObject.put("vchViewUrl", obj[3].toString());
			        jsonObject.put("intLinkType", (Integer) obj[4]);
			        jsonObject.put("intParentLinkId", (Integer) obj[5]);
			        jsonObject.put("trIntId", (Integer) obj[6]);
			        jsonObject.put("moduleName", moduleName);
			        jsonObject.put(TIN_MANAGE_RIGHT, tinManageRight);
			        jsonObject.put(TIN_EDIT_RIGHT, tinEditRight);
			        jsonObject.put(TIN_PUBLISH_RIGHT, tinPublishRight);
			        jsonObject.put(TIN_ADD_RIGHT, tinAddRight);
			        jsonObject.put(TIN_DELETE_RIGHT, tinDeleteRight);
			        jsonObject.put("tinAllRight", obj[15].toString());

			        if (moduleName.equals("Approval Application")) {
			            if (roleId == 2 || roleId == 10 || roleId == 42) {
			                jsonObject.put("vchLinkName", "Assign/Review Complaint");
			            } else {
			                jsonObject.put("vchLinkName", "Review Complaint");
			            }
			        } else {
			            jsonObject.put("vchLinkName", vchLinkName);
			        }

			        if (menuMap.containsKey(vchLinkName)) {
			            List<JSONObject> existingEntries = menuMap.get(vchLinkName);

			            boolean isDuplicate = false;
			            for (JSONObject existingEntry : existingEntries) {
			                if (existingEntry.getString("moduleName").equals(moduleName)) {
			                    existingEntry.put(TIN_ADD_RIGHT, (existingEntry.getString(TIN_ADD_RIGHT).equals("0") && tinAddRight.equals("0")) ? "0" : "1");
			                    existingEntry.put(TIN_MANAGE_RIGHT, (existingEntry.getString(TIN_MANAGE_RIGHT).equals("0") && tinManageRight.equals("0")) ? "0" : "1");
			                    existingEntry.put(TIN_EDIT_RIGHT, (existingEntry.getString(TIN_EDIT_RIGHT).equals("0") && tinEditRight.equals("0")) ? "0" : "1");
			                    existingEntry.put(TIN_DELETE_RIGHT, (existingEntry.getString(TIN_DELETE_RIGHT).equals("0") && tinDeleteRight.equals("0")) ? "0" : "1");
			                    existingEntry.put(TIN_PUBLISH_RIGHT, (existingEntry.getString(TIN_PUBLISH_RIGHT).equals("0") && tinPublishRight.equals("0")) ? "0" : "1");
			                    isDuplicate = true;
			                    break;
			                }
			            }

			            if (!isDuplicate) {
			                existingEntries.add(jsonObject);
			            }
			        } else {
			            List<JSONObject> newList = new ArrayList<>();
			            newList.add(jsonObject);
			            menuMap.put(vchLinkName, newList);
			        }
			    }

			    makeArray = new JSONArray();
			    for (List<JSONObject> entryList : menuMap.values()) {
			        for (JSONObject obj : entryList) {
			            makeArray.put(obj);  
			        }
			    }

			  
			} else { 
				  
				 List<Object[]> menuList = tmenulinksRepository.getDataFromLinksandPermission(roleId); 
				 for (Object[] obj : menuList) {
					 JSONObject jsonObject = new JSONObject();
					 jsonObject.put("intId", (Integer) obj[0]);  
					 jsonObject.put("vchViewUrl", obj[3].toString());
					 jsonObject.put("intLinkType", (Integer) obj[4]);
					 jsonObject.put("intParentLinkId", (Integer) obj[5]);
					 jsonObject.put("trIntId", (Integer) obj[6]); 
					 jsonObject.put("moduleName",obj[9].toString()); jsonObject.put("tinManageRight", obj[10].toString());
					 jsonObject.put("tinEditRight", obj[11].toString());
					 jsonObject.put("tinPublishRight", obj[12].toString());
					 jsonObject.put("tinAddRight", obj[13].toString());
					 jsonObject.put("tinDeleteRight", obj[14].toString());
					 jsonObject.put("tinAllRight", obj[15].toString()); 
					 if(obj[9].toString().equals("Approval Application")) { 
						 if (roleId == 2 ||roleId == 10 || roleId == 42) { 
							 jsonObject.put("vchLinkName", "Assign/Review Complaint"); 
						 } else { 
							 jsonObject.put("vchLinkName", "Review Complaint"); 
						 } 
					} else { 
						jsonObject.put("vchLinkName",obj[1].toString()); 
					} 
					 
					 makeArray.put(jsonObject); 
				 }
			  
			  }
		}
		return makeArray;
	}
	
	// to clear cache whenever it need
	 public void clearMenuCache() {
	        Cache cache = cacheManager.getCache("getMenuLinks");
	        if (cache != null) {
	            cache.clear();
	        }
	    }

	@Override
	public JSONObject getAllFormList() {
		List<Tmenulinks> tMenuLinksList = tmenulinksRepository
				.findAllByBitDeletedFlagAndIntApplicableForAndSelLinkTypeOrderByDtmCreatedOnDesc(false, 2, 2);
		JSONObject json = new JSONObject();
		json.put("result", new JSONArray(tMenuLinksList));
		json.put("status", 200);
		return json;
	}

	@Override
	public JSONObject GlobalLinkwithIcon(JSONArray makeArray) {

		JSONArray iconArray = new JSONArray();
		IntStream.range(0, makeArray.length()).mapToObj(makeArray::getJSONObject)
				.map(obj -> obj.getString("moduleName")).distinct().forEach(x -> {
					List<Object[]> menuIconList = tmenulinksRepository.getIconEncString(x);
					if (menuIconList != null && menuIconList.size() > 0) {
						for (Object[] obj : menuIconList) {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("globalLinkName", x);

							if (obj[0] != null) {
								jsonObject.put("iconEncString", obj[0].toString());
							}
							if (obj[1] != null) {
								jsonObject.put("iconFileType", obj[1].toString());
							}
							iconArray.put(jsonObject);
						}
					}

				});

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("GlobalMenuLink", iconArray);

		return jsonObject;
	}

	@Override
	public JSONObject LinkType() {

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObjectFinal = new JSONObject();
		IntStream.range(1, 6).forEach(x -> {

			if (x == 1) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("LinkType", "Global Link");
				jsonObject.put("LinkId", "1");
				jsonArray.put(jsonObject);
			}

			if (x == 2) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("LinkType", "Parent Link");
				jsonObject.put("LinkId", "2");
				jsonArray.put(jsonObject);
			}
			if (x == 3) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("LinkType", "Secondary Link");
				jsonObject.put("LinkId", "3");
				jsonArray.put(jsonObject);
			}
			if (x == 4) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("LinkType", "Button");
				jsonObject.put("LinkId", "4");
				jsonArray.put(jsonObject);
			}
			if (x == 5) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("LinkType", "Tab");
				jsonObject.put("LinkId", "5");
				jsonArray.put(jsonObject);
			}
		});

		jsonObjectFinal.put("LinkTypeList", jsonArray);

		return jsonObjectFinal;
	}

}
