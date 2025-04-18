package app.ewarehouse.service;

import org.json.JSONObject;

import app.ewarehouse.entity.ComplaintMgmtHearingSchedule;

public interface ComplaintMgmtHearingService {

	JSONObject saveHearingSchedule(String data);

	ComplaintMgmtHearingSchedule getHearingData(Integer id);

}
