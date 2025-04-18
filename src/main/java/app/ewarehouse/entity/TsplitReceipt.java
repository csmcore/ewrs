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
@Table(name = "t_split_receipt")
public class TsplitReceipt {
    @Id
    @GeneratedValue(generator = "split-id")
    @GenericGenerator(
            name = "split-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_split_receipt"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "vchSplitId")
            })
    @Column(name = "vchSplitId")
    private String splitId;

    @Column(name = "vchSplitReceiptNumber", length = 45)
    private String splitReceiptNo;

    @Column(name = "intLotNo")
    private Integer lotNo;

    @Column(name = "intQty")
    private Double qtyInEachLot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vchSplitIdReceipt",referencedColumnName = "vchSplitApplicationId")
    private MsplitReceiptMain splitReceipt;

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
}
