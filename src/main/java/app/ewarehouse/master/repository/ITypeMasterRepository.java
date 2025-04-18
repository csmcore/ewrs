package app.ewarehouse.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.ewarehouse.master.entity.TypeMaster;

public interface ITypeMasterRepository extends JpaRepository<TypeMaster, Integer> {

}
