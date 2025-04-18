package app.ewarehouse.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.ewarehouse.entity.CommodityType;
import app.ewarehouse.repository.CommodityTypeRepository;
import app.ewarehouse.service.CommodityTypeService;

@Service
public class CommodityTypeServiceImpl implements CommodityTypeService {
	@Autowired
	private CommodityTypeRepository commodityTypeRepository;
	
	@Override
	public boolean save(CommodityType commodityType) {
	        try {
	            commodityTypeRepository.save(commodityType);
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }

	@Override
	public List<CommodityType> getAll() {
		return commodityTypeRepository.findAll();
	}

	@Override
	public CommodityType getById(Integer id) {
		java.util.Optional<CommodityType> optionalCommodityType = commodityTypeRepository.findById(id);
        return optionalCommodityType.orElse(null);
	}

}
