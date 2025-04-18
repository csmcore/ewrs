package app.ewarehouse.serviceImpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import app.ewarehouse.dto.ComplaintMgmtInspScheduleDto;
import app.ewarehouse.entity.ComplaintMgmtInspSchedule;
import app.ewarehouse.repository.ComplaintMgmtInspScheduleRepository;
import app.ewarehouse.service.ComplaintMgmtInspScheduleService;
import app.ewarehouse.util.CommonUtil;

@Service
public class ComplaintMgmtInspScheduleServiceImpl implements ComplaintMgmtInspScheduleService {

	private static final String STATUS = "status";
	private static final String AN_UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred: ";
	private static final String ERROR = "error";

	@Autowired
	private ComplaintMgmtInspScheduleRepository repo;
	
	@Autowired
    private ObjectMapper om;

	@Override
	public JSONObject saveSchedule(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		om.registerModule(new JavaTimeModule());
		try {
			ComplaintMgmtInspScheduleDto dto = om.readValue(decodedData, ComplaintMgmtInspScheduleDto.class);
			if (dto.getInspectionId() != null) {
				ComplaintMgmtInspSchedule entity = repo.findById(dto.getInspectionId()).get();
				if (entity.getInspectorId().equals(dto.getInspectorId())
						&& entity.getVchInspStatus().equals("NotStarted")) {
					entity.setInspectionDate(dto.getDateOfInspect());
					entity.setInspectionTime(dto.getTimeOfInspect());
					entity.setInspectionStartRemarks(dto.getTxtDescriptionOfInspection());
					entity.setVchInspStatus("InProgress");
				} else if (entity.getInspectorId().equals(dto.getInspectorId())
						&& entity.getVchInspStatus().equals("InProgress")) {
					entity.setInspectionCompleteDate(dto.getDateOfInspect());
					entity.setInspectionCompleteTime(dto.getTimeOfInspect());
					entity.setInspectionCompleteRemarks(dto.getTxtDescriptionOfInspection());
					entity.setVchInspStatus("Completed");
				}
				repo.save(entity);
			}
//			else {
//				ComplaintMgmtInspSchedule entity = new ComplaintMgmtInspSchedule();
//				entity.setInspectionDate(dto.getDateOfInspect());
//				entity.setInspectionTime(dto.getTimeOfInspect());
//				entity.setInspectionStartRemarks(dto.getTxtDescriptionOfInspection());
//				entity.setComplaintId(dto.getComplaintId());
//				entity.setInspectorId(dto.getInspectorId());
//				entity.setInspectorName(dto.getInspectorName());
//				repo.save(entity);
//			}
			json.put(STATUS, 200);
		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}

	@Override
	public ComplaintMgmtInspSchedule getData(Integer id) {
		ComplaintMgmtInspSchedule entity = repo.findByComplaintId(id);
		return entity;
	}

}
