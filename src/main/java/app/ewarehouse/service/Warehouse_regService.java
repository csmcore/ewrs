package app.ewarehouse.service;

import org.json.JSONObject;

public interface Warehouse_regService {
JSONObject save(String warehouse_reg)throws Exception;
JSONObject getById(Integer Id)throws Exception;
JSONObject getAll(String formParams)throws Exception;
JSONObject deleteById(Integer id)throws Exception;
}