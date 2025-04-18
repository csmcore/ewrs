package app.ewarehouse.serviceImpl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.entity.LossReceipt;
import app.ewarehouse.entity.LossReceiptFiles;
import app.ewarehouse.entity.RetireReceipt;
import app.ewarehouse.entity.TwarehouseReceipt;
import app.ewarehouse.entity.enmReceiptStatus;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.LossReceiptFilesRepository;
import app.ewarehouse.repository.LossReceiptRepository;
import app.ewarehouse.repository.RetireReceiptRepository;
import app.ewarehouse.repository.TwarehouseReceiptRepository;
import app.ewarehouse.service.RetireReceiptService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import app.ewarehouse.util.JsonFileExtractorUtil;

@Service
public class RetireReceiptServiceImpl implements RetireReceiptService {

	 @Autowired
	    private RetireReceiptRepository retireReceiptRepository;

	    @Autowired
	    private TwarehouseReceiptRepository twarehouseReceiptRepository;
	    
	    @Autowired
	    private LossReceiptRepository lossReceiptRepository;
	    
	    @Autowired
	    private LossReceiptFilesRepository lossReceiptFilesRepository;
	    
	    @Autowired
	    private TwarehouseReceiptRepository receiptRepo;
	    
	    @Autowired
		private ObjectMapper objectMapper;
	    
	    @Autowired
	    ErrorMessages errorMessages;
	    
	    private RetireReceipt retireReceipt;
	    
	    private LossReceipt lossReceipt;

	    @Transactional
	    @Override
	    public String retireWarehouseReceipt(String retireReceiptData) throws JsonProcessingException {
	    	String decodedData = CommonUtil.inputStreamDecoder(retireReceiptData);
	    	try {
	    		retireReceipt = objectMapper.readValue(decodedData, RetireReceipt.class);
			} catch (Exception e) {
				throw new CustomGeneralException("Invalid data format: " + e);
			}
	    	retireReceipt.setTxtPassportPath(JsonFileExtractorUtil.uploadFile(retireReceipt.getTxtPassportPath(),
	    	        FolderAndDirectoryConstant.RETIRE_RECEIPT_FOLDER));
	        TwarehouseReceipt warehouseReceipt = twarehouseReceiptRepository
	            .findById(retireReceipt.getWarehouseReceipt().getTxtWarehouseReceiptId())
	            .orElseThrow(() -> new CustomGeneralException("Warehouse receipt not found"));

	        if (warehouseReceipt.getReceiptStatus() != enmReceiptStatus.Pending) {
	            throw new CustomGeneralException(errorMessages.getOnlyPendingRecipts());
	        }

	        warehouseReceipt.setReceiptStatus(enmReceiptStatus.valueOf(retireReceipt.getStatus()));

	        retireReceipt.setWarehouseReceipt(warehouseReceipt); 
	        RetireReceipt savedRetireReceipt = retireReceiptRepository.save(retireReceipt);
	        
	        return CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(savedRetireReceipt.getWarehouseReceipt().getTxtWarehouseReceiptId())).toString();
	    }

	    @Transactional
		@Override
		public String replaceLostReceipt(String lossReceiptData) throws JsonProcessingException {
	    	String decodedData = CommonUtil.inputStreamDecoder(lossReceiptData);
	    	try {
	    		lossReceipt = objectMapper.readValue(decodedData, LossReceipt.class);
			} catch (Exception e) {
				throw new CustomGeneralException("Invalid data format: " + e);
			}
	    	
	    	String path1 = JsonFileExtractorUtil.uploadFile(lossReceipt.getPoliceLossReportDoc(),
	    	        FolderAndDirectoryConstant.LOSS_RECEIPT);
	    	String path2 = JsonFileExtractorUtil.uploadFile(lossReceipt.getLossReceiptCopyDoc(),
	    	        FolderAndDirectoryConstant.LOSS_RECEIPT);

	        LossReceipt savedLossReceipt = lossReceiptRepository.save(lossReceipt);
	    	LossReceiptFiles receiptFile1 = new LossReceiptFiles();
	    	receiptFile1.setFilePath(path1);
	    	receiptFile1.setLossReceiptId(savedLossReceipt.getId());
	    	LossReceiptFiles receiptFile2 = new LossReceiptFiles();
	    	receiptFile2.setFilePath(path2);
	    	receiptFile2.setLossReceiptId(savedLossReceipt.getId());
	    	lossReceiptFilesRepository.saveAll(List.of(receiptFile1,receiptFile2));
	        return CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(savedLossReceipt)).toString();
	    }
	    
	    @Override
		public String validateReceipt(String receipt) throws JsonProcessingException {
			boolean status = false;
			if(receipt!=null) {
				status = receiptRepo.existsById(receipt);
			}
			return CommonUtil.inputStreamEncoder(Boolean.toString(status)).toString();
		}
}
