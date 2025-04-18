/**
 * 
 */
package app.ewarehouse.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import app.ewarehouse.dto.ComplaintTypeResponse;
import app.ewarehouse.entity.ComplaintType;

/**
 * Priyanka Singh
 */
public interface MasterService {

	//Integer saveComplainttype(ComplaintType complaintType);
	Integer saveComplainttype(String complaintType);
	
	Integer update(String complaintType);

	ComplaintType getbyid(Integer complaintId);

	ComplaintTypeResponse getById(Integer complaintId);

	Integer resetComplaintId(Integer complaintId);

	Page<ComplaintTypeResponse> getAllcomplaintList(Pageable pageable);

	List<ComplaintTypeResponse> getAllComplaintTypes();

	void deleteComplaint(Integer complaintId);

	List<ComplaintTypeResponse> getComplaintTypesByRoleId(Integer roleId);

	Page<ComplaintTypeResponse> getAllcomplaintList(Integer pageNumber, Integer pageSize, String sortCol);

	void deleteComplaintType(Integer id);

}
