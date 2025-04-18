package app.ewarehouse.service;

import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import app.ewarehouse.entity.TconformityAction;

public interface TconformityActionService {

	void save(String data) throws JsonMappingException, JsonProcessingException;

	List<TconformityAction> findByApplicationId(String intApplicantId);

	List<TconformityAction> findAll();
	

}
