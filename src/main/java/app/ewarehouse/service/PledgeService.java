package app.ewarehouse.service;

import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;

import app.ewarehouse.dto.LoanSanctionDTO;
import app.ewarehouse.entity.IssuanceWareHouseRecipt;
import app.ewarehouse.entity.Pledge;


public interface PledgeService {

	Pledge saveLoan(String request);

	JSONObject savePaymentData(String data) throws JsonProcessingException;

	List<IssuanceWareHouseRecipt> getDepositorDetails(String emailId);

	List<IssuanceWareHouseRecipt> getDepositorDetailsById(String id,Integer id1);

	LoanSanctionDTO getLoanSanctionDetails(Integer issueId, String loanStatus);

}
