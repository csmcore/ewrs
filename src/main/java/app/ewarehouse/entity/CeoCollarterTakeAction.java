package app.ewarehouse.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "t_ceo_collarter_takeaction")
@Data
public class CeoCollarterTakeAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intId")
    private Integer id;

    @Column(name = "vchapplicationId", length = 20, nullable = false)
    private String applicationId;

    @Column(name = "vchRejectRemark", length = 255)
    private String rejectRemark;

    @Column(name = "intCreatedBy", nullable = false)
    private Integer createdBy;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmCreatedOn", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Timestamp createdOn;

    @Column(name = "dtmUpdatedOn")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Timestamp updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;
    
    @Column(name = "isEditCase")
    private Boolean isEditCase = false;
    
    
}
