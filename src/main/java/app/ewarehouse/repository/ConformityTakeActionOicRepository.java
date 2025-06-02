package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ConformityTakeActionOic;

@Repository
public interface ConformityTakeActionOicRepository extends JpaRepository<ConformityTakeActionOic, Integer>{

	ConformityTakeActionOic findByConformityId(Integer conformityId);
}
