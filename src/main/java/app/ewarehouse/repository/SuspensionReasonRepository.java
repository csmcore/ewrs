package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.SuspensionReason;

@Repository
public interface SuspensionReasonRepository extends JpaRepository<SuspensionReason, Integer> {
	SuspensionReason findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

	@Query("From SuspensionReason where bitDeletedFlag=:bitDeletedFlag ")
	List<SuspensionReason> findAllByBitDeletedFlagAndIntInsertStatus(Boolean bitDeletedFlag, Pageable pageRequest);

	Integer countByBitDeletedFlag(Boolean bitDeletedFlag);

	@Query("SELECT COUNT(1) FROM SuspensionReason s WHERE s.txtReason=:data")
	Integer countDuplicateData(String data);
}
