package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_oic_collateral_take_action")
public class OicCollateralTakeAction {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intOicTakeActionId")
    private Integer id;

    @ManyToOne
   	@JoinColumn(name = "vchapplicationId" , nullable = false)
   	private ApplicationCertificateOfCollateral colletral;

    @Column(name = "remarkForDeferring", length = 255)
    private String remarkForDeferring;

    @Column(name = "remarkByOic", length = 255)
    private String remarkByOic;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "dtmCreatedOn")
    private LocalDateTime createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmUpdatedOn")
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag;
}
