package app.ewarehouse.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_issuance_warehouse_operartor_charges")
public class IssuaneWareHouseOperatorCharges {
	
 
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
 
	@Column(name = "intIssueId")
	private Integer intIssueOperatorId;
    
	@ManyToOne
	@JoinColumn(name = "intIssueanceWhId")
    @JsonBackReference // Prevents infinite recursion
	private IssuanceWareHouseRecipt intIssueanceWhId;
	
	@ManyToOne
	@JoinColumn(name = "intIssueanceWhId",insertable = false,updatable = false) 
	@JsonBackReference
	private IssuanceWarehouseReceiptRetire issuanceWarehouseReceiptRetire;
	
	@ManyToOne
	@JoinColumn(name = "chargeHeaders")
	private ChargesHeader chargeHeaders;
    
	@ManyToOne
	@JoinColumn(name = "unitType")
	private UnitType unitType;
 
	@Column(name = "unitCharge")
	private Double unitCharge;
 
	@Column(name = "quantity")
	private Double quantity;
 
	@Column(name = "totalCharge")
	private Double totalCharge;
 
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
	
	//@Column(name="commodityName")
	@ManyToOne
	@JoinColumn(name = "Id")
	private Commodity commodityName;
	
	
	@Column(name = "grandTotal")
	private Double grandTotal;

}
