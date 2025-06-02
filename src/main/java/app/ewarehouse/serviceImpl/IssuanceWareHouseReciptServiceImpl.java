package app.ewarehouse.serviceImpl;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import app.ewarehouse.dto.BuyerDepositorAndWareHouseOperatorDto;
import app.ewarehouse.dto.IssuanceWareHouseReciptDto;
import app.ewarehouse.dto.IssuaneWareHouseOperatorChargesDto;
import app.ewarehouse.entity.BuyerDepositorAndWareHouseOperator;
import app.ewarehouse.entity.BuyerWareHosueDetails;
import app.ewarehouse.entity.ChargesHeader;
import app.ewarehouse.entity.Commodity;
import app.ewarehouse.entity.Country;
import app.ewarehouse.entity.County;
import app.ewarehouse.entity.IssuanceWareHouseRecipt;
import app.ewarehouse.entity.IssuanceWarehouseReceiptActionHistory;
import app.ewarehouse.entity.IssuaneWareHouseOperatorCharges;
import app.ewarehouse.entity.Seasonality;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.SubCounty;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.entity.UnitType;
import app.ewarehouse.master.entity.WardMaster;
import app.ewarehouse.master.repository.WardMasterRepo;
import app.ewarehouse.repository.BuyerDepositorAndWareHouseOperatorRepository;
import app.ewarehouse.repository.BuyerWareHosueDetailsRepository;
import app.ewarehouse.repository.ChargesHeaderRepository;
import app.ewarehouse.repository.CommodityMasterRepository;
import app.ewarehouse.repository.CountryRepository;
import app.ewarehouse.repository.CountyRepository;
import app.ewarehouse.repository.IssuanceWareHouseReciptRepository;
import app.ewarehouse.repository.IssuanceWarehouseReceiptActionHistoryRepository;
import app.ewarehouse.repository.SeasonalityRepository;
import app.ewarehouse.repository.SubCountyRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.repository.UnitTypeRepository;
import app.ewarehouse.service.IssuanceWareHouseReciptService;
import app.ewarehouse.util.CommonUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IssuanceWareHouseReciptServiceImpl implements IssuanceWareHouseReciptService {
	private static final String ERROR = "error";
	private static final Logger logger = LoggerFactory.getLogger(ConformityParticularServiceImpl.class);
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${finalUpload.path}")
	private String finalUploadPath;
	
	private static final String PREFIXAPPLCNTID = "WRS";
	private static final String SUFFIX = "S1";
	private static final AtomicInteger counter = new AtomicInteger(1272);
	
	private static final String PREFIX = "WR";
	private static final String YEAR = "2025";
	//private static final AtomicInteger counter = new AtomicInteger(120);
	private static final DecimalFormat ID_FORMAT = new DecimalFormat("000");
	
		@Autowired
		private BuyerDepositorAndWareHouseOperatorRepository buyerDepositorAndWareHouseOperatorRepository;
		
		
		@Autowired
		private CommodityMasterRepository commoditymasterrepository;
		
		@Autowired
		private SeasonalityRepository seasonalityRepository;
		
		@Autowired
		private ChargesHeaderRepository chargesHeaderRepository;
		
		@Autowired
		private IssuanceWareHouseReciptRepository issuanceWareHouseReciptRepository;
	
		@Autowired
		private UnitTypeRepository unitTypeRepository;
	
	 	@Autowired
	 	private ObjectMapper om;
	 
		
		@Autowired
		private BuyerDepositorAndWareHouseOperatorRepository depositorRepositoryDetails;
		
	    @Autowired
	    private SubCountyRepository subCountyRepository;
	    
	    @Autowired
	    private CountyRepository countyRepository;
	    
	    @Autowired
	    private WardMasterRepo wardMasterRepo;
	    
	    @Autowired
	    private CountryRepository countryRepository;
	    
	    @Autowired
	    private TuserRepository tuserRepository;
	    
	    @Autowired
	    private BuyerWareHosueDetailsRepository buyerWareHosueDetailsRepository;
	    
		@Autowired
		IssuanceWarehouseReceiptActionHistoryRepository issuanceWarehouseReceiptActionHistoryRepository;
	    

//	 @Override
//		public List<Map<String, Object>> getAllDepositorId(String vchWarehouseId) {
//		
//		 List<Object[]> results = buyerDepositorAndWareHouseOperatorRepository.findDepositorIdsByWarehouseId(vchWarehouseId);
//	        List<Map<String, Object>> response = new ArrayList<>();
//
//	        for (Object[] row : results) {
//	            Map<String, Object> map = new HashMap<>();
//	            map.put("depoId", row[0]);  // Integer
//	            map.put("depoName", row[0] + "-" + row[1]);  // String
//	            response.add(map);
//	        }
//	        return response;
//			
//		}
	 
	 
	 @Override
		public List<Map<String, Object>> getAllDepositorId() {
		
		 List<Object[]> results = buyerDepositorAndWareHouseOperatorRepository.findDepositorIdsByWarehouseId();
	        List<Map<String, Object>> response = new ArrayList<>();

	        for (Object[] row : results) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("depoId", row[0]);  // Integer
	            map.put("depoName", row[0] + "-" + row[1]);  // String
	            response.add(map);
	        }
	        return response;
			
		}
	 

	@Override
	public Map<String, Object> getDepositorIdByWareHouse(String id) { 
		Optional<BuyerDepositorAndWareHouseOperator> optionalDepositor = buyerDepositorAndWareHouseOperatorRepository.findByIntDepositorWhOperator(id);

    if (optionalDepositor.isPresent()) {
        BuyerDepositorAndWareHouseOperator depositor = optionalDepositor.get();
        
        // Map only required fields
        Map<String, Object> response = new HashMap<>();
        response.put("nameOfPerson", depositor.getNameOfDepositor());
        response.put("passportNumber", depositor.getVchIdNo());
        response.put("centralRegistryIdentifier", "--"); // Static value
        response.put("postalAddress", depositor.getPostalAddress());
        response.put("telephoneNo", depositor.getMobileNumber());
        response.put("emailAddress", depositor.getEmailAddress());
        response.put("county", depositor.getCountyId() != null ? depositor.getCountyId().getName() : null);
        response.put("subCounty", depositor.getSubCountyId() != null ? depositor.getSubCountyId().getTxtSubCountyName() : null);
        response.put("ward", depositor.getIntWard() != null ? depositor.getIntWard().getWardName() : null);
        response.put("userId", depositor.getIntCreatedBy());
        return response;
    } else {
    	 throw new NoSuchElementException("Depositor not found with ID: " + id);
    }
}


	@Override
	public List<Map<String, Object>> getAllCommodityName() {
		
		 List<Object[]> results = commoditymasterrepository.getAllCommodityName();
	        List<Map<String, Object>> response = new ArrayList<>();

	        for (Object[] row : results) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("Id", row[0]);  // Integer
	            map.put("name", row[1]);  // String
	            response.add(map);
	        }
	        return response;
		
	}

	@Override
	public List<Map<String, Object>> getCommodityNameByOriginAndCode(Integer id) {
		
		List<Object[]> results = commoditymasterrepository.getCommodityNameByOriginAndCode(id);
	    List<Map<String, Object>> commodities = new ArrayList<>();

	    for (Object[] row : results) {
	        Map<String, Object> commodity = new HashMap<>();
	        commodity.put("commodityOrigin", row[0]); // First column
	        commodity.put("commodityCode", row[1]);   // Second column
	        commodities.add(commodity);
	    }
	    return commodities;
		
		
	}

	@Override
    public List<Map<String, Object>> getAllSeason() {  // ✅ Matches interface
        List<Object[]> results = seasonalityRepository.getAllSeason();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("Id", row[0]);  // Integer
            map.put("seasonName", row[1]);  // String
            response.add(map);
        }
        return response;
    }


	@Override
	public List<Map<String, Object>> getAllChargesHeader() {
		
		List<Object[]> results = chargesHeaderRepository.getAllChargesHeader();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("intChargesHeader", row[0]);
            map.put("vchChargesHeader", row[1]);
            response.add(map);
        }
        return response;
        

	}

	@Override
	public List<Map<String, String>> getChargesHeaderByUnitType(Integer chargesId) {
	    List<Object[]> results = chargesHeaderRepository.getChargesHeaderByUnitType(chargesId);
	    List<Map<String, String>> response = new ArrayList<>();

	    for (Object[] row : results) {
	        Map<String, String> map = new HashMap<>();
	        map.put("intUnitType", row[0].toString());  // Convert to String
	        map.put("vchUnitType", row[1].toString());
	        response.add(map);
	    }
	    return response;
	}
	
	@Override
	@Transactional
	public IssuanceWareHouseRecipt saveIssuanceWareHouseRecipt(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
																																																																																																																																																																																																																																																																																																																																																																																																																							om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try {
		IssuanceWareHouseReciptDto	dto = om.readValue(decodedData, IssuanceWareHouseReciptDto.class);
		
	    IssuanceWareHouseRecipt issuance = new IssuanceWareHouseRecipt();

	    // ✅ Fetch Depositor/Warehouse Operator
	    BuyerDepositorAndWareHouseOperator bd = buyerDepositorAndWareHouseOperatorRepository
	            .findById(dto.getIntDepositorWhOperator())
	            .orElseThrow(() -> new RuntimeException("Depositor not found: " + dto.getIntDepositorWhOperator()));
	    issuance.setIntDepositorWhOperator(bd);

	    // ✅ Fetch Commodity Type
//	    Commodity commodity = commoditymasterrepository
//            .findById(dto.getCommodityType())
//           .orElseThrow(() -> new RuntimeException("Commodity not found: " + dto.getCommodityType()));
//	    issuance.setCommodityType(commodity);
	    issuance.setCommodityType(dto.getCommodityType());

	    // ✅ Set Other Parent Fields
	    issuance.setCommodityOrigin(dto.getCommodityOrigin());
	    issuance.setCommodityCode(dto.getCommodityCode());
	    issuance.setOriginalQuantity(dto.getOriginalQuantity());
	    issuance.setOriginalGrossWeight(dto.getOriginalGrossWeight());
	    issuance.setOriginalNetWeight(dto.getOriginalNetWeight());
	    issuance.setCurrentQuantity(dto.getCurrentQuantity());
	    issuance.setCurrentNetWeight(dto.getCurrentNetWeight());
	    issuance.setGrnPath(dto.getGrnPath());
	    issuance.setQualityInspectionPath(dto.getQualityInspectionPath());
	    issuance.setWeighingTicketPath(dto.getWeighingTicketPath());
	    issuance.setVchWarehouseId(dto.getWareHouseId());
	    
	    // ✅ Fetch Crop Year (Seasonality)
	    Seasonality seasonality = seasonalityRepository
	            .findById(dto.getCropYear())
	            .orElseThrow(() -> new RuntimeException("Crop year not found: " + dto.getCropYear()));
	    issuance.setCropYear(seasonality);

	    issuance.setGrade(dto.getGrade());
	    issuance.setLotNumber(dto.getLotNumber());
//        processDocument(dto.getQualityInspectionPath(),  issuance::setQualityInspectionPath, "ISSUANCE_WAREHOUSE_RECIPT_", 
//        		FolderAndDirectoryConstant.ISSUANCE_WAREHOUSE_RECIPT);
//
//	    //issuance.setQualityInspectionPath(dto.getQualityInspectionPath());
//        processDocument(dto.getWeighingTicketPath(),  issuance::setWeighingTicketPath, "ISSUANCE_WAREHOUSE_RECIPT_", 
//        		FolderAndDirectoryConstant.ISSUANCE_WAREHOUSE_RECIPT);
//	   // issuance.setWeighingTicketPath(dto.getWeighingTicketPath());
//        processDocument(dto.getGrnPath(),  issuance::setGrnPath, "ISSUANCE_WAREHOUSE_RECIPT_", 
//        		FolderAndDirectoryConstant.ISSUANCE_WAREHOUSE_RECIPT);
//	    //issuance.setGrnPath(dto.getGrnPath());
	    
	    List<String> fileUploadList = Arrays.asList(
	            dto.getQualityInspectionPath(),
	            dto.getWeighingTicketPath(),
	            dto.getGrnPath()
	        );
	    
	    fileUploadList.stream().filter(file -> !Strings.isNullOrEmpty(file))
		.forEach(file -> copyFileToDestination(file, "ISSUANCE_WAREHOUSE_RECIPT"));

//	        // ✅ Process Each File
//	        fileUploadList.stream()
//	            .filter(file -> file != null && !file.isEmpty()) // Avoid NullPointerException
//	            .forEach(file -> processDocument(file, filePath -> {
//	                if (file.equals(dto.getQualityInspectionPath())) {
//	                    issuance.setQualityInspectionPath(filePath);
//	                } else if (file.equals(dto.getWeighingTicketPath())) {
//	                    issuance.setWeighingTicketPath(filePath);
//	                } else if (file.equals(dto.getGrnPath())) {
//	                    issuance.setGrnPath(filePath);
//	                }
//	            }, "ISSUANCE_WAREHOUSE_RECIPT_", FolderAndDirectoryConstant.ISSUANCE_WAREHOUSE_RECIPT));


	    // ✅ Set Created & Updated By
	    //issuance.setIntCreatedBy(1);
	    issuance.setIntCreatedBy(dto.getUserId());
	    issuance.setSelectDate(dto.getDatePicker());
	    //issuance.setWareHouseReciptNo(generateNextID());

	    // ✅ Set Deleted Flag
	   // issuance.setBitDeletedFlag(false);

	    // ✅ Create Child List
	    List<IssuaneWareHouseOperatorCharges> chargesList = new ArrayList<>();

	    if (dto.getWareHouseChargesOperation() != null) {  // ✅ Prevents NullPointerException
            for (IssuaneWareHouseOperatorChargesDto chargeDto : dto.getWareHouseChargesOperation()) {
                IssuaneWareHouseOperatorCharges charge = new IssuaneWareHouseOperatorCharges();


	        // ✅ Fetch Charge Headers
	        ChargesHeader charges = chargesHeaderRepository
	                .findById(chargeDto.getChargeHeaders())
	                .orElseThrow(() -> new RuntimeException("Charge Header not found: " + chargeDto.getChargeHeaders()));
	        charge.setChargeHeaders(charges);

	        // ✅ Fetch Unit Type
	        UnitType unit = unitTypeRepository
	                .findById(chargeDto.getUnitType())
	                .orElseThrow(() -> new RuntimeException("Unit Type not found: " + chargeDto.getUnitType()));
	        charge.setUnitType(unit);

	        // ✅ Set Child Fields
	        charge.setUnitCharge(chargeDto.getUnitCharge());
	        charge.setQuantity(chargeDto.getQuantity());
	        charge.setTotalCharge(chargeDto.getTotalCharge());
	        charge.setGrandTotal(dto.getGrandTotal());
	        
	        Commodity commodity = commoditymasterrepository
	              .findById(chargeDto.getCommodityName())
	             .orElseThrow(() -> new RuntimeException("Commodity not found: " + chargeDto.getCommodityName()));
	        charge.setCommodityName(commodity);
	        		
	       // charge.setCommodityName(chargeDto.getCommodityName());
	        //charge.setIntCreatedBy(chargeDto.getUserId());

	        // ✅ Link Child to Parent
	        charge.setIntIssueanceWhId(issuance);
	        chargesList.add(charge);
	    }
	    }

	    // ✅ Set Child List in Parent
	    issuance.setIssuaneWareHouseOperatorCharges(chargesList);

	    // ✅ Save Parent (Cascade Saves Child)
	    return issuanceWareHouseReciptRepository.save(issuance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	private void copyFileToDestination(String fileUpload, String folderName) {
		String srcPath = tempUploadPath + fileUpload;
		// String destDir = finalUploadPath + "certificate-conformity-form-one-a/";
		String destDir = finalUploadPath + folderName + "/";
		String destPath = destDir + fileUpload;

		File srcFile = new File(srcPath);
		if (!srcFile.exists()) {
			logger.error("File not found: {}", srcPath);
			return;
		}

		try {
			Files.createDirectories(Paths.get(destDir));
			Files.copy(srcFile.toPath(), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
			Files.delete(srcFile.toPath());
			logger.info("File moved successfully: {}", fileUpload);
		} catch (IOException e) {
			logger.error("Error moving file: {}", fileUpload, e);
			throw new RuntimeException("Error moving file");
		}
	}
	
	
	@Transactional
	public synchronized String generateNextID() {
	    // Fetch the largest middle number from DB (e.g., 121 from "WR-121-2025")
	    Integer lastNumber = issuanceWareHouseReciptRepository.findLatestReceiptId();

	    // Start from 120 if no records exist, otherwise increment the last number
	    int nextID = (lastNumber != null && lastNumber > 0) ? lastNumber + 1 : 120;

	    // Generate the new unique receipt ID
	    return PREFIX + "-" + ID_FORMAT.format(nextID) + "-" + YEAR;
	}

	
	
//		private void processDocument(String path, Consumer<String> setter, String filePrefix, String folder) {
//		    try {
//		        // ✅ Decode Base64 Data
//		        byte[] decodedBytes = Base64.getDecoder().decode(path.getBytes());
//
//		        // ✅ Upload File & Get Uploaded Path
//		        String uploadedFileName = filePrefix + System.currentTimeMillis();
//		        String uploadedPath = DocumentUploadutil.uploadFileByte(uploadedFileName, decodedBytes, folder);
//		        
//		        // ✅ Move File to Final Destination
//		        copyFileToDestination(uploadedFileName, folder);
//
//		        // ✅ Set Processed File Path
//		        setter.accept(uploadedPath);
//
//		    } catch (IllegalArgumentException e) {
//		        logger.error("Invalid Base64 data for file: {}", path, e);
//		        throw new RuntimeException("Invalid Base64 encoding");
//		    } catch (Exception e) {
//		        logger.error("Error processing document: {}", path, e);
//		        throw new RuntimeException("Error processing document");
//		    }
//		}


	@Override
	public Page<IssuanceWareHouseRecipt> viewIssuance(String vchWarehouseId,Pageable pageable) {
		return issuanceWareHouseReciptRepository.findByVchWarehouseId(vchWarehouseId,pageable);
	}
	
	@Override
	public Page<IssuanceWareHouseRecipt> viewIssuanceOic(Pageable pageable) {
		return issuanceWareHouseReciptRepository.findAllByPaymentStatusReceived(pageable);
	}
	
	@Override
	public Page<IssuanceWareHouseRecipt> viewIssuanceCEO(Pageable pageable) {
		return issuanceWareHouseReciptRepository.findAllByOICStatusReceived(pageable);
	}

	@Override
	public Map<String, Object> updateConfirmationByDepositor(String id,Integer id1,String data) {
		
		   String decodedData = CommonUtil.inputStreamDecoder(data);
		    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		   
	    
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	 JsonNode jsonNode = om.readTree(decodedData);
			    
			    Integer userId=jsonNode.get("userId").asInt();
			    String userName=jsonNode.get("userName").asText();
			    Integer roleId=jsonNode.get("roleId").asInt();
			    String roleName=jsonNode.get("roleName").asText();
			    
	        IssuanceWareHouseRecipt wareHouseRecipt = issuanceWareHouseReciptRepository.findByIdAndIntDepositorWhOperatorAndBitDeletedFlag(id,id1,false);
	        
	        if (wareHouseRecipt != null) {
	            wareHouseRecipt.setReciptStatus(Status.ConfirmationreceivefromDepositor);
	            issuanceWareHouseReciptRepository.save(wareHouseRecipt);
	            
	            // Create new ActionHistory record
	            IssuanceWarehouseReceiptActionHistory actionHistory = new IssuanceWarehouseReceiptActionHistory();
	            actionHistory.setIntIssueanceWhId(wareHouseRecipt.getIssuanceWhId()); // or whatever field holds the PK
	            actionHistory.setDepositorId(id);
	            actionHistory.setVchActionTakenBy(userName);
	            actionHistory.setRoleId(roleId);
	            actionHistory.setRoleName(roleName);
	            actionHistory.setUserId(userId);
	            actionHistory.setVchStatus("Confirmed");
	            
                issuanceWarehouseReceiptActionHistoryRepository.save(actionHistory);
	        }
	        response.put("status", 200);
	        response.put("message", "Confirmation updated successfully");
	    } catch (Exception e) {
	        response.put("status", 500);
	        response.put("message", "Error updating confirmation");
	    }
	    return response;
	}
 
 
	@Override
	public Map<String, Object> generateWarehouseReceiptNo(String id,Integer id1,String data) {
		
		 String decodedData = CommonUtil.inputStreamDecoder(data);
		    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	    JsonNode jsonNode = om.readTree(decodedData);
			    
			    Integer userId=jsonNode.get("userId").asInt();
			    String userName=jsonNode.get("userName").asText();
			    Integer roleId=jsonNode.get("roleId").asInt();
			    String roleName=jsonNode.get("roleName").asText();
			    
	        IssuanceWareHouseRecipt wareHouseRecipt = issuanceWareHouseReciptRepository.findByIdAndIntDepositorWhOperatorAndBitDeletedFlag(id,id1,false);
	        
	        if (wareHouseRecipt != null) {
	            wareHouseRecipt.setWareHouseReciptNo(generateNextID());
	            wareHouseRecipt.setReciptStatus(Status.Issued);
	            issuanceWareHouseReciptRepository.save(wareHouseRecipt);
	            
	            // Step 2: Update existing ActionHistory record
	            IssuanceWarehouseReceiptActionHistory actionHistory = new IssuanceWarehouseReceiptActionHistory();
	            actionHistory.setIntIssueanceWhId(wareHouseRecipt.getIssuanceWhId()); // or whatever field holds the PK
	            actionHistory.setDepositorId(id);
	            actionHistory.setVchActionTakenBy(userName);
	            actionHistory.setRoleId(roleId);
	            actionHistory.setRoleName(roleName);
	            actionHistory.setUserId(userId);
	            actionHistory.setVchStatus("Receipt Generated");
	            
	            issuanceWarehouseReceiptActionHistoryRepository.save(actionHistory);
	        }
	        response.put("status", 200);
	        response.put("message", "Warehouse Receipt Generated successfully");
	    } catch (Exception e) {
	        response.put("status", 500);
	        response.put("message", "Error Generating Warehouse Receipt No.");
	    }
	    return response;
	}
 
	@Override
	public Map<String, Object> takeActionForCentralRegistration(String id,Integer id1, String data) {
	    String decodedData = CommonUtil.inputStreamDecoder(data);
	    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    
	    Map<String, Object> response = new HashMap<>();
	    try {
	        // Extracting the "remark" value from the JSON string
	        String details = "";
	        Integer userId = null;
	        String userName=null;
	        Integer roleId=null;
	        String roleName=null;
	        JsonNode jsonNode = om.readTree(decodedData);
	        if (jsonNode.has("remark")) {
	            details = jsonNode.get("remark").asText().trim(); // Extract only remark value
	        }
	        if (jsonNode.has("userId")) {
	        	userId = jsonNode.get("userId").asInt(); 
	        }
	        if (jsonNode.has("userName")) {
	        	userName = jsonNode.get("userName").asText(); 
	        }
	        if (jsonNode.has("roleId")) {
	        	roleId = jsonNode.get("roleId").asInt(); 
	        }
	        if (jsonNode.has("roleName")) {
	            roleName = jsonNode.get("roleName").asText();
	        }
 
	        IssuanceWareHouseRecipt registration = issuanceWareHouseReciptRepository.findByIdAndIntDepositorWhOperatorAndBitDeletedFlag(id,id1,false);
	        
	        if (registration != null) {
	            registration.setVchRemark(details); // Save only the extracted remark
	            registration.setOicStatus(Status.ForwaredToRegistrar);
	            issuanceWareHouseReciptRepository.save(registration);
	            
	            // Step 2: Update existing ActionHistory record
	            
	            IssuanceWarehouseReceiptActionHistory actionHistory = new IssuanceWarehouseReceiptActionHistory();
	            actionHistory.setIntIssueanceWhId(registration.getIssuanceWhId()); // or whatever field holds the PK
	            actionHistory.setDepositorId(id);
	            actionHistory.setVchActionTakenBy(userName);
	            actionHistory.setVchRemark(details);
	            actionHistory.setRoleId(roleId);
	            actionHistory.setRoleName(roleName);
	            actionHistory.setVchStatus("Forwarded To Registrar");
	            actionHistory.setUserId(userId);
	            
	            issuanceWarehouseReceiptActionHistoryRepository.save(actionHistory);
	        }
	        response.put("status", 200);
	        response.put("message", "Forwarded To Registrar successfully");
	    } catch (Exception e) {
	        response.put("status", 500);
	        response.put("message", "Error Forwarding To Registrar");
	        e.printStackTrace(); // Log the error for debugging
	    }
	    return response;
	}
 
	@Override
	public Map<String, Object> takeActionForIssueRegistration(String id,Integer id1, String data) {
	    String decodedData = CommonUtil.inputStreamDecoder(data);
	    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    
	    Map<String, Object> response = new HashMap<>();
	    try {
	        // Extracting the "remark" value from the JSON string
	        String details = "";
	        Integer userId=null;
	        String userName=null;
	        Integer roleId=null;
	        String roleName=null;
	        JsonNode jsonNode = om.readTree(decodedData);
	        if (jsonNode.has("remark")) {
	        	details = jsonNode.get("remark").asText().trim(); // Extract only remark value
	        }
	        if (jsonNode.has("userId")) {
	            userId = jsonNode.get("userId").asInt(); 
	        }
	        if (jsonNode.has("userName")) {
	        	userName = jsonNode.get("userName").asText(); 
	        }
	        if (jsonNode.has("roleId")) {
	        	roleId = jsonNode.get("roleId").asInt(); 
	        }
	        if (jsonNode.has("roleName")) {
	            roleName = jsonNode.get("roleName").asText();
	        }
 
 
	        IssuanceWareHouseRecipt issueDetails = issuanceWareHouseReciptRepository.findByIdAndIntDepositorWhOperatorAndBitDeletedFlag(id,id1,false);
	        
	        if (issueDetails != null) {
	        	issueDetails.setIssueRemark(details); // Save only the extracted remark
	        	issueDetails.setCeoStatus(Status.RegistrationCertificateIssued);
	            issuanceWareHouseReciptRepository.save(issueDetails);
	            
	            // Step 2: Update existing ActionHistory record
	            IssuanceWarehouseReceiptActionHistory actionHistory = new IssuanceWarehouseReceiptActionHistory();
	            actionHistory.setIntIssueanceWhId(issueDetails.getIssuanceWhId()); // or whatever field holds the PK
	            actionHistory.setDepositorId(id);
	            actionHistory.setVchActionTakenBy(userName);
	            actionHistory.setVchRemark(details);
	            actionHistory.setRoleId(roleId);
	            actionHistory.setRoleName(roleName);
	            actionHistory.setVchStatus("Issue Registration Certificate");
	            actionHistory.setUserId(userId);
	            
	            issuanceWarehouseReceiptActionHistoryRepository.save(actionHistory);
	        }
	        response.put("status", 200);
	        response.put("message", "Issue Registrar successfully");
	    } catch (Exception e) {
	        response.put("status", 500);
	        response.put("message", "Error Issue Registrar");
	        e.printStackTrace(); // Log the error for debugging
	    }
	    return response;
	}
 
@Override
	public List<IssuanceWarehouseReceiptActionHistory> getIssuanceWhReceiptActionHistory(Integer issuanceWhId,String depositorId) {
		List<IssuanceWarehouseReceiptActionHistory> issuanceWarehouseReceiptActionHistoryList = issuanceWarehouseReceiptActionHistoryRepository.findByIntIssueanceWhId(issuanceWhId,depositorId);
		
          return issuanceWarehouseReceiptActionHistoryList;
		
	    }
 

	@Override
	public Map<String, Object> takeActionForBlockRegistration(String id,Integer id1, String data) {
	    String decodedData = CommonUtil.inputStreamDecoder(data);
	    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    
	    Map<String, Object> response = new HashMap<>();
	    try {
	        // Extracting the "remark" value from the JSON string
	        String details = "";
	        JsonNode jsonNode = om.readTree(decodedData);
	        if (jsonNode.has("remark")) {
	        	details = jsonNode.get("remark").asText().trim(); // Extract only remark value
	        }

	        IssuanceWareHouseRecipt blockDetails = issuanceWareHouseReciptRepository.findByIdAndIntDepositorWhOperatorAndBitDeletedFlag(id,id1,false);
	        
	        if (blockDetails != null) {
	        	blockDetails.setBlockRemark(details); // Save only the extracted remark
	        	//blockDetails.setOicStatus(Status.RegistrationCertificateIssued);
	            issuanceWareHouseReciptRepository.save(blockDetails);
	        }
	        response.put("status", 200);
	        response.put("message", "Block Registrar successfully");
	    } catch (Exception e) {
	        response.put("status", 500);
	        response.put("message", "Error Block Registrar");
	        e.printStackTrace(); // Log the error for debugging
	    }
	    return response;
	}

	@Override
	public String getOperatorLicenceNo(String warehouseId) {
		JSONObject response=new JSONObject();
		try {
		String operatorLicenceNo = issuanceWareHouseReciptRepository.getOperatorLicenceNo(warehouseId);
		response.put("operatorLicenceNo", operatorLicenceNo);
		response.put("status", 200);
		}catch (Exception e) {
			response.put("status", 500);
		}
		return response.toString();
	}


	@Override
	@Transactional
	public BuyerDepositorAndWareHouseOperator save(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			BuyerDepositorAndWareHouseOperatorDto dto = om.readValue(decodedData, BuyerDepositorAndWareHouseOperatorDto.class);
			BuyerDepositorAndWareHouseOperator entity = new BuyerDepositorAndWareHouseOperator();
			JsonNode rootNode = om.readTree(decodedData);

			// Get issueId from the JSON
			Integer issueId = rootNode.get("issueId").asInt();
			Integer userId= rootNode.get("userId").asInt();
			String userName= rootNode.get("userName").asText();
			Integer roleId=rootNode.get("roleId").asInt();
			String roleName= rootNode.get("roleName").asText();
			String depositorId=rootNode.get("depositorId").asText();
			
//			if (dto.getIntDepositorWhOperator() != null && !dto.getIntDepositorWhOperator().isEmpty()) {
//		        entity = depositorRepositoryDetails.findById(dto.getIntDepositorWhOperator()).get();
//		           
//		    } else {
//		        entity = new BuyerDepositorAndWareHouseOperator(); // Creating a new one
//		    }
//			
//			if (dto.getRegisterBy() == 54) {
//	            Tuser userDataForRoleUpdate = tuserRepository.findByTxtEmailId(dto.getEmail());
//	            if (userDataForRoleUpdate != null) {
//	                userDataForRoleUpdate.setSelRole(49);
//	                userDataForRoleUpdate.setWarehouseId(dto.getWareHouseName());
//	                tuserRepository.save(userDataForRoleUpdate);
//	            }
//	            entity.setRegisterBy(49); // ✅ Move this after fetching entity
//	        } else
	        	
	        if(dto.getIntDepositorWhOperator() == null || dto.getIntDepositorWhOperator().isEmpty()){
	            entity.setRegisterBy(dto.getRegisterBy());
	            entity.setInternalUserId(2);
//	            String pwd=dto.getEmail().split("@")[0]+"@123";
//	            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//	            String encryptedPassword = passwordEncoder.encode(pwd);
	            Tuser tuser =new Tuser();
				tuser.setTxtFullName(dto.getName());
				tuser.setTxtMobileNo(dto.getMobileNumber());
				tuser.setTxtEmailId(dto.getEmail());
				tuser.setTxtPassword("6e95001cee8e57360ffd1ed3ca37035b413a3f198d71df367e3a47a3d822e4df");
			
				tuser.setSelCounty(dto.getCounty());
				tuser.setSelSubCounty(dto.getSubCounty());
				tuser.setSelWard(dto.getWard());
				tuser.setWarehouseId(dto.getWareHouseName());
				tuser.setChkPrevilege(3);
				tuser.setSelGender(1);
				tuser.setTxtUserId(dto.getEmail());
				tuser.setSelRole(49);
				tuser.setIntWhInternalUserId(2);
				tuserRepository.save(tuser);
	        }
	        
	        Optional<BuyerWareHosueDetails> buyerWareHouseDetails = buyerWareHosueDetailsRepository.findByVchWarehouseId(dto.getWareHouseName());
	        if (buyerWareHouseDetails.isPresent()) {
	        	entity.setWareHouseName(buyerWareHouseDetails.get());
	        } else {
	        	throw new EntityNotFoundException("Warehouse not found with ID: " + dto.getWareHouseName());
	        }
			
		    entity.setTypeOfUser("Depositor");
		    entity.setCompanyName(buyerWareHouseDetails.get().getVchCompanyId());		    
		    entity.setTypeOfEntity(dto.getEntityType());
		    entity.setTypeOfDepositor(dto.getDepositorType());
		    entity.setNameOfDepositor(dto.getName());
		    entity.setMobileNumber(dto.getMobileNumber());
		    entity.setEmailAddress(dto.getEmail());
		    entity.setPostalCode(dto.getPostalCode());
		    entity.setPostalAddress(dto.getPostalAddress());
		    entity.setIntCreatedBy(dto.getUserId());
		    

		    Optional<County> county = countyRepository.findById(dto.getCounty());
		    entity.setCountyId(county.get());
		    
		    Optional<SubCounty> subCounty = subCountyRepository.findById(dto.getSubCounty());
		    entity.setSubCountyId(subCounty.get());
		    
		    Optional<WardMaster> ward = wardMasterRepo.findById(dto.getWard());
		    entity.setIntWard(ward.get());
		    
		    Optional<Country> country = countryRepository.findById(dto.getNationality());
		    entity.setIntNationaity(country.get());
		    
		    entity.setNationalId(dto.getNationalId());
		    entity.setEntityRegistrationNumber(dto.getEntityRegistrationNumber());
		    entity.setPassportNumber(dto.getPassportNumber());
		    entity.setAlienId(dto.getAlienId());
		    entity.setGovtIssuedId(dto.getGovtIssuedId());
		    entity.setVchIdNo(dto.getVchIdNo());		  
		    // entity.setRegisterBy(dto.getRegisterBy());
		    entity.setApplicationId(generateApplicationID());
		    
		    // Save entity to database
		     BuyerDepositorAndWareHouseOperator savedDepositor = depositorRepositoryDetails.saveAndFlush(entity);
		     
		     
		     if (savedDepositor != null) {
		    	    String intDepositorWhOperator = savedDepositor.getIntDepositorWhOperator();

		    	    Optional<IssuanceWareHouseRecipt> optionalIWR = issuanceWareHouseReciptRepository.findById(issueId);
		    	    if (optionalIWR.isPresent()) {
		    	        IssuanceWareHouseRecipt iWR = optionalIWR.get();

		    	        BuyerDepositorAndWareHouseOperator bd = buyerDepositorAndWareHouseOperatorRepository
		    	                .findById(intDepositorWhOperator)
		    	                .orElseThrow(() -> new RuntimeException("Depositor not found: " + intDepositorWhOperator));

		    	        iWR.setIntDepositorWhOperator(bd);
		    	        iWR.setIsTransfer(true);
		    	        iWR.setVchRemark(null);
		    	        iWR.setOicStatus(Status.PendingatCentralRegistry);
		    	        iWR.setIssueRemark(null);
		    	        iWR.setBlockRemark(null);
		    	        iWR.setCeoStatus(Status.Pendingforregistration);
		    	        iWR.setPaymentStatus(Status.Pending);
		    	        iWR.setVchWarehouseId(iWR.getVchWarehouseId());
		    	        iWR.setIsRetired(false);
		    	        iWR.setDoPaymentLater(false);
		    	        issuanceWareHouseReciptRepository.save(iWR);
		    	        
		    	        //for action history
		    	        IssuanceWarehouseReceiptActionHistory actionHistory = new IssuanceWarehouseReceiptActionHistory();
			            actionHistory.setIntIssueanceWhId(issueId);
			            actionHistory.setDepositorId(depositorId);
			            actionHistory.setVchActionTakenBy(userName);
			            actionHistory.setRoleId(roleId);
			            actionHistory.setRoleName(roleName);
			            actionHistory.setVchStatus("Trasfer Receipt");
			            actionHistory.setUserId(userId);
			            issuanceWarehouseReceiptActionHistoryRepository.save(actionHistory);
			            
			            //entry the updated details of new depositor
			            List<IssuanceWarehouseReceiptActionHistory> actionHistoryDetails =
			            	    issuanceWarehouseReceiptActionHistoryRepository.findByIntIssueanceWhId(issueId, depositorId);

			            	// Proceed only if at least two records are available
			            	if (actionHistoryDetails.size() >= 2) {
			            	    IssuanceWarehouseReceiptActionHistory firstHistory = actionHistoryDetails.get(0);
			            	    IssuanceWarehouseReceiptActionHistory secondHistory = actionHistoryDetails.get(1);

			            	    // Create first new entry
			            	    IssuanceWarehouseReceiptActionHistory actionHistoryOne = new IssuanceWarehouseReceiptActionHistory();
					            actionHistoryOne.setIntIssueanceWhId(firstHistory.getIntIssueanceWhId());
					            actionHistoryOne.setDepositorId(iWR.getIntDepositorWhOperator().getIntDepositorWhOperator());
					            actionHistoryOne.setVchActionTakenBy(firstHistory.getVchActionTakenBy());
					            actionHistoryOne.setDtmActionTaken(firstHistory.getDtmActionTaken());
					            actionHistoryOne.setRoleId(firstHistory.getRoleId());
					            actionHistoryOne.setRoleName(firstHistory.getRoleName());
					            actionHistoryOne.setVchStatus(firstHistory.getVchStatus());
					            actionHistoryOne.setUserId(firstHistory.getUserId());

			            	    // Save first new record
			            	    issuanceWarehouseReceiptActionHistoryRepository.save(actionHistoryOne);

			            	    // Create second new entry
			            	    IssuanceWarehouseReceiptActionHistory actionHistoryTwo = new IssuanceWarehouseReceiptActionHistory();
			            	    actionHistoryTwo.setIntIssueanceWhId(secondHistory.getIntIssueanceWhId());
			            	    actionHistoryTwo.setDepositorId(iWR.getIntDepositorWhOperator().getIntDepositorWhOperator());
			            	    actionHistoryTwo.setVchActionTakenBy(secondHistory.getVchActionTakenBy());
			            	    actionHistoryOne.setDtmActionTaken(firstHistory.getDtmActionTaken());
			            	    actionHistoryTwo.setRoleId(secondHistory.getRoleId());
			            	    actionHistoryTwo.setRoleName(secondHistory.getRoleName());
			            	    actionHistoryTwo.setVchStatus(secondHistory.getVchStatus());
			            	    actionHistoryTwo.setUserId(secondHistory.getUserId()); 

			            	    // Save second new record
			            	    issuanceWarehouseReceiptActionHistoryRepository.save(actionHistoryTwo);
			            	}

			            
			            
		    	    } else {
		    	        throw new RuntimeException("IssuanceWareHouseRecipt not found with ID 2");
		    	    }
		    	}

		     return savedDepositor;
		} catch (Exception e) {
			log.error("DepositorServiceImpl : save()",e);
		}
		return null;
}

	// Start from 1273
    public static String generateApplicationID() { 
    	int nextNumber = counter.incrementAndGet();
    	DecimalFormat df = new DecimalFormat("0000"); 
    	return PREFIXAPPLCNTID + df.format(nextNumber) + SUFFIX;
    	}


	@Override
	public Page<IssuanceWareHouseRecipt> viewTransferIssuance(Pageable pageable) {
		return issuanceWareHouseReciptRepository.viewTransferIssuance(pageable);
	}


	@Override
	public Page<IssuanceWareHouseRecipt> viewTransferIssuanceOIC(Pageable pageable) {
		return issuanceWareHouseReciptRepository.viewTransferIssuanceOIC(pageable);
	}


	@Override
	public Page<IssuanceWareHouseRecipt> viewTransferIssuanceCEO(Pageable pageable) {
		return issuanceWareHouseReciptRepository.viewTransferIssuanceCEO(pageable);
	}
	

}



	

