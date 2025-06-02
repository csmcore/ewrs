package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.ewarehouse.entity.TempUser;


public interface TempUserRepository extends JpaRepository<TempUser, Integer> {

	TempUser findByEmail(String email);
    

	@Query(value = "select vchCompanyName,vchWarehouseName  from m_company_warehouse_details where vchWarehouseId =:email and bitDeletedFlag=0",nativeQuery=true)

//	@Query(value = "SELECT vchCompanyName, vchWarehouseName,vchWarehouseId,vchCompanyId \r\n"
//			+ "            FROM m_company_warehouse_details  \r\n"
//			//+ "            INNER JOIN m_temp_user tu \r\n"
//			//+ "            ON w.vchWarehouseId = tu.warehouse_id\r\n"
//			+ "            WHERE vchWarehouseId =:email \r\n"
//			+ "            AND bitDeletedFlag = 0", 
//
//    nativeQuery = true)
	List<Object[]> findCompanyAndWarehouseByEmail(@Param("email") String email);
	
	//	List<Object[]> findCompanyAndWarehouseByEmail(@Param("email") String email);



}
