package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.CommodityType;

@Repository
public interface CommodityTypeRepository extends JpaRepository<CommodityType, Integer> {
    
	

}
