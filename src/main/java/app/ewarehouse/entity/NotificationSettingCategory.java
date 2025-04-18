package app.ewarehouse.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name="notific_setting_cat")
public class NotificationSettingCategory {
	
	//private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_cat_id")
    private Integer notificationCategoryId;
	
	@Column(name = "notification_cat_type")
	private String notificationCayegoryType;
	
	@Column(name="notification_details")
	private String notificationDetails;
	
	@Column(name = "createdBy", nullable = false)
	private Integer intCreatedBy;

	@Column(name = "updatedBy")
	private Integer intUpdatedBy;

	@Column(name = "createdOn")
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date dtmCreatedOn;

	@Column(name = "updatedOn")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date stmUpdatedOn;

	@Column(name = "bitDeletedFlag")
	private Boolean bitDeletedFlag;
	
	

}
