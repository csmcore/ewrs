package app.ewarehouse.service;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import app.ewarehouse.dto.GraderWeigherMainFormDTO;

public interface GraderWeigherRegService {

	JSONObject saveGraderWeigherDetails(String data);

	Page<GraderWeigherMainFormDTO> getGraderWeigherDetails(int page, int size);

	GraderWeigherMainFormDTO getGraderWeigherDetailsById(String graderWeigherId);

	JSONObject removeExperience(String experienceId);

	boolean duplicateCheck(String empId);

}
