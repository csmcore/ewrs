package app.ewarehouse.service;

import app.ewarehouse.dto.InspectorSuspensionComplaintResponse;
import app.ewarehouse.entity.InspectorSuspensionComplaint;
import app.ewarehouse.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InspectorSuspensionComplaintService {
    String save(String complaintData);
    InspectorSuspensionComplaintResponse getById(String id);
    List<InspectorSuspensionComplaintResponse> getAll();
    Page<InspectorSuspensionComplaintResponse> getAll(Integer pageNumber, Integer pageSize, String sortCol, String sortDir, String search);
    Page<InspectorSuspensionComplaint> getComplaintList(String status, Integer actionTakenBy, Pageable pageable);
    void forwardComplaint(String id, Integer actionTakenBy, String remarks, Status status, Integer userId);
}
