package app.ewarehouse.entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_operator_license_form_one_c")
public class OperatorLicenseFormOneC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intFormOneCId")
    private Long formOneCId;

    @ManyToOne
    @JoinColumn(name = "intWhLocationId", nullable = false)
    private ApplicationOfConformityLocationDetails warehouseLocation;

    @ManyToOne
    @JoinColumn(name = "vchCompanyId", nullable = false)
    private AocCompProfileDetails company;

    @Column(name = "vchCopyOfId", length = 255)
    private String copyOfId;

    @Column(name = "vchCopyOfInsurancePolicy", length = 255)
    private String copyOfInsurancePolicy;

    @Column(name = "vchProofOfPaymentOfPerformanceBond", length = 255)
    private String proofOfPaymentOfPerformanceBond;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn", columnDefinition = "DATETIME")
    private LocalDateTime createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;

    @Column(name = "bitDraftStatus")
    private Boolean draftStatus = true;
}

