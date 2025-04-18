package app.ewarehouse.service;


import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import app.ewarehouse.dto.ComplaintDetailsComprehensive;
import app.ewarehouse.dto.ComplaintmanagementResponse;
import app.ewarehouse.entity.ComplaintObservation;

public interface Complaint_managmentService {
JSONObject save(String complaint_managment)throws Exception;
JSONObject getById(Integer Id)throws Exception;
JSONObject getAll(String formParams)throws Exception;
JSONObject deleteById(Integer id)throws Exception;
JSONObject getByActionWise(String data)throws Exception;
JSONObject getPreviewDetails(Integer id)throws Exception;
JSONObject getEventTakeActionDetails(String data)throws Exception;
ComplaintDetailsComprehensive findById(int id);
Page<ComplaintmanagementResponse> getFilteredComplaint( String search, String sortColumn, String sortDirection, Pageable sortedPageable);
List<ComplaintObservation> getComplaintOservation(Integer lableId,Integer RoleId);
String documentRemoved(int id);
JSONObject checkOperatorSusStatus(Integer lableId,Integer userId)throws Exception;

String  getICMemberList(Integer lableId,Integer appId)throws Exception;
}