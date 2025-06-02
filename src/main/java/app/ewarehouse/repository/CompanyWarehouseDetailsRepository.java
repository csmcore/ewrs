package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.CompanyWarehouseDetails;

@Repository
public interface CompanyWarehouseDetailsRepository extends JpaRepository<CompanyWarehouseDetails, Integer> {

	List<CompanyWarehouseDetails> findByDeletedFlagFalse();

}
