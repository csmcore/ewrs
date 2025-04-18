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

@Data
@Entity
@Table(name = "t_application_of_conformity_action_history")
public class ApplicationOfConformityActionHistory {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "intActionHistoryId")
	    private Integer intActionHistoryId;

	    @Column(name = "vchWareHouseId", length = 20)
	    private String vchWareHouseId;

	    @Column(name = "vchRemarks")
	    private String vchRemarks;

	    @Column(name = "dtmActionTaken")
	    @CreationTimestamp
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	    private Date dtmActionTaken;

	    @Column(name = "intUserId")
	    private Integer intUserId;

	    @Column(name = "vchUserName", length = 45)
	    private String vchUserName;

	    @Column(name = "intRoleId")
	    private Integer intRoleId;

	    @Column(name = "vchRoleName", length = 45)
	    private String vchRoleName;

	    @Column(name = "vchStatus", length = 45)
	    private String vchStatus;

	    @Column(name = "bitDeletedFlag")
	    private Boolean bitDeletedFlag = false;


	


}
