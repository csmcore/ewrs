package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ApprovedCountiesDto;
import app.ewarehouse.dto.CountyResponse;
import app.ewarehouse.entity.Country;
import app.ewarehouse.entity.County;
import app.ewarehouse.exception.CustomEntityNotFoundException;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.CountryRepository;
import app.ewarehouse.repository.CountyRepository;
import app.ewarehouse.service.CountyService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;

@Service
public class CountyServiceImpl implements CountyService {

    @Autowired
    private CountyRepository countyRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ObjectMapper om;
    
    @Autowired
    ErrorMessages errorMessages;
    
    @Cacheable(value = "getCounties")
    @Override
    public List<CountyResponse> getAllCounties() {
    	System.err.println("hellooo........");
       return countyRepository.findByBitDeletedFlagFalse().stream()
    		   .map(Mapper::mapToCountyResponseWithSubCounties)
    		   .toList();
       
    }
       
    
    @Override
    public Page<CountyResponse> getAllCounties(int page, int size,String search) {
    	Pageable pageable = PageRequest.of(page, size);
    	 Page<CountyResponse> resp;
         if (StringUtils.hasText(search)) {
        	 resp = countyRepository.findByCountyFilters(search, pageable).map(Mapper::mapToCountyResponseWithSubCounties);
         } else {
        	 resp = countyRepository.findAllCounties(pageable).map(Mapper::mapToCountyResponseWithSubCounties);
         }
     	
         return resp;
    }
    
    @Override
    public Page<CountyResponse> getAllCounties(Pageable pageable,String search) {
    	
        Page<CountyResponse> page;
        if (StringUtils.hasText(search)) {
            page = countyRepository.findByCountyFilters(search, pageable).map(Mapper::mapToCountyResponseWithSubCounties);
        } else {
            page = countyRepository.findAllCounties(pageable).map(Mapper::mapToCountyResponseWithSubCounties);
        }
    	
        return page;
    }


    @Override
    public CountyResponse getCountyById(Integer id) {
        County county = countyRepository.findById(id).orElse(null);
        return Mapper.mapToCountyResponseWithSubCounties(county);
    }
    
    @Override
    @CacheEvict(value = "getCounties", allEntries = true)
    public County saveCounty(String countyData){
    	
    	String decodedData = CommonUtil.inputStreamDecoder(countyData);
    	County county;
    	
    	try {
			county = om.readValue(decodedData, County.class);
		} catch (Exception e) {
            throw new CustomGeneralException("Invalid JSON data format");
        }
    	
        Optional.ofNullable(county.getCountry())
                .map(Country::getCountryId)
                .map(countryRepository::findById)
                .orElseThrow(() -> new RuntimeException("Country not found"))
                .ifPresent(county::setCountry);
        try {
            return countyRepository.save(county);
        } catch (DataIntegrityViolationException ex) {
            throw new CustomGeneralException(errorMessages.getDuplicateCountyName());
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "getCounties", allEntries = true)
    public void deleteCounty(Integer id) {
        if (!countyRepository.existsById(id)) {
            throw new EntityNotFoundException("County with id " + id + " not found");
        }
        County county = countyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("County with id " + id + " not found"));
        county.setBitDeletedFlag(!county.getBitDeletedFlag());
        countyRepository.save(county);
    }

    @Override
    public County updateCounty(Integer id, String countyData) {
    	String decodedData = CommonUtil.inputStreamDecoder(countyData);
    	County county;
    	try {
			county = om.readValue(decodedData, County.class);
		} catch (Exception e) {
            throw new CustomGeneralException("Invalid JSON data format");
        }
        if (!id.equals(county.getId())) {
            throw new IllegalArgumentException("ID in path must match ID in request body");
        }
        return countyRepository.findById(county.getId())
                .map(existingCounty -> {
                    existingCounty.setName(county.getName());
                    existingCounty.setCountry(county.getCountry());
                    return countyRepository.save(existingCounty);
                })
                .orElseThrow(() -> new CustomEntityNotFoundException("County not found"));
    }

	@Override
	public List<ApprovedCountiesDto> getApprovedCounties() {
		List<ApprovedCountiesDto> countyListDto = new ArrayList<>();
		List<Tuple> countyList = countyRepository.getApprovedCountyList();
		for(Tuple tuple : countyList) {
			ApprovedCountiesDto dto = new ApprovedCountiesDto();
			dto.setCountyId((Integer)tuple.get("countyId"));
			dto.setCountyName((String)tuple.get("countyName"));
			countyListDto.add(dto);
		}
		return countyListDto;
	}


	@Override
	public List<ApprovedCountiesDto> getGraderCounties() {
		
		List<ApprovedCountiesDto> countyListDto = new ArrayList<>();
		List<Object[]> countyList = countyRepository.findGraderCounties();
		for(Object[] tuple : countyList) {
			ApprovedCountiesDto dto = new ApprovedCountiesDto();
			dto.setCountyId((Integer)tuple[0]);
			dto.setCountyName((String)tuple[1]);
			countyListDto.add(dto);
		}
		return countyListDto;
	}


	@Override
	public List<ApprovedCountiesDto> getInspectorCounties(Integer RoleId) {
		List<ApprovedCountiesDto> countyListDto = new ArrayList<>();
		List<Object[]> countyList = countyRepository.findCountyByRoleId(RoleId);
		for(Object[] tuple : countyList) {
			ApprovedCountiesDto dto = new ApprovedCountiesDto();
			dto.setCountyId((Integer)tuple[0]);
			dto.setCountyName((String)tuple[1]);
			countyListDto.add(dto);
		}
		return countyListDto;
	}


	@Override
	public List<ApprovedCountiesDto> getWareHouseCounties() {
		List<ApprovedCountiesDto> countyListDto = new ArrayList<>();
		List<Object[]> countyList = countyRepository.findWareHouseCounties();
		for(Object[] tuple : countyList) {
			ApprovedCountiesDto dto = new ApprovedCountiesDto();
			dto.setCountyId((Integer)tuple[0]);
			dto.setCountyName((String)tuple[1]);
			countyListDto.add(dto);
		}
		return countyListDto;
	}
	
	
	
}
