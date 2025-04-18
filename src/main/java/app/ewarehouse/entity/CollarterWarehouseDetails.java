package app.ewarehouse.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Entity
@Table(name = "t_collarter_warehouse_details")
@Data

public class CollarterWarehouseDetails {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intId")
    private Integer id;

    @Column(name = "vchWarehouseId", length = 50)
    private String warehouseId;

    @Column(name = "vchWarehouseName", length = 50)
    private String warehouseName;

    @Column(name = "vchWhOperatorName", length = 50)
    private String warehouseOperatorName;

    @Column(name = "vchEmail", length = 45)
    private String email;

    @Column(name = "vchMobileNumber", length = 15)
    private String mobileNumber;

    @Column(name = "vchLrNumber", length = 20)
    private String lrNumber;

    @Column(name = "intCountyId")
    private Integer countyId;

    @Column(name = "intSubCountyId")
    private Integer subCountyId;

    @Column(name = "intWardId")
    private Integer wardId;

    @Column(name = "vchLongitude", length = 20)
    private String longitude;

    @Column(name = "vchLatitude", length = 20)
    private String latitude;

    @Column(name = "vchStreetName", length = 50)
    private String streetName;

    @Column(name = "vchBuilding", length = 50)
    private String building;

    @Column(name = "vchPolicyNumber", length = 30)
    private String policyNumber;

    @Column(name = "intCreateBy")
    private Integer createdBy;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmCreatedOn", updatable = false, insertable = false)
    private Timestamp createdOn;

    @Column(name = "dtmUpdatedOn", insertable = false)
    private Timestamp updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag;
}
