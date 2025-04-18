package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity @Getter @Setter @ToString(exclude = "remarks")
@Table(name = "t_operator_licence")
@DynamicUpdate
public class OperatorLicence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operator_licence_id")
    private Integer id;
    @Column(name = "vchApplicationNo", unique = true, nullable = false, length = 20)
    private String vchApplicationNo;
    @Column(name = "business_name")
    private String businessName;
    @Column(name = "business_reg_number")
    private String businessRegNumber;
    @Column(name = "business_entity_type")
    private String businessEntityType;
    @Column(name = "business_address")
    private String businessAddress;
    @Column(name = "email_address")
    private String emailAddress;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "kra_pin")
    private String kraPin;
    @Column(name = "physical_address_warehouse")
    private String physicalAddressWarehouse;
    @Column(name = "warehouse_size")
    private Integer warehouseSize;
    @Column(name = "goods_stored")
    private String goodsStored;
    @Column(name = "storage_capacity")
    private Integer storageCapacity;
    @Column(name = "security_measures")
    private String securityMeasures;
    @Column(name = "waste_disposal_methods")
    private String wasteDisposalMethods;
    @Column(name = "declaration")
    private Boolean declaration;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.Pending;
    @Enumerated(EnumType.STRING)
    @Column(name = "forwarded_to")
    private Stakeholder forwardedTo = Stakeholder.APPLICANT;
    @Enumerated(EnumType.STRING)
    @Column(name = "current_action")
    private Action action = Action.Pending;
    @OneToMany(mappedBy = "operatorLicence", cascade = {}, orphanRemoval = false)
    private List<OperatorLicenceRemarks> remarks;
    @OneToMany(mappedBy = "operatorLicence", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperatorLicenceFiles> files;
    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;
    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;
    @CreationTimestamp
    @Column(name = "dtmCreatedAt")
    private Date dtmCreatedAt;
    @UpdateTimestamp
    @Column(name = "stmUpdatedAt")
    private Date stmUpdatedAt;
    @Column(name = "bitDeleteFlag")
    private Boolean bitDeleteFlag = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmSaveStatus")
    private SaveStatus enmSaveStatus;
    
    private String applicationId;
}
