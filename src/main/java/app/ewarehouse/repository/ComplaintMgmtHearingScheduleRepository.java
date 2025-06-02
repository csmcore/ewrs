package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintMgmtHearingSchedule;


@Repository
public interface ComplaintMgmtHearingScheduleRepository extends JpaRepository<ComplaintMgmtHearingSchedule, Integer> {
	ComplaintMgmtHearingSchedule findByComplaintId(Integer complaintId);
}

