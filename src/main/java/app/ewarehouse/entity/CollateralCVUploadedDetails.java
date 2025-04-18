package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "t_uploadedcv_details")
public class CollateralCVUploadedDetails {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "cvId")
	    private Integer cvId;

	    @ManyToOne
	    @JoinColumn(name = "vchapplicationId")
	    private ApplicationCertificateOfCollateral applicationId;

	    @Column(name = "vchTechnicalStaffName", length = 200, nullable = false)
	    private String technicalStaffName;

	    @Column(name = "vchUploadCvPath", length = 255, nullable = false)
	    private String uploadCvPath;

	    @Column(name = "intCreatedBy", nullable = false)
	    private Integer createdBy;

	    @Column(name = "intUpdatedBy")
	    private Integer updatedBy;

	    @Column(name = "dtmCreatedOn", nullable = false, updatable = false)
	    @Temporal(TemporalType.TIMESTAMP)
	    @CreationTimestamp
	    private Date createdOn;

	    @Column(name = "dtmUpdatedOn")
	    @Temporal(TemporalType.TIMESTAMP)
	    @UpdateTimestamp
	    private Date updatedOn;

	    @Column(name = "bitDeletedFlag")
	    private Boolean deletedFlag = false;
}
