package app.ewarehouse.serviceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.CountryResponse;
import app.ewarehouse.entity.Country;
import app.ewarehouse.exception.CustomEntityNotFoundException;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.CountryRepository;
import app.ewarehouse.service.CountryService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class CountryServiceImpl implements CountryService {
    private static final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);
    private final CountryRepository countryRepository;
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final ErrorMessages errorMessages;

    public CountryServiceImpl(CountryRepository countryRepository, ObjectMapper objectMapper, Validator validator, ErrorMessages errorMessages) {
        this.countryRepository = countryRepository;
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.errorMessages = errorMessages;
    }

    @Override
    @Transactional
    @CacheEvict(value = "getCountries", allEntries = true)
    public void saveOrUpdate(String data) {
        log.info("Inside saveOrUpdate method of CountryServiceImpl");
        try {
            String decodedData = CommonUtil.inputStreamDecoder(data);
            Country country = objectMapper.readValue(decodedData, Country.class);
            Set<ConstraintViolation<Country>> violations = validator.validate(country);
            if (!violations.isEmpty()) {
                log.error("Inside save method of CountryServiceImpl Validation errors: {}", violations);
                throw new CustomGeneralException(violations);
            }
            if (country.getCountryId() == null) {
                country.setIsActive(true);
                countryRepository.save(country);
            } else {
                Country existingCountry = countryRepository.findCountryById(country.getCountryId())
                        .orElseThrow(() -> new CustomEntityNotFoundException(errorMessages.getEntityNotFound()));

                existingCountry.setCountryName(country.getCountryName());
                existingCountry.setCountryCode(country.getCountryCode());
                countryRepository.save(existingCountry);
            }
        } catch (DataIntegrityViolationException e) {
            log.error("DataIntegrityViolationException: {}", e.getMessage(), e);
            Throwable cause = e.getMostSpecificCause();
            if (cause instanceof SQLException sqlException) {
                String message = sqlException.getMessage();
                if (message.contains("country_name")) {
                    throw new CustomGeneralException(errorMessages.getDuplicateCountryName());
                } else if (message.contains("country_code")) {
                    throw new CustomGeneralException(errorMessages.getDuplicateCountryCode());
                } else {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            log.error("Error occurred in saveOrUpdate method of CountryServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CountryResponse getById(Integer countryId) {
        log.info("Inside getById method of CountryServiceImpl");
        try {
            Country country = countryRepository.findCountryById(countryId)
                    .orElseThrow(() -> new EntityNotFoundException(errorMessages.getEntityNotFound()));

            return Mapper.mapToCountryDto(country);
        } catch (Exception e) {
            log.error("Error occurred in getById method of CountryServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryResponse> getAll() {
        log.info("Inside getAll method of CountryServiceImpl");
        try {
            return countryRepository.findAll().stream()
                    .map(Mapper::mapToCountryDto)
                    .toList();
        } catch (Exception e) {
            log.error("Error occurred in getAll method of CountryServiceImpl: : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CountryResponse> getAll(Integer pageNumber, Integer pageSize, String sortCol, String sortDir, String search) {
        log.info("Inside getAll paginated method of CountryServiceImpl");
        try {

            Sort sort = Sort.by(Sort.Direction.fromString(sortDir != null ? sortDir : "DESC"),sortCol != null ? sortCol : "createdAt");
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

            Page<Country> page;
            if (StringUtils.hasText(search)) {
                page = countryRepository.findByFilters(search, pageable);
            } else {
                page = countryRepository.findAllCountries(pageable);
            }

            List<CountryResponse> countryResponses = page.getContent()
                    .stream()
                    .map(Mapper::mapToCountryDto)
                    .toList();
            return new PageImpl<>(countryResponses, pageable, page.getTotalElements());
        } catch (Exception e) {
            log.info("Error occurred in getAll paginated method of CountryServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    
    
    
    @Override
    @Transactional
    @CacheEvict(value = "getCountries", allEntries = true)
    public void toggleActivationStatus(String data) {
        log.info("Inside toggleActivationStatus method of CountryServiceImpl");
        try {
            String decodedData = CommonUtil.inputStreamDecoder(data);
            Map<String, Object> jsonMap = objectMapper.readValue(decodedData, new TypeReference<>() {
            });
            Integer countryId = (Integer) jsonMap.get("countryId");
            Country existingCountry = countryRepository.findCountryById(countryId)
                    .orElseThrow(() -> new CustomEntityNotFoundException(errorMessages.getEntityNotFound()));
            existingCountry.setIsActive(!existingCountry.getIsActive());
            countryRepository.save(existingCountry);
        } catch (Exception e) {
            log.error("Error occurred in toggleActivationStatus method CountryServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

	@Override
	@Cacheable(value = "getCountries")
	public List<CountryResponse> getAllCountry() {
		
		
		
		List<CountryResponse> countryResponses = countryRepository.findByIsActiveTrue()
                .stream()
                .map(Mapper::mapToCountryDto)
                .toList();
		
		
		return countryResponses ;
		
	}

	@Override
	public Page<CountryResponse> getCountryDetails(int page, int size,String search) {
		Pageable pageable = PageRequest.of(page, size);
		 Page<CountryResponse> resp;
         if (StringUtils.hasText(search)) {
        	 resp = countryRepository.findByFilters(search, pageable).map(Mapper::mapToCountryDto);
         } else {
        	 resp = countryRepository.findAllCountries(pageable).map(Mapper::mapToCountryDto);
         }

       return resp;
	}
}