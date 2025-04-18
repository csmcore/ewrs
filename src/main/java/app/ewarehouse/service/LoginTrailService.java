package app.ewarehouse.service;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import app.ewarehouse.dto.LoginTrailDTO;

public interface LoginTrailService {

	Page<LoginTrailDTO> getAll(Integer pageNumber, Integer pageSize, String sortCol, String sortDir, String search);

	JSONObject saveLoginTrailData(String data);

}
