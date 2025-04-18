package app.ewarehouse.service;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import app.ewarehouse.dto.LotMasterResponseDto;

public interface LotMasterService {

	JSONObject saveLot(String data);
	
	Page<LotMasterResponseDto> getAll(Integer pageNumber, Integer pageSize, String sortCol);

}
