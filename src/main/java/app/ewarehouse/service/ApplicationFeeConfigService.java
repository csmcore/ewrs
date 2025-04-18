package app.ewarehouse.service;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import app.ewarehouse.dto.ApplicationFeeResponseDto;

public interface ApplicationFeeConfigService {

	JSONObject saveApplicationFeeConfig(String data);
	
	boolean isDuplicate(String applicationType, Integer id);

	Page<ApplicationFeeResponseDto> getAll(Integer pageNumber, Integer pageSize, String sortCol);

	JSONObject getFeeByAliasName(String data);

}
