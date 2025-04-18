package app.ewarehouse.serviceImpl;


import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ReceiptDealFinalDto;
import app.ewarehouse.dto.SalesApplicationListResponse;
import app.ewarehouse.dto.SalesApplicationMainResponse;
import app.ewarehouse.dto.SalesApplicationTabOneDTO;
import app.ewarehouse.entity.ApplicationStatus;
import app.ewarehouse.entity.BuyerDepositor;
import app.ewarehouse.entity.SalesApplicationMain;
import app.ewarehouse.entity.SalesApplicationTabOne;
import app.ewarehouse.entity.SalesApplicationTabThree;
import app.ewarehouse.entity.SalesApplicationTabTwo;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TreceiveCommodity;
import app.ewarehouse.entity.TwarehouseReceipt;
import app.ewarehouse.entity.enmReceiptStatus;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.BuyerDepositorRepository;
import app.ewarehouse.repository.SalesApplicationMainRepository;
import app.ewarehouse.repository.SalesApplicationTabOneRepository;
import app.ewarehouse.repository.SalesApplicationTabThreeRepository;
import app.ewarehouse.repository.SalesApplicationTabTwoRepository;
import app.ewarehouse.repository.TreceiveCommodityRepository;
import app.ewarehouse.repository.TwarehouseReceiptRepository;
import app.ewarehouse.service.SalesApplicationMainService;
import app.ewarehouse.util.DocumentUploadutil;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import app.ewarehouse.util.Mapper;


@Service
public class SalesApplicationMainServiceImpl implements SalesApplicationMainService {

    @Autowired
    private SalesApplicationMainRepository salesApplicationRepository;
    @Autowired
    private SalesApplicationTabOneRepository salesApplicationTabOneRepository;
    @Autowired
    private SalesApplicationTabTwoRepository salesApplicationTabTwoRepository;
    @Autowired
    private SalesApplicationTabThreeRepository salesApplicationTabThreeRepository;
    @Autowired
    private TwarehouseReceiptRepository twarehouseReceiptRepository;
    @Autowired
    private TreceiveCommodityRepository treceiveCommodityRepository;
    @Autowired
    private BuyerDepositorRepository buyerDepositorRepository;
    @Autowired
    private ObjectMapper om;

    private static final Logger logger = LoggerFactory.getLogger(SalesApplicationMainServiceImpl.class);

    @Override
    public SalesApplicationTabTwo saveTwo(String salesApplication) {
        logger.info("inside save Two method");
        try {
            //String decodedData = CommonUtil.inputStreamDecoder(salesApplication);
            SalesApplicationTabTwo salesApplicationTabTwo = om.readValue(salesApplication, SalesApplicationTabTwo.class);
            logger.info("salesApplicationTabTwo decoded is:{}", salesApplicationTabTwo);

            Optional<SalesApplicationTabTwo> existingSalesApplicationTabTwo = salesApplicationTabTwoRepository.findByApplicationId(salesApplicationTabTwo.getApplicationId());
            SalesApplicationTabTwo savedTabTwo= new SalesApplicationTabTwo();

            if (existingSalesApplicationTabTwo.isPresent()) {
                SalesApplicationTabTwo existingRecord = existingSalesApplicationTabTwo.get();
                existingRecord.setStmUpdatedAt(new Date());
                existingRecord.setPriceOfReceipt(salesApplicationTabTwo.getPriceOfReceipt());
                existingRecord.setOtherSaleReason(salesApplicationTabTwo.getOtherSaleReason());
                existingRecord.setReasonForSale(salesApplicationTabTwo.getReasonForSale());

                boolean isProofOfOwnershipChanged = !existingRecord.getUploadProofOfOwnership().equals(salesApplicationTabTwo.getUploadProofOfOwnership());

                if (isProofOfOwnershipChanged) {
                    byte[] decodedProof = Base64.getDecoder().decode(salesApplicationTabTwo.getUploadProofOfOwnership().getBytes());
                    String uniqueFileNameProof = "Proof_of_Ownership_" + UUID.randomUUID().toString();
                    String proofFileUrl = DocumentUploadutil.uploadFileByte(uniqueFileNameProof, decodedProof, FolderAndDirectoryConstant.WAREHOUSE_RECEIPT_FOLDER);

                    if (proofFileUrl.startsWith("Document")) {
                        throw new RuntimeException(proofFileUrl);
                    }

                    String proofFilePath = proofFileUrl.substring(proofFileUrl.indexOf(FolderAndDirectoryConstant.WAREHOUSE_RECEIPT_FOLDER));
                    existingRecord.setUploadProofOfOwnership(proofFilePath);
                }
                else {
                    existingRecord.setUploadProofOfOwnership(salesApplicationTabTwo.getUploadProofOfOwnership());
                }

                boolean isWarehouseReceiptChanged = !existingRecord.getUploadWarehouseReceipt().equals(salesApplicationTabTwo.getUploadWarehouseReceipt());
                if (isWarehouseReceiptChanged) {
                    byte[] decodedReceipt = Base64.getDecoder().decode(salesApplicationTabTwo.getUploadWarehouseReceipt().getBytes());
                    String uniqueFileNameReceipt = "Warehouse_Receipt_" + UUID.randomUUID().toString();
                    String receiptFileUrl = DocumentUploadutil.uploadFileByte(uniqueFileNameReceipt, decodedReceipt, FolderAndDirectoryConstant.WAREHOUSE_RECEIPT_FOLDER);

                    if (receiptFileUrl.startsWith("Document")) {
                        throw new RuntimeException(receiptFileUrl);
                    }
                    String receiptFilePath = receiptFileUrl.substring(receiptFileUrl.indexOf(FolderAndDirectoryConstant.WAREHOUSE_RECEIPT_FOLDER));
                    existingRecord.setUploadWarehouseReceipt(receiptFilePath);
                }else {
                    existingRecord.setUploadWarehouseReceipt(salesApplicationTabTwo.getUploadWarehouseReceipt());
                }

                logger.info("exisitng record bring saved is:{}",existingRecord);
                salesApplicationTabTwoRepository.save(existingRecord);

            } else {
                byte[] decode = Base64.getDecoder().decode(salesApplicationTabTwo.getUploadWarehouseReceipt().getBytes());
                String uniqueFileName = "Warehouse_receipt_" + UUID.randomUUID().toString();
                String file_url = DocumentUploadutil.uploadFileByte(uniqueFileName, decode,FolderAndDirectoryConstant.WAREHOUSE_RECEIPT_FOLDER);
                if (file_url.startsWith("Document")) {
                    throw new RuntimeException(file_url);
                }
                String filePath = file_url.substring(file_url.indexOf(FolderAndDirectoryConstant.WAREHOUSE_RECEIPT_FOLDER));
                salesApplicationTabTwo.setUploadWarehouseReceipt(filePath);


                byte[] decode2 = Base64.getDecoder().decode(salesApplicationTabTwo.getUploadProofOfOwnership().getBytes());
                String uniqueFileName2 = "Proof_of_Ownership_" + UUID.randomUUID().toString();
                String file_url2 = DocumentUploadutil.uploadFileByte(uniqueFileName2, decode2,FolderAndDirectoryConstant.WAREHOUSE_RECEIPT_FOLDER);
                if (file_url2.startsWith("Document")) {
                    throw new RuntimeException(file_url2);
                }
                String filePath2 = file_url2.substring(file_url2.indexOf(FolderAndDirectoryConstant.WAREHOUSE_RECEIPT_FOLDER));
                salesApplicationTabTwo.setUploadProofOfOwnership(filePath2);

                savedTabTwo = salesApplicationTabTwoRepository.save(salesApplicationTabTwo);
                SalesApplicationMain salesApplicationMain = salesApplicationRepository.findBySalesApplicationMainIdAndBitDeleteFlag(salesApplicationTabTwo.getApplicationId(), false).orElseThrow(() -> new CustomGeneralException("Application does not exist"));
                salesApplicationMain.setIdtabtwo(savedTabTwo);
                salesApplicationRepository.save(salesApplicationMain);
            }
                return savedTabTwo;

            } catch(JsonProcessingException e){
                throw new RuntimeException(e);
            }
        }

    @Override
    public String saveOne(String salesTabOne) throws JsonProcessingException {
       try {
           //String decodedData = CommonUtil.inputStreamDecoder(salesTabOne);
           SalesApplicationTabOne salesApplicationTabOne = om.readValue(salesTabOne, SalesApplicationTabOne.class);

           logger.info("tab one save:{}",salesApplicationTabOne );
           logger.info("does applicationid exist:{}",salesApplicationTabOne.getApplicationId());

           SalesApplicationTabOne savedSalesTabOne;
           SalesApplicationMain savedSalesApplicationMain;

           if (salesApplicationTabOne.getApplicationId() == null || salesApplicationTabOne.getApplicationId().trim().isEmpty()){
               savedSalesTabOne = salesApplicationTabOneRepository.save(salesApplicationTabOne);

               savedSalesApplicationMain = new SalesApplicationMain();
               savedSalesApplicationMain.setIdtabone(savedSalesTabOne);
               savedSalesApplicationMain = salesApplicationRepository.save(savedSalesApplicationMain);

               savedSalesTabOne.setApplicationId(savedSalesApplicationMain.getSalesApplicationMainId());
               savedSalesTabOne = salesApplicationTabOneRepository.save(salesApplicationTabOne);


               logger.info("saved main id is:{}",savedSalesApplicationMain.getSalesApplicationMainId());
               logger.info("saved tab one data is :{}",savedSalesTabOne);

               return savedSalesApplicationMain.getSalesApplicationMainId();
           }else{
               Optional<SalesApplicationTabOne> existingTabOne = salesApplicationTabOneRepository.findByApplicationId(salesApplicationTabOne.getApplicationId());
               logger.info("existingTabOne:{}",existingTabOne);
               savedSalesTabOne = new SalesApplicationTabOne();

               if (existingTabOne.isPresent()) {
                   SalesApplicationTabOne existingRecord = existingTabOne.get();
                   existingRecord.setStmUpdatedAt(new Date());
                   savedSalesTabOne = salesApplicationTabOneRepository.save(existingRecord);
               }

               Optional<SalesApplicationMain> existingMain = salesApplicationRepository.findBySalesApplicationMainIdAndBitDeleteFlag(salesApplicationTabOne.getApplicationId(),false);
               savedSalesApplicationMain = new SalesApplicationMain();

               if (existingMain.isPresent()) {
                   savedSalesApplicationMain = existingMain.get();
                   savedSalesApplicationMain.setStmUpdatedAt(new Date());
                   savedSalesApplicationMain = salesApplicationRepository.save(savedSalesApplicationMain);
               }

               logger.info("inside else, application id is null,  saved main id is:{}",savedSalesApplicationMain.getSalesApplicationMainId());
               logger.info("inside else, application id is null,  saved tab one data is :{}",savedSalesTabOne);
               return savedSalesApplicationMain.getSalesApplicationMainId();
           }
       }catch (JsonProcessingException e){
           throw new CustomGeneralException("Error occured "+e);
       }

    }

    @Override
    public String saveThree(String tabThreeData) throws JsonProcessingException {
        try{
            
            SalesApplicationTabThree salesApplicationTabThree = om.readValue(tabThreeData, SalesApplicationTabThree.class);

            Optional<SalesApplicationTabThree> existingSalesApplicationTabThree = salesApplicationTabThreeRepository.findByApplicationId(salesApplicationTabThree.getApplicationId());
            SalesApplicationTabThree savedTabThree;

            //updating a new record based on ApplicationId
            if (existingSalesApplicationTabThree.isPresent()) {
                SalesApplicationTabThree existingRecord = existingSalesApplicationTabThree.get();
                existingRecord.setStmUpdatedAt(new Date());
                existingRecord.setDateOfIncident(salesApplicationTabThree.getDateOfIncident());
                existingRecord.setIsDeclared(salesApplicationTabThree.getIsDeclared());

                boolean isUploadContractAgreement = !existingRecord.getUploadContractAgreement().equals(salesApplicationTabThree.getUploadContractAgreement());
                if (isUploadContractAgreement) {
                    byte[] decodedReceipt = Base64.getDecoder().decode(salesApplicationTabThree.getUploadContractAgreement().getBytes());
                    String uniqueFileNameReceipt = "contract_agreement_" + UUID.randomUUID().toString();
                    String receiptFileUrl = DocumentUploadutil.uploadFileByte(uniqueFileNameReceipt, decodedReceipt, FolderAndDirectoryConstant.CONTRACT_AGREEMENT_FOLDER1);

                    if (receiptFileUrl.startsWith("Document")) {
                        throw new RuntimeException(receiptFileUrl);
                    }
                    String receiptFilePath = receiptFileUrl.substring(receiptFileUrl.indexOf(FolderAndDirectoryConstant.CONTRACT_AGREEMENT_FOLDER1));
                    existingRecord.setUploadContractAgreement(receiptFilePath);
                }else {
                    existingRecord.setUploadContractAgreement(salesApplicationTabThree.getUploadContractAgreement());
                }
                savedTabThree = salesApplicationTabThreeRepository.save(existingRecord);

            }else{
                //saving a new record
                byte[] decode = Base64.getDecoder().decode(salesApplicationTabThree.getUploadContractAgreement().getBytes());
                String uniqueFileName = "contract_agreement_" + UUID.randomUUID().toString();
                String file_url = DocumentUploadutil.uploadFileByte(uniqueFileName, decode,
                        FolderAndDirectoryConstant.CONTRACT_AGREEMENT_FOLDER1);

                if (file_url.startsWith("Document")) {
                    throw new RuntimeException(file_url);
                }

                String filePath = file_url.substring(file_url.indexOf(FolderAndDirectoryConstant.CONTRACT_AGREEMENT_FOLDER1));
                salesApplicationTabThree.setUploadContractAgreement(filePath);

                savedTabThree = salesApplicationTabThreeRepository.save(salesApplicationTabThree);
                SalesApplicationMain salesAppMain = salesApplicationRepository.findBySalesApplicationMainIdAndBitDeleteFlag(salesApplicationTabThree.getApplicationId(),false).orElseThrow(()->new CustomGeneralException("Application does not exist"));

                TwarehouseReceipt twarehouseReceipt = twarehouseReceiptRepository.findByTxtWarehouseReceiptIdAndStatusAndBitDeleteFlag(salesAppMain.getIdtabone().getReceiptNo(),Status.Approved,false);
                if (twarehouseReceipt == null){
                    throw  new CustomGeneralException("Warehouse receipt is empty");
                }

                twarehouseReceipt.setReceiptStatus(enmReceiptStatus.DealFinal);

                TreceiveCommodity treceiveCommodity = treceiveCommodityRepository.findByTxtReceiveCIdAndBitDeleteFlag(twarehouseReceipt.getReceiveCommodity().getTxtReceiveCId(),false);
                if (treceiveCommodity == null){
                    throw  new CustomGeneralException("Commodity is empty");
                }
                 String buyerId=salesAppMain.getIdtabone().getBuyerId();
                 
                 treceiveCommodity.setVchPurchasedBy(buyerId);
                 treceiveCommodity.setBitSaleStatus(false);
                 treceiveCommodity.setBitPaymentStatus(false);
                treceiveCommodityRepository.save(treceiveCommodity);
                
                twarehouseReceipt.setVchPurchasedBy(buyerId);
                twarehouseReceipt.setBitSaleStatus(false);
                twarehouseReceipt.setBitPaymentStatus(false);
                twarehouseReceiptRepository.save(twarehouseReceipt);
                salesAppMain.setIdtabthree(savedTabThree);
                salesAppMain.setApplicationStatus(ApplicationStatus.DealFinal);
                salesApplicationRepository.save(salesAppMain);
            }
            logger.info("saved tab three application Id is : "+savedTabThree.getApplicationId());
            return savedTabThree.getApplicationId();

        } catch (JsonProcessingException e) {
            throw  new CustomGeneralException("SalesApplication not saved in save Three"+e);
        }
    }

    @Override
    public SalesApplicationMain getApplicationInProgress() {
        return salesApplicationRepository.findByApplicationStatusAndBitDeleteFlag(ApplicationStatus.inProgress,false).orElse(null);
    }

    @Override
    public SalesApplicationMainResponse getSalesReceiptDetails(String applicationId) {
        logger.info("Application id in get Sales Receipt Details is:"+applicationId);
       Optional<SalesApplicationMain> salesApplicationMain = salesApplicationRepository.findBySalesApplicationMainIdAndBitDeleteFlag(applicationId,false);

       if (salesApplicationMain.isPresent()) {
           SalesApplicationMain savedSalesApplicationMain = salesApplicationMain.get();

           SalesApplicationMainResponse response = Mapper.mapToSalesApplicationMainResponse(savedSalesApplicationMain);
           SalesApplicationTabOneDTO salesApplicationTabOneDTO = response.getSalesApplicationTabOneDTO();

           BuyerDepositor depositor = buyerDepositorRepository.findByIntIdAndBitDeletedFlag(savedSalesApplicationMain.getIdtabone().getDepositorId(),false);
           salesApplicationTabOneDTO.setDepositorId(Mapper.mapToBuyerResponse(depositor));

           BuyerDepositor buyer = buyerDepositorRepository.findByIntIdAndBitDeletedFlag(savedSalesApplicationMain.getIdtabone().getBuyerId(),false);
           salesApplicationTabOneDTO.setBuyerId(Mapper.mapToBuyerResponse(buyer));

           TwarehouseReceipt twarehouseReceipt = twarehouseReceiptRepository.findByTxtWarehouseReceiptIdAndStatusAndBitDeleteFlag(savedSalesApplicationMain.getIdtabone().getReceiptNo(),Status.Approved,false);
           salesApplicationTabOneDTO.setReceiptNo(Mapper.mapToWarehouseReceiptResponse(twarehouseReceipt));

           return response;
       }else{
           throw new CustomGeneralException("Application With Id : "+applicationId+" does Not Exist ");
       }
    }

    @Override
    public SalesApplicationMain getSalesApplicationsByStatus() {
        //return salesApplicationRepository.findByApplicationStatusAndBitDeleteFlag(Arrays.asList("DealFinal", "Sold"), false).orElse(null);
    	
    	return null;
    }

	@Override
	public ReceiptDealFinalDto getDealDetails(String receiptNO) {
		
		   ReceiptDealFinalDto detailsDto=new ReceiptDealFinalDto();
		
		  List<Object[]> objList=salesApplicationRepository.getDealDetails(receiptNO);
		  
		  for(Object[] object :objList) {
			  
              if(object.length>0) {
            	  detailsDto.setSales_application_id(object[0].toString());
    			  detailsDto.setNew_owner_id(object[1].toString());
    			  detailsDto.setDepositor_id(object[2].toString());
    			  detailsDto.setStatus(true); 
              }
              else {
            	  detailsDto.setSales_application_id("0");
    			  detailsDto.setNew_owner_id("0");
    			  detailsDto.setDepositor_id("0");
    			  detailsDto.setStatus(false); 
              }
			 
		  }
		  
		
		return detailsDto;
	}

	@Override
	public ReceiptDealFinalDto checkReceiptPayment(String receiptNO) {


		 ReceiptDealFinalDto detailsDto=new ReceiptDealFinalDto();
			
		 // List<Object[]> objList=salesApplicationRepository.getReceiptPaymentDetails(receiptNO);
		  
		  List<Object[]> objList=salesApplicationRepository.getDealDetails(receiptNO);
		  
		  for(Object[] object :objList) {
			  
             if(object.length>0) {
           	  detailsDto.setSales_application_id(object[0].toString());
   			  detailsDto.setNew_owner_id(object[1].toString());
   			  detailsDto.setDepositor_id(object[2].toString());
   			  detailsDto.setStatus(true); 
             }
             else {
           	  detailsDto.setSales_application_id("0");
   			  detailsDto.setNew_owner_id("0");
   			  detailsDto.setDepositor_id("0");
   			  detailsDto.setStatus(false); 
             }
			 
		  }
		  		
		return detailsDto;

	}

	@Override
	public Page<SalesApplicationListResponse> getAllSalesApplicationList(Pageable pageable,String search) {
		// TODO Auto-generated method stub
		 return salesApplicationRepository.findAllSalesApplicationList(List.of("DealFinal","Sold"),pageable,search);
	}
	
	


	
}
