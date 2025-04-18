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
@Table(name = "aoc_comp_prof_sign_seal")
public class AocCompProfSignSeal {
    @Id
    @Column(name = "vchSignSealId", length = 20)
    private String signSealId;

    @ManyToOne
    @JoinColumn(name = "vchProfDetId")
    private AocCompProfileDetails profDet;

    @Column(name = "vchSignName", length = 30)
    private String signName;

    @Column(name = "vchSignTitle", length = 45)
    private String signTitle;

    @Column(name = "vchSignPath")
    private String signPath;

    @Column(name = "vchSealPath")
    private String sealPath;

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

