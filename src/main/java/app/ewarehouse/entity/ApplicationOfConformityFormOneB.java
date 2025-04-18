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
@Table(name = "t_application_of_conformity_form_one_b")
public class ApplicationOfConformityFormOneB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intFormOnebDocId")
    private Long formOneBDocId;
    
    @ManyToOne
    @JoinColumn(name = "intWhLocationId", nullable = false)
    private ApplicationOfConformityLocationDetails warehouseLocation;

    @ManyToOne
    @JoinColumn(name = "vchCompanyId", nullable = false)
    private AocCompProfileDetails company;

    @Column(name = "vchStrategicPlan", length = 255)
    private String strategicPlan;

    @Column(name = "vchLeaseAgreement", length = 255)
    private String leaseAgreement;

    @Column(name = "vchTitleDeed", length = 255)
    private String titleDeed;

    @Column(name = "vchAssetRegister", length = 255)
    private String assetRegister;

    @Column(name = "vchRecommendationLetters", length = 255)
    private String recommendationLetters;

    @Column(name = "vchAuditedFinancialReports", length = 255)
    private String auditedFinancialReports;

    @Column(name = "vchRiskAnalysis", length = 255)
    private String riskAnalysis;

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
    
}
