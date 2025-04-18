package app.ewarehouse.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;

import app.ewarehouse.dto.InspectorDTO;
import app.ewarehouse.entity.Action;
import app.ewarehouse.entity.Inspector;
import app.ewarehouse.entity.Stakeholder;
import app.ewarehouse.entity.Status;

public interface InspectorService {
    Page<InspectorDTO> getAllInspectors(Pageable pageable);

	Inspector getInspectorById(Integer id);

	String createInspector(String inspectorData) throws JsonProcessingException;

	List<InspectorDTO> getAllInspectors();

//	Page<InspectorDTO> getPendingApplicationsForCeo(Pageable pageable);

	String handleCeoAction(String ceoActionRequestData) throws JsonProcessingException;

//	Page<InspectorDTO> getPendingApplicationsForOicLegal(Pageable pageable);

	String handleOicLegalAction(String oicLegalActionRequestData) throws JsonProcessingException;

//	Page<InspectorDTO> getPendingApplicationsForApprover(Pageable pageable);

	String handleApproverAction(String approverActionRequestData) throws JsonProcessingException;

//	Page<InspectorDTO> getPendingApplicationsForCeoSecond(Pageable pageable);

	String handleCeoSecondAction(String ceoSecondActionRequestData) throws JsonProcessingException;
	

//	Page<InspectorDTO> getApplicationsByStatusAndForwardedTo(Status forwarded, Stakeholder approver, Pageable pageable);
	Page<InspectorDTO> findApplicationsByStatusAndForwardedTo(Date fromDate, Date toDate, String search, Status forwarded, Stakeholder approver, Pageable pageable);

//	Page<InspectorDTO> getApplicationsByStatusAndForwardedTo(Action forwarded, Stakeholder approver, Pageable pageable);
//	Page<InspectorDTO> findApplicationsByActionAndForwardedTo(Action forwarded, Stakeholder approver, Pageable pageable);
	Page<InspectorDTO> findApplicationsByActionAndForwardedTo(Date fromDate, Date toDate, String search, Action action,
			Stakeholder forwardedTo, Pageable pageable);
	

	Page<InspectorDTO> getFilteredApplications(Date fromDate, Date toDate, Status status, Action action,
			Stakeholder stakeholder, String search, Stakeholder forwardedTo, Pageable pageable);

	Page<InspectorDTO> getPendingApplicationsForCeo(Date fromDate, Date toDate, Status status, Action action,
			Stakeholder stakeholder, String search, Stakeholder forwardedTo, Pageable pageable);

	Page<InspectorDTO> getPendingApplicationsForOicLegal(Date fromDate, Date toDate, Status status, Action action,
			Stakeholder stakeholder, String search, Stakeholder forwardedTo, Pageable pageable);

	Page<InspectorDTO> getPendingApplicationsForApprover(Date fromDate, Date toDate, Status status, Action action,
			Stakeholder stakeholder, String search, Stakeholder forwardedTo, Pageable pageable);



}
