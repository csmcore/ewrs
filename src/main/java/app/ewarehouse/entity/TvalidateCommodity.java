package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_validate_commodity")
public class TvalidateCommodity {

    @Id
    @GeneratedValue(generator = "validate-commodity-id")
    @GenericGenerator(
            name = "validate-commodity-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_validate_commodity"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "vchValidateId")
            })
    @Column(name = "vchValidateId")
    private String txtValidateId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "vchWarehouseReceiptId", referencedColumnName = "vchWarehouseReceiptId")
    private TwarehouseReceipt WarehouseReceipt;

    @OneToOne
    @JoinColumn(name = "vchReceiveCId", referencedColumnName = "vchReceiveCId")
    private TreceiveCommodity receiveCommodity;

    @Column(name = "vchRemark")
    private String txtRemark;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.Pending;


    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private Date dtmCreatedAt;

    @UpdateTimestamp
    @Column(name = "stmUpdatedOn")
    private Date stmUpdatedAt;

    @Column(name = "bitDeleteFlag")
    private Boolean bitDeleteFlag = false;

}
