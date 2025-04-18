package app.ewarehouse.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import app.ewarehouse.dto.RoutineComplianceInspDTO;

public interface RoutineComplianceService {
    public String takeAction(String data) throws JsonMappingException, JsonProcessingException;
	String saveCompliance(String routineComplianceInspection);
	Page<RoutineComplianceInspDTO> getCompanyRoutineCompData(int page, int size);
	Page<RoutineComplianceInspDTO> findByFiltersData(Integer roleId, String search, String sortColumn,
			String sortDirection, Integer userId, Pageable pageable);
	Map<String, Object> getWareHosedata(String warehouseId, String status);
	Object getCeoOneData(Long id);
	Object getComplianceTwoData(Long editId);
	Object getCeoSecondData(Long editId);
	Map<String, Object> getWareHouseIdandRcId(Long routineComId);
	Map<String, Object> getEditRoutineStageWiseData(Long routinComplianceId);
	Object getInspectorData(Long editId);
	boolean checkDuplicateWarehouse(String warehouseId);
	public Object removeAddmore(Long id, String type);
	
}
