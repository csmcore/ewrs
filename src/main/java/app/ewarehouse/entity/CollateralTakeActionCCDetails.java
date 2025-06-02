package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;
@Entity
@Table(name = "t_oic_collateral_take_action_cc_details")
@Data
public class CollateralTakeActionCCDetails {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intCCDetailsId", nullable = false)
    private Integer ccDetailsId;

    @ManyToOne
    @JoinColumn(name = "vchapplicationId", nullable = false)
    private ApplicationCertificateOfCollateral colletral;

    @ManyToOne
    @JoinColumn(name = "intAllocateCCId", nullable = false)
    private CollateralTakeActionCC allocateCCId;

    @Column(name = "vchSupportingDocumentName", length = 255)
    private String supportingDocumentName;
    
    @Column(name = "vchRemarkByCC", length = 255)
    private String remarkByCC;

    @Column(name = "vchSupportingDocument", length = 255)
    private String supportingDocument;

    @Column(name = "intCreatedBy", nullable = false)
    private Integer createdBy;

    @Column(name = "dtmCreatedOn", updatable = false, insertable = false)
    private Timestamp createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmUpdatedOn", insertable = false)
    private Timestamp updatedOn;

	/*
	 * @Column(name = "bitDeletedFlag") private Boolean deletedFlag;
	 */
    
    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean deletedFlag = false;
    
    @Column(name = "bitIsEdit")
    private Boolean isEdit = false;
	

}
