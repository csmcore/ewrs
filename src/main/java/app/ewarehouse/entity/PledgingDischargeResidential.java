package app.ewarehouse.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_pledging_discharge_residential")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PledgingDischargeResidential {

	@Id
	@GeneratedValue(generator = "pledging_discharge_residential_id_gen")
	@GenericGenerator(name = "pledging_discharge_residential_id_gen", type = app.ewarehouse.util.CustomIdGenerator.class, parameters = {
			@org.hibernate.annotations.Parameter(name = "tableName", value = "t_pledging_discharge_residential"),
			@org.hibernate.annotations.Parameter(name = "idName", value = "vchPdResidentialId") })
    @Column(name = "vchPdResidentialId", length = 20, nullable = false)
    private String pdResidentialId;

	@ManyToOne
    @JoinColumn(name = "pledging_discharge_receipt_id", referencedColumnName = "pledging_discharge_receipt_id", nullable = true)
    private PledgingDischargeDepositorWarehouse pledgingDischargeDepositorWarehouse;

    @Column(name = "area", length = 100)
    private String area;

    @Column(name = "plot_no", length = 100)
    private String plotNo;

    @Column(name = "street_name", length = 100)
    private String streetName;

    @Column(name = "length_of_stay_at_present_address", length = 100)
    private String lengthOfStayAtPresentAddress;

    @Column(name = "nearest_market", length = 50)
    private String nearestMarket;

    @Column(name = "current_police_station", length = 50)
    private String currentPoliceStation;

    @Column(name = "years_of_stay")
    private Integer yearsOfStay;

    @Column(name = "gender", length = 45)
    private String gender;

    @Column(name = "marital_status", length = 45)
    private String maritalStatus;

    @Column(name = "religion", length = 45)
    private String religion;

    @Column(name = "is_employed", length = 10)
    private String isEmployed;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "dtmCreatedOn", nullable = true, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmUpdatedOn", nullable = true, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;

    @Column(name = "bitDraftStatus")
    private Boolean draftStatus = false;
    
    @OneToOne(mappedBy = "pledgingDischargeResidential", cascade = CascadeType.ALL)
    @JsonManagedReference
    private EmploymentDetails employmentDetails;
}
