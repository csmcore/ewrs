package app.ewarehouse.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
@Data
@Entity
@Table(name = "aoc_compliance_collarter_director_det")
public class AocComplianceCollarterDirector {

	@Id
    @Column(name = "vchDirectorId", length = 20)
    private String directorId;

    @ManyToOne
    @JoinColumn(name = "vchapplicationId")
    private ApplicationCertificateOfCollateral applicationId;

    @Column(name = "vchDirectorName", length = 50)
    private String directorName;

    @Column(name = "intNationalityId")
    private Integer nationalityId;

    @Column(name = "vchPassportNo", length = 20)
    private String passportNo;
    
    @Column(name = "vchPosition", length = 20)
    private String position;

    @Column(name = "vchWorkPermitPath", length = 255)
    private String workPermitPath;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "dtmCreatedOn", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmUpdatedOn")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;
}
