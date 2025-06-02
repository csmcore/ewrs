package app.ewarehouse.service;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import app.ewarehouse.dto.PledgingDischargeWarehouseReceiptResponse;
import app.ewarehouse.entity.PledgingDischargeBankDetails;
import app.ewarehouse.entity.PledgingDischargeDepositorLoanApp;
import app.ewarehouse.entity.PledgingDischargeDepositorWarehouse;
import app.ewarehouse.entity.PledgingDischargeMain;
import app.ewarehouse.entity.PledgingDischargeResidential;
import app.ewarehouse.entity.PledgingDischargeUploadDocs;

public interface PledgingDischargeWarehouseReceiptService {
    String saveAsDraft(String data);
    String publishDraft(String data);
    Page<PledgingDischargeWarehouseReceiptResponse> getAll(Integer pageNumber, Integer pageSize, String sortCol, String sortDir, String search);
    PledgingDischargeWarehouseReceiptResponse getDraft();
    JSONObject saveStepOne(String data);
	String getCountStepOneByCreatedByAndDraftStatus(Integer intId);
	PledgingDischargeDepositorWarehouse getStepOneDataById(String id);
	JSONObject saveStepTwo(String data);
	String getDraftStatusOfStepTwo(Integer intId);
	PledgingDischargeDepositorLoanApp getStepTwoDataById(String id);
	JSONObject saveStepThree(String data);
	String getDraftStatusOfStepThree(Integer intId);
	PledgingDischargeResidential getStepThreeDataById(String id);
	JSONObject saveStepFour(String data);
	String getDraftStatusOfStepFour(Integer intId);
	PledgingDischargeBankDetails getStepFourDataById(String id);
	JSONObject saveStepFive(String data);
	String getDraftStatusOfStepFive(Integer intId);
	PledgingDischargeUploadDocs getStepFiveDataById(String id);
	Page<PledgingDischargeWarehouseReceiptResponse> getLoanApplicants(Integer pageNumber, Integer pageSize, String sortCol, String sortDir, String search);
	PledgingDischargeWarehouseReceiptResponse getByDepositorId(Integer pageNumber, Integer pageSize, String sortCol, String sortDir, String search, String depositorId);

}
