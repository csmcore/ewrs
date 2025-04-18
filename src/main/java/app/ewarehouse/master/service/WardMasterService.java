package app.ewarehouse.master.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import app.ewarehouse.entity.Ward;
import app.ewarehouse.master.dto.WardMasterDto;
import app.ewarehouse.master.entity.WardMaster;

public interface WardMasterService {

	WardMaster createWardMaster(String wardData);

	WardMaster updateWardMaster(Integer id, String wardMAsterData);

	WardMaster getWardById(Integer id);

	List<WardMaster> getAllWardLists();

	JSONObject changeWardMasterStatus(String data);

	Page<WardMasterDto> getAllWard(Pageable pageable, String search);

	JSONArray getWardsByCountyAndSubCounty(Integer id, Integer subcountyId);

}
