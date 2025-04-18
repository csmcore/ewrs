package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.ewarehouse.entity.Complaints;

public interface ComplaintsRepository extends JpaRepository<Complaints, String>{

    @Query("from Complaints c ORDER BY c.dtmCreatedAt desc")
    List<Complaints> findByBitDeleteFlag(boolean b);
}
