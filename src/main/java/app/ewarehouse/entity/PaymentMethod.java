package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Table(name="m_payment_method_master")
@Entity
@DynamicUpdate
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="intId")
    private Integer intId;

    @Column(name="vchPaymentMethod")
    private String txtPaymentMethod;
    @Column(name="vchDescription")
    private String txtDescription;
    @Column(name="intCreatedBy")
    private Integer intCreatedBy;
    @Column(name="intUpdatedBy")
    private Integer intUpdatedBy;
    @Column(name="dtmCreatedOn")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Date dtmCreatedOn;
    @Column(name="stmUpdatedOn")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Date stmUpdatedOn;
    @Column(name="bitDeletedFlag")
    private Boolean bitDeletedFlag=false;
}

