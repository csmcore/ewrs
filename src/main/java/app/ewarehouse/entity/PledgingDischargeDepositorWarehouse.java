package app.ewarehouse.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_pledging_discharge_depositor_warehouse")
public class PledgingDischargeDepositorWarehouse implements Serializable {

//    @Id
//    @Column(name = "pledging_discharge_receipt_id", length = 20, nullable = false)
//    private String pledgingDischargeReceiptId;
//
//    @ManyToOne
//    @JoinColumn(name = "depositor_id", referencedColumnName = "intId")
//    private BuyerDepositor depositor;
//
//    @ManyToOne
//    @JoinColumn(name = "warehouse_receipt_no", referencedColumnName = "vchWarehouseReceiptId")
//    private TwarehouseReceipt warehouseReceipt;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "pledging_discharge_receipt_id_gen")
	@GenericGenerator(name = "pledging_discharge_receipt_id_gen", type = app.ewarehouse.util.CustomIdGenerator.class, parameters = {
			@org.hibernate.annotations.Parameter(name = "tableName", value = "t_pledging_discharge_depositor_warehouse"),
			@org.hibernate.annotations.Parameter(name = "idName", value = "pledging_discharge_receipt_id") })
	@Column(name = "pledging_discharge_receipt_id")
	private String pledgingDischargeReceiptId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "depositor_id")
	private BuyerDepositor buyer;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "warehouse_receipt_no")
	private TwarehouseReceipt warehouseReceipt;

	@Column(name = "intCreatedBy")
	private Integer intCreatedBy;

	@CreationTimestamp
	@Column(name = "dtmCreatedOn", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp dtmCreatedOn;

	@Column(name = "intUpdatedBy")
	private Integer intUpdatedBy;

	@UpdateTimestamp
	@Column(name = "dtmUpdatedOn", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private Timestamp dtmUpdatedOn;

	@Column(name = "bitDeletedFlag")
	private Boolean bitDeletedFlag = false;
	
	@Column(name = "bitDraftStatus")
	private Boolean bitDraftStatus = false;
	
	
	

}
