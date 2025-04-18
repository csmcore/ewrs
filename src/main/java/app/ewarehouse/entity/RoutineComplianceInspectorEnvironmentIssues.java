package app.ewarehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_rout_com_env_issues")
public class RoutineComplianceInspectorEnvironmentIssues {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_environment_issues_id")
    private Long intEnvironmentIssuesId;

    @Column(name = "bit_stored_hazards")
    private Boolean bitStoredHazards;

    @Column(name = "vch_stored_hazards_comments")
    private String vchStoredHazardsComments;

    @Column(name = "bit_environment_concerns")
    private Boolean bitEnvironmentConcerns;

    @Column(name = "vch_environment_concerns_comments")
    private String vchEnvironmentConcernsComments;

    @Column(name = "bit_earthquake_exposure")
    private Boolean bitEarthquakeExposure;

    @Column(name = "bit_flooding_exposure")
    private Boolean bitFloodingExposure;

    @Column(name = "bit_landslide_exposure")
    private Boolean bitLandslideExposure;

}
