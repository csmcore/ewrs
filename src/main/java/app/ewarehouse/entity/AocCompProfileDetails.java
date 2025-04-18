package app.ewarehouse.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "aoc_comp_profile_det")
public class AocCompProfileDetails {
    @Id
    @Column(name = "vchProfDetId", length = 20)
    private String profDetId;

    @Column(name = "vchApplicantName", length = 50)
    private String applicantName;

    @Column(name = "vchMobileNumber", length = 15)
    private String mobileNumber;

    @Column(name = "vchEmail", length = 50)
    private String email;

    @Column(name = "vchPostalAddress", length = 150)
    private String postalAddress;

    @Column(name = "vchPostalCode", length = 10)
    private String postalCode;

    @Column(name = "vchTown", length = 50)
    private String town;
    
    @Column(name = "vchTelephoneNumber", length = 15)
    private String telephone;
    
    @Column(name = "vchWebsite", length = 50)
    private String website;
    
    @Column(name = "vchTypeOfEntity", length = 45)
    private String typeOfEntity;

    @Column(name = "vchCompany", length = 50)
    private String company;
    
    @Column(name = "vchCompanyRegdNo", length = 45)
    private String companyRegNo;

    @Column(name = "vchKraPin", length = 20)
    private String kraPin;

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
    
    @Column(name = "isEditOptionEnabled")
    private Boolean isEditOptionEnabled = true;
}

