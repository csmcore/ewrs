package app.ewarehouse.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "m_lot")
public class LotMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lotId")
    private Integer lotId;

    @Column(name = "typeOfLot", length = 20)
    private String typeOfLot;

    @Column(name = "noOfBags")
    private Integer noOfBags;

    @Column(name = "kgPerBag")
    private Integer kgPerBag;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn", updatable = false)
    private LocalDateTime dtmCreatedOn;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn")
    private LocalDateTime dtmUpdatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;

    
}
