package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "t_dispute_declaration_ceo")
public class DisputeDeclarationCeo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer intId;

    @Column(name = "bitComplete")
    private Boolean bitComplete;

    @Column(name = "bitValid")
    private Boolean bitValid;

    @Column(name = "bitClear")
    private Boolean bitClear;

    @Column(name = "bitImmediateAttention")
    private Boolean bitImmediateAttention;

    @Column(name = "vchRemark", length = 2000)
    private String vchRemark;

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
}


