package app.ewarehouse.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import app.ewarehouse.dto.ApplicationCertificateOfCollateralDto;
import app.ewarehouse.dto.CollateralCertificateDTO;
import app.ewarehouse.dto.CollateralManagerMainFormDTO;
import app.ewarehouse.dto.PaymentDTO;
import app.ewarehouse.dto.TempUserDTO;
import app.ewarehouse.entity.CollateralTakeActionCC;
@Service
public interface AOCCollateralDetailsService {
	
	TempUserDTO getCollateralInfoByUserId(String emailId);

	JSONObject saveAsDraftCollateralDetails(String data);

	CollateralManagerMainFormDTO getCollateralDetails(String collarteralId);

	List<CollateralManagerMainFormDTO> getCollateralDataByUserId(Integer userId);

	JSONObject updateMainTable(String code);


	Page<CollateralManagerMainFormDTO> getCollateralDataByUserId(Integer userId, int page, int size);
	
	Page<ApplicationCertificateOfCollateralDto> getAllApplicationsData(Integer page, Integer size, String status, Integer roleId, Integer userId);

	JSONObject forwardToCC(String data);

	JSONObject forwardToInsp(String data);

	JSONObject submitCCData(String data);

	JSONObject submitInspectorData(String data);

	JSONObject submitOicSecondData(String data);

	Object getInspectorDataById(String collateralId, Integer userId);

	Map<String, Object> getOicTwoData(String collateralId);
	
	PaymentDTO getPaymentDetailsByUserId(String applicationId, Integer userId);

	JSONObject saveCeoTakeAction(String data);

	JSONObject removeCollateralDirector(String directorId);

	JSONObject getCollateralCountByUserId(Integer userId);
	
	CollateralTakeActionCC getCCdDetailsDataById(Integer roleId, Integer userid, String applicationId);

	JSONObject generateCollateralCertificate(String data);

	CollateralCertificateDTO getCollateralcertificate(String applicantId);

	PaymentDTO getPaymentDetails(String applicationId);

	JSONObject removeCvDetails(String cvId);

	JSONObject getCollateralCount(Integer userId);


}
