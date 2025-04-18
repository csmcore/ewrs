package app.ewarehouse.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.dto.WarehouseUserEmpTypeDTO;
import app.ewarehouse.service.WarehouseUserEmpTypeService;
import app.ewarehouse.util.CommonUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/warehouse-user-emp-type")
public class WarehouseUserEmpTypeController {

	private ObjectMapper objectMapper;
	private WarehouseUserEmpTypeService service;
	
	public WarehouseUserEmpTypeController(ObjectMapper objectMapper , WarehouseUserEmpTypeService service) {
		this.objectMapper = objectMapper;
		this.service = service;
	}

	@GetMapping("/by-role/{roleId}")
	public ResponseEntity<?> getByRoleId(@PathVariable Integer roleId) throws JsonProcessingException {
		List<WarehouseUserEmpTypeDTO> result = service.getByRoleId(roleId);
		if (result.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(result))
				.toString());
	}

	private <T> String buildJsonResponse(T response) throws JsonProcessingException {
		Object result;
		if (response instanceof JSONObject) {
			result = response.toString();
		} else {
			result = response;
		}

		return objectMapper.writeValueAsString(ResponseDTO.builder().status(200).result(result).build());
	}

}
