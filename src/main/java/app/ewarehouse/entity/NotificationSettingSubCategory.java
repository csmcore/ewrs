package app.ewarehouse.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name="notific_setting_subcat")
public class NotificationSettingSubCategory{
	//private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notific_subcat_id")
	private Integer notificationSubCategoryId;
	
	@ManyToOne
	@JoinColumn(name = "notification_cat_id")
	private NotificationSettingCategory notificationCategory;
	
	@Column(name="notific_subcat_name")
	private String notificationSubCategoryName;
	
	@Column(name="notific_subcat_status")
	private Boolean notificSubcatStatus;
	
	@Column(name = "createdBy", nullable = false)
	private Integer intCreatedBy;

	@Column(name = "updatedBy")
	private Integer intUpdatedBy;

	@CreationTimestamp
    @Column(name = "createdOn", nullable = true, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date dtmCreatedOn;

	@UpdateTimestamp
    @Column(name = "updatedOn", nullable = true, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private Date stmUpdatedOn;

	@Column(name = "bitDeletedFlag")
	private Boolean bitDeletedFlag=false;
	
	
}
