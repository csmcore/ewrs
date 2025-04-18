package app.ewarehouse.serviceImpl;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import app.ewarehouse.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.WareHouseRemarksDto;
import app.ewarehouse.dto.WarehouseApplicantResponse;
import app.ewarehouse.entity.Inspector;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.WarehouseApplicant;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.WarehouseApplicantRepository;
import app.ewarehouse.service.WarehouseApplicantService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class WarehouseApplicantServiceImpl implements WarehouseApplicantService {
	@Autowired
	private WarehouseApplicantRepository warehouseApplicantRepository;
	@Autowired
	private ErrorMessages errorMessages;
	@Autowired
	private Validator validator;
	@Autowired
    private ObjectMapper om;
	private static final Logger logger = LoggerFactory.getLogger(WarehouseApplicantServiceImpl.class);

	@Override
	public String save(String data) {
		logger.info("Inside save method of WarehouseApplicantServiceImpl");

		String decodedData = CommonUtil.inputStreamDecoder(data);
		WarehouseApplicant warehouseApplicant;

		WarehouseApplicant savedWarehouseApplicant;

		try {
			warehouseApplicant = om.readValue(decodedData, WarehouseApplicant.class);

			Set<ConstraintViolation<WarehouseApplicant>> violations = validator.validate(warehouseApplicant);
			if (!violations.isEmpty()) {
				logger.error("Inside save method of WarehouseApplicantServiceImpl some error occur:" + violations);
				throw new CustomGeneralException(violations);
			}

			warehouseApplicant.getWarehouseParticulars().setVchWarehouseName(
					warehouseApplicant.getWarehouseParticulars().getVchOperatorName() + " Warehouse");

			savedWarehouseApplicant = warehouseApplicantRepository.save(warehouseApplicant);
		} catch (DataIntegrityViolationException exception) {
			logger.error(
					"Inside save method of WarehouseApplicantServiceImpl some error occur:" + exception.getMessage());
			String msg = exception.getMessage();
			if (msg.contains("'email'")) {
				throw new CustomGeneralException(errorMessages.getEmailExists());
			}
			else if (msg.contains("'company_email'")) {
				throw new CustomGeneralException(errorMessages.getCompanyEmailExists());
			}
			else if (msg.contains("'company_kra_pin'")) {
				throw new CustomGeneralException(errorMessages.getKraPinExists());
			}
			else if (msg.contains("'company_reg_no'")) {
				throw new CustomGeneralException(errorMessages.getCompanyRegExists());
			}
			else if (msg.contains("'passport'")) {
				throw new CustomGeneralException(errorMessages.getPassportExsits());
			}
			else if (msg.contains("'phone'")) {
				throw new CustomGeneralException(errorMessages.getPhoneNoExists());
			}
			throw new CustomGeneralException(errorMessages.getGeneralDuplicateRecords());
		} catch (Exception e) {
			logger.error("Inside save method of WarehouseApplicantServiceImpl some error occur:" + e.getMessage());
			throw new CustomGeneralException(errorMessages.getUnknownError());
		}
		return savedWarehouseApplicant.getVchApplicantId();
	}

	@Override
	public WarehouseApplicantResponse getById(String id) {
		logger.info("Inside getById method of WarehouseApplicantServiceImpl");
		WarehouseApplicant warehouseApplicant = warehouseApplicantRepository.findByVchApplicantIdAndBitDeletedFlag(id,
				false);

		return Mapper.mapToWarehouseApplicantResponse(warehouseApplicant);
	}

	@Override
	public List<WarehouseApplicantResponse> getAll() {
		logger.info("Inside getAll method of WarehouseApplicantServiceImpl");
		List<WarehouseApplicant> warehouseApplicantList = warehouseApplicantRepository.findAllByBitDeletedFlag(false);
		return warehouseApplicantList.stream().map(Mapper::mapToWarehouseApplicantResponse)
				.collect(Collectors.toList());
	}

	@Override
	public String deleteById(String id) {
		logger.info("Inside deleteById method of WarehouseApplicantServiceImpl");
		warehouseApplicantRepository.softDeleteWarehouseApplicant(id);
		return id;
	}

	@Override
	public Page<WarehouseApplicantResponse> getAll(Pageable pageable) {
		logger.info("Inside getAll paged method of WarehouseApplicantServiceImpl");
		Page<WarehouseApplicant> warehouseApplicantsPage = warehouseApplicantRepository.findAllByBitDeletedFlag(false,
				pageable);

		List<WarehouseApplicantResponse> warehouseApplicantResponses = warehouseApplicantsPage.getContent().stream()
				.map(Mapper::mapToWarehouseApplicantResponse).collect(Collectors.toList());

		return new PageImpl<>(warehouseApplicantResponses, pageable, warehouseApplicantsPage.getTotalElements());
	}

	@Override
	public Page<WarehouseApplicantResponse> getFilteredApplicants(Date fromDate, Date toDate, Status status,
			Pageable pageable) {
		Page<WarehouseApplicant> warehouseApplicantsPage = warehouseApplicantRepository.findByFilters(fromDate, toDate,
				status, pageable);

		List<WarehouseApplicantResponse> warehouseApplicantResponses = warehouseApplicantsPage.getContent().stream()
				.map(Mapper::mapToWarehouseApplicantResponse).collect(Collectors.toList());

		return new PageImpl<>(warehouseApplicantResponses, pageable, warehouseApplicantsPage.getTotalElements());
	}

	@Override
	public void giveWareHouseRemarks(String data) {
		try {
			String decodedData = CommonUtil.inputStreamDecoder(data);
			WareHouseRemarksDto dto = om.readValue(decodedData, WareHouseRemarksDto.class);
			WarehouseApplicant waData = warehouseApplicantRepository.findByVchApplicantIdAndBitDeletedFlag(dto.getIntWareHouseId(),false);
			switch (dto.getUserName()) {
			case "Finance officer":
				waData.setFinanceRemarks(dto.getTxtRemark());
				break;
			case "Verification officer":
				waData.setVerificationRemarks(dto.getTxtRemark());
				break;
			case "Manager RC":
				Inspector ins = new Inspector();
				ins.setId(dto.getInspectorId());
				waData.setInspector(ins);
				waData.setManagerRcRemarks(dto.getTxtRemark());
				break;
				//from this work will continue
			case "inspector":
				byte[] decodedFile = Base64.getDecoder().decode(dto.getTxtInspectorFilePath());
                String filePath = DocumentUploadutil.uploadFileByte(
                    "WAREHOUSE_STRUCTURE_INSPECTION_PLAN_REPORT_" + System.currentTimeMillis(),
                    decodedFile,
                    FolderAndDirectoryConstant.WAREHOUSE_STRUCTURE_INSPECTION_PLAN_FOLDER
                );
                LocalDateTime localDateTime = LocalDateTime.parse(dto.getDtmInspection());
                waData.setDateTimeInspection(localDateTime);
                waData.setUploadInspectionPlan(filePath);
                waData.setInspectorRemarks(dto.getTxtRemark());
                break;
			case "Manager RCC":
				waData.setManagerRccRemarks(dto.getTxtRemark());
				break;
			case "Food Crops Directorate":
				waData.setFoodCropsRemarks(dto.getTxtRemark());
				if(dto.getEnmStatus().equals("Accepted")) {
				waData.setEnmStatus(Status.Accepted);
				}else if(dto.getEnmStatus().equals("Deferred")) {
					waData.setEnmStatus(Status.Deferred);
				}
				else {
					waData.setEnmStatus(Status.Rejected);
				}
				break;	
			default:
				logger.warn("Invalid username: {}", dto.getUserName());
                throw new IllegalArgumentException("Invalid username: " + dto.getUserName());
			}
			warehouseApplicantRepository.save(waData);
		} catch (JsonProcessingException e) {
			logger.error("error happend while giving remark", e);
		}

	}
}
