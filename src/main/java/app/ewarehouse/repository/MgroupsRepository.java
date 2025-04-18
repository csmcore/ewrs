package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Mgroups;
@Repository
public interface MgroupsRepository extends JpaRepository<Mgroups, Integer> {
Mgroups findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

@Query("From Mgroups where bitDeletedFlag=:bitDeletedFlag ORDER BY intId DESC")
List<Mgroups> findAllByBitDeletedFlag(Boolean bitDeletedFlag);

@Query("Select count(*) from Mgroups where txtGroupName=:txtGroupName AND bitDeletedFlag=:bitDeletedFlag AND intId !=:intId ")
Integer getCountByGroupNameANDbitDeletedFlagNOTIntId(Integer intId,String txtGroupName, boolean bitDeletedFlag);
@Query("Select count(*) from Mgroups where txtAliasName=:txtAliasName AND bitDeletedFlag=:bitDeletedFlag AND intId !=:intId ")
Integer getCountByAliasNameANDbitDeletedFlagNOTIntId(Integer intId,String txtAliasName, boolean bitDeletedFlag);
@Query("Select count(*) From Mgroups where txtGroupName=:txtGroupName AND bitDeletedFlag=:bitDeletedFlag")
Integer countByGroupNameANDBitDeletedFlag(String txtGroupName, Boolean bitDeletedFlag);
@Query("Select count(*) From Mgroups where txtAliasName=:txtAliasName AND bitDeletedFlag=:bitDeletedFlag")
Integer countByTxtAliasNameANDBitDeletedFlag(String txtAliasName, Boolean bitDeletedFlag);
}
