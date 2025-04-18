package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.EntityType;
@Repository
public interface EntityTypeRepository extends JpaRepository<EntityType, Long> {

	List<EntityType> findByBitDeletedFlagFalse();
}
