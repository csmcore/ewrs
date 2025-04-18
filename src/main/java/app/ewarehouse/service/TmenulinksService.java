package app.ewarehouse.service;

import org.json.JSONArray;
import org.json.JSONObject;
public interface TmenulinksService {
JSONObject save(String tmenulinks);
JSONObject getById(Integer Id);
JSONArray getAll(String formParams);
JSONObject deleteById(Integer id);
JSONArray getByDataUsing(String data);
JSONObject getAllFormList();
JSONArray getAllForOrderScreen(String formParams);
public JSONObject GlobalLinkwithIcon(JSONArray makeArray);
public JSONObject LinkType();
}
