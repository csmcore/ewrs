package app.ewarehouse.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.ewarehouse.dto.WarehouseUserEmpTypeDTO;
import app.ewarehouse.entity.WarehouseUserEmpType;
import app.ewarehouse.repository.WarehouseUserEmpTypeRepository;
import app.ewarehouse.service.WarehouseUserEmpTypeService;

@Service
public class WarehouseUserEmpTypeServiceImpl implements WarehouseUserEmpTypeService {

	private WarehouseUserEmpTypeRepository repository;

	public WarehouseUserEmpTypeServiceImpl(WarehouseUserEmpTypeRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public List<WarehouseUserEmpTypeDTO> getByRoleId(Integer roleId) {
        List<WarehouseUserEmpType> entities = repository.findByRoleIdAndDeletedFlagFalse(roleId);
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
	
	private WarehouseUserEmpTypeDTO convertToDTO(WarehouseUserEmpType entity) {
        WarehouseUserEmpTypeDTO dto = new WarehouseUserEmpTypeDTO();
        dto.setWarehouseEmpTypeId(entity.getWarehouseEmpTypeId());
        dto.setWarehouseEmpTypeName(entity.getWarehouseEmpTypeName());
        return dto;
    }

}
