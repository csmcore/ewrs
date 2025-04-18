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
@Table(name = "t_receive_commodity")
public class TreceiveCommodity {
    @Id
    @GeneratedValue(generator = "receive-commodity-id")
    @GenericGenerator(
            name = "receive-commodity-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_receive_commodity"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "vchReceiveCId")
            })
    @Column(name = "vchReceiveCId")
    private String txtReceiveCId;

    @ManyToOne
    @JoinColumn(name = "vchDepositorsIntId", referencedColumnName = "intId")
    private BuyerDepositor depositor;

    @ManyToOne
    @JoinColumn(name = "intCommodityType", referencedColumnName = "Id")
    private Commodity commodity;

    @Column(name = "intQuantity")
    private Double intQuantity;

    @ManyToOne
    @JoinColumn(name = "intSeasonalityId", referencedColumnName = "Id")
    private Seasonality seasonality;

    @Column(name = "vchGrade")
    private String txtGrade;

    @Column(name = "vchLotNo")
    private String txtLotNo;

    @Column(name = "warehouseId")
    private String warehouseId;

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
    @Column(name = "status")
    private Status status=Status.Pending;
    @Column(name = "vchPurchasedBy")
    private String vchPurchasedBy;
    
    @Column(name = "bitSaleStatus")
    private boolean bitSaleStatus=false;
    
    @Column(name = "bitPaymentStatus")
    private boolean bitPaymentStatus=false;
    
    
    
    
}
