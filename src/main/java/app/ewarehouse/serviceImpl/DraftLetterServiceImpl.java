package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import app.ewarehouse.entity.DraftLetterEntity;
 import app.ewarehouse.entity.DynamicLetterConfig;
 import app.ewarehouse.entity.OnlineServiceApprovalNotings;
 import app.ewarehouse.entity.TOnlineServiceApproval;
 import app.ewarehouse.entity.Tmenulinks;
 import app.ewarehouse.repository.DraftLetterRepository;
 import app.ewarehouse.repository.LetterConfigurationRepository;
 import app.ewarehouse.util.FnAuthority;
 import app.ewarehouse.repository.OnlineServiceApprovalNotingsRepository;
 import app.ewarehouse.repository.TOnlineServiceApprovalRepository;
 import app.ewarehouse.repository.TSetAuthorityRepository;
 import app.ewarehouse.repository.TmenulinksRepository;
 import app.ewarehouse.service.DraftLetterService;
@Service
public class DraftLetterServiceImpl implements DraftLetterService {

	@Autowired
	private DraftLetterRepository draftLetterRepository;
	@Autowired
	private TmenulinksRepository tmenuLinksRepository;
	@Autowired
	private FnAuthority fnAuthority;
	@Autowired
	private LetterConfigurationRepository letterConfigurationRepository;
	@Autowired
	private TOnlineServiceApprovalRepository tOnlineServiceApprovalRepository;
	@Autowired
	private OnlineServiceApprovalNotingsRepository onlineServiceApprovalNotingsRepository;
	@Autowired
	TSetAuthorityRepository tSetAuthorityRepository;
	@Autowired
	private TSetAuthorityRepository tsetAutoritiesRepo;

	public static final String PENDING_AUTHORITIES = "pendingAuthorities";
	public static final String STAGENO = "stageNo";
	public static final String ATAPROCESSID = "intATAProcessId";
    private static final Logger logger = LoggerFactory.getLogger(DraftLetterServiceImpl.class);
	JSONObject resp = new JSONObject();

	@Override
	public JSONObject saveDraftLetter(String data) {
		 logger.info("Inside saveDraftLetter method of DraftLetterServiceImpl");
		JSONObject getAuthArr = new JSONObject();
		JSONObject jsObject = new JSONObject(data);
		Integer intServiceId = Integer.parseInt(jsObject.getString("intServiceId"));
		Integer intLetterConfigId = Integer.parseInt(jsObject.getString("intLetterConfigId"));
		DraftLetterEntity saveData = new DraftLetterEntity();
		saveData.setIntLetterConfigId(Integer.parseInt(jsObject.getString("intLetterConfigId")));
		DynamicLetterConfig dynamicLetterConfig = letterConfigurationRepository
				.findById(saveData.getIntLetterConfigId()).get();
		saveData.setVchLetterTitle(dynamicLetterConfig.getTxtLetterName());
		saveData.setTxtLetterContent(jsObject.getString("txtLetterContent"));
		saveData.setIntApplicationId(intServiceId);
		saveData.setVchGenerateStatus(0);
		DraftLetterEntity draftLetterEntity = draftLetterRepository
				.findByIntLetterConfigIdAndIntApplicationIdAndBitDeletedFlag(intLetterConfigId, intServiceId, false);
		if (draftLetterEntity != null) {
			saveData.setIntConfigId(draftLetterEntity.getIntConfigId());

		}
		DraftLetterEntity draftLetter = draftLetterRepository.save(saveData);
		Integer processId = dynamicLetterConfig.getIntFormId();
		resp.put("status", 200);
		resp.put("result", new JSONObject(draftLetter));

		return resp;
	}

	public JSONObject getLetterById(Integer intConfigId) {
		 logger.info("Inside getLetterById method of DraftLetterServiceImpl");
		try {
			DraftLetterEntity draftLetter = draftLetterRepository.findByIntConfigIdAndBitDeletedFlag(intConfigId,false);
			resp.put("status", 200);
			resp.put("letterTitel", draftLetter.getVchLetterTitle());
			resp.put("result", draftLetter.getTxtLetterContent());
		} catch (Exception e) {
			logger.error("DraftLetterServiceImpl:getLetterById()", e);
			resp.put("status", 400);
		}
		return resp;
	}

	@Override
	public JSONObject generateStatusByintConfigId(Integer intConfigId,Integer processId,Integer roleId) {
		 logger.info("Inside generateStatusByintConfigId method of DraftLetterServiceImpl");
		try {
			DraftLetterEntity draftLetter = draftLetterRepository.findByIntConfigIdAndBitDeletedFlag(intConfigId,
					false);
			draftLetter.setVchGenerateStatus(1);
			DraftLetterEntity draftLetterupdate = draftLetterRepository.save(draftLetter);
			resp.put("status", 200);
			resp.put("result", new JSONObject(draftLetterupdate));

		} catch (Exception e) {
			logger.error("Inside generateStatusByintConfigId method of DraftLetterServiceImpl error occur:",e);
			resp.put("status", 400);
		}
		return resp;
	}

	@Override
	public String getLetterContentByIntConfigId(Integer intLetterConfigId, Integer intServiceId) {

		 logger.info("Inside getLetterContentByIntConfigId method of DraftLetterServiceImpl");

		String content = draftLetterRepository.getLetterContentByIntConfigId(intLetterConfigId, intServiceId);
		return content;
	}

	@Override
	public String getIntLetterConfigIdByIntConfigId(Integer intConfigId) {

		 logger.info("Inside getIntLetterConfigIdByIntConfigId method of DraftLetterServiceImpl");

		JSONObject json = new JSONObject();

		String content = draftLetterRepository.getIntLetterConfigIdByIntConfigId(intConfigId);
		return content;

	}

	@Override
	public String saveDocumentByIds(String fileData, Integer processId, Integer serviceId,Integer roleId) {
		logger.info("Inside saveDocumentByIds method of DraftLetterServiceImpl");
		 JSONObject getAuthArr = new JSONObject();
		TOnlineServiceApproval tOnlineServiceApproval = tOnlineServiceApprovalRepository
				.getAllDataByUsingIntIdAndOnlineServiceId(processId, serviceId);

		tOnlineServiceApproval.setVchApprovalDoc(fileData);
		tOnlineServiceApproval.setTinVerifyLetterGenStatus(1);
		tOnlineServiceApprovalRepository.save(tOnlineServiceApproval);
		
		Integer tinStatus = tOnlineServiceApproval.getTinStatus();
		OnlineServiceApprovalNotings notings = onlineServiceApprovalNotingsRepository
				.getDataBYUsingprocessIdAndServiceId(processId, serviceId);
String[] pendingData = tOnlineServiceApproval.getIntPendingAt().split(",");
		List<String> intPending = new ArrayList<>();
		for (String value : pendingData) {
			intPending.add(value);
		}

		if (tinStatus == 1 || tinStatus == 11) {
			
			if (intPending.size() > 1) {
				intPending.remove(roleId.toString());
				String intPendingString = String.join(",", intPending);

				notings.setIntToAuthority(intPendingString);

				tOnlineServiceApproval.setIntPendingAt(intPendingString);
				tOnlineServiceApproval.setIntForwardTo(intPendingString);
				tOnlineServiceApproval.setTinVerifiedBy(0);
				tOnlineServiceApproval.setIntForwardedUserId(0);

			}else {
			getAuthArr = fnAuthority.getAuthority(processId, tOnlineServiceApproval.getIntStageNo() + 1,0);

			if (getAuthArr != null) {
				String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
				Integer newStageNo = getAuthArr.getInt(STAGENO);
				Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);

				notings.setIntToAuthority(pendingAt.toString());

				tOnlineServiceApproval.setIntStageNo(newStageNo);
				tOnlineServiceApproval.setIntPendingAt(pendingAt.toString());
				tOnlineServiceApproval.setIntATAProcessId(intATAProcessId);
				tOnlineServiceApproval.setIntForwardTo(pendingAt);
				tOnlineServiceApproval.setTinVerifiedBy(0);
				tOnlineServiceApproval.setIntForwardedUserId(0);
			}
			}
			

		}else if(tinStatus==8) {
			notings.setIntToAuthority("0");
			notings.setTinStageCtr(0);

			tOnlineServiceApproval.setIntStageNo(0);
			tOnlineServiceApproval.setIntPendingAt("0");
			tOnlineServiceApproval.setIntATAProcessId(0);
			tOnlineServiceApproval.setIntForwardTo("0");
			tOnlineServiceApproval.setTinResubmitStatus(0);
		}
		onlineServiceApprovalNotingsRepository.save(notings);
		tOnlineServiceApprovalRepository.save(tOnlineServiceApproval);
		Tmenulinks menuLinks = tmenuLinksRepository.findByIntIdAndBitDeletedFlag(processId, false);

		String formData = menuLinks.getVchViewUrl();
		String[] parts = formData.split("/");
		String output = parts[parts.length - 1];

		return output;
	}


	

	@Override
	public JSONObject generateStatuByRoleId(Integer intServiceId) {
		logger.info("Inside generateStatuByRoleId method of DraftLetterServiceImpl");

		List<DraftLetterEntity> letterList = draftLetterRepository.findByIntApplicationIdAndBitDeletedFlag(intServiceId,
				false);
		Integer length = letterList.size();
		resp.put("result", length);
		return resp;
	}

	@Override
	public JSONObject saveInApprovalForWorkFolw(String data) {
		logger.info("Inside saveInApprovalForWorkFolw method of DraftLetterServiceImpl");
		JSONObject jsb = new JSONObject(data);
		JSONObject getAuthArr = new JSONObject();
		Integer processId = jsb.getInt("processId");
		Integer serviceId = jsb.getInt("serviceId");
		TOnlineServiceApproval tOnlineServiceApproval = tOnlineServiceApprovalRepository
				.getAllDataByUsingIntIdAndOnlineServiceId(processId, serviceId);

		Integer tinStatus = tOnlineServiceApproval.getTinStatus();
		OnlineServiceApprovalNotings notings = onlineServiceApprovalNotingsRepository
				.getDataBYUsingprocessIdAndServiceId(processId, serviceId);

		String[] pendingData = tOnlineServiceApproval.getIntPendingAt().split(",");
		List<String> intPending = new ArrayList<>();
		for (String value : pendingData) {
			intPending.add(value);
		}
		
		if (tinStatus == 1 || tinStatus == 11) {
			
			if(intPending.size() > 1) {
				intPending.remove(jsb.get("roleId").toString());
				String intPendingString = String.join(",", intPending);

				notings.setIntToAuthority(intPendingString);

				
				tOnlineServiceApproval.setIntPendingAt(intPendingString);
				tOnlineServiceApproval.setIntForwardTo(intPendingString);
				tOnlineServiceApproval.setTinVerifiedBy(0);
				tOnlineServiceApproval.setIntForwardedUserId(0);
				
			}else {
				getAuthArr = fnAuthority.getAuthority(processId, tOnlineServiceApproval.getIntStageNo() + 1,0);

				if (getAuthArr != null) {
					String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
					Integer newStageNo = getAuthArr.getInt(STAGENO);
					Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);

					notings.setIntToAuthority(pendingAt.toString());

					tOnlineServiceApproval.setIntStageNo(newStageNo);
					tOnlineServiceApproval.setIntPendingAt(pendingAt.toString());
					tOnlineServiceApproval.setIntATAProcessId(intATAProcessId);
					tOnlineServiceApproval.setIntForwardTo(pendingAt);
					tOnlineServiceApproval.setTinVerifiedBy(0);
					tOnlineServiceApproval.setIntForwardedUserId(0);
				}
			}
			

		}else if(tinStatus==8) {
			notings.setIntToAuthority("0");
			notings.setTinStageCtr(0);

			tOnlineServiceApproval.setIntStageNo(0);
			tOnlineServiceApproval.setIntPendingAt("0");
			tOnlineServiceApproval.setIntATAProcessId(0);
			tOnlineServiceApproval.setIntForwardTo("0");
			tOnlineServiceApproval.setTinResubmitStatus(0);
		}
		onlineServiceApprovalNotingsRepository.save(notings);
		tOnlineServiceApprovalRepository.save(tOnlineServiceApproval);
		

		Tmenulinks menuLinks = tmenuLinksRepository.findByIntIdAndBitDeletedFlag(processId, false);

		String formData = menuLinks.getVchViewUrl();
		String[] parts = formData.split("/");
		String output = parts[parts.length - 1];

		resp.put("status", 200);
        resp.put("result", output);
		return resp;   
	}

	

}
