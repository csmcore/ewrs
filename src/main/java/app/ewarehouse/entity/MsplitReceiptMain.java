package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_split_receipt_main")
public class MsplitReceiptMain {

    @Id
    @GeneratedValue(generator = "split-main-id")
    @GenericGenerator(
            name = "split-main-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "m_split_receipt_main"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "vchSplitApplicationId")
            })
    @Column(name = "vchSplitApplicationId")
    private String txtSplitApplicationId;

    @ManyToOne
    @JoinColumn(name = "vchWarehouseReceiptId",referencedColumnName = "vchWarehouseReceiptId")
    private TwarehouseReceipt warehouseReceipt;

    @Column(name = "intTotalQuantity")
    private Integer totalQuantity;

    @Column(name = "intTotalLotNo", length = 45)
    private String totalLotNo;

    @Column(name = "vchSurrenderReceiptCheck")
    private Boolean surrenderReceipt;

    @Column(name = "vchOldWarehouseReceiptPath", length = 1000)
    private String oldWarehouseReceipt;

    @Column(name = "vchReceiptName", length = 255)
    private String documentName;

    @OneToMany(mappedBy = "splitReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TsplitReceipt> splits;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private enmReceiptStatus status;

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
