package app.ewarehouse.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "m_unit_in_metric_ton_cubic_mt")
public class UnitOfMeasurement implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intUnitId")
    private Integer unitId;

    @Column(name = "vchUnitName")
    private String unitName;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "dtmCreatedOn")
    private Date createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmUpdatedOn")
    private Date updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag;

    // Getters and Setters
}

