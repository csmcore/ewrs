package app.ewarehouse.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ApplicationFeeRequestDto;
import app.ewarehouse.dto.ApplicationFeeResponseDto;
import app.ewarehouse.entity.ApplicationFeeConfig;
import app.ewarehouse.exception.DuplicateComplaintStatusException;
import app.ewarehouse.repository.ApplicationFeeConfigRepository;
import app.ewarehouse.service.ApplicationFeeConfigService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.Mapper;

@Service
public class ApplicationFeeConfigServiceImpl implements ApplicationFeeConfigService {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationFeeConfigServiceImpl.class);
	private static final String STATUS = "status";
	private final ApplicationFeeConfigRepository repository;
	private final ObjectMapper objectMapper;

	public ApplicationFeeConfigServiceImpl(ApplicationFeeConfigRepository repository, ObjectMapper objectMapper) {
		this.repository = repository;
		this.objectMapper = objectMapper;
	}

	// Updated Service Code
	@Override
	public JSONObject saveApplicationFeeConfig(String data) {
		logger.info("Inside saveApplicationFeeConfig of ApplicationFeeConfigServiceImpl class");

		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject responseJson = new JSONObject();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			ApplicationFeeRequestDto dto = objectMapper.readValue(decodedData, ApplicationFeeRequestDto.class);

			boolean isDuplicate = repository.findByApplicationType(dto.getApplicationType()).filter(
					existingEntity -> dto.getId() == null || !existingEntity.getApplicationFeeId().equals(dto.getId()))
					.isPresent();

			if (isDuplicate) {
				logger.warn("Duplicate entry detected for application type: {}", dto.getApplicationType());
				throw new DuplicateComplaintStatusException("Duplicate entry: Application Type already exists");
			}

			// Handle update or save operation
			Optional<ApplicationFeeConfig> optionalEntity = Optional.ofNullable(dto.getId())
					.flatMap(repository::findById);

			optionalEntity.ifPresentOrElse(entity -> {
				entity.setPaymentValue(dto.getApplicationFee());
				repository.save(entity);
				logger.info("Application fee updated successfully");
				responseJson.put(STATUS, "Application Fee updated Successfully");
			}, () -> {
				ApplicationFeeConfig newEntity = new ApplicationFeeConfig();
				newEntity.setApplicationType(dto.getApplicationType());
				newEntity.setApplicationTypeAliasName(generateAbbreviation(dto.getApplicationType()));
				newEntity.setPaymentValue(dto.getApplicationFee());
				repository.save(newEntity);
				logger.info("Application fee saved successfully");
				responseJson.put(STATUS, "Application Fee saved Successfully");
			});

		} catch (DuplicateComplaintStatusException e) {
			throw e;
		} catch (Exception e) {
			logger.error("An error occurred while saving the data", e);
			responseJson.put(STATUS, "Error");
			responseJson.put("message", "There is some issue while saving data");
		}

		return responseJson;
	}

	private static String generateAbbreviation(String input) {
		String[] words = input.split("\\s+");
		StringBuilder abbreviation = new StringBuilder();
		for (String word : words) {
			abbreviation.append(Character.toUpperCase(word.charAt(0)));
		}
		String result = abbreviation.toString();
		if (result.startsWith("AF")) {
			result = result.substring(2);
		}
		return result;
	}

	@Override
	public boolean isDuplicate(String applicationType, Integer id) {
		Optional<ApplicationFeeConfig> existing = repository.findByApplicationType(applicationType);
		return existing.isPresent() && !existing.get().getApplicationFeeId().equals(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ApplicationFeeResponseDto> getAll(Integer pageNumber, Integer pageSize,String search) {
		logger.info("Inside getAll paginated method of ApplicationFeeConfigServiceImpl");
		try {

			Pageable pageable = PageRequest.of(pageNumber, pageSize);

			Page<ApplicationFeeConfig> page;
			if (StringUtils.hasText(search)) {
				page = repository.findByFilters(search, pageable);
			} else {
				page = repository.findAllApplicationFeeData(pageable);
			}

			List<ApplicationFeeResponseDto> responses = page.getContent().stream().map(Mapper::mapToApplicationFeesDto)
					.toList();
			return new PageImpl<>(responses, pageable, page.getTotalElements());
		} catch (Exception e) {
			logger.info("Error occurred in getAll paginated method of ApplicationFeeConfigServiceImpl: {}",
					e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public JSONObject getFeeByAliasName(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject(decodedData);
		String aliasName = json.getString("name");
		Double fee = repository.findPaymentValueByAliasName(aliasName);
		String feeMessage = Optional.ofNullable(fee).map(value -> value.toString()).orElse("Fee is not set");
		JSONObject response = new JSONObject();
		response.put("fee", feeMessage);
		return response;
	}

}
