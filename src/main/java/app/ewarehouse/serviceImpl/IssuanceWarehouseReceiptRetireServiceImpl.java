package app.ewarehouse.serviceImpl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.entity.BuyerDepositorAndWareHouseOperator;
import app.ewarehouse.entity.IssuanceWareHouseRecipt;
import app.ewarehouse.entity.IssuanceWarehouseReceiptRetire;
import app.ewarehouse.entity.Seasonality;
import app.ewarehouse.repository.BuyerDepositorAndWareHouseOperatorRepository;
import app.ewarehouse.repository.IssuanceWareHouseReciptRepository;
import app.ewarehouse.repository.IssuanceWarehouseReceiptRetireRepository;
import app.ewarehouse.repository.SeasonalityRepository;
import app.ewarehouse.service.IssuanceWarehouseReceiptRetireService;
import app.ewarehouse.util.CommonUtil;

@Service
public class IssuanceWarehouseReceiptRetireServiceImpl implements IssuanceWarehouseReceiptRetireService{

	@Autowired
    private IssuanceWarehouseReceiptRetireRepository retireRepository;
	
	@Autowired
 	private ObjectMapper om;
	
	@Autowired
	private IssuanceWareHouseReciptRepository issuanceWareHouseReciptRepository;
	
	@Autowired
	private BuyerDepositorAndWareHouseOperatorRepository buyerDepositorAndWareHouseOperatorRepository;
	
	@Autowired
	private SeasonalityRepository seasonalityRepository;
	

    @Override
    public Page<IssuanceWarehouseReceiptRetire> getAllRetiredReceipts(Pageable pageable) {
        return retireRepository.findAllRetiredData(pageable);
    }

	@Override
	@Transactional
	public IssuanceWarehouseReceiptRetire saveRetirementData(String data){
		String decodedData = CommonUtil.inputStreamDecoder(data);
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
		//JsonNode rootNode = om.readTree(decodedData);
		int issueId = Integer.parseInt(decodedData);
		
		if (issueId>0) {

    	    Optional<IssuanceWareHouseRecipt> optionalIWR = issuanceWareHouseReciptRepository.findById(issueId);
    	    if (optionalIWR.isPresent()) {
    	        IssuanceWareHouseRecipt iWR = optionalIWR.get();
    	        //System.err.println(iWR);
    	        IssuanceWarehouseReceiptRetire iWRRetire=new IssuanceWarehouseReceiptRetire();
    	        
    	        //BeanUtils.copyProperties(iWR, iWRRetire);
    	        //iWRRetire.setIntIssuanceWhId(issueId);
    	        iWRRetire.setIssuanceWareHouseRecipt(iWR);
    	     // ✅ Fetch Depositor/Warehouse Operator
    		    BuyerDepositorAndWareHouseOperator bd = buyerDepositorAndWareHouseOperatorRepository
    		            .findById(iWR.getIntDepositorWhOperator().getIntDepositorWhOperator())
    		            .orElseThrow(() -> new RuntimeException("Depositor not found: " + iWR.getIntDepositorWhOperator().getIntDepositorWhOperator()));
    		    iWRRetire.setIntDepositorWhOperator(bd);
    		    iWRRetire.setCommodityType(iWR.getCommodityType());
    		    iWRRetire.setCommodityOrigin(iWR.getCommodityOrigin());
    		    iWRRetire.setCommodityCode(iWR.getCommodityCode());
    		    iWRRetire.setOriginalQuantity(iWR.getOriginalQuantity());
    		    iWRRetire.setOriginalGrossWeight(iWR.getOriginalGrossWeight());
    		    iWRRetire.setOriginalNetWeight(iWR.getOriginalNetWeight());
    		    iWRRetire.setCurrentQuantity(iWR.getCurrentQuantity());
    		    iWRRetire.setCurrentNetWeight(iWR.getCurrentNetWeight());
    		    Seasonality seasonality = seasonalityRepository
    		            .findById(iWR.getCropYear().getId())
    		            .orElseThrow(() -> new RuntimeException("Crop year not found: " + iWR.getCropYear().getId()));
    	        iWRRetire.setCropYear(seasonality);
    	        
    	        iWRRetire.setGrade(iWR.getGrade());
    	        iWRRetire.setLotNumber(iWR.getLotNumber());
    	        iWRRetire.setQualityInspectionPath(iWR.getQualityInspectionPath());
    	        iWRRetire.setWeighingTicketPath(iWR.getWeighingTicketPath());
    	        iWRRetire.setGrnPath(iWR.getGrnPath());
    	        iWRRetire.setIntCreatedBy(iWR.getIntCreatedBy());
    	        iWRRetire.setSelectDate(iWR.getSelectDate());
    	        iWRRetire.setReciptStatus(iWR.getReciptStatus());
    	        iWRRetire.setWareHouseReciptNo(iWR.getWareHouseReciptNo());
    	        iWRRetire.setVchRemark(iWR.getVchRemark());
    	        iWRRetire.setOicStatus(iWR.getOicStatus());
    	        iWRRetire.setIssueRemark(iWR.getIssueRemark());
    	        iWRRetire.setBlockRemark(iWR.getBlockRemark());
    	        iWRRetire.setCeoStatus(iWR.getCeoStatus());
    	        iWRRetire.setPaymentStatus(iWR.getPaymentStatus());
    	        iWRRetire.setVchWarehouseId(iWR.getVchWarehouseId());
    	        iWRRetire.setIsRetired(true);
    	        iWRRetire.setDoPaymentLater(iWR.getDoPaymentLater());
    		    
    	        IssuanceWarehouseReceiptRetire savedRetire = retireRepository.save(iWRRetire);
    	        return savedRetire;
    	    } else {
    	        throw new RuntimeException("IssuanceWareHouseRecipt not found with ID 2");
    	    }
    	}
		
		}catch(Exception e) {
			
		}
		return null;
	}

	@Override
	public Page<IssuanceWarehouseReceiptRetire> getAllRetiredReceiptsForDepositor(Pageable pageable,
			String depositorId) {
		return retireRepository.findAllRetiredDataForDepositor(pageable,depositorId);
	}
	

}
