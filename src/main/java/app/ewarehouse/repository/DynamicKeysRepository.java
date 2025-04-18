package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.DynamicKeys;

@Repository
public interface DynamicKeysRepository extends JpaRepository<DynamicKeys, Integer> {

	List<DynamicKeys> findByBitDeletedFlag(boolean b);

	
}
