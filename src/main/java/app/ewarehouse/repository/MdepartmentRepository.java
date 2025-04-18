package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Mdepartment;
@Repository
public interface MdepartmentRepository extends JpaRepository<Mdepartment, Integer> {
Mdepartment findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

@Query("From Mdepartment where bitDeletedFlag=:bitDeletedFlag ORDER BY intId DESC")
List<Mdepartment> findAllByBitDeletedFlag(Boolean bitDeletedFlag);


@Query("Select count(*) from Mdepartment where txtDepartmentName=:txtDepartmentName AND bitDeletedFlag=:bitDeletedFlag AND intId !=:intId ")
Integer getCountByDeptName(Integer intId,String txtDepartmentName, boolean bitDeletedFlag);
@Query("Select count(*) from Mdepartment where txtAliasName=:txtAliasName AND bitDeletedFlag=:bitDeletedFlag AND intId !=:intId ")
Integer getCountByAliasName(Integer intId,String txtAliasName, boolean bitDeletedFlag);
@Query("Select count(*) From Mdepartment where txtDepartmentName=:txtDepartmentName AND bitDeletedFlag=:bitDeletedFlag")
Integer countByTxtDepartmentNameANDBitDeletedFlag(String txtDepartmentName, Boolean bitDeletedFlag);
@Query("Select count(*) From Mdepartment where txtAliasName=:txtAliasName AND bitDeletedFlag=:bitDeletedFlag")
Integer countByTxtAliasNameANDBitDeletedFlag(String txtAliasName, Boolean bitDeletedFlag);
}
