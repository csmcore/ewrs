/**
 * 
 */
package app.ewarehouse.serviceImpl;


import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.BuyerDepositorAndWareHouseOperatorDto;
import app.ewarehouse.dto.BuyerDepositorResponse;
import app.ewarehouse.dto.DepositorResponse;
import app.ewarehouse.dto.DepositoryDetailsDto;
import app.ewarehouse.entity.BuyerDepositorAndWareHouseOperator;
import app.ewarehouse.entity.BuyerWareHosueDetails;
import app.ewarehouse.entity.Country;
import app.ewarehouse.entity.County;
import app.ewarehouse.entity.Depositor;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.SubCounty;
import app.ewarehouse.entity.TempUser;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.master.entity.WardMaster;
import app.ewarehouse.master.repository.WardMasterRepo;
import app.ewarehouse.repository.ApplicationConformityLocDetRepository;
import app.ewarehouse.repository.BuyerDepositorAndWareHouseOperatorRepository;
import app.ewarehouse.repository.BuyerWareHosueDetailsRepository;
import app.ewarehouse.repository.CountryRepository;
import app.ewarehouse.repository.CountyRepository;
import app.ewarehouse.repository.DepositorRepository;
import app.ewarehouse.repository.SubCountyRepository;
import app.ewarehouse.repository.TempUserRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.DepositorService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Priyanka Singh
 */
@Slf4j
@Service
public class DepositorServiceImpl implements DepositorService {

	@Autowired
    private DepositorRepository depositorRepository;
	
	@Autowired
	private BuyerDepositorAndWareHouseOperatorRepository depositorRepositoryDetails;
	
    @Autowired
    private Validator validator;
    
    @Autowired
    private TempUserRepository tempUserRepo;
    
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
    private ApplicationConformityLocDetRepository applicationConformityLocDetRepository;
    
    @Autowired
    private BuyerWareHosueDetailsRepository buyerWareHosueDetailsRepository;
    
    
    @Autowired
    private ObjectMapper om;
    private static final Logger logger = LoggerFactory.getLogger(MroleServiceImpl.class);
    
	private static final String PREFIX = "WRS";
	private static final String SUFFIX = "S1";
	private static final AtomicInteger counter = new AtomicInteger(1272);
	Integer parentId = 0;
	@Override
	@Transactional
	public BuyerDepositorAndWareHouseOperator save(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			BuyerDepositorAndWareHouseOperatorDto dto = om.readValue(decodedData, BuyerDepositorAndWareHouseOperatorDto.class);
			BuyerDepositorAndWareHouseOperator entity = new BuyerDepositorAndWareHouseOperator();
		   
			
			if (dto.getIntDepositorWhOperator() != null && !dto.getIntDepositorWhOperator().isEmpty()) {
		        entity = depositorRepositoryDetails.findById(dto.getIntDepositorWhOperator()).get();
		           
		    } else {
		        entity = new BuyerDepositorAndWareHouseOperator(); // Creating a new one
		    }
			
			if (dto.getRegisterBy() == 54) {
	            Tuser userDataForRoleUpdate = tuserRepository.findByTxtEmailId(dto.getEmail());
	            if (userDataForRoleUpdate != null) {
	                userDataForRoleUpdate.setSelRole(49);
	                userDataForRoleUpdate.setWarehouseId(dto.getWareHouseName());
	                tuserRepository.save(userDataForRoleUpdate);
	            }
	            entity.setRegisterBy(49); // ✅ Move this after fetching entity
	        } else if(dto.getIntDepositorWhOperator() == null || dto.getIntDepositorWhOperator().isEmpty()){
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
			
		    entity.setTypeOfUser("Depositor");
		    entity.setCompanyName(dto.getCompanyName());
		   // entity.setWareHouseName(dto.getWareHouseName());
		    
		    entity.setTypeOfEntity(dto.getEntityType());
		    entity.setTypeOfDepositor(dto.getDepositorType());
		    entity.setNameOfDepositor(dto.getName());
		    entity.setMobileNumber(dto.getMobileNumber());
		    entity.setEmailAddress(dto.getEmail());
		    entity.setPostalCode(dto.getPostalCode());
		    entity.setPostalAddress(dto.getPostalAddress());
		    entity.setIntCreatedBy(dto.getUserId());
		    
		    Optional<BuyerWareHosueDetails> buyerWareHouseDetails = buyerWareHosueDetailsRepository.findByVchWarehouseId(dto.getWareHouseName());
		    if (buyerWareHouseDetails.isPresent()) {
		        entity.setWareHouseName(buyerWareHouseDetails.get());
		    } else {
		        throw new EntityNotFoundException("Warehouse not found with ID: " + dto.getWareHouseName());
		    }

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
		    entity.setApplicationId(generateNextID());
		    
		    // Save entity to database
		    return depositorRepositoryDetails.saveAndFlush(entity);
		} catch (Exception e) {
			log.error("DepositorServiceImpl : save()",e);
		}
		return null;
}
	
	// Start from 1273
    public static String generateNextID() { 
    	int nextNumber = counter.incrementAndGet();
    	DecimalFormat df = new DecimalFormat("0000"); 
    	return PREFIX + df.format(nextNumber) + SUFFIX;
    	}

	@Override
	public DepositorResponse getById(String id) {
		 logger.info("Inside getById method of BuyerServiceImpl");
	        Depositor depositor = depositorRepository.findByIntIdAndBitDeletedFlag(id, false);
	        return Mapper.mapToDepositorResponse(depositor);
	}

//	@Override
//	public List<Depositor> getAll() {
//		 logger.info("Inside getAll method of BuyerServiceImpl");
//		 return depositorRepository.findAll();
//	}

	@Override
	public String deleteById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @Override public Page<BuyerResponse> getFilteredBuyers(Date fromDate, Date
	 * toDate, Status status, Pageable pageable) { // TODO Auto-generated method
	 * stub return null; }
	 */
	@Override
	public Object takeAction(String buyer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Depositor> getAll() {
		return depositorRepository.findAll();
		

	}

	@Override
	public Page<DepositorResponse> getFilteredDepositors(Date fromDate, Date toDate, Status status, Pageable pageable) {
		 logger.info("Inside getFilteredDepositors method of DepositorServiceImpl");
	        Page<Depositor> depositorPage = depositorRepository.findByFilters(fromDate, toDate, status, pageable);
	        logger.info("from: " + fromDate + " to: " + toDate);
	        List<DepositorResponse> depositorResponses = depositorPage.getContent().stream()
	                .map(Mapper::mapToDepositorResponse)
	                .collect(Collectors.toList());

	        return new PageImpl<>(depositorResponses, pageable, depositorPage.getTotalElements());
	}

	@Override
	public Page<BuyerDepositorResponse> getFilteredBuyers(Date fromDate, Date toDate, Status status,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DepositoryDetailsDto getUserDataByEmailIdByDepositor(String email) {
		TempUser tempEntity = tempUserRepo.findByEmail(email);
		if (tempEntity != null) {
			DepositoryDetailsDto dto = new DepositoryDetailsDto();
			
			  // Fetch Company Name & Warehouse Name using the custom query
			List<Object[]> results = tempUserRepo.findCompanyAndWarehouseByEmail(tempEntity.getWarehouseId());

	        String companyName = "";
	        String warehouseName = "";

	        if (results != null && !results.isEmpty()) {
				System.out.println(results.get(0));
            Object[] result = results.get(0); // Get the first record
            logger.info("First record: " + Arrays.toString(result));
	            companyName = (result[0] != null) ? result[0].toString() : "";
	            warehouseName = (result[1] != null) ? result[1].toString() : "";
	        }

           // Set User 
			if(tempEntity.getUserType() == 1) {
				dto.setTypeOfUser("Warehouse Operator");
			}
			if(tempEntity.getUserType() ==2) {
				dto.setTypeOfUser("Collateral Manager");
			}
			if(tempEntity.getUserType() == 3) {
				dto.setTypeOfUser("Depositor");
			}
		
			dto.setCompanyId(tempEntity.getCompanyId());
			dto.setWareHouseId(tempEntity.getWarehouseId());
			
			 dto.setCompanyName(companyName);
	         dto.setWareHouseName(warehouseName);
	            
			if(tempEntity.getIntDepositorId() == 1) {
				dto.setTypeOfDepositor("Entity");
			}
			if(tempEntity.getIntDepositorId() == 2) {
				dto.setTypeOfDepositor("Individual");
			}
			if(tempEntity.getIntDepositorId() == 1) {
				dto.setTypeOfEntity("Cooperative");
			}
			
			if(tempEntity.getIntDepositorId() == 2) {
				dto.setTypeOfEntity("Partnership");
			}
			
			dto.setNameOfDepositor(tempEntity.getApplicantName());
			dto.setMobileNumber(tempEntity.getMobile());
			dto.setEmailAddress(tempEntity.getEmail());
			dto.setPostalAddress(tempEntity.getPostalAddress());
			dto.setPostalCode(tempEntity.getPostalCode());
			logger.info("Final Warehouse Name ",dto.getWareHouseName());
			return dto;
		}
		else {
			BuyerDepositorAndWareHouseOperator bdo=depositorRepositoryDetails.findByEmailAddressAndInternalUserId(email,2);
			if(bdo!=null) {
				DepositoryDetailsDto dto = new DepositoryDetailsDto();
				  // Fetch Company Name & Warehouse Name using the custom query
				List<Object[]> results = tempUserRepo.findCompanyAndWarehouseByEmail(bdo.getWareHouseName().getVchWarehouseId());
				System.err.println(results);
 
		        String companyName = "";
		        String warehouseName = "";
 
		        if (results != null && !results.isEmpty()) {
					System.out.println(results.get(0));
	            Object[] result = results.get(0); // Get the first record
	            logger.info("First record: " + Arrays.toString(result));
		            companyName = (result[0] != null) ? result[0].toString() : "";
		            warehouseName = (result[1] != null) ? result[1].toString() : "";
		        }
 
	           // Set User 
				if(bdo.getTypeOfUser() !=null) {
					dto.setTypeOfUser(bdo.getTypeOfUser());
				}
				else{
					dto.setTypeOfUser("Depositor");
				}
				dto.setTypeOfDepositor(bdo.getTypeOfDepositor());

				dto.setCompanyId(bdo.getCompanyName());
				dto.setWareHouseId(bdo.getWareHouseName().getVchWarehouseId());
				 dto.setCompanyName(companyName);
		         dto.setWareHouseName(warehouseName);
		        dto.setTypeOfEntity(bdo.getTypeOfEntity());
				dto.setNameOfDepositor(bdo.getNameOfDepositor());
				dto.setMobileNumber(bdo.getMobileNumber());
				dto.setEmailAddress(bdo.getEmailAddress());
				dto.setPostalAddress(bdo.getPostalAddress());
				dto.setPostalCode(bdo.getPostalCode());
				dto.setDepositorId(bdo.getIntDepositorWhOperator());
				dto.setCountyName(bdo.getApplicationOfConformityLocationDetails().getCounty().getName());
				dto.setSubCountyName(bdo.getApplicationOfConformityLocationDetails().getSubCounty().getTxtSubCountyName());
				dto.setWard(bdo.getApplicationOfConformityLocationDetails().getWard().getWardName());
				dto.setStatus(bdo.getDepositorStatus());
				dto.setNationality(bdo.getIntNationaity().getCountryName());
				dto.setAlienId(bdo.getAlienId());
				dto.setNationalId(bdo.getNationalId());
				dto.setPassportNumber(bdo.getPassportNumber());
				dto.setEntityRegistrationNumber(bdo.getEntityRegistrationNumber());
				
				logger.info("Final Warehouse Name ",dto.getWareHouseName());
				return dto;
		}
		}
		return null;
	}

	@Override
    public Page<BuyerDepositorAndWareHouseOperator> findByIntCreatedBy(Integer id, Pageable pageable) {
        return depositorRepositoryDetails.findByIntCreatedBy(id, pageable);
    }

	@Override
	public BuyerDepositorAndWareHouseOperator getDepositorById(String intDepositorWhOperator) {
	 Optional<BuyerDepositorAndWareHouseOperator> configData= depositorRepositoryDetails.findById(intDepositorWhOperator);
		 return configData.orElse(null);
	}

	@Override
	public DepositoryDetailsDto getWareHouseOperatorDataByUserId(String wareId) {
		DepositoryDetailsDto dto = new DepositoryDetailsDto();
		
		// Fetch Company Name & Warehouse Name using the custom query
		List<Object[]> results = depositorRepositoryDetails.findCompanyAndWarehouseById(wareId);
		// Default values in case no result is found
		String companyName = "";
		String companyId = "";
		String warehouseName = "";
		String warehouseId = "";

	// Check if results exist and extract first row
	if (results != null && !results.isEmpty()) {
		    Object[] result = results.get(0); // Get the first record
		    companyName = (result[0] != null) ? result[0].toString() : "";
	    warehouseName = (result[1] != null) ? result[1].toString() : "";
		    warehouseId = (result[2] != null) ? result[2].toString() : "";
		    companyId = (result[3] != null) ? result[3].toString() : "";
		}

		// Set values in DTO
		dto.setCompanyName(companyName);
		dto.setCompanyId(companyId);
		dto.setWareHouseName(warehouseName);
		dto.setWareHouseId(warehouseId);
			         
			return dto;
}

	@Override
	public Integer getDepositorCount(Integer createdBy) {
		return depositorRepositoryDetails.getDepositorCount(createdBy);
	}

	@Override
	public Page<BuyerDepositorAndWareHouseOperator> getAllWareHouseList(Pageable pageable) {
		return depositorRepositoryDetails.findBybitDeletedFlag(pageable,false);
	}

	@Override
	public Page<BuyerDepositorAndWareHouseOperator> getAllWareHouseListById(String vchWareHouseId, Pageable pageable) {
		return depositorRepositoryDetails.findByWareHouseName_VchWarehouseId(vchWareHouseId,pageable);
	}


	
	

}
