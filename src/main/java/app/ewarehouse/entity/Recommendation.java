package app.ewarehouse.entity;

import java.time.LocalDate;

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
@Table(name = "t_routine_com_inpector_recommendation")
public class Recommendation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    private Long recommendationId;

    @Column(name = "vch_severity", nullable = false)
    private String vchSeverity;

    @Column(name = "int_severity_id", nullable = false)
    private Integer intSeverityId;

    @Column(name = "vch_recommendation_date", nullable = false)
    private String vchRecommendationDate;

    @Column(name  = "int_location_id")
    private Long  intLocationId;

    @Column(name = "bit_deleted_flag", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean bitDeletedFlag = false;

}
