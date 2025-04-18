package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "t_warehouse_particulars")
@DynamicUpdate
public class WarehouseParticulars implements Serializable {

    @Id
    @GeneratedValue(generator = "warehouse-particulars-custom-id")
    @GenericGenerator(
            name = "warehouse-particulars-custom-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_warehouse_particulars"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "intWareHouseParticularsId")
            })
    @Column(name = "intWareHouseParticularsId")
    private String intWareHouseParticularsId;

    @Column(name = "vchWarehouseName")
    private String vchWarehouseName;

    @NotBlank(message = "Operator name is mandatory")
    @Column(name = "vchOperatorName")
    private String vchOperatorName;

    @NotBlank(message = "Operator license number is mandatory")
    @Column(name = "vchOperatorLicenseNo")
    private String vchOperatorLicenseNo;

    @NotBlank(message = "Name and building location is mandatory")
    @Column(name = "vchNameAndBuildingLocation")
    private String vchNameAndBuildingLocation;

    @Column(name = "vchStreetName")
    private String vchStreetName;

    @ManyToOne
    @JoinColumn(name = "intWardId")
    private Ward ward;

    @Column(name = "vchOperatorsInsurance")
    private String vchOperatorsInsurance;

    @Column(name = "vchPolicyNo")
    private String vchPolicyNo;

    @Column(name = "dtmCreatedOn")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date dtmCreatedOn;

    @Column(name = "stmUpdatedOn")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date stmUpdatedOn;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;

    @Column(name = "intApprovedBy")
    private Integer intApprovedBy;

    @PrePersist
    @PreUpdate
    private void ensureWard() {
        if (this.ward.getIntId() == null) {
            this.ward = null;
        }
    }
}
