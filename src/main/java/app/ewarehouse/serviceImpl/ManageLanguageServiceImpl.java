package app.ewarehouse.serviceImpl;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.ewarehouse.entity.ManageLanguage;
import app.ewarehouse.repository.ManageLanguageRepository;
import app.ewarehouse.service.ManageLanguageService;@Service
public class ManageLanguageServiceImpl implements ManageLanguageService {

	@Autowired
	private ManageLanguageRepository manageLanguageRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ManageLanguageServiceImpl.class);

	
	@Override
	public JSONObject getActiveLanguageDetailsList() {
	    JSONObject jsonObject=new JSONObject();
	    		
	     try {
	    	 List<ManageLanguage> ManageLangageList= manageLanguageRepository.getActiveLanguageList(false);
				JSONArray jsonArray = new JSONArray();
				for (ManageLanguage managelanguage : ManageLangageList) {
					JSONObject jsonObject1 = new JSONObject();
					jsonObject1.put("vchLanguageName", managelanguage.getVchLanguageName());
					jsonObject1.put("vchAliasName", managelanguage.getVchAliasName());
					jsonArray.put(jsonObject1);
				}
				jsonObject.put("status", 200);
				jsonObject.put("result", jsonArray);
			} catch (Exception e) {
				logger.error("ManageLanguageServiceImpl:getActiveLanguageDetailsList()", e);
				jsonObject.put("status", 400);
				jsonObject.put("msg", "error");
			}
		return jsonObject;
	}

}
