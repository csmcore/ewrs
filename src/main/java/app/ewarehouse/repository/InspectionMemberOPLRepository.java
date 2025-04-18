package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.InspectionMemberOPL;

@Repository
public interface InspectionMemberOPLRepository extends JpaRepository<InspectionMemberOPL, Integer> {
	
}