package app.ewarehouse.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import app.ewarehouse.dto.AocCompProfDetailsMainDTO;

public interface AocCompProfDetailsMainService {

	JSONObject saveCompanyDetails(String data);

	AocCompProfDetailsMainDTO getCompanyDetails(String profDetId);

	List<AocCompProfDetailsMainDTO> getCompanyProfileDataByUserId(Integer userId);
	
	List<AocCompProfDetailsMainDTO> getAllCompanyDetails();

	Page<AocCompProfDetailsMainDTO> getCompanyProfileDataByUserId(Integer userId, int page, int size);

	Integer getCountProfileDetails(Integer userId);

	JSONObject removeDirector(String directorId);

}
