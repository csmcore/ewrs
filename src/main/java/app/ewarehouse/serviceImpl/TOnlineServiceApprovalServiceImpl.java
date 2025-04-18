package app.ewarehouse.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import app.ewarehouse.entity.ApplicationOfConformity;
import app.ewarehouse.entity.ComplaintMgmtInspScheduleDocs;
import app.ewarehouse.entity.OnlineServiceApprovalNotings;
import app.ewarehouse.entity.Supporting_document;
import app.ewarehouse.entity.TOnlineServiceApproval;
import app.ewarehouse.entity.TOnlineServiceQueryDocument;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.repository.ApplicationOfConformityRepository;
import app.ewarehouse.repository.ComplaintIcmRepository;
import app.ewarehouse.repository.ComplaintMgmtInspScheduleDocsRepository;
import app.ewarehouse.repository.OnlineServiceApprovalNotingsRepository;
import app.ewarehouse.repository.TOnlineServiceApprovalRepository;
import app.ewarehouse.repository.TOnlineServiceQueryDocumentRepository;
import app.ewarehouse.repository.TSetAuthorityRepository;
import app.ewarehouse.repository.TmenulinksRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.TOnlineServiceApprovalService;
import app.ewarehouse.util.DateTimeUtility;
import app.ewarehouse.util.FnAuthority;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class TOnlineServiceApprovalServiceImpl implements TOnlineServiceApprovalService {

	
	@Autowired
	private TOnlineServiceApprovalRepository tOnlineServiceApprovalRepo;
	
	@Autowired
	private ApplicationOfConformityRepository appRepo;
	
	@Autowired
	TSetAuthorityRepository tsetAthorityRepository;
	@Autowired
    private ComplaintIcmRepository complaintIcmRepository;
	@Autowired
	private FnAuthority fnAuthority;
	@Autowired

	private TmenulinksRepository tmenulinksRepository;

	@Autowired
	private TOnlineServiceQueryDocumentRepository tOnlineServiceQueryDocumentRepository;

	@Autowired
	private OnlineServiceApprovalNotingsRepository onlineServiceApprovalNotingsRepository;

	@Autowired
	private TuserRepository tuserRepository;
	@Autowired
	private ComplaintMgmtInspScheduleDocsRepository docRepo;
	@Value("${tempUpload.path}")
	private String tempUploadPath;

	@Value("${queryFileUpload.path}")
	private String queryFileUploadPath;
	JSONObject respdata = new JSONObject();
	JSONObject getAuthArr = new JSONObject();
	JSONObject rdoGender = new JSONObject("{1:M,2:F}");

	@Override
	public JSONObject getTakeActionDetails(String data) {

		JSONObject jsonObj = new JSONObject(data);
		Integer serviceId = jsonObj.getInt("onlineServiceId");
		Integer roleId = jsonObj.getInt("roleId");
		Integer userId = jsonObj.getInt("intuserId");
		Integer intId = jsonObj.getInt("intId");
		Integer stageNo = jsonObj.getInt("stageNo");
		Integer labelId=Integer.parseInt(jsonObj.getString("labelId"));
		Integer applicationRoleId = Integer.parseInt(jsonObj.getString("applicationRoleId"));

		String status = "200";
		String outMsg = "Success!!";

		JSONObject arrActionDetails = new JSONObject();
		JSONArray fwdActionDetails = new JSONArray();
		List<Map<String, Object>> actionList = new ArrayList<>();
		if(applicationRoleId == 52 ||applicationRoleId == 12) {
			if(stageNo==3||stageNo==2) {
				actionList = tOnlineServiceApprovalRepo.getDataByproceeIdAndroleIdAndStageNo(intId,2, applicationRoleId, serviceId,labelId);
			}
			else if(stageNo==5||stageNo==6) {
				actionList = tOnlineServiceApprovalRepo.getDataByproceeIdAndroleIdAndStageNo(intId,5, applicationRoleId, serviceId,labelId);
			}
			else {
				actionList = tOnlineServiceApprovalRepo.getDataByproceeIdAndroleIdAndStageNo(intId,stageNo, applicationRoleId, serviceId,labelId);	
			}
			
		}else {
		actionList = tOnlineServiceApprovalRepo.getDataByproceeIdAndroleIdAndStageNo(intId,stageNo, roleId, serviceId,labelId);
		}

		if (!actionList.isEmpty()) {
			for (Map<String, Object> mapData : actionList) {
				JSONObject actionDetails = new JSONObject();
				actionDetails.put("intSetAuthId", (Integer) mapData.get("intSetAuthId"));
				actionDetails.put("intPrimaryAuth", (Integer) mapData.get("intPrimaryAuth"));
				actionDetails.put("jsnVerifyDocument", mapData.get("jsnVerifyDocument"));
				actionDetails.put("vchAuthTypes", Arrays.asList(mapData.get("vchAuthTypes").toString().split(",")));
				actionDetails.put("tinStatus", (Integer) mapData.get("tinStatus"));
				arrActionDetails.put("actionDetails", actionDetails);
			}
		} else {
			List<TOnlineServiceApproval> allForwardActions = tOnlineServiceApprovalRepo.getAllForwardActionData(serviceId, roleId, intId, userId);

			if (allForwardActions != null) {
				for (TOnlineServiceApproval resultTab : allForwardActions) {
					JSONObject fwdAction = new JSONObject();
					fwdAction.put("tinStatus", resultTab.getTinStatus());
					fwdAction.put("vchForwardAllAction", Arrays.asList(resultTab.getVchForwardAllAction().split(",")));
					fwdAction.put("intForwardedUserId", resultTab.getIntForwardedUserId());
					arrActionDetails.put("actionDetails", fwdAction);
				}

			}
		}

		JSONObject resultMap = new JSONObject();
		resultMap.put("status", status);
		resultMap.put("msg", outMsg);
		resultMap.put("result", arrActionDetails);

		return resultMap;
	}

	@Override
	public JSONObject getAllNotingDetails(String data) {
		JSONObject jsonObj = new JSONObject(data);
		Integer processId = jsonObj.getInt("intId");
		Integer serviceId = jsonObj.getInt("onlineServiceId");
		Integer roleId = jsonObj.getInt("roleId");
		Integer lableId = jsonObj.getInt("lableId");
		JSONArray notingDetails = new JSONArray();
		List<Object[]> onlineServiceApprovalNotings = onlineServiceApprovalNotingsRepository.getAllDetailsByServiceIdProcessId(serviceId, processId);
		for (Object[] obj : onlineServiceApprovalNotings) {
			JSONObject newObj = new JSONObject();

			Integer notingsId=Integer.parseInt(obj[0].toString());
		
			
			newObj.put("txtNoting", obj[3]);
			newObj.put("dtActionTaken", obj[4]);
			newObj.put("vchActionName", obj[7]);
			newObj.put("vchRolename", obj[8]);
			newObj.put("vchDocumentFile", obj[9]);
			newObj.put("vchDocumentName", obj[10]);
			Integer tintStatus = Integer.parseInt(obj[11].toString());
			newObj.put("tinStatus", tintStatus);
			if (tintStatus == 4) {
				newObj.put("userName", obj[12].toString());
			}
			
			Integer resDocListeId=Integer.parseInt(obj[17].toString());
			if(resDocListeId!=0) {
				List<ComplaintMgmtInspScheduleDocs> supporting_documentList = docRepo.findByParentIdAndBitDeletedFlag(resDocListeId, false);
				newObj.put("label", "Supporting Documents");
				newObj.put("SupportingDoc",supporting_documentList);
			}
              
              
              Integer intCreatedBy = Integer.parseInt(obj[18].toString());
			if(intCreatedBy!=0) {
				Tuser cm = tuserRepository.findById(intCreatedBy).orElse(null);
				if(cm != null) {
				newObj.put("userName", cm.getTxtFullName());
				}
			}
			if(obj[19]!=null) {
				newObj.put("susReason", obj[19]);
			}
			else {
				newObj.put("susReason", "-");
			}
			
			if(obj[20]!=null) {
				newObj.put("actionTaken", obj[20]);
			}
			else {
				newObj.put("actionTaken", "-");
			}
			if(obj[21]!=null) {
				newObj.put("actionTakenId", (Integer)obj[21]);
			}
			else {
				newObj.put("actionTakenId", 0);
			}
			if(obj[13] != null) {
			Integer docId = Integer.parseInt(obj[13].toString());
			ComplaintMgmtInspScheduleDocs doc = docRepo.findById(docId).get();
			newObj.put("docName", doc.getDocName());
			newObj.put("docPath", doc.getDocPath());
			}
			Integer stageNo=Integer.parseInt(obj[14].toString());
			newObj.put("intStageNo", stageNo);
			
			if(obj[15] != null) {	
				if(obj[15] == Integer.valueOf(101)) {
					newObj.put("complaintAgainst", "Warehosue operator");
				}
				else if(obj[15] == Integer.valueOf(102)) {
					newObj.put("complaintAgainst", "Colletral Manager");
					Integer intValue = (Integer)obj[16];
					Tuser cm = tuserRepository.findById(intValue).orElse(null);
					if(cm != null) {
					newObj.put("complaintAgainstName", cm.getTxtFullName());
					}
				}
				else if(obj[15] == Integer.valueOf(103)) {
					newObj.put("complaintAgainst", "Inspector");
					Integer intValue = (Integer) obj[16];
					Tuser cm = tuserRepository.findById(intValue).orElse(null);
					if(cm != null) {
					newObj.put("complaintAgainstName", cm.getTxtFullName());
					}
				}
				else if(obj[15] == Integer.valueOf(104)) {
					newObj.put("complaintAgainst", "Grader");
					Integer intValue = (Integer)obj[16];
					Tuser cm = tuserRepository.findById(intValue).orElse(null);
					if(cm != null) {
					newObj.put("complaintAgainstName", cm.getTxtFullName());
					}
				}
				else {
					newObj.put("complaintAgainst", "--");
					newObj.put("complaintAgainstName", "--");
				}
			}
			notingDetails.put(newObj);
		}
		
		//JSONArray originalArray = new JSONArray(notingDetails);
        JSONArray beforeList = new JSONArray();
        JSONArray afterList = new JSONArray();

        boolean foundTinStatus8 = false;
        if (notingDetails.length() > 0) {
            for (int i = 0; i < notingDetails.length(); i++) {
                JSONObject obj = notingDetails.getJSONObject(i);
                int tinStatus = obj.getInt("tinStatus");

                if (!foundTinStatus8) {
                    beforeList.put(obj); // Add to beforeList only before tinStatus = 8 is found
                }

                if (tinStatus == 8) {
                    foundTinStatus8 = true; // Mark that we reached tinStatus = 8
                } else if (foundTinStatus8) {
                    afterList.put(obj); // Add all remaining objects to afterList
                }
            }
        }

        
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", notingDetails);
		jsonObject.put("result1", beforeList);
		jsonObject.put("result2", afterList);
		jsonObject.put("status", 200);
		jsonObject.put("outMsg", "success!!");
        
		return jsonObject;
	}

	@Override
	public JSONObject getQueryDetailsByUsingData(String data) {
		JSONObject jsObj = new JSONObject(data);
		String outMsg = "";
		String status = "";
		JSONObject newData = new JSONObject();
		JSONObject responseData = new JSONObject();
		try {
			Integer intProcessId = jsObj.getInt("intProcessId");
			Integer intOnlineServiceId = jsObj.getInt("intOnlineServiceId");
			OnlineServiceApprovalNotings queryData = onlineServiceApprovalNotingsRepository
					.getDataBYUsingprocessIdAndServiceId(intProcessId, intOnlineServiceId);

			newData.put("txtNoting", queryData.getTxtNoting());
			newData.put("dtActionTaken",
					new SimpleDateFormat("dd MMM yyyy hh.mm a").format(queryData.getDtActionTaken()));
			newData.put("schemeName", tmenulinksRepository.getVchLinkNameByUsingintProcessId(intProcessId));
			
			status = "200";
			outMsg = "Success !!!";

		} catch (Exception e) {
			log.error("TOnlineServiceApprovalServiceImpl:getQueryDetailsByUsingData()",e);
			status = "400";
			outMsg = "Something Went Wrong !!!!";
		}

		responseData.put("status", status);
		responseData.put("result", newData);
		responseData.put("outMsg", outMsg);

		return responseData;
	}

	@Override
	public JSONObject saveIntoQueryDetails(JSONObject jsObj) {

		JSONObject jsObject = new JSONObject();
		String status = "";
		String outMsg = "";
		try {
			Integer intId = jsObj.getInt("processId");
			Integer onlineServiceId = jsObj.getInt("serviceId");
			String remark = jsObj.getString("remark");
			String lableId = jsObj.getString("lableId");
			
			Integer labelIdValue=0;
			
			if(lableId!=null&&lableId.length()>0) {
				
				labelIdValue=Integer.parseInt(lableId);
				
			}
			
			
			Long currentTime = System.currentTimeMillis();
			Date date = new Date(currentTime);
			TOnlineServiceApproval onlineServiceApproval = tOnlineServiceApprovalRepo.findByIntProcessIdAndIntOnlineServiceIdAndBitDeletedFlag(intId, onlineServiceId,labelIdValue);

			OnlineServiceApprovalNotings notings = onlineServiceApprovalNotingsRepository.getDataBYUsingprocessIdAndServiceId(intId, onlineServiceId);

			onlineServiceApproval.setTinStatus(9);
			onlineServiceApproval.setDtmStatusDate(date);
			onlineServiceApproval.setIntPendingAt(onlineServiceApproval.getVchATAAuths());
			onlineServiceApproval.setVchATAAuths("0");
			onlineServiceApproval.setIntStageNo(notings.getTinStageCtr());

			
			  tOnlineServiceApprovalRepo.save(onlineServiceApproval);
			  
			  Integer action = onlineServiceApproval.getTinStatus();
			  
				OnlineServiceApprovalNotings insertNoting = new OnlineServiceApprovalNotings();
				insertNoting.setIntOnlineServiceId(onlineServiceId);
				insertNoting.setIntProcessId(intId);
				insertNoting.setIntFromAuthority(0);
				insertNoting.setIntToAuthority(String.valueOf(notings.getTinStageCtr()));
				insertNoting.setDtActionTaken(date);
				insertNoting.setIntStatus(action);
				insertNoting.setTxtNoting(remark);
				insertNoting.setTinStageCtr(0);

				OnlineServiceApprovalNotings onlineServiceApprovalNotings = onlineServiceApprovalNotingsRepository
						.saveAndFlush(insertNoting);

				JSONArray jsonArray = new JSONArray(jsObj.getString("dynamicListArr"));
				String comString = jsObj.getString("arrUploadedFiles").replace("\"", "");

				String[] splitString = comString.replace("[", "").replace("]", "").split(",");

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject objjs = jsonArray.getJSONObject(i);
					TOnlineServiceQueryDocument queryDetails = new TOnlineServiceQueryDocument();
					queryDetails.setIntNotingId(onlineServiceApprovalNotings.getIntNotingsId());
					queryDetails.setIntOnlineServiceId(onlineServiceId);
					queryDetails.setVchDocumentName(objjs.getString("vchDocumentName"));
					queryDetails.setVchDocumentFile(splitString[i]);
					tOnlineServiceQueryDocumentRepository.save(queryDetails);
				}
				status = "200";
				outMsg = "Success!!!!";

				for (int i = 0; i < splitString.length; i++) {
					String fileUpload = splitString[i];
					if (!fileUpload.equals("")) {
						File f = new File(tempUploadPath + fileUpload);
						if (f.exists()) {
							File src = new File(tempUploadPath + fileUpload);
							File dest = new File(queryFileUploadPath + fileUpload);
							try {
								Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
								Files.delete(src.toPath());
							} catch (IOException e) {
								System.out.println("Iniside Error");
							}
						}
					}
				}
			 

		} catch (Exception e) {
			log.error("TOnlineServiceApprovalServiceImpl:getAllForwradAuthority()",e);
			status = "400";
			outMsg = "Something went wrong!!!!";
		}

		// file shift is pending---------
		jsObject.put("status", status);
		jsObject.put("outMsg", outMsg);
		return jsObject;
	}

	@Override
	public JSONObject noResubmitStatus(String data) {
		JSONObject jsonObject = new JSONObject(data);
		List<Object[]> getResubmitHistory = onlineServiceApprovalNotingsRepository
				.getResubmitHistoryByOnlineServiceIdAndProcessId(jsonObject.getInt("processId"),
						jsonObject.getInt("serviceId"));
		JSONArray resubmitHsitory = new JSONArray();
		for (Object[] obj : getResubmitHistory) {
			resubmitHsitory.put(new JSONObject().put("historyId", obj[0]).put("intFromAuthority", obj[1])
					.put("intToAuthority", obj[2]).put("dtActionTaken", obj[3]).put("txtNoting", obj[4])
					.put("roleName", obj[5].toString()).put("serviceId", jsonObject.getInt("serviceId")));
		}
		JSONObject response = new JSONObject();
		response.put("status", 200);
		response.put("result", resubmitHsitory);
		return response;
	}

	@Override
	public JSONObject getAllForwradAuthority(String decodedStr) {
		JSONObject response = new JSONObject();
		try {
			JSONObject data = new JSONObject(decodedStr);
			Integer roleId = Integer.parseInt(data.get("intRoleId").toString());
			List<Map<String, Object>> userList = tuserRepository.findUserListByRoleId(roleId);
			JSONArray jsArr = new JSONArray();
			if (!userList.isEmpty()) {
				for (Map<String, Object> tuser : userList) {
					JSONObject jsb = new JSONObject();
					jsb.put("intUserId", (Integer) tuser.get("intId"));
					jsb.put("userName", tuser.get("vchFullName").toString());
					jsb.put("roleName", tuser.get("vchRoleName").toString());
					jsArr.put(jsb);
				}
				response.put("result", jsArr);
				response.put("status", 200);

			} else {
				response.put("status", 417);
			}

		} catch (Exception e) {
			response.put("status", 400);
			log.error("TOnlineServiceApprovalServiceImpl:getAllForwradAuthority()",e);
		}
		return response;
	}

	@Override
	public JSONObject checkUserSusStatus(Integer lableId, Integer onlineServiceId) throws Exception {
		JSONObject jsonObj = new JSONObject();
		try {
			
			Integer suspensionStatus=1;
			
			if(lableId == 100) {
				suspensionStatus =  tOnlineServiceApprovalRepo.checkLicenseSuspension(lableId,onlineServiceId);
			}
			
			if(lableId == 101) {
				suspensionStatus =  tOnlineServiceApprovalRepo.checkCertificateSuspension(lableId,onlineServiceId);
			}
			
			if((lableId == 104 || lableId == 103 || lableId == 102 )) {
				suspensionStatus =  tOnlineServiceApprovalRepo.checkUserSuspension(lableId,onlineServiceId);
			}

			
			if (suspensionStatus==1 && lableId == 100) {
				jsonObj.put("msg", "The Warehouse Operator license has already been suspended.");
				jsonObj.put("suspStatus", true);
				jsonObj.put("status", 200);
				jsonObj.put("lableId", lableId);

			} else if (suspensionStatus==1 && lableId == 101) {
				jsonObj.put("msg", "The Warehouse Operator certificate has already been suspended.");
				jsonObj.put("suspStatus", true);
				jsonObj.put("status", 200);
				jsonObj.put("lableId", lableId);
			} 
			else if(suspensionStatus==1 && lableId == 102) {
				
				jsonObj.put("msg", "The Collateral Manager has already been suspended.");
				jsonObj.put("suspStatus", true);
				jsonObj.put("status", 200);
				jsonObj.put("lableId", lableId);
			}
			else if(suspensionStatus==1 && lableId == 103) {
				jsonObj.put("msg", "The Inspector has already been suspended.");
				jsonObj.put("suspStatus", true);
				jsonObj.put("status", 200);
				jsonObj.put("lableId", lableId);
			}
			else if(suspensionStatus==1 && lableId == 104) {
				
				jsonObj.put("msg", "The Grader has already been suspended.");
				jsonObj.put("suspStatus", true);
				jsonObj.put("status", 200);
				jsonObj.put("lableId", lableId);
			}
			else {
				jsonObj.put("msg", "Action  can be taken");
				jsonObj.put("suspStatus", false);
				jsonObj.put("lableId", lableId);
				jsonObj.put("status", 200);
			}

		} catch (Exception e) {
			jsonObj.put("msg", "Internal Problem Occured");
			jsonObj.put("suspStatus", true);
			jsonObj.put("status", 201);
		}

		return jsonObj;
	}

}
