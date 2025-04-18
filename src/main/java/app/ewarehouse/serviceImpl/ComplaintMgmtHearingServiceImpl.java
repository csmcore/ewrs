package app.ewarehouse.serviceImpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import app.ewarehouse.dto.ComplaintMgmtHearingScheduleDTO;
import app.ewarehouse.entity.ComplaintMgmtHearingSchedule;
import app.ewarehouse.repository.ComplaintMgmtHearingScheduleRepository;
import app.ewarehouse.service.ComplaintMgmtHearingService;
import app.ewarehouse.util.CommonUtil;

@Service
public class ComplaintMgmtHearingServiceImpl implements ComplaintMgmtHearingService {
	
	private static final String STATUS = "status";
	private static final String AN_UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred: ";
	private static final String ERROR = "error";
	
	@Autowired
	private ComplaintMgmtHearingScheduleRepository repo;
	
	@Autowired
    private ObjectMapper om;

	@Override
	public JSONObject saveHearingSchedule(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		om.registerModule(new JavaTimeModule());
		try {
			ComplaintMgmtHearingScheduleDTO dto = om.readValue(decodedData, ComplaintMgmtHearingScheduleDTO.class);
			ComplaintMgmtHearingSchedule entityData = new ComplaintMgmtHearingSchedule();
			entityData.setComplaintId(dto.getComplaintId());
			entityData.setReasonForHearing(dto.getReasonForHearing());
			entityData.setDateOfHearing(dto.getDateOfHearing());
			entityData.setTimeOfHearing(dto.getTimeOfHearing());
			entityData.setDocumentsToBeProduced(dto.getDocumentsToBeProduced());
			entityData.setRemedialAction(dto.getRemedialAction());
			entityData.setIsHearingScheduled(true);
			entityData.setIntCreatedBy(dto.getCreatedBy());
			repo.save(entityData);
			json.put(STATUS, 200);
		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}

	@Override
	public ComplaintMgmtHearingSchedule getHearingData(Integer id) {
		return repo.findByComplaintId(id);
	}

}
