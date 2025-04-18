package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "m_buyer_depositor_master")
public class MBuyerDepositor implements Serializable {

    @Id
    @GeneratedValue(generator = "buyer-depositor-custom-id")
    @GenericGenerator(
            name = "buyer-depositor-custom-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "m_buyer_depositor_master"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "vchEntityId")
            })
    @Column(name = "vchEntityId")
    private String vchEntityId;

    @OneToOne
    @JoinColumn(name = "vchApplicationId")
    private BuyerDepositor applicationDetails;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmStatus")
    private CreatedStatus enmStatus;

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
}

