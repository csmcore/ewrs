package app.ewarehouse.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "t_warehouse_details")
@Data
public class WarehouseEntity {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String warehouseName;

	    private String warehouseOperatorName;

	    private String emailId;

	    private String mobileNumber;

	    private String lrNumber;

	    private String county;

	    private String subCounty;

	    private String ward;

	    private String longitude;

	    private String latitude;

	    private String streetName;

	    private String building;

	    private String policyNumber;

	    private String depositorId;
	    
		private Integer intCreatedBy;
		private Integer intUpdatedBy;
		@CreationTimestamp
		private Date dtmCreatedOn;
		@UpdateTimestamp
		private Date dtmUpdatedOn;
		private Boolean bitDeletedFlag = false;

}
