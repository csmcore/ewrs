package app.ewarehouse.serviceImpl;

import java.util.List;
import java.util.Objects;
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

import app.ewarehouse.dto.LotMasterRequestDto;
import app.ewarehouse.dto.LotMasterResponseDto;
import app.ewarehouse.entity.LotMaster;
import app.ewarehouse.exception.DuplicateDataFoundException;
import app.ewarehouse.repository.LotMasterRepository;
import app.ewarehouse.service.LotMasterService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.Mapper;
@Service
public class LotMasterServiceImpl implements LotMasterService {

	private static final Logger logger = LoggerFactory.getLogger(LotMasterServiceImpl.class);
	private static final String STATUS = "status";
	private LotMasterRepository lotMasterRepository;
	private ObjectMapper objectMapper;
	
	public LotMasterServiceImpl(LotMasterRepository lotMasterRepository , ObjectMapper objectMapper) {
		this.lotMasterRepository = lotMasterRepository;
		this.objectMapper = objectMapper;
	}

	@Override
	public JSONObject saveLot(String data) {
		logger.info("Inside saveLot of LotMasterServiceImpl class");
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject responseJson = new JSONObject();
	    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    try {
			LotMasterRequestDto dto = objectMapper.readValue(decodedData , LotMasterRequestDto.class);
			//check duplicate entry if exist
			boolean isDuplicate = lotMasterRepository.findByTypeOfLot(dto.getTypeOfLot())
				    .filter(existingLot -> dto.getId() == null || !Objects.equals(existingLot.getLotId(), dto.getId()))
				    .isPresent();
			if(isDuplicate) {
				logger.warn("Duplicate entry detected for Type Of Lot : {}", dto.getTypeOfLot());
	            throw new DuplicateDataFoundException("Duplicate entry: Type Of Lot already exists");
			}
			if("Maximum".equals(dto.getTypeOfLot())) {
				Optional<Integer> minimumNoOfBag =   lotMasterRepository.findNoOfBagsForMinimumType();
				minimumNoOfBag.ifPresent(minBags -> {
	                if (dto.getNoOfBags() < minBags) {
	                    throw new IllegalArgumentException("Maximum Number of bags cannot be less than the minimum Number of bags.");
	                }
	            });
			}
			if("Minimum".equals(dto.getTypeOfLot())) {
				Optional<Integer> maximumNoOfBag =   lotMasterRepository.findNoOfBagsForMaximumType();
				maximumNoOfBag.ifPresent(maxBags -> {
	                if (dto.getNoOfBags() > maxBags) {
	                    throw new IllegalArgumentException("Minimum Number of bags cannot be greater than the maximum Number of bags.");
	                }
	            });
			}
			
			Optional<LotMaster> optionalEntity = Optional.ofNullable(dto.getId())
	                .flatMap(lotMasterRepository::findById);
			optionalEntity.ifPresentOrElse(
	                entity -> {
	                	entity.setNoOfBags(dto.getNoOfBags());
	                	entity.setKgPerBag(dto.getKgPerBag());
	                    lotMasterRepository.save(entity);
	                    logger.info("Type of Lot updated successfully");
	                    responseJson.put(STATUS, "Type of Lot updated Successfully");
	                },
	                () -> {
	                    LotMaster newEntity = new LotMaster();
	                    newEntity.setTypeOfLot(dto.getTypeOfLot());
	                    newEntity.setNoOfBags(dto.getNoOfBags());
	                    newEntity.setKgPerBag(dto.getKgPerBag());
	                    lotMasterRepository.save(newEntity);
	                    logger.info("Type of Lot saved successfully");
	                    responseJson.put(STATUS, "Type of Lot saved Successfully");
	                }
	        );
			
		} 
	    catch (DuplicateDataFoundException de) {
	        throw de;
	    }
	    catch (IllegalArgumentException ie) {
	    	throw ie;
	    } 
	    catch (Exception e) {
			logger.error("An error occurred while saving the data", e);
	        responseJson.put(STATUS, "Error");
	        responseJson.put("message", "There is some issue while saving data");
		}
		return responseJson;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LotMasterResponseDto> getAll(Integer pageNumber, Integer pageSize, String search) {
		logger.info("Inside getAll paginated method of LotMasterServiceImpl");
        try {

            
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<LotMaster> page;
            if (StringUtils.hasText(search)) {
                page = lotMasterRepository.findByFilters(search, pageable);
            } else {
                page = lotMasterRepository.findAllLotData(pageable);
            }

            List<LotMasterResponseDto> responses = page.getContent()
                    .stream()
                    .map(Mapper::mapToLotDto)
                    .toList();
            return new PageImpl<>(responses, pageable, page.getTotalElements());
        } catch (Exception e) {
            logger.info("Error occurred in getAll paginated method of LotMasterServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
	}

}
