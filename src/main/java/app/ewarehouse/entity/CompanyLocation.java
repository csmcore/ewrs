package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "t_company_location")
@DynamicUpdate
public class CompanyLocation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intId")
    private Integer intId;

    @Column(name = "intPostalCode")
    private Integer intPostalCode;

    @Column(name = "vchPostalAddress")
    private String vchPostalAddress;

    @Column(name = "vchBuildingName")
    private String vchBuildingName;

    @Column(name = "vchStreetName")
    private String vchStreetName;

    @Column(name = "vchPlotNo")
    private String vchPlotNo;

    @ManyToOne
    @JoinColumn(name = "intWardId")
    private Ward ward;

    @Column(name = "vchLocation")
    private String vchLocation;

    @Column(name = "vchSubLocation")
    private String vchSubLocation;

    @Column(name = "vchTown")
    private String vchTown;

    @Column(name = "vchVillage")
    private String vchVillage;

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
