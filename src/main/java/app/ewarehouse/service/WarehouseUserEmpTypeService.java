package app.ewarehouse.service;

import java.util.List;

import app.ewarehouse.dto.WarehouseUserEmpTypeDTO;

public interface WarehouseUserEmpTypeService {

	List<WarehouseUserEmpTypeDTO> getByRoleId(Integer roleId);

}
