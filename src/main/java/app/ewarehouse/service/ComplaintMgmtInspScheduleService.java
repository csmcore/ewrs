package app.ewarehouse.service;

import org.json.JSONObject;

import app.ewarehouse.entity.ComplaintMgmtInspSchedule;

public interface ComplaintMgmtInspScheduleService {

	JSONObject saveSchedule(String data);

	ComplaintMgmtInspSchedule getData(Integer id);

}
