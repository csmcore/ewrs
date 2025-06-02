package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.StatusMaster;

@Repository
public interface StatusMasterRepository extends JpaRepository<StatusMaster, Integer> {

}
