package app.ewarehouse.controller;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.TconformityAction;
import app.ewarehouse.service.ConformityParticularService;
import app.ewarehouse.service.TconformityActionService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certAction")
@CrossOrigin("*")
public class TconformityActionController {

	@Autowired
	TconformityActionService tconformityActionService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	ConformityParticularService conformityParticularService;

	private static final Logger logger = LoggerFactory.getLogger(TconformityAction.class);

	@PostMapping("/getAll")
	public ResponseEntity<String> getAll() throws JsonProcessingException {
		List<TconformityAction> conformityActionList = tconformityActionService.findAll();
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(conformityActionList)).toString());
	}
	@PostMapping("/createAction")
	public ResponseEntity<?> saveConformityAction(@RequestBody String remark) throws JsonProcessingException {
		tconformityActionService.save(remark);
			return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(remark)).toString());
	}

	@GetMapping("/remarks/{intApplicantId}")
	public ResponseEntity<?> getRemarksByApplicantId(@PathVariable String intApplicantId) throws JsonProcessingException {
		conformityParticularService.findByApplicationIdWithDirectors(intApplicantId);
		List<TconformityAction> comments = tconformityActionService.findByApplicationId(intApplicantId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(comments)).toString());
	}

	private <T> String buildJsonResponse(T response) throws JsonProcessingException {
		return objectMapper.writeValueAsString(ResponseDTO.<T>builder().status(200).result(response).build());
	}
}
