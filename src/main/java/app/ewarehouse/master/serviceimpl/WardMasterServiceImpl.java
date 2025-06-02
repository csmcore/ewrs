package app.ewarehouse.master.serviceimpl;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.entity.Ward;
import app.ewarehouse.exception.CustomEntityNotFoundException;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.master.dto.WardMasterDto;
import app.ewarehouse.master.entity.WardMaster;
import app.ewarehouse.master.repository.WardMasterRepo;
import app.ewarehouse.master.service.WardMasterService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;

@Service
public class WardMasterServiceImpl implements WardMasterService {
	
	 @Autowired
	 ErrorMessages errorMessages;
	private static final Logger logger = LoggerFactory.getLogger(ComplaintStatusMasterServiceImpl.class);

    private final WardMasterRepo repository;

    public WardMasterServiceImpl(WardMasterRepo repository) {
        this.repository = repository;
    }

    @Override
    @CacheEvict(value = "getCounties", allEntries = true)
    public WardMaster createWardMaster(String data) {
//        String decodedData = CommonUtil.inputStreamDecoder(data);
//    	WardMaster wardMaster;
//    	
//    	try {
//    		wardMaster = new ObjectMapper().readValue(decodedData, WardMaster.class);
//		} catch (Exception e) {
//            throw new CustomGeneralException("Invalid JSON data format");
//        }
//    	
//        try {
//        	wardMaster.setBitDeletedFlag(false);
//            return repository.save(wardMaster);
//        } catch (DataIntegrityViolationException ex) {
//            throw new CustomGeneralException(errorMessages.getGeneralDuplicateRecords());
//        }
    	WardMaster wardMaster;

        try {
            String decodedData = CommonUtil.inputStreamDecoder(data);
            wardMaster = new ObjectMapper().readValue(decodedData, WardMaster.class);
        } catch (Exception e) {
            throw new CustomGeneralException("Invalid JSON data format.");
        }

        try {
            boolean isDuplicate = repository.existsByWardNameAndBitDeletedFlagFalse(wardMaster.getWardName());
            if (isDuplicate) {
                throw new CustomGeneralException("Ward with the same name already exists.");
            }

            wardMaster.setBitDeletedFlag(false);
            return repository.save(wardMaster);
        } catch (DataIntegrityViolationException ex) {
            throw new CustomGeneralException(errorMessages.getGeneralDuplicateRecords());
        }
    	
    }

	@Override
	@CacheEvict(value = "getCounties", allEntries = true)
	public WardMaster updateWardMaster(Integer id, String data) {
	        String decodedData = CommonUtil.inputStreamDecoder(data);
	        WardMaster wardMaster;
	    	try {
	    		wardMaster = new ObjectMapper().readValue(decodedData, WardMaster.class);
			} catch (Exception e) {
	            throw new CustomGeneralException("Invalid JSON data format");
	        }
	        if (!id.equals(wardMaster.getIntWardMasterId())) {
	            throw new IllegalArgumentException("ID in path must match ID in request body");
	        }
	        return repository.findById(wardMaster.getIntWardMasterId())
	                .map(existingCounty -> {
	                    existingCounty.setWardName(wardMaster.getWardName());
	                    existingCounty.setIntCountyId(wardMaster.getIntCountyId());
	                    existingCounty.setIntSubCountyId(wardMaster.getIntSubCountyId());
	                    return repository.save(existingCounty);
	                })
	                .orElseThrow(() -> new CustomEntityNotFoundException("WardName not found"));
	}

	@Override
	public WardMaster getWardById(Integer id) {
		logger.info("Fetching Ward Name with ID {}", id);
        return repository.findById(id).orElse(null);
	}

	@Override
	public List<WardMaster> getAllWardLists() {
		logger.info("Fetching all  getAllWardLists");
        return repository.findAll();
	}
  
	@Transactional
	@Override
	@CacheEvict(value = "getCounties", allEntries = true)
	public JSONObject changeWardMasterStatus(String data) {
		JSONObject json = new JSONObject();
    	try {
        String decodedData = CommonUtil.inputStreamDecoder(data);
		Map<String, Object> jsonMap = new ObjectMapper().readValue(decodedData, new TypeReference<>() {
		});
		Integer id = (Integer) jsonMap.get("intWardMasterId");
		WardMaster existingWard = repository.findById(id)
				.orElseThrow(() -> new CustomEntityNotFoundException(errorMessages.getEntityNotFound()));
		existingWard.setBitDeletedFlag(!existingWard.getBitDeletedFlag());
		repository.save(existingWard);
        json.put("status", 200);
        json.put("isDeletedStatus", existingWard.getBitDeletedFlag());
    	}catch(Exception e) {
    		json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
    		json.put("message",e.getMessage());
    	}
        return json;
	}

	@Override
	public Page<WardMasterDto> getAllWard(Pageable pageable, String search) {
		Page<Object>  page;
	        if (StringUtils.hasText(search)) {
	            page = repository.findByWardFilters(search, pageable);
	        } else {
	            page = repository.findAllWard(pageable);
	        }
	        Page<WardMasterDto> wardPage = page.map(obj -> mapToWardMaster((Object[]) obj));
	        return wardPage;
	}

	private WardMasterDto mapToWardMaster(Object[] obj) {
		WardMasterDto wardMaster = new WardMasterDto();
		wardMaster.setIntWardMasterId((Integer) obj[0]);
		wardMaster.setWardName((String) obj[1]); 
		wardMaster.setIntSubCountyId((Integer) obj[2]); 
		wardMaster.setVchSubCountyName((String) obj[3]); 
		wardMaster.setIntCountyId((Integer) obj[4]); 
		wardMaster.setCounty_name((String) obj[5]); 
		wardMaster.setBitDeletedFlag((Boolean) obj[6]); 
	    return wardMaster;
	}

	@Override
	public JSONArray getWardsByCountyAndSubCounty(Integer countyId,Integer subcountyId) {
		JSONArray arr = new JSONArray();
		List<Object[]> data = repository.getWardsByCountyAndSubCounty(countyId,subcountyId);
		for(Object[] obj : data) {
			JSONObject json = new JSONObject();
			json.put("wardId", obj[0]);
			json.put("wardName", obj[1]);
			arr.put(json);
		}
		return arr;
	}
	


	

}
