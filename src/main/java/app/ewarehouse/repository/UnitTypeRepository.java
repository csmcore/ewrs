package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.UnitType;

@Repository
public interface UnitTypeRepository extends JpaRepository<UnitType, Integer> {

}
