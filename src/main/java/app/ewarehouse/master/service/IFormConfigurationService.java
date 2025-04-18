package app.ewarehouse.master.service;

import java.util.List;

import app.ewarehouse.master.dto.CheckerRequestBean;
import app.ewarehouse.master.dto.FormConfigurationActualResponse;
import app.ewarehouse.master.dto.FormConfigurationBean;
import app.ewarehouse.master.dto.FormConfigurationCopyBean;
import app.ewarehouse.master.dto.FormConfigurationResponse;

/**
 * @author pravat.behera
 * @Date 24-03-2025 
 */
public interface IFormConfigurationService {
	
	public FormConfigurationResponse saveFormConfiguration(FormConfigurationBean formConfigurationBean);

	public FormConfigurationResponse updateFormConfiguration(FormConfigurationBean formConfigurationBean);
	
	public List<FormConfigurationBean> allFormConfiguration();
	
	public List<FormConfigurationBean> allFormConfigurationWithoutDelete();
	
	public FormConfigurationBean getFormConfiguration(Integer formDocId);
	
	public FormConfigurationBean deleteFormConfiguration(Integer formDocId,String updatedBy);
	
	//Check will view using this method
	public List<FormConfigurationCopyBean>  getCheckerDataFromCopyTable();
	
	//Get Data Using form Id from Copy Id 
	public FormConfigurationCopyBean getFormConfigurationCopy(Integer formDocId);
	
	//Checker Approval 
	public FormConfigurationCopyBean approvalChecker(CheckerRequestBean checkerRequestBean );
	
	//Checker reject 
	public FormConfigurationCopyBean rejectChecker(CheckerRequestBean checkerRequestBean );
	
	//get final Approve form document by type id 
	public List<FormConfigurationActualResponse> getAllFormConfigurationByFormType(Integer typeId);
	
}
