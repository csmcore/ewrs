package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ApprovedSubCountyDto;
import app.ewarehouse.dto.CountyResponse;
import app.ewarehouse.dto.SubCountyResponse;
import app.ewarehouse.entity.County;
import app.ewarehouse.entity.SubCounty;
import app.ewarehouse.exception.CustomEntityNotFoundException;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.CountyRepository;
import app.ewarehouse.repository.SubCountyRepository;
import app.ewarehouse.service.SubCountyService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.Mapper;
import jakarta.persistence.Tuple;

@Service
public class SubCountyServiceImpl implements SubCountyService {

	@Autowired
    private SubCountyRepository subCountyRepository;

    @Autowired
    ErrorMessages errorMessages;
	
	@Autowired
	private CountyRepository countyRepository;
	
	@Autowired
    private ObjectMapper om;

    private static final Logger logger = LoggerFactory.getLogger(SubCountyServiceImpl.class);
    

    @Override
    public List<SubCountyResponse> getAllSubCounties() {
        logger.info("Inside getAllSubCounties method of SubCountyServiceImpl");
        return subCountyRepository.findAllByBitDeletedFlag(false).stream().map(Mapper::mapToSubCountyResponse).collect(Collectors.toList());
    }

    @Override
    public List<SubCountyResponse> getAllSubCountiesActiveAndInactive() {
        logger.info("Inside getAllSubCountiesActiveAndInactive method of SubCountyServiceImpl");
        return subCountyRepository.findAll().stream().map(Mapper::mapToSubCountyResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<SubCountyResponse> getSubCountyById(Integer id) {
        logger.info("Inside getSubCountyById method of SubCountyServiceImpl");
        return subCountyRepository.findByIntIdAndBitDeletedFlag(id, false).map(Mapper::mapToSubCountyResponse);
    }

    @Override
    @CacheEvict(value = "getCounties", allEntries = true)
    public SubCountyResponse createSubCounty(String data) {
        logger.info("Inside createSubCounty method of SubCountyServiceImpl");

        try {
            String decodedData = CommonUtil.inputStreamDecoder(data);
            SubCounty subCounty = om.readValue(decodedData, SubCounty.class);
            return Mapper.mapToSubCountyResponse(subCountyRepository.save(subCounty));
        }
        catch (DataIntegrityViolationException ex){
            logger.error("Inside save method of SubCountyServiceImpl some error occur:" + ex.getMessage());
            throw new CustomGeneralException(errorMessages.getSubCountyExists());
        }
        catch (Exception e) {
            logger.error("Inside save method of SubCountyServiceImpl some error occur:" + e.getMessage());
            throw new CustomGeneralException(errorMessages.getUnknownError());
        }
    }

    @Override
    @CacheEvict(value = "getCounties", allEntries = true)
    public SubCountyResponse updateSubCounty(Integer id, String data) {
        logger.info("Inside updateSubCounty method of SubCountyServiceImpl");

        String decodedData = CommonUtil.inputStreamDecoder(data);
        SubCounty subCounty;

        try {
            subCounty = om.readValue(decodedData, SubCounty.class);
        } catch (Exception e) {
            throw new CustomGeneralException("Invalid JSON data format.");
        }

        if (subCountyRepository.existsById(id)) {
        	SubCounty subCountyEdit = subCountyRepository.findById(id).orElseThrow(() -> new CustomGeneralException("Sub county not found."));
            subCounty.setIntId(id);
            subCounty.setBitDeletedFlag(subCountyEdit.getBitDeletedFlag());
            return Mapper.mapToSubCountyResponse(subCountyRepository.save(subCounty));
        } else {
            throw new RuntimeException("SubCounty not found with id " + id);
        }
    }

    @Override
    public void deleteSubCounty(Integer id) {
        logger.info("Inside deleteSubCounty method of SubCountyServiceImpl");
        SubCounty subCounty = subCountyRepository.findById(id).orElseThrow(() -> new CustomGeneralException("Sub county not found."));
        subCounty.setBitDeletedFlag(!subCounty.getBitDeletedFlag());
        subCountyRepository.save(subCounty);
    }
    
    @Override
    public List<SubCountyResponse> getSubCountiesByCountyId(Integer countyId) {
        logger.info("Inside getSubCountiesByCountyId method of SubCountyServiceImpl");
    	Optional<County> countyOpt = countyRepository.findById(countyId);
    	if(countyOpt.isPresent()) {
    		County county = countyOpt.get();
    		return subCountyRepository.findByCountyAndBitDeletedFlag(county, false).stream().map(Mapper::mapToSubCountyResponse).collect(Collectors.toList());
    	}
		return Collections.emptyList();
        
    }

	@Override
	public List<ApprovedSubCountyDto> getApprovedSubCounties(Integer countyId,Integer roleId) {
		List<Tuple> result = subCountyRepository.getApprovedSubCountyList(countyId,roleId);
		List<ApprovedSubCountyDto> subCountyList = new ArrayList<>();
		for(Tuple tuple : result) {
			ApprovedSubCountyDto dto = new ApprovedSubCountyDto();
			dto.setSubCountyId((Integer)tuple.get("subCountyId"));
			dto.setSubCountyName((String)tuple.get("subCountyName"));
			subCountyList.add(dto);
		}
		return subCountyList;
	}

	@Override
	public Page<SubCountyResponse> getAllSubCountiesList(Integer pageNumber, Integer pageSize, String search) {
		logger.info("Inside getAllSubCountiesList paginated method of SubCountyServiceImpl");
		   Page<Object> page;
	        try {

	            Pageable pageable = PageRequest.of(pageNumber, pageSize);

	            if (StringUtils.hasText(search)) {
	            	 page = subCountyRepository.findAllSubCountyFilter(pageable,search);
	            } else {
	                page = subCountyRepository.findAllSubCountyDetails(pageable);
	            }
	        } catch (Exception e) {
	            logger.info("Error occurred in getAllSubCountiesList paginated method of SubCountyServiceImpl: {}",e);
	            throw new RuntimeException(e);
	        }
	        Page<SubCountyResponse> subCountyPage = page.map(obj -> mapToSubCounty((Object[]) obj));
	        return subCountyPage;
	}
	
	private SubCountyResponse mapToSubCounty(Object[] obj) {
		SubCountyResponse subCounty = new SubCountyResponse();

	    // Map fields from Object[] to SubCounty
	    subCounty.setIntId((Integer) obj[0]); // Assuming this maps to `intId`
	    subCounty.setTxtSubCountyName((String) obj[1]); // Assuming this maps to `txtSubCountyName`

	    // Map the County object
	    CountyResponse county = new CountyResponse();
	    county.setId((Integer) obj[2]); // Assuming this maps to `intCountyId`
	    county.setName((String) obj[9]); // Assuming this maps to `countyName`
	    subCounty.setCounty(county);

	    // Map other fields
	    subCounty.setDtmCreatedOn((Date) obj[3]);  // Assuming this maps to `dtmCreatedOn`
	    subCounty.setStmUpdatedOn((Date) obj[4]);  // Assuming this maps to `stmUpdatedOn`
	    subCounty.setBitDeletedFlag((Boolean) obj[7]); // Assuming this maps to `bitDeletedFlag`

	    return subCounty;
	}

	@Override
	@CacheEvict(value = "getCounties", allEntries = true)
	public void delete(String data) {
		logger.info("Inside delete method of SubCountyServiceImpl");
		try {
			String decodedData = CommonUtil.inputStreamDecoder(data);
			Map<String, Object> jsonMap = om.readValue(decodedData, new TypeReference<>() {
			});
			Integer id = (Integer) jsonMap.get("id");
			SubCounty existingdata = subCountyRepository.findSubcountyById(id)
					.orElseThrow(() -> new CustomEntityNotFoundException(errorMessages.getEntityNotFound()));
			existingdata.setBitDeletedFlag(!existingdata.getBitDeletedFlag());
			subCountyRepository.save(existingdata);
		} catch (Exception e) {
			logger.error("Error occurred in delete method SubCountyServiceImpl: {}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
		
	}
	
	
    
}
