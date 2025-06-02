package app.ewarehouse.entity;

import java.util.Date;

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
@Table(name = "t_application_of_conformity_form_one_a")
public class ApplicationOfConformityFormOneA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intFormOneaDocId")
    private Long formOneADocId;

    @ManyToOne
    @JoinColumn(name = "vchCompanyId", nullable = false)
    private AocCompProfileDetails company;

    @Column(name = "vchBusinessPlan", length = 255)
    private String businessPlan;

    @Column(name = "vchCurrentTaxComplianceCertificate", length = 255)
    private String currentTaxComplianceCertificate;

    @Column(name = "vchAuthorizedSignatoryCertificateOfGoodConduct", length = 255)
    private String authorizedSignatoryCertificateOfGoodConduct;

    @Column(name = "vchKraPinOfTheAuthorizedSignatory", length = 255)
    private String kraPinOfTheAuthorizedSignatory;

    @Column(name = "vchNationalIdOfTheAuthorizedSignatory", length = 255)
    private String nationalIdOfTheAuthorizedSignatory;

    @Column(name = "vchInsurancePolicy", length = 255)
    private String insurancePolicy;

    @Column(name = "vchValidLicenseForTheWarehouse", length = 255)
    private String validLicenseForTheWarehouse;

    @Column(name = "vchCertificateOfIncorporationRegistration", length = 255)
    private String certificateOfIncorporationRegistration;

    @Column(name = "vchLatestCR12", length = 255)
    private String latestCR12;

    @Column(name = "vchLatestCertificateOfCompliance", length = 255)
    private String latestCertificateOfCompliance;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private Date createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn")
    private Date updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;

    @Column(name = "bitDraftStatus")
    private Boolean draftStatus = true;
    
    @ManyToOne
    @JoinColumn(name = "intWhLocationId", nullable = false)
    private ApplicationOfConformityLocationDetails warehouseLocation;

}
