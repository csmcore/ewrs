package app.ewarehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_routine_compliance_trail")
public class RoutineComplianceTrail {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rcTrailId;

    @Column(name = "warehouse_id", nullable = false)
    private Integer warehouseId;

    @Column(name = "action_taken_by", nullable = false)
    private Integer actionTakenBy;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate = LocalDateTime.now();

}
