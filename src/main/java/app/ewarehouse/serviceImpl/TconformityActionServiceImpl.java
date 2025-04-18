package app.ewarehouse.serviceImpl;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import app.ewarehouse.entity.ApplicationOfConformity;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TconformityAction;
import app.ewarehouse.repository.TconformityActionRepository;
import app.ewarehouse.service.ConformityParticularService;
import app.ewarehouse.service.TconformityActionService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.JsonFileExtractorUtil;

@Service
public class TconformityActionServiceImpl implements TconformityActionService {

	@Autowired
	TconformityActionRepository tconformityActionRepo;
	@Autowired
	ConformityParticularService conformityParticularService;
	


	private static final Logger logger = LoggerFactory.getLogger(TconformityActionServiceImpl.class);

	@Override
	@Transactional
	public void save(String data) throws JsonMappingException, JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject jsonObject = new JSONObject(decodedData);
		
		TconformityAction tconformityAction = new TconformityAction();
		ApplicationOfConformity applicationConformity = new ApplicationOfConformity();
		try {
			tconformityAction.setTxtRemark(jsonObject.getString("txtRemark"));
			tconformityAction.setRole(jsonObject.getInt("roleId"));		
			
			 if (jsonObject.has("txtInspectorFilePath") && !jsonObject.isNull("txtInspectorFilePath")) {
		            String inspectorFilePath = jsonObject.getString("txtInspectorFilePath");
		            if (!inspectorFilePath.isEmpty()) {
		                tconformityAction.setTxtInspectorFilePath(JsonFileExtractorUtil.uploadFile(inspectorFilePath));
		            }
		        }
			applicationConformity.setApplicationId(jsonObject.getString("intApplicantId"));
			tconformityAction.setApplicationConformity(applicationConformity);
			
		} catch (IllegalArgumentException e) {
		throw new IllegalArgumentException("Error processing data: " + e.getMessage());
		}
		 tconformityActionRepo.save(tconformityAction);
		 
		 
		 conformityParticularService.updateApplicationStatus(Status.valueOf(jsonObject.getString("enmStatus")), jsonObject.getInt("roleId"), jsonObject.getString("intApplicantId"));
	}

	@Override
	public List<TconformityAction> findByApplicationId(String applicantId) {
		return tconformityActionRepo.findByApplicationId(applicantId);
	}

	@Override
	public List<TconformityAction> findAll() {
		return tconformityActionRepo.findAll();
	}

}
