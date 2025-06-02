package app.ewarehouse.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_issuance_ware_house_recipt_application")
public class IssuanceWareHouseRecipt {
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
 
	@Column(name = "intIssueanceWhId")
	private Integer issuanceWhId;
    
	@ManyToOne
	@JoinColumn(name = "intDepositorWhOperator")
	private BuyerDepositorAndWareHouseOperator intDepositorWhOperator;
	
//	@Column(name="commodityName")
//	private String commodityName;
    
//	@ManyToOne
//	@JoinColumn(name = "commoditytype")
//	private Commodity commodityType;	
   
	private Integer commodityType; 
 
	@Column(name = "commodityOrigin")
	private String commodityOrigin;
 
	@Column(name = "commodityCode")
	private String commodityCode;
 
	@Column(name = "originalQuantity")
	private Double originalQuantity;
 
	@Column(name = "originalGrossWeight")
	private Double originalGrossWeight;
 
	@Column(name = "originalNetWeight")
	private Double originalNetWeight;
 
	@Column(name = "currentQuantity")
	private Double currentQuantity;
 
	@Column(name = "currentNetWeight")
	private Double currentNetWeight;
	
	@ManyToOne
	@JoinColumn(name = "cropYear")
	private Seasonality cropYear;
 
	@Column(name = "grade")
	private String grade;
 
	@Column(name = "lotNumber")
	private String lotNumber;
 
	@Column(name = "qualityInspectionPath")
	private String qualityInspectionPath;
 
	@Column(name = "weighingTicketPath")
	private String weighingTicketPath;
 
	@Column(name = "grnPath")
	private String grnPath;
 
 
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
	
	 @Column(name = "selectDate")
	private String selectDate;
	
    @OneToMany(mappedBy = "intIssueanceWhId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
	private List<IssuaneWareHouseOperatorCharges> issuaneWareHouseOperatorCharges;
    
    
    @Column(name = "tradeStatus")
    private Boolean tradeStatus= false;
    
    @Column(name = "pledgeStatus")
    private Boolean pledgeStatus= false;
    
    @OneToMany(mappedBy = "issuanceWareHouseRecipt", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<CommodityMarket> commodityMarket;

	
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "intDepositorWhOperator", referencedColumnName = "depositorId", insertable = false, updatable = false)
//   
    @OneToMany(mappedBy = "issuanceWareHouseRecipt", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<Pledge> pledge;
    
    @Enumerated(EnumType.STRING)
	@Column(name = "reciptStatus")
	private Status reciptStatus = Status.WaitingforDepositorConfirmation;
    
    @Column(name = "wareHouseReciptNo")
	private String wareHouseReciptNo;
    
    @Column(name = "vchRemark")
   	private String vchRemark;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "oicStatus")
   	private Status oicStatus = Status.PendingatCentralRegistry;
    
    @Column(name = "issueRemark")
   	private String issueRemark;
    
    @Column(name = "blockRemark")
   	private String blockRemark;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ceoStatus")
   	private Status ceoStatus = Status.Pendingforregistration;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentStatus")
   	private Status paymentStatus = Status.Pending;
    
    @Column(name = "vchWarehouseId")
   	private String vchWarehouseId;
    
    @Column(name = "isRetired")
    private Boolean isRetired= false;
    
    @Column(name = "isTransfer")
    private Boolean isTransfer= false;
    
    @Column(name = "doPaymentLater")
    private Boolean doPaymentLater= false;
	
 
}