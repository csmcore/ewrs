package app.ewarehouse.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import app.ewarehouse.entity.Tmenulinks;
 import app.ewarehouse.entity.DynamicLetterConfig;
  import app.ewarehouse.util.CommonUtil;
 import app.ewarehouse.repository.LetterConfigurationRepository;
 import app.ewarehouse.repository.TmenulinksRepository;
  import app.ewarehouse.service.LetterConfigurationService;
 import app.ewarehouse.service.TmenulinksService;
 import app.ewarehouse.entity.DraftLetterEntity;
 import app.ewarehouse.entity.DynamicKeys;
 import app.ewarehouse.entity.TOnlineServiceApproval;
 import app.ewarehouse.entity.TSetAuthority;
 import app.ewarehouse.repository.DraftLetterRepository;
 import app.ewarehouse.repository.DynamicKeysRepository;
 import app.ewarehouse.repository.TOnlineServiceApprovalRepository;
 import app.ewarehouse.repository.TSetAuthorityRepository;
 import app.ewarehouse.util.FnAuthority;
@Service
public class LetterConfigurationServiceImpl implements LetterConfigurationService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	EntityManager entityManager;
	@Autowired
	private TmenulinksRepository tmenuLinksRepository;
	@Autowired
	private LetterConfigurationRepository letterConfigurationRepository;
	@Autowired
	private DynamicKeysRepository dynamicKeysRepository;
	@Autowired
	private DraftLetterRepository draftLetterRepository;
	@Autowired
	private FnAuthority fnAuthority;
	@Autowired
	private TOnlineServiceApprovalRepository tOnlineServiceApprovalRepository;
	@Autowired
	private TSetAuthorityRepository tsetAuthorityRepository;
	Object dynamicValue = null;

	private static final Logger logger = LoggerFactory.getLogger(LetterConfigurationServiceImpl.class);

	private static final Map<Integer, String> GATEWAYTYPE = Map.of(1, "Mail", 2, "SMS", 3, "Payment Gateway", 4, "API",
			5, "BPAS Auto DCR API", 6, "Digi Locker", 7, "Digital Signature HSM API", 8,
			"Digital Signature Dongle API");

	private static final Map<Integer, String> LETTERTYPE = Map.of(1, "Acknowledgment", 2, "Letter");

	JSONObject resp = new JSONObject();
	Integer parentId = 0;

	public JSONObject getAllFormData(String data) {
		logger.info("Inside getAllFormData method of LetterConfigurationServiceImpl");
		JSONArray jsArr = new JSONArray();
		try {
			List<Tmenulinks> formList = tmenuLinksRepository.getAllFormData();
			for (Tmenulinks tmenulinks : formList) {
				JSONObject jsobj = new JSONObject();
				jsobj.put("intId", tmenulinks.getIntId());
				jsobj.put("vchProcessName", tmenulinks.getTxtLinkName());
				jsArr.put(jsobj);
			}

			if (jsArr != null) {
				resp.put("status", 200);
				resp.put("result", jsArr);
			}

		} catch (Exception e) {
			logger.warn("Inside getAllFormData method of LetterConfigurationServiceImpl  Error");
			resp.put("status", 400);
		}

		return resp;
	}

	public JSONObject getwayType() {
		logger.info("Inside getwayType method of LetterConfigurationServiceImpl");
		JSONArray result = new JSONArray();
		logger.info("Inside getwayType method of LetterConfigurationServiceImpl");
		for (Map.Entry<Integer, String> entry : GATEWAYTYPE.entrySet()) {

			JSONObject jsobj = new JSONObject();
			jsobj.put("typeId", entry.getKey());
			jsobj.put("typeName", entry.getValue());
			result.put(jsobj);
		}

		if (result != null) {
			resp.put("status", 200);
			resp.put("result", result);

		}

		return resp;

	}

	public JSONObject getLetterTypeData() {
		JSONArray result = new JSONArray();
		logger.info("Inside getLetterTypeData method of LetterConfigurationServiceImpl");
		for (Map.Entry<Integer, String> entry : LETTERTYPE.entrySet()) {

			JSONObject jsobj = new JSONObject();
			jsobj.put("typeId", entry.getKey());
			jsobj.put("typeName", entry.getValue());
			result.put(jsobj);
		}

		if (result != null) {
			resp.put("status", 200);
			resp.put("result", result);

		}

		return resp;

	}

	@Override
	public JSONObject save(String data) throws Exception {
		logger.info("Inside save method of LetterConfigurationServiceImpl");

		JSONObject entity = new JSONObject(data);
		DynamicLetterConfig letterConfig = new DynamicLetterConfig();
		letterConfig.setIntFormId(entity.getInt("formId"));
		letterConfig.setIntLetterType(entity.getInt("letterType"));
		letterConfig.setTxtLetterName(entity.getString("lettername"));
		letterConfig.setSignType(entity.getInt("SignType"));
		letterConfig.setTinSignTypeStatus(entity.getInt("SignTypeStatus"));
		letterConfig.setTxtLetterContent(entity.getString("letterContent"));
		letterConfig.setIntCreatedBy(0);
		letterConfig.setIntUpdatedBy(0);

		if (entity.getString("itemId") != "" && !entity.isNull("itemId")) {
			letterConfig.setIntLetterConfigId(Integer.parseInt(entity.getString("itemId")));
			DynamicLetterConfig getData = letterConfigurationRepository
					.findByIntLetterConfigIdAndBitDeletedFlag(letterConfig.getIntLetterConfigId(), false);
			letterConfig.setDtmCreatedOn(getData.getDtmCreatedOn());
			DynamicLetterConfig updateData = letterConfigurationRepository.save(letterConfig);
			parentId = updateData.getIntLetterConfigId();
			resp.put("status", "202");

		} else {

			DynamicLetterConfig saveData = letterConfigurationRepository.save(letterConfig);
			resp.put("status", 200);
		}

		resp.put("id", parentId);
		return resp;
	}

	@Override
	public JSONArray getConfigurationKeys() {
		logger.info("Inside getConfigurationKeys method of LetterConfigurationServiceImpl");
		JSONArray keyData = new JSONArray();
		String query = "select keyName,keyDescription from dyn_dynamic_keys where bitdeletedFlag=0";
		List<Object[]> dataList = CommonUtil.getDynResultList(entityManager, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("keyName", data[0]);
			jsonObj.put("keyLabelName", data[1]);
			keyData.put(jsonObj);
		}

		return keyData;
	}

	@Override
	public JSONObject getAll(String formParams) {
		logger.info("Inside getAll method of LetterConfigurationServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		Integer intLetterConfigId = 0;
		if (jsonData.has("intLetterConfigId") && !jsonData.isNull("intLetterConfigId")
				&& !jsonData.getString("intLetterConfigId").equals("")) {
			intLetterConfigId = jsonData.getInt("intLetterConfigId");
		}
		String letterName = "0";
		if (jsonData.has("LetterName") && !jsonData.isNull("LetterName")
				&& !jsonData.getString("LetterName").equals("")) {
			letterName = jsonData.getString("LetterName");
		}
		Integer formId = 0;
		if (jsonData.has("formId") && !jsonData.isNull("formId") && !(jsonData.get("formId").toString()).equals("")) {
			formId = jsonData.getInt("formId");
		}

		List<DynamicLetterConfig> letterConfigList = letterConfigurationRepository
				.findAllByBitDeletedFlagAndIntInsertStatus(false, intLetterConfigId, letterName, formId);
		JSONArray letter = new JSONArray();
		for (DynamicLetterConfig letterConfig : letterConfigList) {
			JSONObject jsObj = new JSONObject();

			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select vchLinkName from m_admin_menulinks where bitDeletedFlag=0 and intId="
								+ letterConfig.getIntFormId());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			jsObj.put("intSignType", letterConfig.getSignType());
			jsObj.put("tinSignTypeSts", letterConfig.getTinSignTypeStatus());
			jsObj.put("intLetterType", letterConfig.getIntLetterType());
			jsObj.put("intLetterConfigId", letterConfig.getIntLetterConfigId());
			jsObj.put("intformId", letterConfig.getIntFormId());
			jsObj.put("vchFormName", dynamicValue.toString());
			jsObj.put("vchLetterName", letterConfig.getTxtLetterName());
			jsObj.put("vchLetterType", LETTERTYPE.get(letterConfig.getIntLetterType()));
			jsObj.put("txtLetterContent", letterConfig.getTxtLetterContent());
			jsObj.put("dtmCreatedOn", new SimpleDateFormat("dd-MMM-yyyy").format(letterConfig.getDtmCreatedOn()));
			letter.put(jsObj);
		}

		resp.put("status", 200);
		resp.put("result", letter);

		return resp;
	}

	@Override
	public JSONObject deleteById(Integer id) {
		logger.info("Inside deleteById method of LetterConfigurationServiceImpl");
		DynamicLetterConfig letterConfig = letterConfigurationRepository.findByIntLetterConfigIdAndBitDeletedFlag(id,
				false);

		letterConfig.setBitDeletedFlag(true);
		letterConfigurationRepository.save(letterConfig);
		resp.put("status", 200);
		return resp;
	}

	@Override
	public JSONObject viewAllPublishLetters(Integer intFormId) {

		logger.info("Inside viewAllPublishLetters method of LetterConfigurationServiceImpl");
		JSONArray jsArr = new JSONArray();
		try {
			List<DynamicLetterConfig> letterList = letterConfigurationRepository
					.findByIntFormIdAndBitDeletedFlag(intFormId, false);

			if (letterList != null) {

				for (DynamicLetterConfig letter : letterList) {
					JSONObject jsObj = new JSONObject();
					jsObj.put("intLetterConfigId", letter.getIntLetterConfigId());
					jsObj.put("vchLetterName", letter.getTxtLetterName());
					jsArr.put(jsObj);
				}
				resp.put("status", 200);
				resp.put("result", jsArr);
			} else {
				resp.put("status", 417);
			}

		} catch (Exception e) {
			logger.error("Inside viewAllPublishLetters method  of LetterConfigurationServiceImpl error occur :",e);
			resp.put("status", 400);
			resp.put("msg", "error");
		}

		return resp;
	}

	@Override
	public JSONObject viewLetterList(String data) {
		logger.info("Inside viewLetterList method of LetterConfigurationServiceImpl");
		JSONArray jsArr = new JSONArray();
		JSONObject getAuthArr = new JSONObject();
		Integer countsta = 0;
		try {
			JSONObject jsonObject = new JSONObject(data);
			Integer processId = jsonObject.getInt("intFormId");
			Integer serviceId = jsonObject.getInt("intServiceId");
			Integer roleId = jsonObject.getInt("roleId");
			Integer count = 0;
			TOnlineServiceApproval tOnlineServiceApproval = tOnlineServiceApprovalRepository
					.getAllDataByUsingIntIdAndOnlineServiceId(processId, serviceId);
			List<Map<String, Object>> dataList = new ArrayList<>();
			if (tOnlineServiceApproval.getIntForwardedUserId() > 0) {
				roleId = tOnlineServiceApproval.getIntSentFrom();
			}
			TSetAuthority authority = tsetAuthorityRepository.getByProcessIdAndStageNo(processId,
					tOnlineServiceApproval.getIntStageNo(), roleId);

			String[] vchAuthList = authority.getVchAuthTypes().split(",");
			boolean isPresent = false;
			boolean isGenerate = false;
			for (String num : vchAuthList) {
				int currentNumber = Integer.parseInt(num.trim());
				if (currentNumber == 13) {
					isPresent = true;

				}
				if (currentNumber == 14) {
					isGenerate = true;
				}
			}

			if (isPresent && isGenerate) {
				List<Integer> authLetters = convertIdsToList(authority.getAuthLetters());
				dataList = letterConfigurationRepository.getByDataServiceIdAndAuthLetters(authLetters, serviceId,
						processId);
			} else if (isPresent) {
				List<Integer> authLetters = convertIdsToList(authority.getAuthLetters());
				dataList = letterConfigurationRepository.getDataForDaraftUsingAuthLetters(authLetters, serviceId);

			} else if (isGenerate) {

				dataList = letterConfigurationRepository.getDataForGenerateUsingServiceId(serviceId, processId);
			}

			Tmenulinks menuLinks = tmenuLinksRepository.findByIntIdAndBitDeletedFlag(processId, false);

			String formData = menuLinks.getVchViewUrl();
			String[] parts = formData.split("/");
			String output = parts[parts.length - 1];

			if (dataList != null) {
				for (Map<String, Object> letterData : dataList) {
					JSONObject jsObj = new JSONObject();
					jsObj.put("intLetterConfigId", Integer.parseInt(letterData.get("intLetterConfigId").toString()));
					jsObj.put("vchLetterName", letterData.get("vchLetterName"));
					if (letterData.get("vchGenerateStatus") != null) {
						jsObj.put("vchGenerateStatus", letterData.get("vchGenerateStatus"));
					} else {
						jsObj.put("vchGenerateStatus", 0);
					}
					count = draftLetterRepository.countByIntLetterConfigIdAndIntServiceIdAndBitDeletedFlag(
							Integer.parseInt(letterData.get("intLetterConfigId").toString()),
							jsonObject.getInt("intServiceId"), false);
					if (count == 0) {
						jsObj.put("count", 0);
					} else {
						jsObj.put("count", 1);
						countsta++;
					}
					jsArr.put(jsObj);
				}
				resp.put("status", 200);
			} else {
				resp.put("status", 417);
			}
			if (isGenerate) {
				resp.put("confirm", 1);
			} else {
				resp.put("confirm", 0);
			}
			resp.put("result", jsArr);
			resp.put("length", jsArr.length());
			resp.put("viewpage", output);
			resp.put("countsta", countsta);
		} catch (Exception e) {
			logger.error("Inside viewLetterList method  of LetterConfigurationServiceImpl error occur :",e);
			resp.put("status", 400);
			resp.put("msg", "error");
		}
		return resp;
	}

	private List<Integer> convertIdsToList(String authLetters) {

		return List.of(authLetters.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
	}

	@Override
	public JSONObject getletterData(String getData) {
		JSONObject getListDataJson = new JSONObject();
		logger.info("Inside getletterData method of LetterConfigurationServiceImpl");
		try {
			JSONObject jsObj = new JSONObject(getData);
			Integer letterConfigId = Integer.parseInt(jsObj.getString("intLetterConfigId"));
			Integer serviceId = Integer.parseInt(jsObj.getString("intOnlineServiceId"));
			Integer formId = Integer.parseInt(jsObj.getString("intProcessId"));
			List<String> msgData = new ArrayList<String>();
			List<String> configuredMsgData = new ArrayList<String>();
			DraftLetterEntity draftLetterEntity = draftLetterRepository
					.findByIntLetterConfigIdAndIntApplicationIdAndBitDeletedFlag(letterConfigId, serviceId, false);
			DynamicLetterConfig dynamicLetterConfig = null;
			List<DynamicKeys> dynamicKeysList = dynamicKeysRepository.findByBitDeletedFlag(false);
			String letterContent = "";
			if (draftLetterEntity == null) {
				dynamicLetterConfig = letterConfigurationRepository
						.findByIntLetterConfigIdAndBitDeletedFlag(letterConfigId, false);

				letterContent = dynamicLetterConfig.getTxtLetterContent();
				getListDataJson.append("txtLetterContent", dynamicLetterConfig.getTxtLetterContent());
				getListDataJson.append("vchLetterName", dynamicLetterConfig.getTxtLetterName());
			} else {
				getListDataJson.append("txtLetterContent", draftLetterEntity.getTxtLetterContent());
				getListDataJson.append("vchLetterName", draftLetterEntity.getVchLetterTitle());
				letterContent = draftLetterEntity.getTxtLetterContent();

			}
			for (Integer i = 0; i < dynamicKeysList.size(); i++) {
				String keyReference = dynamicKeysList.get(i).getKeyReference();
				String keyName = dynamicKeysList.get(i).getKeyName();
				if (letterContent.indexOf(keyName) > 0) {
					JSONArray keyReferenceData = new JSONArray(keyReference);
					JSONObject keyData = new JSONObject(keyReferenceData.get(0).toString());
					if (keyData.getString("type").equals("table")) {
						JSONObject getValue = new JSONObject(keyData.get("value").toString());
						String tableName = getValue.get("tableName").toString();
						String columnName = getValue.get("columnName").toString();
						String fetchCondn = getValue.get("fetchCondn").toString();

						JSONObject conditon = new JSONObject();
						conditon.put("<intId>", serviceId);
						fetchCondn = getDynamicVariableContent(conditon, fetchCondn);
						String sqlStatement = "Select " + columnName + " from " + tableName + " where " + fetchCondn;
						List<Map<String, Object>> aliasToValueMapList = jdbcTemplate.queryForList(sqlStatement);
						String actualData = aliasToValueMapList.get(0).get(columnName).toString();
						configuredMsgData.add(keyName);
						msgData.add(actualData);
					}
				}
			}

			letterContent = replaceAllDynamicData(letterContent, configuredMsgData, msgData);

			getListDataJson.put("txtLetterContent", letterContent);
			resp.put("status", 200);
			resp.put("result", getListDataJson);

		} catch (Exception e) {

			logger.error("Inside viewFormDetailsData method  of LetterConfigurationServiceImpl error occur :" + e);
			resp.put("status", 417);
		}
		return resp;
	}

	public String getDynamicVariableContent(JSONObject allDataParms, String replaceOccurenceData) {
		logger.info("Inside getDynamicVariableContent method of LetterConfigurationServiceImpl");
		Set<String> allDyamicKeys = allDataParms.keySet();
		for (String allDyamickeysLoop : allDyamicKeys) {
			if (replaceOccurenceData.indexOf(allDyamickeysLoop) >= 0) {
				replaceOccurenceData = replaceOccurenceData.replaceAll(allDyamickeysLoop,
						allDataParms.get(allDyamickeysLoop).toString());
			}
		}
		return replaceOccurenceData;
	}

	public String replaceAllDynamicData(String msgContent, List<String> regex, List<String> replacement) {
		logger.info("Inside replaceAllDynamicData method of LetterConfigurationServiceImpl");
		msgContent = msgContent.replaceAll("\\[", "").replaceAll("\\]", "");
		for (Integer contLoop = 0; contLoop < regex.size(); contLoop++) {
			String regxData = regex.get(contLoop).toString().replaceAll("\\[", "").replaceAll("\\]", "");
			String replacementData = replacement.get(contLoop).toString();
			msgContent = msgContent.replaceAll(regxData, replacementData);
		}
		return msgContent;
	}

}
