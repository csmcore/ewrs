package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Memployeetype;
@Repository
public interface MemployeetypeRepository extends JpaRepository<Memployeetype, Integer> {
Memployeetype findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

@Query("From Memployeetype where bitDeletedFlag=:bitDeletedFlag order by intId desc")
List<Memployeetype> findAllByBitDeletedFlag(Boolean bitDeletedFlag);

@Query("Select count(*) from Memployeetype where txtEmployeeType=:txtEmployeeType AND bitDeletedFlag=:bitDeletedFlag AND intId !=:intId ")
Integer getCountByEmployeeTypeANDbitDeletedFlagNOTIntId(Integer intId,String txtEmployeeType, boolean bitDeletedFlag);
@Query("Select count(*) from Memployeetype where txtAliasName=:txtAliasName AND bitDeletedFlag=:bitDeletedFlag AND intId !=:intId ")
Integer getCountByAliasNameANDbitDeletedFlagNOTIntId(Integer intId,String txtAliasName, boolean bitDeletedFlag);
@Query("Select count(*) From Memployeetype where txtEmployeeType=:txtEmployeeType AND bitDeletedFlag=:bitDeletedFlag")
Integer countByEmployeeTypeANDBitDeletedFlag(String txtEmployeeType, Boolean bitDeletedFlag);
@Query("Select count(*) From Memployeetype where txtAliasName=:txtAliasName AND bitDeletedFlag=:bitDeletedFlag")
Integer countByTxtAliasNameANDBitDeletedFlag(String txtAliasName, Boolean bitDeletedFlag);
}
