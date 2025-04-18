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
@Table(name="userwise_notification")
public class UserWiseNotification{
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "userwise_notification_id")
	    private Integer userwiseNotificationId;

	    @ManyToOne
	    @JoinColumn(name = "notification_cat_id")
	    private NotificationSettingCategory notificationCategory;

	    @ManyToOne
	    @JoinColumn(name = "notific_subcat_id")
	    private NotificationSettingSubCategory notificationSubCategory;

	    @ManyToOne
	    @JoinColumn(name = "intId")
	    private Tuser userId;


	    @Column(name = "userwise_notific_status")
	    private Boolean userwiseNotificStatus;
	    
	    @Column(name = "createdBy", nullable = false)
	    private Integer createdBy;

	    @Column(name = "updatedBy")
	    private Integer updatedBy;

	    @Column(name = "createdOn")
	    @Temporal(TemporalType.TIMESTAMP)
		@CreationTimestamp
	    private Date createdOn;

	    @Column(name = "updatedOn")
	    @Temporal(TemporalType.TIMESTAMP)
		@UpdateTimestamp
	    private Date updatedOn;

	    @Column(name = "bitDeletedFlag", nullable = false)
	    private Boolean bitDeletedFlag;


}
