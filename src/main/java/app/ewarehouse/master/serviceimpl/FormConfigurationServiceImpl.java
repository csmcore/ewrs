package app.ewarehouse.master.serviceimpl;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import app.ewarehouse.exception.FormConfigurationException;
import app.ewarehouse.master.dto.CheckerRequestBean;
import app.ewarehouse.master.dto.FormConfigurationActualResponse;
import app.ewarehouse.master.dto.FormConfigurationBean;
import app.ewarehouse.master.dto.FormConfigurationCopyBean;
import app.ewarehouse.master.dto.FormConfigurationResponse;
import app.ewarehouse.master.entity.FormConfiguration;
import app.ewarehouse.master.entity.FormConfigurationCopy;
import app.ewarehouse.master.entity.TypeMaster;
import app.ewarehouse.master.repository.IFormConfigurationCopyRepository;
import app.ewarehouse.master.repository.IFormConfigurationRepository;
import app.ewarehouse.master.service.IFormConfigurationService;

/**
 * @author pravat.behera
 * @Date 24-03-2025
 * @Description This service use for form configuration all service business
 *              logic
 */
@Service
public class FormConfigurationServiceImpl implements IFormConfigurationService {

	private final IFormConfigurationRepository repo;
	private final Logger logger;
	private final IFormConfigurationCopyRepository copyRepo;

	public FormConfigurationServiceImpl(IFormConfigurationRepository repo, Logger logger,
			IFormConfigurationCopyRepository copyRepo) {
		this.repo = repo;
		this.logger = logger;
		this.copyRepo = copyRepo;
	}

	// Copy main table to copy form configuration table
	private void copyMainToCopyTable(FormConfiguration formConfiguration) {
		// Copy Table Object
		FormConfigurationCopy formCofigurationCopy = new FormConfigurationCopy();
		// Copy main to copy form configuration table
		BeanUtils.copyProperties(formConfiguration, formCofigurationCopy);
		// Save Copy Data
		formCofigurationCopy = copyRepo.save(formCofigurationCopy);
	}

	/**
	 * @author pravat.behera
	 * @Date 24-03-2025 16:38 PM
	 */
	@Override
	public FormConfigurationResponse saveFormConfiguration(FormConfigurationBean formConfigurationBean) {

		FormConfigurationResponse formConfigurationResponse = new FormConfigurationResponse();

		try {
			FormConfiguration formConfiguration = new FormConfiguration();
			TypeMaster typemaster = new TypeMaster();
			typemaster.setTypeId(formConfigurationBean.getTypeMaster());

			// Check Duplicate Entry
			long duplicaeCount = this.repo
					.duplicateCheckDocumentNameInNewRecord(formConfigurationBean.getDocumentName(), typemaster);

			if (duplicaeCount > 0) {
				// Return with Duplicate Value
				formConfigurationResponse.setStatus("duplicate");
				formConfigurationResponse.setMessage("Duplicate document name found");
				formConfigurationResponse.setFormConfigurationBean(formConfigurationBean);
				this.logger.error("Duplicate document name found");
				return formConfigurationResponse;
			}

			BeanUtils.copyProperties(formConfigurationBean, formConfiguration);

			formConfiguration.setCreatedOn(new Date());

			formConfiguration.setTypeMaster(typemaster);
			// pending at Checker CEO
			formConfiguration.setFinalStatus(1);
			// New Record
			formConfiguration.setStatusType(1);

			formConfiguration = repo.save(formConfiguration);
			BeanUtils.copyProperties(formConfiguration, formConfigurationBean);
			logger.info(
					"saveFormConfiguration method for saveFormConfiguration Service for Form Configuration successfully created");
			// Copy form configuration copy table
			this.copyMainToCopyTable(formConfiguration);
			// Success message
			formConfigurationResponse.setStatus("success");
			formConfigurationResponse.setMessage("Successfully save in  form configuration");
			formConfigurationResponse.setFormConfigurationBean(formConfigurationBean);
		} catch (Exception e) {
			this.logger.error("Eception occuer in saveFormConfiguration service class saveFormConfiguration method {}",
					e);
			// Return with Exception message Value
			formConfigurationResponse.setStatus("exception");
			formConfigurationResponse.setMessage("Excpetion found in " + e.getMessage());
			formConfigurationResponse.setFormConfigurationBean(formConfigurationBean);
		}
		return formConfigurationResponse;
	}

	// Checker get data from copy table
	public List<FormConfigurationCopyBean> getCheckerDataFromCopyTable() {
		logger.info("Inside FormConfigurationServiceImpl Service for getCheckerDataFromCopyTable method get all data");
		// Get all Data From copy table
		List<FormConfigurationCopy> listData = this.copyRepo.findAll();
		// .filter(formConf -> formConf.getDeletedFlag())
		return listData.stream()
				.sorted(Comparator.comparing((FormConfigurationCopy formConfig) -> formConfig.getTypeMaster().getType())
						.thenComparing(formConfig -> formConfig.getDocumentName()))
				.map(formConfig -> new FormConfigurationCopyBean(formConfig.getId(),
						formConfig.getTypeMaster().getTypeId(), formConfig.getFormat(), formConfig.getSize(),
						formConfig.getDocumentName(), formConfig.getIsMandetory(), formConfig.getIsActive(),
						formConfig.getCheckerStatus(), formConfig.getCreatedBy(), formConfig.getCreatedOn(),
						formConfig.getUpdatedBy(), formConfig.getUpdatedOn(), formConfig.getDeletedFlag(),
						formConfig.getCheckerStatus(), formConfig.getCheckerUpdatedBy(),
						formConfig.getCheckerUpdatedOn(), formConfig.getTypeMaster().getType(),
						formConfig.getCheckerStatus() == 0 ? "Pending"
								: formConfig.getCheckerStatus() == 1 ? "Pending"
										: formConfig.getCheckerStatus() == 2 ? "Approved" : "Reject",
						formConfig.getUserId(), formConfig.getIdCopy(), formConfig.getStatusType(),
						formConfig.getRemark()))
				.collect(Collectors.toList());
	}

	/**
	 * @author pravat.behera
	 * @Date 24-03-2025 16:38 PM
	 */
	// Copy Service data to backend entity Data
	private void copyUpdateDataTobackendData(FormConfigurationCopy formConfigurationCopy,
			FormConfigurationBean formConfigurationBean) {

		formConfigurationCopy.setIsActive(formConfigurationBean.getIsActive());
		formConfigurationCopy.setIsMandetory(formConfigurationBean.getIsMandetory());
		formConfigurationCopy.setUpdatedBy(formConfigurationBean.getUpdatedBy());
		formConfigurationCopy.setUpdatedOn(new Date());
		formConfigurationCopy.setDeletedFlag(formConfigurationBean.getDeletedFlag());
		formConfigurationCopy.setDocumentName(formConfigurationBean.getDocumentName());
		formConfigurationCopy.setSize(formConfigurationBean.getSize());
		formConfigurationCopy.setFormat(formConfigurationBean.getFormat());
		formConfigurationCopy.setCheckerUpdatedBy(null);
		formConfigurationCopy.setCheckerUpdatedOn(null);
		formConfigurationCopy.setRemark(null);

		if (!formConfigurationBean.getTypeMaster().equals(formConfigurationCopy.getTypeMaster().getTypeId())) {
			TypeMaster typemaster = new TypeMaster();
			typemaster.setTypeId(formConfigurationBean.getTypeMaster());
			formConfigurationCopy.setTypeMaster(typemaster);
		}
	}

	/**
	 * @author pravat.behera
	 * @Date 26-03-2025 14:55
	 * @Description status 1 means pending at CEO status 2 means approval status 3
	 *              means reject
	 */
	@Override
	public FormConfigurationResponse updateFormConfiguration(FormConfigurationBean formConfigurationBean) {
		FormConfigurationResponse formConfigurationResponse = new FormConfigurationResponse();
		try {
			TypeMaster typemaster = new TypeMaster();
			typemaster.setTypeId(formConfigurationBean.getTypeMaster());

			// Check Duplicate
			long duplicateCount = this.repo.duplicateCheckDocumentNameInUpdate(formConfigurationBean.getDocumentName(),
					typemaster, formConfigurationBean.getId());
			if (duplicateCount > 0) {
				// Return with Duplicate Value
				formConfigurationResponse.setStatus("duplicate");
				formConfigurationResponse.setMessage("Duplicate document name found");
				formConfigurationResponse.setFormConfigurationBean(formConfigurationBean);
				this.logger.error("Duplicate document name found in updateFormConfiguration method");
				return formConfigurationResponse;
			}
			// Get Copy Table data
			FormConfigurationCopy formConfigurationCopy = this.copyRepo.findById(formConfigurationBean.getId())
					.orElseThrow(() -> new FormConfigurationException(
							"From Configuration Copy data not found !!" + formConfigurationBean.getId()));
			// Copy update to in DB
			this.copyUpdateDataTobackendData(formConfigurationCopy, formConfigurationBean);

			// For Update checker status will update in copy table and main table also not
			// required
			// formConfiguration.setCheckerStatus(1);//Main table status update

			// Update Copy table
			formConfigurationCopy.setCheckerStatus(1);
			// Status type 1 mean new record 2 means update or delete action
			formConfigurationCopy.setStatusType(2);

			formConfigurationCopy = this.copyRepo.save(formConfigurationCopy);
			BeanUtils.copyProperties(formConfigurationCopy, formConfigurationBean);
			logger.info(
					"updateFormConfiguration method for FormConfigurationServiceImpl Service for Form Configuration successfully updated");
			// Success message
			formConfigurationResponse.setStatus("success");
			formConfigurationResponse.setMessage("Successfully updated in  form configuration");
			formConfigurationResponse.setFormConfigurationBean(formConfigurationBean);
		} catch (Exception e) {
			this.logger.error(
					"Eception occuer in saveFormConfiguration service class updateFormConfiguration method {}", e);
			// Return with Exception message Value
			formConfigurationResponse.setStatus("exception");
			formConfigurationResponse.setMessage("Excpetion found in " + e.getMessage());
			formConfigurationResponse.setFormConfigurationBean(formConfigurationBean);
		}
		return formConfigurationResponse;
	}

	/**
	 * @author pravat.behera
	 * @Date 24-03-2025 16:38 PM
	 */
	@Override
	public List<FormConfigurationBean> allFormConfiguration() {
		logger.info("Inside FormConfigurationServiceImpl Service for allFormConfiguration method get all data");
		List<FormConfiguration> listData = this.repo.findAll();
		return listData.stream().filter(formConf -> !formConf.getDeletedFlag())
				.sorted(Comparator.comparing((FormConfiguration formConfig) -> formConfig.getTypeMaster().getType())
						.thenComparing(formConfig -> formConfig.getDocumentName()))
				.map(formConfig -> new FormConfigurationBean(formConfig.getId(), formConfig.getTypeMaster().getTypeId(),
						formConfig.getFormat(), formConfig.getSize(), formConfig.getDocumentName(),
						formConfig.getIsMandetory(), formConfig.getIsActive(), formConfig.getFinalStatus(),
						formConfig.getCreatedBy(), formConfig.getCreatedOn(), formConfig.getUpdatedBy(),
						formConfig.getUpdatedOn(), formConfig.getDeletedFlag(), formConfig.getCheckerStatus(),
						formConfig.getCheckerUpdatedBy(), formConfig.getCheckerUpdatedOn(),
						formConfig.getTypeMaster().getType(),
						formConfig.getFinalStatus() == 0 || formConfig.getFinalStatus() == 1 ? "Pending"
								: formConfig.getFinalStatus() == 2 ? "Approved" : "Reject",
						formConfig.getUserId(), formConfig.getStatusType()))
				.collect(Collectors.toList());
	}

	/**
	 * @author pravat.behera
	 * @Date 24-03-2025 16:38 PM
	 */
	@Override
	public List<FormConfigurationBean> allFormConfigurationWithoutDelete() {
		logger.info("Inside FormConfigurationServiceImpl Service for allFormConfiguration method get all data");
		List<FormConfiguration> listData = this.repo.findAll();
		return listData.stream()
				.sorted(Comparator.comparing((FormConfiguration formConfig) -> formConfig.getTypeMaster().getType())
						// .reversed() // Reverse the sorting for 'type'
						.thenComparing(formConfig -> formConfig.getDocumentName()))
				// .reversed()) // Reverse the sorting for 'documentName'

				.map(formConfig -> new FormConfigurationBean(formConfig.getId(), formConfig.getTypeMaster().getTypeId(),
						formConfig.getFormat(), formConfig.getSize(), formConfig.getDocumentName(),
						formConfig.getIsMandetory(), formConfig.getIsActive(), formConfig.getFinalStatus(),
						formConfig.getCreatedBy(), formConfig.getCreatedOn(), formConfig.getUpdatedBy(),
						formConfig.getUpdatedOn(), formConfig.getDeletedFlag(), formConfig.getCheckerStatus(),
						formConfig.getCheckerUpdatedBy(), formConfig.getCheckerUpdatedOn(),
						formConfig.getTypeMaster().getType(),
						formConfig.getFinalStatus() == 0 || formConfig.getFinalStatus() == 1 ? "Pending"
								: formConfig.getFinalStatus() == 2 ? "Approved" : "Reject",
						formConfig.getUserId(), formConfig.getStatusType()))
				.collect(Collectors.toList());
	}

	/**
	 * @author pravat.behera
	 * @Date 24-03-2025 16:38 PM
	 */
	private FormConfigurationBean copyDbToUserUse(FormConfiguration formConfig) {
		return new FormConfigurationBean(formConfig.getId(), formConfig.getTypeMaster().getTypeId(),
				formConfig.getFormat(), formConfig.getSize(), formConfig.getDocumentName(), formConfig.getIsMandetory(),
				formConfig.getIsActive(), formConfig.getFinalStatus(), formConfig.getCreatedBy(),
				formConfig.getCreatedOn(), formConfig.getUpdatedBy(), formConfig.getUpdatedOn(),
				formConfig.getDeletedFlag(), formConfig.getCheckerStatus(), formConfig.getCheckerUpdatedBy(),
				formConfig.getCheckerUpdatedOn(), formConfig.getTypeMaster().getType(),
				formConfig.getFinalStatus() == 0 || formConfig.getFinalStatus() == 1 ? "Pending"
						: formConfig.getFinalStatus() == 2 ? "Approved" : "Reject",
				formConfig.getUserId(), formConfig.getStatusType());
	}

	/**
	 * @author pravat.behera
	 * @Date 24-03-2025 16:38 PM
	 */
	private FormConfigurationBean copyDbToUserUseCopy(FormConfigurationCopy formConfig) {
		return new FormConfigurationBean(formConfig.getId(), formConfig.getTypeMaster().getTypeId(),
				formConfig.getFormat(), formConfig.getSize(), formConfig.getDocumentName(), formConfig.getIsMandetory(),
				formConfig.getIsActive(), formConfig.getCheckerStatus(), formConfig.getCreatedBy(),
				formConfig.getCreatedOn(), formConfig.getUpdatedBy(), formConfig.getUpdatedOn(),
				formConfig.getDeletedFlag(), formConfig.getCheckerStatus(), formConfig.getCheckerUpdatedBy(),
				formConfig.getCheckerUpdatedOn(), formConfig.getTypeMaster().getType(),
				formConfig.getCheckerStatus() == 0 || formConfig.getCheckerStatus() == 1 ? "Pending"
						: formConfig.getCheckerStatus() == 2 ? "Approved" : "Reject",
				formConfig.getUserId(), formConfig.getStatusType());
	}

	/**
	 * @author pravat.behera
	 * @Date 24-03-2025 16:38 PM
	 */
	@Override
	public FormConfigurationBean getFormConfiguration(Integer formDocId) {
		logger.info(
				"Inside FormConfigurationServiceImpl Service for getFormConfiguration method get Form Configuration Data");
		FormConfiguration formConfiguration = repo.findById(formDocId)
				.orElseThrow(() -> new FormConfigurationException("From Configuration data not found !!" + formDocId));
		FormConfigurationBean response = new FormConfigurationBean(formConfiguration.getId(),
				formConfiguration.getTypeMaster().getTypeId(), formConfiguration.getFormat(),
				formConfiguration.getSize(), formConfiguration.getDocumentName(), formConfiguration.getIsMandetory(),
				formConfiguration.getIsActive(), formConfiguration.getFinalStatus(), formConfiguration.getCreatedBy(),
				formConfiguration.getCreatedOn(), formConfiguration.getUpdatedBy(), formConfiguration.getUpdatedOn(),
				formConfiguration.getDeletedFlag(), formConfiguration.getCheckerStatus(),
				formConfiguration.getCheckerUpdatedBy(), formConfiguration.getCheckerUpdatedOn(),
				formConfiguration.getTypeMaster().getType(), // Setting type name from TypeMaster
				formConfiguration.getFinalStatus() == 0 || formConfiguration.getFinalStatus() == 1 ? "Pending"
						: formConfiguration.getFinalStatus() == 2 ? "Approved" : "Reject",
				formConfiguration.getUserId(), formConfiguration.getStatusType());
		return response;
	}

	/**
	 * @author pravat.behera
	 * @Date 2024-03-2025 15:42 checker status 1 for pending checker status 2 for
	 *       approval checker status 3 for reject
	 */
	@Override
	public FormConfigurationBean deleteFormConfiguration(Integer formDocId, String updatedBy) {
		logger.info(
				"Inside FormConfigurationServiceImpl Service for getFormConfiguration method get Form Configuration Data");
		FormConfigurationCopy formConfigurationCopy = this.copyRepo.findById(formDocId)
				.orElseThrow(() -> new FormConfigurationException("From Configuration data not found !!" + formDocId));

		formConfigurationCopy.setDeletedFlag(true);
		formConfigurationCopy.setUpdatedBy(updatedBy);
		formConfigurationCopy.setUpdatedOn(new Date());
		// Status type 1 mean new record 2 means update or delete action
		formConfigurationCopy.setStatusType(2);
		// Need to Update Checker Status for approval
		formConfigurationCopy.setCheckerStatus(1);

		formConfigurationCopy = this.copyRepo.save(formConfigurationCopy);

		return copyDbToUserUseCopy(formConfigurationCopy);
	}

	/**
	 * @author pravat.behera
	 * @Date 26-03-2025 15:36
	 * 
	 */
	@Override
	public FormConfigurationCopyBean getFormConfigurationCopy(Integer formDocId) {
		logger.info(
				"Inside FormConfigurationServiceImpl Service for getFormConfigurationCopy method get Form Configuration Data");
		FormConfigurationCopy formConfiguration = this.copyRepo.findById(formDocId)
				.orElseThrow(() -> new FormConfigurationException("From Configuration data not found !!" + formDocId));

		FormConfigurationCopyBean response = new FormConfigurationCopyBean(formConfiguration.getId(),
				formConfiguration.getTypeMaster().getTypeId(), formConfiguration.getFormat(),
				formConfiguration.getSize(), formConfiguration.getDocumentName(), formConfiguration.getIsMandetory(),
				formConfiguration.getIsActive(), formConfiguration.getCheckerStatus(), formConfiguration.getCreatedBy(),
				formConfiguration.getCreatedOn(), formConfiguration.getUpdatedBy(), formConfiguration.getUpdatedOn(),
				formConfiguration.getDeletedFlag(), formConfiguration.getCheckerStatus(),
				formConfiguration.getCheckerUpdatedBy(), formConfiguration.getCheckerUpdatedOn(),
				formConfiguration.getTypeMaster().getType(),
				formConfiguration.getCheckerStatus() == 0 || formConfiguration.getCheckerStatus() == 1 ? "Pending"
						: formConfiguration.getCheckerStatus() == 2 ? "Approved" : "Reject",
				formConfiguration.getUserId(), formConfiguration.getIdCopy(), formConfiguration.getStatusType(),
				formConfiguration.getRemark());
		return response;
	}

	/**
	 * @author pravat.behera
	 * @Date 26-03-2025 15:39
	 * @Approval Checker
	 * @Description when CEO approval change field Then All Copy Field Data will
	 *              move to main table and copy table checker status will updated in
	 *              approval state and main table final status will updated in
	 *              approval state and checker status approval
	 * 
	 * 
	 *              Insert remark field in copy table
	 */
	private void copyfromCopytableToMainTable(FormConfigurationCopy formConfigurationCopy,
			FormConfiguration formConfiguration) {

		formConfiguration.setCheckerUpdatedBy(formConfigurationCopy.getCheckerUpdatedBy());
		formConfiguration.setDeletedFlag(formConfigurationCopy.getDeletedFlag());
		formConfiguration.setDocumentName(formConfigurationCopy.getDocumentName());
		formConfiguration.setFormat(formConfigurationCopy.getFormat());
		formConfiguration.setSize(formConfigurationCopy.getSize());
		formConfiguration.setIsActive(formConfigurationCopy.getIsActive());
		formConfiguration.setIsMandetory(formConfigurationCopy.getIsMandetory());
		formConfiguration.setTypeMaster(formConfigurationCopy.getTypeMaster());
		formConfiguration.setCheckerUpdatedOn(formConfigurationCopy.getCheckerUpdatedOn());
		formConfiguration.setStatusType(formConfigurationCopy.getStatusType());
		formConfiguration.setRemark(formConfigurationCopy.getRemark());
		formConfiguration.setUpdatedBy(formConfigurationCopy.getUpdatedBy());
		formConfiguration.setUpdatedOn(formConfigurationCopy.getUpdatedOn());
	}

	private void copyfromMaintableToCopyTable(FormConfigurationCopy formConfiguration,
			FormConfiguration formConfigurationCopy) {

		formConfiguration.setCheckerUpdatedBy(formConfigurationCopy.getCheckerUpdatedBy());
		formConfiguration.setDeletedFlag(formConfigurationCopy.getDeletedFlag());
		formConfiguration.setDocumentName(formConfigurationCopy.getDocumentName());
		formConfiguration.setFormat(formConfigurationCopy.getFormat());
		formConfiguration.setSize(formConfigurationCopy.getSize());
		formConfiguration.setIsActive(formConfigurationCopy.getIsActive());
		formConfiguration.setIsMandetory(formConfigurationCopy.getIsMandetory());
		formConfiguration.setTypeMaster(formConfigurationCopy.getTypeMaster());
		formConfiguration.setCheckerUpdatedOn(formConfigurationCopy.getCheckerUpdatedOn());
		formConfiguration.setCheckerUpdatedBy(formConfigurationCopy.getCheckerUpdatedBy());
		formConfiguration.setRemark(formConfigurationCopy.getRemark());
	}

	@Override
	public FormConfigurationCopyBean approvalChecker(CheckerRequestBean checkerRequestBean) {
		logger.info("Inside FormConfigurationServiceImpl Service for approvalChecker method for Approval checker");

		FormConfigurationCopy formConfigurationCopy = this.getCopyTableUpdateData(checkerRequestBean);
		formConfigurationCopy.setCheckerStatus(2);
		FormConfiguration formConfiguration = this.getMainTableUpdateData(checkerRequestBean, formConfigurationCopy);
		// Approval state
		formConfiguration.setCheckerStatus(2);
		formConfiguration.setFinalStatus(2);
		formConfigurationCopy = this.copyRepo.save(formConfigurationCopy);
		formConfiguration = this.repo.save(formConfiguration);
		FormConfigurationCopyBean formConfigurationCopyBean = new FormConfigurationCopyBean();
		BeanUtils.copyProperties(formConfigurationCopy, formConfigurationCopyBean);
		logger.info(
				"Inside FormConfigurationServiceImpl Service for approvalChecker method for successfully Approval checker");
		return formConfigurationCopyBean;
	}

	private FormConfigurationCopy getCopyTableUpdateData(CheckerRequestBean checkerRequestBean) {
		FormConfigurationCopy formConfigurationCopy = this.copyRepo.findById(checkerRequestBean.getFormId())
				.orElseThrow(() -> new FormConfigurationException(
						"From Configuration Copy data not found !!" + checkerRequestBean.getFormId()));
		// Set Value updated value
		formConfigurationCopy.setRemark(checkerRequestBean.getRemark());
		formConfigurationCopy.setCheckerUpdatedBy(checkerRequestBean.getCreatedBy());
		formConfigurationCopy.setCheckerUpdatedOn(new Date());
		formConfigurationCopy.setUserId(checkerRequestBean.getUserId());

		return formConfigurationCopy;
	}

	private FormConfiguration getMainTableUpdateData(CheckerRequestBean checkerRequestBean,
			FormConfigurationCopy formConfigurationCopy) {
		FormConfiguration formConfiguration = this.repo.findById(checkerRequestBean.getFormId())
				.orElseThrow(() -> new FormConfigurationException(
						"From Configuration Copy data not found !!" + checkerRequestBean.getFormId()));

		// Copy copy to main table
		this.copyfromCopytableToMainTable(formConfigurationCopy, formConfiguration);
		return formConfiguration;
	}

	@Override
	public FormConfigurationCopyBean rejectChecker(CheckerRequestBean checkerRequestBean) {
		logger.info("Inside FormConfigurationServiceImpl Service for rejectChecker method for Reject checker");
		FormConfigurationCopy formConfigurationCopy = this.getCopyTableUpdateData(checkerRequestBean);
		formConfigurationCopy.setCheckerStatus(3);

		FormConfiguration formConfiguration = this.repo.findById(checkerRequestBean.getFormId())
				.orElseThrow(() -> new FormConfigurationException(
						"From Configuration Copy data not found !!" + checkerRequestBean.getFormId()));

		// Copy copy to main table status type 1 means new record insert that means only
		// main table and copy table final status will changes to reject
		this.copyfromMaintableToCopyTable(formConfigurationCopy, formConfiguration);
		// Mean New Record rejected
		if (checkerRequestBean.getStatusType() == 1) {
			formConfiguration.setFinalStatus(3);

			formConfiguration.setRemark(checkerRequestBean.getRemark());
			formConfiguration.setCheckerUpdatedBy(checkerRequestBean.getCreatedBy());
			formConfiguration.setCheckerUpdatedOn(new Date());
			formConfiguration.setUserId(checkerRequestBean.getUserId());

			formConfiguration = this.repo.save(formConfiguration);
		}
		formConfigurationCopy = this.copyRepo.save(formConfigurationCopy);

		FormConfigurationCopyBean formConfigurationCopyBean = new FormConfigurationCopyBean();
		BeanUtils.copyProperties(formConfigurationCopy, formConfigurationCopyBean);

		logger.info(
				"Inside FormConfigurationServiceImpl Service for rejectChecker method for successfully reject checker");
		return formConfigurationCopyBean;
	}

	public List<FormConfigurationActualResponse> getAllFormConfigurationByFormType(Integer typeId) {
		logger.info(
				"Inside FormConfigurationServiceImpl Service for getAllFormConfigurationByFormType method get all data");
		TypeMaster typeMaster = new TypeMaster();
		typeMaster.setTypeId(typeId);
		List<FormConfiguration> listData = this.repo.findByTypeMasterAndApprova(typeMaster);
		return listData.stream()
				.sorted(Comparator.comparing((FormConfiguration formConfig) -> formConfig.getTypeMaster().getType())
						.thenComparing(formConfig -> formConfig.getDocumentName()))
				.map(formConfig -> new FormConfigurationActualResponse(formConfig.getId(),
						formConfig.getTypeMaster().getTypeId(), formConfig.getFormat(), formConfig.getSize(),
						formConfig.getDocumentName(), formConfig.getIsMandetory(), formConfig.getIsActive(),
						formConfig.getFinalStatus(), formConfig.getTypeMaster().getType()))
				.collect(Collectors.toList());
	}

}
