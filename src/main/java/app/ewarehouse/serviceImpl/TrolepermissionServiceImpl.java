package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.ewarehouse.entity.Tmenulinks;
import app.ewarehouse.entity.Trolepermission;
import app.ewarehouse.repository.MroleRepository;
import app.ewarehouse.repository.TmenulinksRepository;
import app.ewarehouse.repository.TrolepermissionRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.TrolepermissionService;
import app.ewarehouse.util.CommonUtil;
import jakarta.persistence.EntityManager;
@Service
public class TrolepermissionServiceImpl implements TrolepermissionService {
	@Autowired
	private TrolepermissionRepository trolepermissionRepository;
	@Autowired
	EntityManager entityManager;
	@Autowired 
	private TmenulinksRepository tmenulinksRepository;
	@Autowired 
	private TuserRepository tUserRepository;
	@Autowired 
	private MroleRepository tRoleRepoSitory;
    
	JSONObject selPermissionFor = new JSONObject("{1:Role,2:Officer}");
	private static final Logger logger = LoggerFactory.getLogger(TrolepermissionServiceImpl.class);
	Integer parentId = 0;
	Object dynamicValue = null;

	@Override
	public JSONObject save(String data) {
		logger.info("Inside save method of TrolepermissionServiceImpl");
		JSONObject json = new JSONObject(data);
		JSONObject reJson= new JSONObject();
		try {
			
			JSONArray jsArray=json.getJSONArray("chkPermission");
			List<Trolepermission> trolePermission=trolepermissionRepository.
					getByForPermissionAndRoleIdAndBitDeletedFlag(json.getInt("selPermissionFor"),json.getInt("selSelectRole"),false);
			
			if(json.getInt("selPermissionFor")== 1) {
				
				if(!trolePermission.isEmpty()) {
					List<Trolepermission>  dataByRoleId=trolepermissionRepository.getDataByUsingthisId(json.getInt("selPermissionFor"),json.getInt("selSelectRole"),false);
					trolepermissionRepository.deleteAll(dataByRoleId);
					for(int r=0;r<jsArray.length();r++) {
						JSONObject jsObj=jsArray.getJSONObject(r);
						System.out.println(jsObj);
						Trolepermission tRolePermission=new Trolepermission();
						tRolePermission.setSelPermissionFor(json.getInt("selPermissionFor"));
						tRolePermission.setSelSelectRole(json.getInt("selSelectRole"));
						tRolePermission.setSelSelectUser(json.getInt("selSelectUser"));
						tRolePermission.setVchLinkId(jsObj.get("intLinkId").toString());
						tRolePermission.setIntadd(jsObj.getBoolean("addManageRight")== true?1:0);
						tRolePermission.setIntEditRight(jsObj.getBoolean("editManageRight")== true?1:0);
						tRolePermission.setIntViewManageRight(jsObj.getBoolean("viewManageRight")==true?1:0);
						tRolePermission.setIntDelete(jsObj.getBoolean("deleteManageRight")== true?1:0);
						tRolePermission.setPublish(jsObj.getBoolean("publishManageRight")== true?1:0);
						tRolePermission.setIntall(jsObj.getBoolean("Intall")== true?1:0);
						
						if(jsObj.getBoolean("addManageRight")== true || jsObj.getBoolean("editManageRight")== true ||
								jsObj.getBoolean("viewManageRight")==true || jsObj.getBoolean("deleteManageRight")== true ||
								jsObj.getBoolean("publishManageRight")== true || jsObj.getBoolean("Intall")== true) {
							
							trolepermissionRepository.save(tRolePermission);
							reJson.put("status", 202);
							
						}
					}
					
				}else {
					for(int i=0;i<jsArray.length();i++) {
						JSONObject jsObj=jsArray.getJSONObject(i);
						System.out.println(jsObj);
						Trolepermission tRolePermission=new Trolepermission();
						tRolePermission.setSelPermissionFor(json.getInt("selPermissionFor"));
						tRolePermission.setSelSelectRole(json.getInt("selSelectRole"));
						tRolePermission.setSelSelectUser(json.getInt("selSelectUser"));
						tRolePermission.setVchLinkId(jsObj.get("intLinkId").toString());
						tRolePermission.setIntadd(jsObj.getBoolean("addManageRight")== true?1:0);
						tRolePermission.setIntEditRight(jsObj.getBoolean("editManageRight")== true?1:0);
						tRolePermission.setIntViewManageRight(jsObj.getBoolean("viewManageRight")==true?1:0);
						tRolePermission.setIntDelete(jsObj.getBoolean("deleteManageRight")== true?1:0);
						tRolePermission.setPublish(jsObj.getBoolean("publishManageRight")== true?1:0);
						tRolePermission.setIntall(jsObj.getBoolean("Intall")== true?1:0);
						
						if(jsObj.getBoolean("addManageRight")== true || jsObj.getBoolean("editManageRight")== true ||
								jsObj.getBoolean("viewManageRight")==true || jsObj.getBoolean("deleteManageRight")== true ||
								jsObj.getBoolean("publishManageRight")== true || jsObj.getBoolean("Intall")== true) {
							
							trolepermissionRepository.save(tRolePermission);
							reJson.put("status", 200);
							
						}
						
					}
				}
				
			}else {
				List<Trolepermission> OfficerWiseData=trolepermissionRepository.findByintForPermissionAndSelUser(json.getInt("selPermissionFor"),json.getInt("selSelectUser"),false);
			   if(!OfficerWiseData.isEmpty()) {
				   List<Trolepermission> OfficerWiseDataDup=trolepermissionRepository.findByintForPermissionAndSelUser(json.getInt("selPermissionFor"),json.getInt("selSelectUser"),false);
				   trolepermissionRepository.deleteAll(OfficerWiseDataDup);
				   for(int k=0;k<jsArray.length();k++) {
					   JSONObject jsObj1=jsArray.getJSONObject(k);
					   Trolepermission tRolePermission=new Trolepermission();
						tRolePermission.setSelPermissionFor(json.getInt("selPermissionFor"));
						tRolePermission.setSelSelectRole(json.getInt("selSelectRole"));
						tRolePermission.setSelSelectUser(json.getInt("selSelectUser"));
						tRolePermission.setVchLinkId(jsObj1.get("intLinkId").toString());
						tRolePermission.setIntadd(jsObj1.getBoolean("addManageRight")== true?1:0);
						tRolePermission.setIntEditRight(jsObj1.getBoolean("editManageRight")== true?1:0);
						tRolePermission.setIntViewManageRight(jsObj1.getBoolean("viewManageRight")==true?1:0);
						tRolePermission.setIntDelete(jsObj1.getBoolean("deleteManageRight")== true?1:0);
						tRolePermission.setPublish(jsObj1.getBoolean("publishManageRight")== true?1:0);
						tRolePermission.setIntall(jsObj1.getBoolean("Intall")== true?1:0);
						
						if(jsObj1.getBoolean("addManageRight")== true || jsObj1.getBoolean("editManageRight")== true ||
								jsObj1.getBoolean("viewManageRight")==true || jsObj1.getBoolean("deleteManageRight")== true ||
										jsObj1.getBoolean("publishManageRight")== true || jsObj1.getBoolean("Intall")== true) {
							
							trolepermissionRepository.save(tRolePermission);
							reJson.put("status", 202);
							
							
						}
					   
				   }
				   
			   }else {
				   for(int k=0;k<jsArray.length();k++) {
					   JSONObject jsObj1=jsArray.getJSONObject(k);
					   Trolepermission tRolePermission=new Trolepermission();
						tRolePermission.setSelPermissionFor(json.getInt("selPermissionFor"));
						tRolePermission.setSelSelectRole(json.getInt("selSelectRole"));
						tRolePermission.setSelSelectUser(json.getInt("selSelectUser"));
						tRolePermission.setVchLinkId(jsObj1.get("intLinkId").toString());
						tRolePermission.setIntadd(jsObj1.getBoolean("addManageRight")== true?1:0);
						tRolePermission.setIntEditRight(jsObj1.getBoolean("editManageRight")== true?1:0);
						tRolePermission.setIntViewManageRight(jsObj1.getBoolean("viewManageRight")==true?1:0);
						tRolePermission.setIntDelete(jsObj1.getBoolean("deleteManageRight")== true?1:0);
						tRolePermission.setPublish(jsObj1.getBoolean("publishManageRight")== true?1:0);
						tRolePermission.setIntall(jsObj1.getBoolean("Intall")== true?1:0);
						
						if(jsObj1.getBoolean("addManageRight")== true || jsObj1.getBoolean("editManageRight")== true ||
								jsObj1.getBoolean("viewManageRight")==true || jsObj1.getBoolean("deleteManageRight")== true ||
										jsObj1.getBoolean("publishManageRight")== true || jsObj1.getBoolean("Intall")== true) {
							
							trolepermissionRepository.save(tRolePermission);
							reJson.put("status", 200);
						}
					   
				   }
			   }
			}
			
		
		
		} catch (Exception e) {
			logger.error("Inside save method of TrolepermissionServiceImpl some error occur:" + e);
			reJson.put("status", 400);
		}
		return reJson;
	}



	@Override
	public JSONArray getById(Integer roleId,Integer userId) {
		logger.info("Inside getById method of TrolepermissionServiceImpl");
		JSONArray jsonArray=new JSONArray();
		List<Trolepermission> trolData=trolepermissionRepository.getByRoleIDAndUserId(roleId,userId,false);

		for (Trolepermission trolePer : trolData) {
			 Tmenulinks tmenu = tmenulinksRepository.findById(Integer.parseInt(trolePer.getVchLinkId())).get();
		    Tmenulinks tmenu1 = tmenulinksRepository.findById(tmenu.getSelParentLink()).get();

		    JSONObject jsonObject = new JSONObject();
		    jsonObject.put("intId", tmenu1.getIntId());
		    jsonObject.put("intParentLinkId", tmenu1.getSelParentLink());
		    jsonObject.put("selLinkType", tmenu1.getSelLinkType());
		    jsonObject.put("txtLinkName", tmenu1.getTxtLinkName());

		    boolean jsonObjectExists = false;
		    for (int i = 0; i < jsonArray.length(); i++) {
		        JSONObject existingObject = jsonArray.getJSONObject(i);
		        if (existingObject.toString().equals(jsonObject.toString())) {
		            jsonObjectExists = true;
		            break;
		        }
		    }

		    if (!jsonObjectExists) {
		        jsonArray.put(jsonObject);
		    }

		    JSONObject jsonObject2 = new JSONObject();
		    jsonObject2.put("intId", tmenu.getIntId());
		    jsonObject2.put("intParentLinkId", tmenu.getSelParentLink());
		    jsonObject2.put("selLinkType", tmenu.getSelLinkType());
		    jsonObject2.put("txtLinkName", tmenu.getTxtLinkName());
		    jsonObject2.put("intViewManageRight", trolePer.getIntViewManageRight());
		    jsonObject2.put("intEditRight", trolePer.getIntEditRight());
		    jsonObject2.put("intDelete", trolePer.getIntDelete());
		    jsonObject2.put("publish", trolePer.getPublish());
		    jsonObject2.put("intadd", trolePer.getIntadd());
		    jsonObject2.put("intall", trolePer.getIntall());
		    jsonArray.put(jsonObject2);
		}




		return jsonArray;
	}

	@Override
	public JSONArray getAll(String formParams) {
	    logger.info("Inside getAll method of TrolepermissionServiceImpl");
	    JSONArray jsArray3 = new JSONArray();

	    List<Object[]> trolepermissionResp = trolepermissionRepository.getDataFromRoleTableAndUserAndRolePermission();
	   
	    Set<Integer> roleIds = trolepermissionResp.stream()
	            .map(obj -> (Integer) obj[2]) 
	            .filter(Objects::nonNull) 
	            .collect(Collectors.toSet());
	    
	    
	    Set<Integer> userIds = trolepermissionResp.stream()
	            .map(obj -> (Integer) obj[3]) 
	            .filter(Objects::nonNull) 
	            .collect(Collectors.toSet());

	    Map<Integer, String> roleMap = fetchRoles(roleIds);
	    Map<Integer, String> userMap = fetchUsers(userIds);
	    

		    for (Object[] obj : trolepermissionResp) {
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("dtmCreatedOn", obj[0]);
	        jsonObject.put("selPermissionFor", obj[1]);
	        jsonObject.put("selPermissionForVal", selPermissionFor.get(obj[1].toString()));
	        jsonObject.put("selSelectRoleVal", roleMap.get(obj[2]));
	        jsonObject.put("intRole", obj[2]);
	        jsonObject.put("selSelectUserVal", userMap.get(obj[3]));
	        jsonObject.put("intSelUser", obj[3]);
	        jsonObject.put("vchRolename", obj[4]);
	        jsonObject.put("vchFullName", obj[5]);
	        jsArray3.put(jsonObject);
	    }

	    return jsArray3;
	}
	
	
	private Map<Integer, String> fetchRoles(Set<Integer> roleIds) {
		String stringRoleIds = roleIds.stream()
									  .map(String::valueOf)
									  .collect(Collectors.joining(","));
	    String query = "SELECT intId, vchRoleName FROM m_admin_role WHERE intId IN (" + stringRoleIds + ")";
	    
	    List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);
	    Map<Integer, String> roleMap = new HashMap<>();
	    Integer roleId=0;
	    String roleName=null;
	    for (Object[] result : results) {
	    	 roleId=(Integer) result[0];
	    	 roleName=(String) result[1];
	        roleMap.put(roleId,roleName);
	    }
	    return roleMap;
	}

	private Map<Integer, String> fetchUsers(Set<Integer> userIds) {
		String stringUserIds = userIds.stream()
									  .map(String::valueOf)
									  .collect(Collectors.joining(","));
	    String query = "SELECT intId, vchFullName FROM m_admin_user WHERE intId IN (" + stringUserIds + ")";
	    
	    List<Object[]> results = CommonUtil.getDynResultList(entityManager, query);
	    Map<Integer, String> userMap = new HashMap<>();
	    Integer userId=0;
	    String userName=null;
	    for (Object[] result : results) {
	    	 userId=(Integer) result[0];
	    	 userName=(String) result[1];
	    	 userMap.put(userId,userName);
	    }
	    return userMap;
	}
	
	@Override
	public JSONObject deleteById(Integer id) {
		logger.info("Inside deleteById method of TrolepermissionServiceImpl");
		JSONObject json = new JSONObject();
		try {
			Trolepermission entity = trolepermissionRepository.findByIntIdAndBitDeletedFlag(id, false);
			entity.setBitDeletedFlag(true);
			trolepermissionRepository.save(entity);
			json.put("status", 200);
		} catch (Exception e) {
			logger.error("TrolepermissionServiceImpl:deleteById()",e);
			json.put("status", 400);
		}
		return json;
	}

	public static JSONArray fillselSelectRoleList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselSelectRoleList method of TrolepermissionServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select intId,vchRoleName from m_admin_role";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("intId", data[0]);
			jsonObj.put("vchRoleName", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselSelectUserList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselSelectUserList method of TrolepermissionServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select intId,vchFullName from m_admin_user";
		List<Object[]> dataList =CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("intId", data[0]);
			jsonObj.put("vchFullName", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	@Override
	public JSONArray bindAllMenuLinks(String data) {
		logger.info("Inside bindAllMenuLinks method of TrolepermissionServiceImpl");
		JSONObject jsobj=new JSONObject(data);
		JSONArray json = new JSONArray();
		try {
			if(jsobj.getInt("permissionfor")==1) {
				List<Trolepermission> rolePermission=trolepermissionRepository.findByRoleId(jsobj.getInt("roleid"),false);
			   if(!rolePermission.isEmpty()) {
				   
				   json= getUpdatedData(jsobj.getInt("roleid"));
				  
			   }else {
				 
				   json=getFirstTimeData();
			   }
			
			}else {
				List<Trolepermission> roleForUser=trolepermissionRepository.findByUserId(jsobj.getInt("roleid"),false);
			     if(!roleForUser.isEmpty()) {
			    	 json= getUserTypeData(jsobj.getInt("roleid"));
			     }else {
			    	 json= getFirstTimeData();
			     }
			
			}
		}catch(Exception e){
			logger.error("Inside bindAllMenuLinks method of TrolepermissionServiceImpl some error occur:" + e);
		}
		return json;
	}

	private JSONArray getFirstTimeData() {
		logger.info("Inside getFirstTimeData method of TrolepermissionServiceImpl");
		JSONArray jsArray=new JSONArray();
		List<Tmenulinks> tmenuData= tmenulinksRepository.findModuleByIntParentId(false);
		for(Tmenulinks tmenuLinks:tmenuData) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("intId", tmenuLinks.getIntId());
		    jsonObject.put("intParentLinkId", tmenuLinks.getSelParentLink());
		    jsonObject.put("selLinkType", tmenuLinks.getSelLinkType());
		    jsonObject.put("txtLinkName", tmenuLinks.getTxtLinkName());
		    jsArray.put(jsonObject);
		    List<Tmenulinks> tData= tmenulinksRepository.findByIntParentLinkIdAndIntId(tmenuLinks.getIntId(),false);
		    for(Tmenulinks tmean:tData) {
		    	JSONObject jsonObj=new JSONObject();
		    	jsonObj.put("intId", tmean.getIntId());
		    	jsonObj.put("intParentLinkId", tmean.getSelParentLink());
		    	jsonObj.put("selLinkType", tmean.getSelLinkType());
		    	jsonObj.put("txtLinkName", tmean.getTxtLinkName());
		    
		    	jsArray.put(jsonObj);
		    }
	
		
		
		}
		
		return jsArray;
	}
	
	private List<Tmenulinks> getMenuLinkListInOrder(){
		logger.info("Inside getMenuLinkListInOrder method of TrolepermissionServiceImpl");
		List<Tmenulinks> menuList = new ArrayList<>();
		List<Tmenulinks> tmenuData= tmenulinksRepository.findModuleByIntParentId(false);
		for(Tmenulinks tmenuLinks:tmenuData) {
			menuList.add(tmenuLinks);
			 List<Tmenulinks> tData= tmenulinksRepository.findByIntParentLinkIdAndIntId(tmenuLinks.getIntId(),false);
			 menuList.addAll(tData);
		}
		return menuList;
	}



	private JSONArray getUserTypeData(Integer userId) {
		logger.info("Inside getUserTypeData method of TrolepermissionServiceImpl");
		 JSONArray jsArray=new JSONArray();
		try {
			List<Tmenulinks> tMenuLinkList =this.getMenuLinkListInOrder();
			List<Trolepermission> tUserList=trolepermissionRepository.findByUserId(userId,false);
			for (Tmenulinks tMenu : tMenuLinkList) {
			    JSONObject jsonObject = new JSONObject();
			    jsonObject.put("intId", tMenu.getIntId());
			    jsonObject.put("intParentLinkId", tMenu.getSelParentLink());
			    jsonObject.put("selLinkType", tMenu.getSelLinkType());
			    jsonObject.put("txtLinkName", tMenu.getTxtLinkName());
			    
			    Trolepermission matchingUser = null;
			    for (Trolepermission tUser : tUserList) {
			        if (Integer.parseInt(tUser.getVchLinkId()) == tMenu.getIntId()) {
			        	matchingUser = tUser;
			            break;
			        }
			    }
			    if (matchingUser != null) {
			        jsonObject.put("intViewManageRight", matchingUser.getIntViewManageRight());
			        jsonObject.put("intEditRight", matchingUser.getIntEditRight());
			        jsonObject.put("intDelete", matchingUser.getIntDelete());
			        jsonObject.put("publish", matchingUser.getPublish());
			        jsonObject.put("intadd", matchingUser.getIntadd());
			        jsonObject.put("intall", matchingUser.getIntall());
			    }
			    jsArray.put(jsonObject);
			}

		}catch(Exception e) {
			logger.error("Inside getUserTypeData method of TrolepermissionServiceImpl some error occur:" + e);
		}
		return jsArray;
	}



	private JSONArray getUpdatedData(Integer roleId) {
		logger.info("Inside getUpdatedData method of TrolepermissionServiceImpl");
		  JSONArray jsArray=new JSONArray();
		try {
			List<Tmenulinks> tMenuLinkList =this.getMenuLinkListInOrder();
			List<Trolepermission> troleList = trolepermissionRepository.getByRoleId(roleId);

			for (Tmenulinks tMenu : tMenuLinkList) {
			    JSONObject jsonObject = new JSONObject();
			    jsonObject.put("intId", tMenu.getIntId());
			    jsonObject.put("intParentLinkId", tMenu.getSelParentLink());
			    jsonObject.put("selLinkType", tMenu.getSelLinkType());
			    jsonObject.put("txtLinkName", tMenu.getTxtLinkName());

			    Trolepermission matchingRole = null;
			    for (Trolepermission tRole : troleList) {
			        if (Integer.parseInt(tRole.getVchLinkId()) == tMenu.getIntId()) {
			            matchingRole = tRole;
			            break;
			        }
			    }

			    if (matchingRole != null) {
			        jsonObject.put("intViewManageRight", matchingRole.getIntViewManageRight());
			        jsonObject.put("intEditRight", matchingRole.getIntEditRight());
			        jsonObject.put("intDelete", matchingRole.getIntDelete());
			        jsonObject.put("publish", matchingRole.getPublish());
			        jsonObject.put("intadd", matchingRole.getIntadd());
			        jsonObject.put("intall", matchingRole.getIntall());
			    }

			    jsArray.put(jsonObject);
			}

		
		    
		}catch(Exception e) {
			logger.error("Inside getUpdatedData method of TrolepermissionServiceImpl some error occur:" + e);
		}
		    return jsArray;
	}
	
	
	
	
	@Override
	public JSONObject givePermissionDetails(String data) {
		logger.info("Inside givePermissionDetails method of TrolepermissionServiceImpl");
		String newData = CommonUtil.inputStreamDecoder(data);
		JSONObject jsObj=new JSONObject(newData);
		JSONObject job=new JSONObject();
		try {
			Integer roleId=jsObj.getInt("RoleId");
			Integer linkId=jsObj.getInt("linkId");
			Integer userId=jsObj.getInt("userId");
			Integer isAdmin=tUserRepository.getcheckPrevilegeByUserId(userId,false);
			List<Trolepermission> rolePermission= trolepermissionRepository.getRolePermissionListByUserId(userId,false);
			if(roleId == 0) {
				job.put("add", 1);
				job.put("view", 1);
				job.put("edit", 1);
				job.put("delete", 1);
				job.put("publish", 1);
			}else {
				Trolepermission trolePermissionUser=trolepermissionRepository.findBySelUserAndIntLinkIdAndBitDeletedFlag(userId, linkId.toString(), false);
				if(!rolePermission.isEmpty() && isAdmin!=2 && trolePermissionUser!=null) {
					
					    job.put("add", trolePermissionUser.getIntadd());
						job.put("view", trolePermissionUser.getIntViewManageRight());
						job.put("edit", trolePermissionUser.getIntEditRight());
						job.put("delete", trolePermissionUser.getIntDelete());
						job.put("publish", trolePermissionUser.getIntDelete());
				}else {
				Trolepermission trolePermissionRole=trolepermissionRepository.findByIntRoleAndIntLinkIdAndBitDeletedFlag(roleId, linkId.toString(), false);
				job.put("add", trolePermissionRole.getIntadd());
				job.put("view", trolePermissionRole.getIntViewManageRight());
				job.put("edit", trolePermissionRole.getIntEditRight());
				job.put("delete", trolePermissionRole.getIntDelete());
				job.put("publish", trolePermissionRole.getIntDelete());
				}
			}
		}catch(Exception e) {
			logger.error("Inside givePermissionDetails method of TrolepermissionServiceImpl some error occur:" + e);
		}
		
		return job;
	}



	@Override
	public List<Trolepermission> getRolePermissionListByUserId(Integer userId) {
		logger.info("Inside getRolePermissionListByUserId method of TrolepermissionServiceImpl");
		
		return trolepermissionRepository.getRolePermissionListByUserId(userId,false);
	}

}
