package app.ewarehouse.master.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.master.dto.CheckerRequestBean;
import app.ewarehouse.master.dto.FormConfigurationActualResponse;
import app.ewarehouse.master.dto.FormConfigurationBean;
import app.ewarehouse.master.dto.FormConfigurationCopyBean;
import app.ewarehouse.master.dto.FormConfigurationResponse;
import app.ewarehouse.master.service.IFormConfigurationService;
import app.ewarehouse.util.ClassHelperUtils;
import app.ewarehouse.util.EncryptionUtils;

/**
 * @Project : EWAR Backend
 * @author pravat.behera
 * @Date: 24-march-2024 : 04:25 PM
 */
@RestController
@RequestMapping(value = "/formConfiguration")
public class FormConfigurationController {

	private final Logger logger;
	private IFormConfigurationService service;

	public FormConfigurationController(Logger logger, IFormConfigurationService service) {
		this.logger = logger;
		this.service = service;
	}

	/**
	 * @author pravat.behera
	 * @Date 24-03-2025
	 * @Description this method used for save type master data
	 * 
	 * 
	 */

	@PostMapping("/save")
	public ResponseEntity<Map<String, Object>> saveFormConfiguration(@RequestBody Map<String, Object> requestTypeData) {
		try {
			logger.info("Inside FormConfigurationController controller for saveFormConfiguration() Method ");

			ObjectMapper objectMapper = new ObjectMapper();
			String data = EncryptionUtils.decryptCode((String) requestTypeData.get("encData"));
			JSONObject jsonObject = new JSONObject(data);
			FormConfigurationBean formConfigurationBean = objectMapper.readValue(jsonObject.toString(),
					FormConfigurationBean.class);
			FormConfigurationResponse formConfigurationResponse = service.saveFormConfiguration(formConfigurationBean);

			logger.info("FormConfigurationController for saveFormConfiguration() Method after execute");

			return formConfigurationResponse.getStatus().equalsIgnoreCase("success")
					? ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap(formConfigurationResponse,
							"Form configuration created successfully."))
					: formConfigurationResponse.getStatus().equalsIgnoreCase("duplicate")
							? ResponseEntity.ok(ClassHelperUtils.createDuplicateEncryptedMap(formConfigurationResponse,
									"duplicate document name found."))
							: ResponseEntity.ok(ClassHelperUtils.createExceptionEncryptedMap(formConfigurationResponse,
									"Something Went Wrong."));

		} catch (Exception e) {
			logger.error("Exception Occurred in saveFormConfiguration() Method of FormConfigurationController : {}",
					e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ClassHelperUtils.createNoContentEncryptedMap(e.getMessage()));
		}
	}

	/**
	 * @author pravat.behera
	 * @Purpose This method used to get all Form configuration Data without delete
	 *          data
	 * @MethodType POST Mapping
	 *
	 */

	@PostMapping(value = "/getAllFormConfiguration")
	public ResponseEntity<Map<String, Object>> getAllFormConfigurationData() {
		try {
			List<FormConfigurationBean> map = service.allFormConfiguration();
			if (map != null && map.size() > 0)
				return ResponseEntity
						.ok(ClassHelperUtils.createSuccessEncryptedMap(map, "Details fetched successfully"));
			else
				return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap("No Data Found"));
		} catch (Exception e) {
			logger.error("Exception Occurred in viewmasterTypeDetails() Method of FormConfigurationController : {}",
					e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ClassHelperUtils.createNoContentEncryptedMap(e.getMessage()));
		}
	}

	/**
	 * @author pravat.behera
	 * @Purpose This method used to get all Form configuration Data without delete
	 *          data
	 * @MethodType POST Mapping
	 *
	 */

	@PostMapping(value = "/getAllFormConfigurationActiveInActive")
	public ResponseEntity<Map<String, Object>> getAllFormConfigurationActiveInActive() {
		try {
			List<FormConfigurationBean> map = service.allFormConfigurationWithoutDelete();
			if (map != null && map.size() > 0)
				return ResponseEntity
						.ok(ClassHelperUtils.createSuccessEncryptedMap(map, "Details fetched successfully"));
			else
				return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap("No Data Found"));
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getAllFormConfigurationActiveInActive() Method of FormConfigurationController : {}",
					e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ClassHelperUtils.createNoContentEncryptedMap(e.getMessage()));
		}
	}

	/**
	 * @author pravat.behera
	 * @Purpose This method used to update existing data
	 * @MethodType POST Mapping
	 * @Date 24-03-2025 16:57
	 *
	 */
	@PutMapping(value = "/update")
	public ResponseEntity<Map<String, Object>> updateFormConfiguration(
			@RequestBody Map<String, Object> requestTypeData) {
		try {
			logger.info("Inside FormConfigurationController controller for updateFormConfiguration() Method ");

			ObjectMapper objectMapper = new ObjectMapper();
			String data = EncryptionUtils.decryptCode((String) requestTypeData.get("encData"));
			JSONObject jsonObject = new JSONObject(data);
			FormConfigurationBean formConfigurationBean = objectMapper.readValue(jsonObject.toString(),
					FormConfigurationBean.class);
			FormConfigurationResponse formConfigurationResponse = service
					.updateFormConfiguration(formConfigurationBean);
			logger.info("FormConfigurationController for updateFormConfiguration() Method after execute");

			return formConfigurationResponse.getStatus().equalsIgnoreCase("success")
					? ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap(formConfigurationResponse,
							"Form configuration updated successfully."))
					: formConfigurationResponse.getStatus().equalsIgnoreCase("duplicate")
							? ResponseEntity.ok(ClassHelperUtils.createDuplicateEncryptedMap(formConfigurationResponse,
									"duplicate document name found."))
							: ResponseEntity.ok(ClassHelperUtils.createExceptionEncryptedMap(formConfigurationResponse,
									"Something Went Wrong."));

		} catch (Exception e) {
			logger.error("Exception Occurred in updateFormConfiguration() Method of FormConfigurationController : {}",
					e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ClassHelperUtils.createNoContentEncryptedMap(e.getMessage()));
		}
	}

	/**
	 * @author pravat.behera
	 * @Purpose This method used to get form Configuration using form config id
	 * @MethodType POST Mapping
	 * @Date 24-03-2025 16:57
	 *
	 */
	@PostMapping(value = "/getFormConfigurationDataById")
	public ResponseEntity<Map<String, Object>> getFormConfigurationDataById(
			@RequestBody Map<String, Object> requestTypeData) {
		try {
			String data = EncryptionUtils.decryptCode((String) requestTypeData.get("encData"));
			JSONObject jsonObject = new JSONObject(data);

			FormConfigurationBean map = service.getFormConfiguration(jsonObject.getInt("formDocId"));
			if (map != null)
				return ResponseEntity
						.ok(ClassHelperUtils.createSuccessEncryptedMap(map, "Details fetched successfully"));
			else
				return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap("No Data Found"));
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getFormConfigurationDataById() Method of FormConfigurationController : {}",
					e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ClassHelperUtils.createNoContentEncryptedMap(e.getMessage()));
		}
	}

	/**
	 * @author pravat.behera
	 * @Purpose This method used to delete form Configuration using form config id
	 * @MethodType POST Mapping
	 * @Date 24-03-2025 16:57
	 *
	 */
	@PostMapping(value = "/deleteFormConfigurationById")
	public ResponseEntity<Map<String, Object>> deleteFormConfigurationById(
			@RequestBody Map<String, Object> requestTypeData) {
		try {
			String data = EncryptionUtils.decryptCode((String) requestTypeData.get("encData"));
			JSONObject jsonObject = new JSONObject(data);

			FormConfigurationBean map = service.deleteFormConfiguration(jsonObject.getInt("formDocId"),
					jsonObject.getString("createdBy"));
			if (map != null)
				return ResponseEntity
						.ok(ClassHelperUtils.createSuccessEncryptedMap(map, "Deleted form configuration successfully"));
			else {
				logger.error(
						"Exception Occurred in deleteFormConfigurationById() Method of FormConfigurationController becouse service lavel issue :: Failed to Deleted form configuration");
				return ResponseEntity
						.ok(ClassHelperUtils.createNoContentEncryptedMap("Failed to Deleted form configuration"));
			}

		} catch (Exception e) {
			logger.error(
					"Exception Occurred in deleteFormConfigurationById() Method of FormConfigurationController : {}",
					e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ClassHelperUtils.createNoContentEncryptedMap(e.getMessage()));
		}
	}

	/**
	 * @author pravat.behera
	 * @Purpose This method used to get form Configuration Copy data using form form
	 *          configuration id
	 * @MethodType POST Mapping
	 * @Date 26-03-2025 16:23
	 *
	 */
	@PostMapping(value = "/getFormConfigurationCopyDataById")
	public ResponseEntity<Map<String, Object>> getFormConfigurationCopyDataById(
			@RequestBody Map<String, Object> requestTypeData) {
		try {
			String data = EncryptionUtils.decryptCode((String) requestTypeData.get("encData"));
			JSONObject jsonObject = new JSONObject(data);

			FormConfigurationCopyBean map = service.getFormConfigurationCopy(jsonObject.getInt("formDocId"));
			if (map != null)
				return ResponseEntity
						.ok(ClassHelperUtils.createSuccessEncryptedMap(map, "Details fetched successfully"));
			else
				return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap("No Data Found"));
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getFormConfigurationCopyDataById() Method of FormConfigurationController : {}",
					e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ClassHelperUtils.createNoContentEncryptedMap(e.getMessage()));
		}
	}

	/**
	 * @author pravat.behera
	 * @Purpose This method used to get all Form configuration Copy Data basically
	 *          used for checker page
	 * @MethodType POST Mapping
	 * @Date 26-03-2025 16:27
	 *
	 */

	@PostMapping(value = "/getAllFormConfigurationChecker")
	public ResponseEntity<Map<String, Object>> getAllFormConfigurationCheckerData() {
		try {
			List<FormConfigurationCopyBean> map = service.getCheckerDataFromCopyTable();
			if (map != null && map.size() > 0)
				return ResponseEntity
						.ok(ClassHelperUtils.createSuccessEncryptedMap(map, "Details fetched successfully"));
			else
				return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap("No Data Found"));
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getAllFormConfigurationCheckerData() Method of FormConfigurationController : {}",
					e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ClassHelperUtils.createNoContentEncryptedMap(e.getMessage()));
		}
	}

	/**
	 * @author pravat.behera
	 * @Purpose This method used to approval form configuration by checker like CEO
	 * @MethodType POST Mapping
	 * @Date 26-03-2025 16:57
	 *
	 */
	@PutMapping(value = "/checkerApproval")
	public ResponseEntity<Map<String, Object>> checkerApproval(@RequestBody Map<String, Object> requestTypeData) {
		try {
			logger.info("Inside FormConfigurationController controller for checkerApproval() Method ");

			ObjectMapper objectMapper = new ObjectMapper();
			String data = EncryptionUtils.decryptCode((String) requestTypeData.get("encData"));
			JSONObject jsonObject = new JSONObject(data);
			CheckerRequestBean formConfigurationBean = objectMapper.readValue(jsonObject.toString(),
					CheckerRequestBean.class);
			FormConfigurationCopyBean formConfigurationCopyBean = service.approvalChecker(formConfigurationBean);
			if (formConfigurationCopyBean != null) {
				logger.info("FormConfigurationController for checkerApproval() Method after execute");
				return ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap(formConfigurationCopyBean,
						"Form configuration approved by checker successfully."));
			} else
				return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap("Something Went Wrong."));

		} catch (Exception e) {
			logger.error("Exception Occurred in checkerApproval() Method of FormConfigurationController : {}",
					e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ClassHelperUtils.createNoContentEncryptedMap(e.getMessage()));
		}
	}

	/**
	 * @author pravat.behera
	 * @Purpose This method used to approval form configuration by checker like CEO
	 * @MethodType POST Mapping
	 * @Date 26-03-2025 16:57
	 *
	 */
	@PutMapping(value = "/checkerReject")
	public ResponseEntity<Map<String, Object>> checkerreject(@RequestBody Map<String, Object> requestTypeData) {
		try {
			logger.info("Inside FormConfigurationController controller for checkerreject() Method ");

			ObjectMapper objectMapper = new ObjectMapper();
			String data = EncryptionUtils.decryptCode((String) requestTypeData.get("encData"));
			JSONObject jsonObject = new JSONObject(data);
			CheckerRequestBean formConfigurationBean = objectMapper.readValue(jsonObject.toString(),
					CheckerRequestBean.class);
			FormConfigurationCopyBean formConfigurationCopyBean = service.rejectChecker(formConfigurationBean);
			if (formConfigurationCopyBean != null) {
				logger.info("FormConfigurationController for checkerreject() Method after execute");
				return ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap(formConfigurationCopyBean,
						"Form configuration rejected by checker successfully."));
			} else
				return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap("Something Went Wrong."));

		} catch (Exception e) {
			logger.error("Exception Occurred in checkerreject() Method of FormConfigurationController : {}",
					e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ClassHelperUtils.createNoContentEncryptedMap(e.getMessage()));
		}
	}
	
	/**
	 * @author pravat.behera
	 * @Purpose This method used to get form Configuration final approved details by form type
	 * @MethodType POST Mapping
	 * @Date 27-03-2025 16:57
	 *
	 */
	@PostMapping(value = "/getFormConfigurationDataByTypeId")
	public ResponseEntity<Map<String, Object>> getFormConfigurationDataByTypeId(
			@RequestBody Map<String, Object> requestTypeData) {
		try {
			String data = EncryptionUtils.decryptCode((String) requestTypeData.get("encData"));
			JSONObject jsonObject = new JSONObject(data);

			List<FormConfigurationActualResponse> map =  service.getAllFormConfigurationByFormType(jsonObject.getInt("typeId"));
			if (map != null)
				return ResponseEntity
						.ok(ClassHelperUtils.createSuccessEncryptedMap(map, "Details fetched successfully"));
			else
				return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap("No Data Found"));
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getFormConfigurationDataById() Method of FormConfigurationController : {}",
					e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ClassHelperUtils.createNoContentEncryptedMap(e.getMessage()));
		}
	}

}
