package app.ewarehouse.master.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import app.ewarehouse.master.dto.ComplaintStatusMasterDto;
import app.ewarehouse.master.dto.ComplaintStatusResponseDto;
import app.ewarehouse.master.entity.ComplaintStatusMaster;
/**
 * @author chinmaya.jena
 * @since 03-07-2024
 */
public interface ComplaintStatusMasterService {

	ComplaintStatusMaster createComplaintStatus(ComplaintStatusMasterDto complaintStatusMasterDto);
    ComplaintStatusMaster updateComplaintStatus(Integer id, ComplaintStatusMasterDto complaintStatusMasterDto);
    ComplaintStatusMaster getComplaintStatusById(Integer id);
    List<ComplaintStatusMaster> getAllComplaintStatuses();
    JSONObject changeComplaintStatus(Integer id);
    Page<ComplaintStatusResponseDto> getAll(Integer pageNumber, Integer pageSize, String sortCol);
	
}
