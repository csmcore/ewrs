package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.CeoCollarterTakeAction;


@Repository
public interface CEOTakeActionRepository extends JpaRepository<CeoCollarterTakeAction, Integer> {

}
