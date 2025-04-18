package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_calculated_warehouse_charges")
public class TcalculatedWarehouseCharges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calculated_charges_id")
    private Integer calculatedChargesId;

    @Column(name = "depositor_id")
    private String depositorId;

    @Column(name = " warehouse_id")
    private String  warehouseId;

    @Column(name = "warehouse_receipt_no")
    private String warehouseReceiptNo;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "total_days")
    private Integer totalDays;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentStatus")
    private PaymentStatus paymentStatus = PaymentStatus.PAID;

}
