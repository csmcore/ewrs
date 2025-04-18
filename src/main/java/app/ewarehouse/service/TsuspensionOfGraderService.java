package app.ewarehouse.service;

import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TsuspensionOfGrader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface TsuspensionOfGraderService {

    Page<TsuspensionOfGrader> getAllSuspensions(int page, int size);
    TsuspensionOfGrader createSuspension(String complaintFormDto) throws JsonProcessingException;
    TsuspensionOfGrader getSuspensionByGraderId(String graderId);
    boolean isContactNumberUnique(String contactNumber);
    List<TsuspensionOfGrader> getAll();
    Map<String, Object> getPendingComplaintsForUser(Integer pageNumber, Integer pageSize, Integer roleId);
    Map<String, Object> getForwardedComplaints(Integer pageNumber, Integer pageSize, Integer userId);
    void verdict(String data);
    void forwardComplaint(String data);
    Map<String, Object> getResolvedComplaints(Integer pageNumber, Integer pageSize, Integer roleId, Status status);
}
