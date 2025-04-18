package app.ewarehouse.entity;

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
@Table(name = "t_warehouse_receipt")
public class TwarehouseReceipt {
    
    @Id
    @GeneratedValue(generator = "warehouse-receipt-id")
    @GenericGenerator(
            name = "warehouse-receipt-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_warehouse_receipt"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "vchWarehouseReceiptId")
            })
    @Column(name = "vchWarehouseReceiptId")
    private String txtWarehouseReceiptId;

    @OneToOne
    @JoinColumn(name = "vchReceiveCId", referencedColumnName = "vchReceiveCId")
    private TreceiveCommodity receiveCommodity;

    @Column(name = "vchRemark")
    private String txtRemark;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.Pending;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmReceiptStatus")
    private enmReceiptStatus receiptStatus = enmReceiptStatus.Pending;

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

    @Column(name="intIndex")
    private Integer intIndex;
    
    @Column(name = "vchPurchasedBy")
    private String vchPurchasedBy;
    
    @Column(name = "bitSaleStatus")
    private boolean bitSaleStatus=false;
    
    @Column(name = "bitPaymentStatus")
    private boolean bitPaymentStatus=false;
}
