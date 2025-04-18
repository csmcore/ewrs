package app.ewarehouse.entity;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_issuance_wh_receipt_action_history")
@Data
@NoArgsConstructor
public class IssuanceWarehouseReceiptActionHistory {
	

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "intActionHistoryId")
	    private Integer intActionHistoryId;
	    
	    @Column(name = "intIssueanceWhId")
	    private Integer intIssueanceWhId;
	    
	    @Column(name = "depositorId")
	    private String depositorId;

	    @Column(name = "vchRemark", length = 500)
	    private String vchRemark;

	    @Column(name = "vchActionTakenBy", length = 45)
	    private String vchActionTakenBy;

	    @Column(name = "dtmActionTaken")
	    @CreationTimestamp
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	    private Date dtmActionTaken;

	    @Column(name = "roleId")
	    private Integer roleId;
	    
	    @Column(name = "roleName")
	    private String roleName;

	    @Column(name = "userId")
	    private Integer userId;
	    
	    @Column(name = "vchStatus", length = 45)
	    private String vchStatus;
	


}
