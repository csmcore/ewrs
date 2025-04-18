package app.ewarehouse.serviceImpl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.BuyerDepositorResponse;
import app.ewarehouse.entity.BuyerDepositor;
import app.ewarehouse.entity.CreatedStatus;
import app.ewarehouse.entity.MBuyerDepositor;
import app.ewarehouse.entity.RegistrationType;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.enmReceiptStatus;
import app.ewarehouse.exception.CustomEntityNotFoundException;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.BuyerDepositorRepository;
import app.ewarehouse.repository.MBuyerDepositorRepository;
import app.ewarehouse.repository.ProcedureCall;
import app.ewarehouse.service.BuyerDepositorService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import app.ewarehouse.util.JsonFileExtractorUtil;
import app.ewarehouse.util.Mapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class BuyerDepositorServiceImpl implements BuyerDepositorService {
    @Autowired
    private BuyerDepositorRepository buyerRepository;
    @Autowired
    private MBuyerDepositorRepository mBuyerDepositorRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    ErrorMessages errorMessages;
    @Autowired
    private Validator validator;
    private static final Logger logger = LoggerFactory.getLogger(BuyerDepositorServiceImpl.class);
    @Autowired
    ProcedureCall  procedureCall;
    
    @Autowired
    ObjectMapper om;
    @Override
    public String save(String data) {
        logger.info("Inside save method of BuyerServiceImpl");

        String decodedData = CommonUtil.inputStreamDecoder(data);
        BuyerDepositor buyer;

        try {
            buyer = om.readValue(decodedData, BuyerDepositor.class);
            buyer.setTxtPassportPath(JsonFileExtractorUtil.uploadFile(buyer.getTxtPassportPath(), "Buyer_Passport_" + System.currentTimeMillis(), FolderAndDirectoryConstant.BUYER_REG_FOLDER));
            buyer.setTxtBankProofPath(JsonFileExtractorUtil.uploadFile(buyer.getTxtBankProofPath(), "Buyer_BankProof_" + System.currentTimeMillis(), FolderAndDirectoryConstant.BUYER_REG_FOLDER));
            buyer.setTxtBusinessRegCertPath(JsonFileExtractorUtil.uploadFile(buyer.getTxtBusinessRegCertPath(), "Buyer_BusinessRegCert_" + System.currentTimeMillis(), FolderAndDirectoryConstant.BUYER_REG_FOLDER));

            Set<ConstraintViolation<BuyerDepositor>> violations = validator.validate(buyer);
            if (!violations.isEmpty()) {
                logger.error("Inside save method of BuyerServiceImpl Validation errors: " + violations);
                throw new CustomGeneralException(violations);
            }

            buyer = buyerRepository.save(buyer);
        } catch (DataIntegrityViolationException exception) {
            logger.error("Inside save method of BuyerServiceImpl some error occur:" + exception.getMessage());
            String msg = exception.getMessage();
            if (msg.contains("'email'")) {
                throw new CustomGeneralException(errorMessages.getEmailExists());
            } else if (msg.contains("'passport'")) {
                throw new CustomGeneralException(errorMessages.getPassportExsits());
            } else if (msg.contains("'phone'")) {
                throw new CustomGeneralException(errorMessages.getPhoneNoExists());
            } else if (msg.contains("'Data truncation'")) {
                throw new CustomGeneralException(errorMessages.getConstraintError());
            }
            throw new CustomGeneralException(errorMessages.getGeneralDuplicateRecords());
        } catch (CustomGeneralException exception) {
            logger.error("Inside save method of BuyerServiceImpl some error occur:" + exception.getMessage());
            throw exception;
        } catch (Exception e) {
            logger.error("Inside save method of BuyerServiceImpl some error occur:" + e.getMessage());
            throw new CustomGeneralException(errorMessages.getUnknownError());
        }

        return buyer.getIntId();
    }

    @Transactional
    @Override
    public String takeAction(String data) {
        logger.info("Inside takeAction method of BuyerServiceImpl");

        String decodedData = CommonUtil.inputStreamDecoder(data);
        String txtEntityId;

        try {
            BuyerDepositorResponse buyer = om.readValue(decodedData, BuyerDepositorResponse.class);

            BuyerDepositor existingBuyer = buyerRepository.findByIntIdAndBitDeletedFlag(buyer.getIntId(), false);
            if (!existingBuyer.getEnmStatus().equals(Status.Pending)) {
                throw new CustomGeneralException(errorMessages.getNotAuthorized());
            }

            existingBuyer.setEnmStatus(Status.valueOf(buyer.getEnmStatus()));

            existingBuyer = buyerRepository.save(existingBuyer);

            MBuyerDepositor mBuyerDepositor = new MBuyerDepositor();
            mBuyerDepositor.setApplicationDetails(existingBuyer);
            mBuyerDepositor.setEnmStatus(buyer.getEnmStatus().equals(Status.Accepted.toString()) ? CreatedStatus.Created : CreatedStatus.NotCreated);
            System.out.println(mBuyerDepositor.getEnmStatus());
            txtEntityId = mBuyerDepositorRepository.save(mBuyerDepositor).getVchEntityId();
        } catch (CustomGeneralException e) {
            logger.error("Inside takeAction method of BuyerServiceImpl some error occur:" + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Inside takeAction method of BuyerServiceImpl some error occur:" + e.getMessage());
            throw new CustomGeneralException(errorMessages.getUnknownError());
        }

        return txtEntityId;
    }


    @Override
    public BuyerDepositorResponse getById(String id) {
        try {
            logger.info("Inside getById method of BuyerServiceImpl");
            BuyerDepositor buyer = buyerRepository.findByIntIdAndBitDeletedFlag(id, false);

            if (buyer == null) {
                throw new CustomEntityNotFoundException(errorMessages.getEntityNotFound());
            }

            return Mapper.mapToBuyerResponse(buyer);
        } catch (CustomEntityNotFoundException e) {
            logger.error("Entity not found: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred in getById method of BuyerServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BuyerDepositorResponse> getAllDepositorsPd() {
        try {
            logger.info("Inside getAllDepositorsPd method of BuyerServiceImpl");
            List<RegistrationType> regTypes = List.of(RegistrationType.Depositor, RegistrationType.DepositorBuyer);  //  enmReceiptStatus.Pending
            return buyerRepository.findAllDepositorsWithAcceptedWarehouseReceiptPd(regTypes, Status.Accepted, false, Status.Approved).stream()
                    .map(Mapper::mapToBuyerResponse)
                    .toList();
        } catch (Exception e) {
            logger.error("Error occurred in getAllDepositorsPd method of BuyerServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<BuyerDepositorResponse> getAllDepositors() {
        try {
            logger.info("Inside getAllDepositors method of BuyerServiceImpl");
            List<RegistrationType> regTypes = List.of(RegistrationType.Depositor, RegistrationType.DepositorBuyer);  //  enmReceiptStatus.Pending
            return buyerRepository.findAllDepositorsWithAcceptedWarehouseReceipt(regTypes, Status.Accepted, false, Status.Approved,enmReceiptStatus.DealFinal).stream()
                    .map(Mapper::mapToBuyerResponse)
                    .toList();
        } catch (Exception e) {
            logger.error("Error occurred in getAllDepositors method of BuyerServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BuyerDepositorResponse> getAllBuyers() {
        try {
            logger.info("Inside getAllBuyers method of BuyerServiceImpl");
            List<RegistrationType> regTypes = List.of(RegistrationType.Buyer,RegistrationType.DepositorBuyer);
            return buyerRepository.findAllBuyers(regTypes, Status.Accepted, false).stream().map(Mapper::mapToBuyerResponse).toList();
        } catch (Exception e) {
            logger.error("Error occurred in getAllBuyers method of BuyerServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public BuyerDepositorResponse findByIdAndRegType(String id) {
        logger.info("Inside findByIdAndRegType method of BuyerServiceImpl");
        List<RegistrationType> regTypes = Arrays.asList(RegistrationType.Depositor, RegistrationType.DepositorBuyer);
        
        
        BuyerDepositor buyer = buyerRepository.findByIntIdAndEnmRegistrationTypeInAndEnmStatusAndBitDeletedFlag(id, regTypes, Status.Accepted, false).orElseThrow(() -> new CustomGeneralException(errorMessages.getValidDepositorId()));
        
        return Mapper.mapToBuyerResponse(buyer);
    }

    @Override
    public List<BuyerDepositorResponse> getAll() {
        logger.info("Inside getAll method of BuyerServiceImpl");
        List<BuyerDepositor> buyersList = buyerRepository.findAllByBitDeletedFlagOrderByDtmCreatedOnDesc(false);
        return buyersList.stream()
                .map(Mapper::mapToBuyerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BuyerDepositorResponse> getAll(Pageable pageable) {
        logger.info("Inside getAll pageable method of BuyerServiceImpl");
        Page<BuyerDepositor> buyersPage = buyerRepository.findAllByBitDeletedFlagOrderByDtmCreatedOnDesc(false, pageable);

        List<BuyerDepositorResponse> buyerResponses = buyersPage.getContent().stream()
                .map(Mapper::mapToBuyerResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(buyerResponses, pageable, buyersPage.getTotalElements());
    }

    @Override
    public String deleteById(String id) {
        logger.info("Inside deleteById method of BuyerServiceImpl");
        BuyerDepositor buyer = buyerRepository.findByIntIdAndBitDeletedFlag(id, false);
        buyer.setBitDeletedFlag(true);
        buyerRepository.save(buyer);
        return buyer.getIntId();
    }

    @Override
    public Page<BuyerDepositorResponse> getFilteredBuyers(Date fromDate, Date toDate, Status status, Pageable pageable) {
        logger.info("Inside getFilteredBuyers method of BuyerServiceImpl");

        if (toDate != null) {
            // Increment toDate by 24 hours
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(toDate);
            calendar.add(Calendar.HOUR, 24);
            toDate = calendar.getTime();
        }

        logger.info("from: " + fromDate + " to: " + toDate);

        Page<BuyerDepositor> buyersPage = buyerRepository.findByFilters(fromDate, toDate, status, pageable);
        logger.info("from: " + fromDate + " to: " + toDate);
        List<BuyerDepositorResponse> buyerResponses = buyersPage.getContent().stream()
                .map(Mapper::mapToBuyerResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(buyerResponses, pageable, buyersPage.getTotalElements());
    }

    @Override
    public Page<BuyerDepositorResponse> getFilteredBuyers(Date fromDate, Date toDate, Status status, String search, String sortColumn,
                                                          String sortDirection, Pageable pageable) {
        logger.info("Inside getFilteredBuyers method of BuyerServiceImpl");

        if (toDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(toDate);
            calendar.add(Calendar.HOUR, 24);
            toDate = calendar.getTime();
        }

        logger.info("from: " + fromDate + " to: " + toDate);

        Page<BuyerDepositor> buyersPage = buyerRepository.findByFilters(fromDate, toDate, status, search, pageable);
        List<BuyerDepositorResponse> buyerResponses = buyersPage.getContent().stream()
                .map(Mapper::mapToBuyerResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(buyerResponses, pageable, buyersPage.getTotalElements());
    }
    @Override
    public List<String> getDepositorById() {
        List<RegistrationType> regTypes = Arrays.asList(RegistrationType.Depositor, RegistrationType.DepositorBuyer);
        List<String> depositorIdList = buyerRepository.findAllDepositorsIntIdsWithAcceptedWarehouseReceipt(regTypes, Status.Accepted, false,Status.Approved,enmReceiptStatus.Pending);

        if (depositorIdList == null) {
            throw new  CustomGeneralException(errorMessages.getValidDepositorId());
        }
        logger.info("Returning only depositor Ids with receipts"+depositorIdList);
      return depositorIdList;
    }

    @Override
    public List<String> getAllBuyersId() {
        try {
            logger.info("Inside getAllBuyer Ids method of BuyerServiceImpl");
            List<RegistrationType> regTypes = List.of(RegistrationType.Buyer,RegistrationType.DepositorBuyer);
            List<String> buyerListString = buyerRepository.findAllBuyersIds(regTypes, Status.Accepted, false);
            logger.info("Inside getAllBuyer Ids method of BuyerServiceImpl {}",buyerListString);
            return buyerListString;
        } catch (Exception e) {
            logger.error("Error occurred in getAllBuyersIds method of BuyerServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private BuyerDepositorResponse mapToResponse(BuyerDepositor depositor) {
        BuyerDepositorResponse response = new BuyerDepositorResponse();
        response.setIntId(depositor.getIntId());
        response.setTxtName(depositor.getTxtName());
        return response;
    }

	@Override
	public String checkDepostiroId(String depositorid) {

		return buyerRepository.getCehckDepositor(depositorid); 
	}

	@Override
	public List<String> getValidReceiptDepositorById(String depositorid) {


		
		return procedureCall.getValidReceiptDepositorById(depositorid);
	}
}
