package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "t_dispute_declaration_oic_legal_two")
public class DisputeDeclarationOicLegalTwo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer intId;

    @Column(name = "bitLegalImplications")
    private Boolean bitLegalImplications;

    @Column(name = "bitFindingsSatisFactory")
    private Boolean bitFindingsSatisFactory;

    @Column(name = "bitValid")
    private Boolean bitValid;

    @Column(name = "bitConsistent")
    private Boolean bitConsistent;

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


