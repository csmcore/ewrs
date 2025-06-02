package app.ewarehouse.master.service;

import java.util.List;
import java.util.Map;

import app.ewarehouse.master.dto.TypeMasterBean;

public interface ITypeMasterService {

	public TypeMasterBean saveTypeMaster(TypeMasterBean typeMasterBean);
	
	public List<TypeMasterBean> allTypeMaster();
	
	/**
	 * @Description this is used in form configuration for dropdown list
	 * @return return list all active master data 
	 */
	public List<Map<Integer,String>> getDropDownTypeMaster();

	
}
