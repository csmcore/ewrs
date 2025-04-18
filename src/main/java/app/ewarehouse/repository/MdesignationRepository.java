package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Mdesignation;
@Repository
public interface MdesignationRepository extends JpaRepository<Mdesignation, Integer> {
Mdesignation findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

@Query("From Mdesignation where bitDeletedFlag=:bitDeletedFlag ORDER BY intId DESC")
List<Mdesignation> findAllByBitDeletedFlag(Boolean bitDeletedFlag);
@Query("Select count(*) from Mdesignation where txtDesignationName=:txtDesignationName AND bitDeletedFlag=:bitDeletedFlag AND intId !=:intId ")
Integer getCountByDesgNameANDbitDeletedFlagNOTIntId(Integer intId,String txtDesignationName, boolean bitDeletedFlag);
@Query("Select count(*) from Mdesignation where txtAliasName=:txtAliasName AND bitDeletedFlag=:bitDeletedFlag AND intId !=:intId ")
Integer getCountByAliasNameANDbitDeletedFlagNOTIntId(Integer intId,String txtAliasName, boolean bitDeletedFlag);
@Query("Select count(*) From Mdesignation where txtDesignationName=:txtDesignationName AND bitDeletedFlag=:bitDeletedFlag")
Integer countByTxtDepartmentNameANDBitDeletedFlag(String txtDesignationName, Boolean bitDeletedFlag);
@Query("Select count(*) From Mdesignation where txtAliasName=:txtAliasName AND bitDeletedFlag=:bitDeletedFlag")
Integer countByTxtAliasNameANDBitDeletedFlag(String txtAliasName, Boolean bitDeletedFlag);
}
