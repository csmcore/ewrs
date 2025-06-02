package app.ewarehouse.serviceImpl;

import app.ewarehouse.dto.BuyerPaymentInvoiceRequest;
import app.ewarehouse.entity.*;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.BuyerDepositorRepository;
import app.ewarehouse.repository.BuyerPaymentInvoiceRepository;
import app.ewarehouse.repository.DocumentRepository;
import app.ewarehouse.repository.SalesApplicationMainRepository;
import app.ewarehouse.repository.TreceiveCommodityRepository;
import app.ewarehouse.repository.TwarehouseReceiptRepository;
import app.ewarehouse.service.BuyerPaymentInvoiceService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.DocumentUploadutil;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static app.ewarehouse.entity.Status.*;

@Service
@RequiredArgsConstructor
public class BuyerPaymentInvoiceServiceImpl implements BuyerPaymentInvoiceService {

    private final BuyerPaymentInvoiceRepository buyerPaymentInvoiceRepository;
    private final DocumentRepository documentRepository;
    private final EntityManager entityManager;
    private final TwarehouseReceiptRepository twarehouseReceiptRepository;
    private final BuyerDepositorRepository buyerDepositorRepository;
    private final ErrorMessages errorMessages;

    @Autowired
    private SalesApplicationMainRepository salesApplicationRepository;
    @Autowired
    private TreceiveCommodityRepository treceiveCommodityRepository;
    
    @Autowired
    private ObjectMapper om;
    private static final Logger logger = LoggerFactory.getLogger(BuyerPaymentInvoiceServiceImpl.class);

    @Override
    @Transactional
    public String saveData(String data) {
        try {

        	BuyerPaymentInvoiceRequest buyerPaymentInvoiceRequest = om.readValue(CommonUtil.inputStreamDecoder(data), BuyerPaymentInvoiceRequest.class);

            logger.info("Inside the savedata{}",buyerPaymentInvoiceRequest);
            byte[] decode = Base64.getDecoder().decode(buyerPaymentInvoiceRequest.getContractAgreement().getBytes());
            String uniqueFileName = "Buyer_Payment_Receipt_" + UUID.randomUUID();
            String file_url = DocumentUploadutil.uploadFileByte(uniqueFileName, decode,FolderAndDirectoryConstant.CONTRACT_AGREEMENT_FOLDER);

            if (file_url.startsWith("Document")) {
                throw new CustomGeneralException(file_url);
            }
         

            String filePath = file_url.substring(file_url.indexOf(FolderAndDirectoryConstant.CONTRACT_AGREEMENT_FOLDER));

            // Save document details
            Document document = new Document();
            document.setDocName(uniqueFileName);
            document.setDocPath(filePath);
            document.setCreatedBy("system");
            documentRepository.save(document);


            BuyerDepositor buyer = buyerDepositorRepository.findByIntIdAndBitDeletedFlag(buyerPaymentInvoiceRequest.getBuyerId(), false);
//           BuyerDepositor buyer = new BuyerDepositor();
//            buyer.setIntId(buyerPaymentInvoiceRequest.getBuyerId());

            TwarehouseReceipt twarehouseReceipt = twarehouseReceiptRepository.findByTxtWarehouseReceiptIdAndStatusAndBitDeleteFlag(buyerPaymentInvoiceRequest.getWareHouseReceiptNo(), Approved,false);
            twarehouseReceipt.setReceiptStatus(enmReceiptStatus.Sold);
            twarehouseReceipt.setVchPurchasedBy(buyerPaymentInvoiceRequest.getBuyerId());
            twarehouseReceipt.setBitSaleStatus(true);
            twarehouseReceipt.setBitPaymentStatus(true);
            twarehouseReceiptRepository.save(twarehouseReceipt);

            TreceiveCommodity treceiveCommodity = treceiveCommodityRepository.findByTxtReceiveCIdAndBitDeleteFlag(twarehouseReceipt.getReceiveCommodity().getTxtReceiveCId(),false);
            if (treceiveCommodity == null){
                throw  new CustomGeneralException("Commodity is empty");
            }
             
             
             treceiveCommodity.setVchPurchasedBy(buyerPaymentInvoiceRequest.getBuyerId());
             treceiveCommodity.setBitSaleStatus(true);
             treceiveCommodity.setBitPaymentStatus(true);
            treceiveCommodityRepository.save(treceiveCommodity);
            
            
            String salesReceiptNO= buyerPaymentInvoiceRepository.getSalesApplicationNo(buyerPaymentInvoiceRequest.getWareHouseReceiptNo());
            
          
            SalesApplicationMain salesAppMain = salesApplicationRepository.findBySalesApplicationMainIdAndBitDeleteFlag(salesReceiptNO,false).orElseThrow(()->new CustomGeneralException("Application does not exist"));
            
            salesAppMain.setApplicationStatus(ApplicationStatus.Sold);
            salesApplicationRepository.save(salesAppMain);
            
            
            BuyerPaymentInvoice buyerPaymentInvoice = new BuyerPaymentInvoice();
            buyerPaymentInvoice.setInvoiceNumber(generateCustomId("t_buyer_payment_invoice", "invoiceNumber"));
            buyerPaymentInvoice.setContractAgreementFileUrl(filePath);
            buyerPaymentInvoice.setBuyer(buyer);
            buyerPaymentInvoice.setWarehouseReceipt(twarehouseReceipt);
            buyerPaymentInvoice.setPrice(buyerPaymentInvoiceRequest.getPriceOfReceipt());
            buyerPaymentInvoice.setDocument(document);
            buyerPaymentInvoice.setStatus(Status.Paid);
            
            BuyerPaymentInvoice savedBuyerPaymentInvoice = buyerPaymentInvoiceRepository.save(buyerPaymentInvoice);
            return savedBuyerPaymentInvoice.getInvoiceNumber();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generateCustomId(String tableName, String idName) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GenerateCustomID")
                .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
                .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
                .registerStoredProcedureParameter(3, String.class, ParameterMode.OUT)
                .setParameter(1, tableName)
                .setParameter(2, idName);

        query.execute();
        return (String) query.getOutputParameterValue(3);
    }

    @Override
    public Optional<BuyerPaymentInvoice> getInvoiceByNumber(String invoiceNumber) {
        return buyerPaymentInvoiceRepository.findByInvoiceNumber(invoiceNumber);
    }

    @Override
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public boolean isPaymentCompleted(String buyerId, String receiptNo) {
        BuyerPaymentInvoice invoice = buyerPaymentInvoiceRepository.getBuyerPaymentInvoice(buyerId,receiptNo);
        if (invoice == null) {
            throw new CustomGeneralException(errorMessages.getHasBuyerPayed());
        }
        return true;
    }

    @Override
    public boolean checkPayment(String invoiceNumber, BigDecimal paidAmount) {
        Optional<BuyerPaymentInvoice> invoice = buyerPaymentInvoiceRepository.getByInvoiceNumber(invoiceNumber);
        if (invoice.isEmpty()) {
            throw new CustomGeneralException(errorMessages.getInvoiceNotFound());
        }
        return paidAmount.compareTo(invoice.get().getPrice()) == 0;
    }

}
