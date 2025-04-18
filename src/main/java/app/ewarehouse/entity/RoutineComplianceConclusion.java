package app.ewarehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_routine_com_inpector_conclusion")
public class RoutineComplianceConclusion {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "conclusion_id")
	    private Long conclusionId;
	 	
	    @Column(name  = "int_location_id")
	    private Long  intLocationId;

	    @Column(name = "vch_conclusion", nullable = false)
	    private String vchOtherImprovement;

	    @Column(name = "bit_deleted_flag", columnDefinition = "BOOLEAN DEFAULT FALSE")
	    private Boolean bitDeletedFlag = false;

}
