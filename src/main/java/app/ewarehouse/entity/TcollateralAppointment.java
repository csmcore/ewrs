package app.ewarehouse.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_collateral_appointment")
public class TcollateralAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer warehouseId;

    @Column(name = "Warehouse_Name", nullable = false)
    private String warehouseName;

    @Column(name = "Warehouse_Location", nullable = false)
    private String warehouseLocation;

    @Column(name = "Warehouse_Capacity", nullable = false)
    private String warehouseCapacity;

    @Column(name = "Types_of_Commodities_Stored")
    private String typesOfCommoditiesStored;

    @Column(name = "Other_Commodities_Stored")
    @Convert(converter = StringListConverter.class)
    private List<String> otherCommoditiesStored;

    @Column(name="Warehouse_shop_name")
    private String warehouseShopName;

    @ManyToOne
    @JoinColumn(name = "Sub_County")
    private SubCounty subCounty;

    @Column(name = "Collateral_Manager", nullable = false)
    private String collateralManager;
    
    @Column(name = "Type_of_Entity")
    private String typeOfEntity;

    @Column(name = "Address")
    private String address;

    @Column(name = "Country")
    private String country = "Kenya";

    @Column(name = "Experience")
    private String experience;

    @Column(name = "Previous_Suspension_Revocation_Incident")
    private Boolean previousSuspensionRevocationIncident;

    @Column(name = "Reasons_for_Previous_Suspension")
    private Boolean reasonsForPreviousSuspension;

    @Column(name = "Remark", length = 2000)
    private String remark;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private Date dtmCreatedOn;

    @UpdateTimestamp
    @Column(name = "stmUpdatedOn")
    private Date stmUpdatedAt;

    @Column(name = "bitDeleteFlag")
    private Boolean bitDeleteFlag = false;

    @Column(name = "complaintId")
    private Integer complaintId;
}
