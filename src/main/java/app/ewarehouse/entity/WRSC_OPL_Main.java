package app.ewarehouse.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_WRSC_OPL_Main")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WRSC_OPL_Main {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer intOplSno;
    
    @Column(nullable = false, length = 50)
    private String vchOplId;
    
    private Integer intOplAppSno;
    
    @Column(length = 50)
    private String vchOplAppId;
    
    private Integer intCreatedBy;
    
    private LocalDateTime dtmCreatedOn;
    
    private LocalDateTime stmUpdatedOn;
    
    private Boolean bitDeletedFlag;
    
    private Integer intCreatedRoleId;
    
    private String vchCertId;
    
    private Boolean bitCertGen;
    
    private LocalDate dtCertIssue;
    
    private LocalDate dtCnfApproval;
    
    private Integer intApprovedBy;
    
    private Integer intApprovedByRole;
    
    @Column(length = 255)
    private String vchApproverRemarks;  
    @Enumerated(EnumType.STRING)
    @Column(name = "vchApprovalStatus")
    private OperatorLicenceStatus vchApprovalStatus;
    
    private Boolean bitLicenseGen;
    
    private Boolean bitLicenceCertGen;
    
    @Column(length = 50)
    private String vchPaymentStatus;
}

