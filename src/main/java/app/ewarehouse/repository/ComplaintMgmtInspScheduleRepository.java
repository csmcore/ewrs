package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintMgmtInspSchedule;


@Repository
public interface ComplaintMgmtInspScheduleRepository extends JpaRepository<ComplaintMgmtInspSchedule, Integer> {

	ComplaintMgmtInspSchedule findByComplaintId(Integer complaintId);
}
