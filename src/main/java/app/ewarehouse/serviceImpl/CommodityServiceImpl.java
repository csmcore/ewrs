package app.ewarehouse.serviceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.entity.BuyerDepositorAndWareHouseOperator;
import app.ewarehouse.entity.Commodity;
import app.ewarehouse.entity.CommodityMarket;
import app.ewarehouse.entity.IssuanceWarehouseReceiptActionHistory;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.exception.CustomEntityNotFoundException;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.master.dto.UpdateCommoditySellingPrice;
import app.ewarehouse.repository.BuyerDepositorAndWareHouseOperatorRepository;
import app.ewarehouse.repository.CommodityMarketRepository;
import app.ewarehouse.repository.CommodityMasterRepository;
import app.ewarehouse.repository.CommodityTypeRepository;
import app.ewarehouse.repository.IssuanceWarehouseReceiptActionHistoryRepository;
import app.ewarehouse.repository.SeasonalityRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.CommodityService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;

@Service
public class CommodityServiceImpl implements CommodityService {
	
	@Autowired
	private CommodityMasterRepository commoditymasterrepository;
	
	@Autowired
	private SeasonalityRepository seasonalityRepository;
	
	@Autowired
	private CommodityTypeRepository commodityTypeRepository;
	
	@Autowired
	CommodityMarketRepository commodityMarketRepository;
	@Autowired
	BuyerDepositorAndWareHouseOperatorRepository buyerDepositorAndWareHouseOperatorRepository;
	
    @Autowired
	TuserRepository tuserRepository ;
		
	@Autowired
	IssuanceWarehouseReceiptActionHistoryRepository issuanceWarehouseReceiptActionHistoryRepository;
	
	private final ErrorMessages errorMessages = new ErrorMessages();
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	

	private static final Logger logger = LoggerFactory.getLogger(CommodityServiceImpl.class);
	
	

	@Transactional
	@Override
	public Integer save(String commodityRequest) {
		String decodedData = CommonUtil.inputStreamDecoder((commodityRequest));
		Commodity commodity;
		 try {
			 commodity = objectMapper.readValue(decodedData, Commodity.class);
			 Commodity complaintTypeDetails = commoditymasterrepository.findByNameIgnoreCase(commodity.getName());
			 if (complaintTypeDetails == null) {
				 var season = seasonalityRepository.findById((commodity.getSeasonality().getId())).orElse(null);
				 var type = commodityTypeRepository.findById((commodity.getType().getId())).orElse(null);

				 Commodity commodity1 = Commodity.builder()
						 .commodityType(commodity.getCommodityType())
						 .name(commodity.getName())
						 .commodityOrigin(commodity.getCommodityOrigin())
						 .charges(commodity.getCharges())
						 .seasonality(season)
						 .type(type)
						 .commodityCode(commodity.getCommodityCode())
						 .bitDeleteFlag(false)
						 .build();

				 var savedCommodity = commoditymasterrepository.save(commodity1);

				 if (savedCommodity.getId() > 0) {
					 return 1;
				 } else {
					 return 0;
				 }
			 } else {
				 return 2;
			 }
		 } catch (Exception e) {
			 logger.error(String.valueOf(e));
	            return 0;
	        }
	}

	@Override
	public List<Commodity> getAll() {
		return commoditymasterrepository.findByBitDeleteFlag(false);
	}

	@Override
	public Commodity getById(Integer id) {
		java.util.Optional<Commodity> optionalCommodity = commoditymasterrepository.findById(id);
        return optionalCommodity.orElse(null);
	}

	@Transactional
	@Override
	public Integer update(Integer commodityId, String commodityRequest) {
		String decodedData = CommonUtil.inputStreamDecoder((commodityRequest));
		Commodity commodity;
	    try {
			commodity = objectMapper.readValue(decodedData, Commodity.class);
			var season = seasonalityRepository.findById((commodity.getSeasonality().getId())).orElse(null);
			var type = commodityTypeRepository.findById((commodity.getType().getId())).orElse(null);

			Commodity duplicateCommodityType = commoditymasterrepository.findByNameIgnoreCase(commodity.getName());
			if (duplicateCommodityType != null && !duplicateCommodityType.getId().equals(commodityId)) {
				return 2;
			}

	        Commodity existingCommodity = commoditymasterrepository.findById(commodityId).orElse(null);
	        if (existingCommodity == null) {
				return 0;
			}
	        existingCommodity.setCommodityType(commodity.getCommodityType());
	        existingCommodity.setName(commodity.getName());
	        existingCommodity.setCommodityOrigin(commodity.getCommodityOrigin());
	        existingCommodity.setCharges(commodity.getCharges());
	        existingCommodity.setSeasonality(season);
	        existingCommodity.setType(type);
	        existingCommodity.setCommodityCode(commodity.getCommodityCode());
			commoditymasterrepository.save(existingCommodity);
			return 1;
	    } catch (Exception e) {
	        return 0;
	    }
	}

	
	@Override
	@Transactional
	public void delete(String data) {
		logger.info("Inside delete method of CommodityServiceImpl");
		try {
			String decodedData = CommonUtil.inputStreamDecoder(data);
			Map<String, Object> jsonMap = objectMapper.readValue(decodedData, new TypeReference<>() {
			});
			Integer id = (Integer) jsonMap.get("id");
			Commodity existingCommodity = commoditymasterrepository.findCommodityById(id)
					.orElseThrow(() -> new CustomEntityNotFoundException(errorMessages.getEntityNotFound()));
			existingCommodity.setBitDeleteFlag(!existingCommodity.getBitDeleteFlag());
			commoditymasterrepository.save(existingCommodity);
		} catch (Exception e) {
			logger.error("Error occurred in toggleActivationStatus method CountryServiceImpl: {}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Page<Commodity> getAllCommoditiesList(Pageable pageable,String search) {
		Page<Commodity> page;
		try {
			if (StringUtils.hasText(search)) {
				page = commoditymasterrepository.findAllCommoditiesFilter(search,pageable);
			} else {
				page = commoditymasterrepository.findAllCommodities(pageable);
			}
		} catch (Exception e) {
            logger.error("Error occurred in getAll paginated method of CommodityServiceImpl: {}",e);
            throw new RuntimeException(e);
        }
	  return page;
	}

	@Override
	public Page<Commodity> allCommoditiesList(Integer pageNumber, Integer pageSize, String sortCol, String sortDir,
			String search) {
		   logger.info("Inside getAll paginated method of CommodityServiceImpl");
		   Page<Commodity> page;
	        try {

	            Sort sort = Sort.by(Sort.Direction.fromString(sortDir != null ? sortDir : "DESC"),
	                    sortCol != null ? sortCol : "dtmCreatedAt");
	            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

	            if (StringUtils.hasText(search)) {
	            	 page = commoditymasterrepository.findAllCommoditiesFilter(search,pageable);
	            } else {
	                page = commoditymasterrepository.findAllCommodities(pageable);
	            }
	        } catch (Exception e) {
	            logger.error("Error occurred in getAll paginated method of CommodityServiceImpl: {}",e);
	            throw new RuntimeException(e);
	        }
            return page;
	}
	
	
//	public CommodityMarket updateSellingPrice(String requestJson) {
//	    try {
//	        String decodedData = CommonUtil.inputStreamDecoder(requestJson);
//
//	        UpdateCommoditySellingPrice request = new ObjectMapper().readValue(decodedData, UpdateCommoditySellingPrice.class);
//
//	        String finalDate = convertDateFormat(request.getDate());
//
//	        CommodityMarket marketData = commodityMarketRepository.findByDepositorIdAndCommodityNameAndDate(
//	                request.getDepositorId(), request.getCommodityName(), finalDate)
//	                .orElseThrow();
//
//	        marketData.setSellingPriceLowerLimit(request.getSellingPriceLowerLimit());
//	        marketData.setSellingPriceUpperLimit(request.getSellingPriceUpperLimit());
//	        commodityMarketRepository.updateTradeStatus(request.getDepositorId());
//	        return commodityMarketRepository.save(marketData);
//
//	    } catch (JsonProcessingException e) {
//	        throw new CustomGeneralException("Invalid JSON format: " + e.getMessage());
//	    }
//	}
	
	public CommodityMarket updateSellingPrice(String requestJson) {
	    try {
	        String decodedData = CommonUtil.inputStreamDecoder(requestJson);
	        
	        JsonNode jsonNode = new ObjectMapper().readTree(decodedData);
	        
	        Integer userId = jsonNode.has("userId") ? jsonNode.get("userId").asInt() : null;
	        String userName = jsonNode.has("userName") ? jsonNode.get("userName").asText() : null;
	        Integer roleId = jsonNode.has("roleId") ? jsonNode.get("roleId").asInt() : null;
	        String roleName = jsonNode.has("roleName") ? jsonNode.get("roleName").asText() : null;
	        

	        //UpdateCommoditySellingPrice request = new ObjectMapper().readValue(decodedData, UpdateCommoditySellingPrice.class);
	        
	     // Now deserialize only the part you care about
	        UpdateCommoditySellingPrice request = new ObjectMapper().treeToValue(jsonNode, UpdateCommoditySellingPrice.class);

	        String finalDate = convertDateFormat(request.getDate());

	        CommodityMarket marketData = new CommodityMarket();
	        marketData.setOpenPrice(request.getOpenPrice());
	        marketData.setClosingPrice(request.getClosingPrice());
	        marketData.setLowPrice(request.getLowPrice());
	        marketData.setHighPrice(request.getHighPrice());
	        marketData.setCommodityName(request.getCommodityName());
	        marketData.setMarketPrice(request.getMarketPrice());
	        marketData.setMarketValue(request.getMarketValue());
	        marketData.setDate(finalDate);
	        marketData.setSellingPriceLowerLimit(request.getSellingPriceLowerLimit());
	        marketData.setSellingPriceUpperLimit(request.getSellingPriceUpperLimit());
	        marketData.setDepositorId(request.getDepositorId());
	        marketData.setIssueId(request.getIssueId());
	        commodityMarketRepository.updateTradeStatus(request.getDepositorId(),request.getIssueId());
	        
	     // Step 2: Update existing ActionHistory record
            IssuanceWarehouseReceiptActionHistory actionHistory = new IssuanceWarehouseReceiptActionHistory();
            actionHistory.setIntIssueanceWhId(request.getIssueId()); // or whatever field holds the PK
            actionHistory.setDepositorId(request.getDepositorId());
            actionHistory.setVchActionTakenBy(userName);
            actionHistory.setRoleId(roleId);
            actionHistory.setRoleName(roleName);
            actionHistory.setVchStatus("Listing Applied");
            actionHistory.setUserId(userId);
            issuanceWarehouseReceiptActionHistoryRepository.save(actionHistory);
            
	        return commodityMarketRepository.save(marketData);

	    } catch (JsonProcessingException e) {
	        throw new CustomGeneralException("Invalid JSON format: " + e.getMessage());
	    }
	}


	private String convertDateFormat(String dateStr) {
	    if (dateStr == null || dateStr.trim().isEmpty()) {
	        throw new CustomGeneralException("Date cannot be null or empty");
	    }

	    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String[] dateFormats = {"dd/MM/yyyy", "dd MMMM, yyyy"};

	    for (String format : dateFormats) {
	        try {
	            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(format);
	            LocalDate date = LocalDate.parse(dateStr, inputFormatter);
	            return date.format(outputFormatter);
	        } catch (DateTimeParseException ignored) {
	            //return null;
	        }
	    }

	    throw new CustomGeneralException("Invalid date format: " + dateStr);
	}



	
	

	 
	public CommodityMarket getMarketData(String depositorId, String commodityName, String date) {
        Optional<CommodityMarket> marketData;

        if (date != null && !date.isEmpty()) {
        	String formattedDate = convertDateFormat(date);
            marketData = commodityMarketRepository.findByDepositorIdAndCommodityNameAndDate(depositorId, commodityName, formattedDate);
        } else {
            marketData = commodityMarketRepository.findTopByDepositorIdAndCommodityNameOrderByDateDesc(depositorId, commodityName);
        }
        return marketData.isPresent() ? marketData.get() : null;
    }

	@Override
	public List<BuyerDepositorAndWareHouseOperator> getDepositorDetails() {
		return buyerDepositorAndWareHouseOperatorRepository.findBybitDeletedFlag(false);
	}
}

