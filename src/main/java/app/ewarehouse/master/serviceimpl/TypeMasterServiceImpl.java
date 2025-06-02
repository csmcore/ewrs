package app.ewarehouse.master.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import app.ewarehouse.master.dto.TypeMasterBean;
import app.ewarehouse.master.entity.TypeMaster;
import app.ewarehouse.master.repository.ITypeMasterRepository;
import app.ewarehouse.master.service.ITypeMasterService;

/**
 * @author pravat.behera
 * @Date 24-03-2025 12:54 PM
 * @Description Type master service and business login implementation
 */
@Service
public class TypeMasterServiceImpl implements ITypeMasterService {
	private final ITypeMasterRepository repo;
	
	public TypeMasterServiceImpl(ITypeMasterRepository repo) {
		this.repo=repo;
	}

	@Override
	public TypeMasterBean saveTypeMaster(TypeMasterBean typeMasterBean) {
		TypeMaster typemaster=new TypeMaster();
		//Copy DTO to entity Class
		BeanUtils.copyProperties(typeMasterBean, typemaster);
		typemaster.setCreatedOn(new Date());
		typemaster=repo.save(typemaster);
		BeanUtils.copyProperties(typemaster,typeMasterBean);
		return typeMasterBean;
	}

	@Override
	public List<TypeMasterBean> allTypeMaster() {
		List<TypeMaster> list=repo.findAll();
		List<TypeMasterBean> response=
				list.stream()
				.filter(typeMaster->!typeMaster.getDeletedFlag())
				.map(typeMaster 
				->
		        new TypeMasterBean(
				typeMaster.getTypeId(),
				typeMaster.getType(),
				typeMaster.getDescription(),
				typeMaster.getCreatedBy(),
				typeMaster.getCreatedOn(),
				typeMaster.getUpdatedBy(),
				typeMaster.getUpdatedOn(),
				typeMaster.getDeletedFlag()
				))
				.collect(Collectors.toList());
		
		return response;
	}

	@Override
	public List<Map<Integer, String>> getDropDownTypeMaster() {
		List<TypeMaster> list=repo.findAll();
		return list.stream()
				.filter(typeMaster->!typeMaster.getDeletedFlag())
				.map(typeMaster 
				->Map.of(typeMaster.getTypeId(),typeMaster.getType())
		       )
				.collect(Collectors.toList());
	}

}
