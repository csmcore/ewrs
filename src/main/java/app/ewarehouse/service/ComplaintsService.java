package app.ewarehouse.service;

import java.util.List;

import app.ewarehouse.dto.ComplaintsResponse;
import app.ewarehouse.entity.ComplaintObservation;
import app.ewarehouse.entity.Complaints;

public interface ComplaintsService {
	
    Complaints save(Complaints complaints);

    List<ComplaintsResponse> getAll();

    Complaints getById(String id);

    Complaints update(String id, Complaints updatedComplaints);

    boolean deleteById(Integer id);
    List<ComplaintObservation> getComplaintOservation(Integer lableId,Integer RoleId);
}
