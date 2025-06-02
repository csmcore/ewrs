package app.ewarehouse.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "t_loss_receipt")
public class LossReceipt {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intId")
    private Integer id;

	@Column(name = "vchNatureOfLoss")
    private String natureOfLoss;
	
	@Column(name = "vchOtherDetails")
    private String otherDetails;
	
	@Column(name = "vchComment")
    private String comment;
	
	@Column(name = "vchDepositorId")
    private String depositorId;
	
	@Column(name = "vchWarehouseReceiptNo")
    private String warehouseReceiptNo;
	
	@Column(name = "vchOperatorLicenceNo")
    private String operatorLicenceNo;
	
//	@OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "intPoliceLossReportId", referencedColumnName = "intId")
//    private LossReceiptFiles policeLossReport;
//	
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "intLossReceiptCopyId", referencedColumnName = "intId")
//    private LossReceiptFiles lossReceiptCopy;
	
	@Column(name = "intCreatedBy")
    private Integer createdBy;
	
	@Column(name = "intUpdatedBy")
    private Integer updatedBy;
	
	@Column(name = "bitDeletedFlag")
	private Boolean bitDeletedFlag = false;
	
	@Transient
	private String policeLossReportDoc;
	@Transient
	private String lossReceiptCopyDoc;
}
