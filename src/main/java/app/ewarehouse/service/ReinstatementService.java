package app.ewarehouse.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import app.ewarehouse.entity.ReinstatementActionHistory;

@Service
public interface ReinstatementService {

	Map<String, Object> getReinstatementData(JSONObject jsonObject);

	String saveReinstaement(String reinStaementData);

	Page<Map<String, Object>> getReinstatementViewData(int page, int size, int userId);

	Page<Map<String, Object>> getReinstatementListData(Integer page, Integer size, Integer userId, Integer roleId);

	JSONObject forwardToCommitteeMember(String data);

	JSONObject submitCMData(String data);

	Map<String, Object> getCMData(String reinsatementId);

	JSONObject submitCeosecondData(String data);

	Map<String, Object> getReinstatementViewDataById(String reinsatementId);

	List<ReinstatementActionHistory> getHistoryData(String reinsatementId);

}
