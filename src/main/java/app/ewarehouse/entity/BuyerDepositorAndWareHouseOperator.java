package app.ewarehouse.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import app.ewarehouse.master.entity.WardMaster;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_buyer_depositor_wh_operator_application")
@DynamicUpdate
public class BuyerDepositorAndWareHouseOperator {
	 @Id
	    @GeneratedValue(generator = "buyer_depositor_wh__operator-custom-id")
	    @GenericGenerator(
	            name = "buyer_depositor_wh__operator-custom-id",
	            type = app.ewarehouse.util.CustomIdGenerator.class,
	            parameters = {
	                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_buyer_depositor_wh_operator_application"),
	                    @org.hibernate.annotations.Parameter(name = "idName", value = "intDepositorWhOperator")
	            })

	@Column(name = "intDepositorWhOperator")
	private String intDepositorWhOperator;

	@Column(name = "typeOfUser")
	private String typeOfUser;
   
//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "vchWarehouseId", referencedColumnName = "vchWarehouseId",nullable = false)
//   private BuyerWareHosueDetails wareHouseName;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vchWarehouseId", referencedColumnName = "vchWarehouseId", nullable = false)
    private BuyerWareHosueDetails wareHouseName;
	 
	
	@Column(name = "vchCompanyId")
	private String companyName;

	@Column(name = "typeOfEntity")
	private String typeOfEntity;

	@Column(name = "typeOfDepositor")
	private String typeOfDepositor;

	@Column(name = "nameOfDepositor")
	private String nameOfDepositor;

	@Column(name = "mobileNumber")
	private String mobileNumber;

	@Column(name = "emailAddress")
	private String emailAddress;

	@Column(name = "postalCode")
	private String postalCode;

	@Column(name = "postalAddress")
	private String postalAddress;

	
	@ManyToOne
    @JoinColumn(name = "intCountyId")
	private County countyId;
    
	@ManyToOne
    @JoinColumn(name = "intSubCountyId")
	private SubCounty subCountyId;

	@ManyToOne
    @JoinColumn(name = "intWardMasterId")
	private WardMaster intWard;

	
	@ManyToOne
    @JoinColumn(name = "intNationaity")
	private Country intNationaity;

	@Column(name = "nationalId")
	private String nationalId;

	@Column(name = "entityRegistrationNumber")
	private String entityRegistrationNumber;

	@Column(name = "passportNumber")
	private String passportNumber;

	@Column(name = "alienId")
	private String alienId;

	@Enumerated(EnumType.STRING)
	@Column(name = "depositorStatus")
	private Status depositorStatus = Status.Active;

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
	
//	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "intDepositorWhOperator", referencedColumnName = "depositorId", insertable = false, updatable = false)
//    private WarehouseEntity warehouseEntity;
	

	
	@Column(name="registerBy")
	private Integer registerBy;
	
	@Column(name="applicationId")
	private String applicationId;
	
	  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
		@JoinColumn(name = "vchWarehouseId", referencedColumnName = "vchWarehouseId", insertable = false, updatable = false)
		private ApplicationOfConformityLocationDetails applicationOfConformityLocationDetails;
	  
	  @Column(name="intInternalUserId")
	  private Integer internalUserId;
	  
	  @Column(name="govtIssuedId")
	  private String govtIssuedId;
	  
	  @Column(name="vchIdNo")
	  private String vchIdNo;

}
