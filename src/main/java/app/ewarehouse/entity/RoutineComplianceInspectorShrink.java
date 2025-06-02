package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "t_rout_com_shrink")
public class RoutineComplianceInspectorShrink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_shrink_id")
    private Long intShrinkId;

    @Column(name = "vch_contractual_agreement_on_shrink")
    private String vchContractualAgreementOnShrink;
}
