
package app.ewarehouse.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "t_notification")
@Data
public class NotificationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intAuthNotificationNo")
	private Integer authNotificationNo;

	@Column(name = "intAuthNotificationId")
	private Long authNotificationId;

	@Column(name = "intFromAuthId")
	private Integer fromAuthId;

	@Column(name = "intToAuthId")
	private Integer toAuthId;

	@Column(name = "vchNotification")
	private String notification;

	@Column(name = "bitReadStatus")
	private Boolean readStatus;

	@Column(name = "dtmCreatedOn")
	@CreationTimestamp
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime createdOn;

	@Column(name = "dtmUpdatedOn")
	@UpdateTimestamp
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime updatedOn;

	@Column(name = "intCreatedBy", nullable = false)
	private Integer createdBy;

	@Column(name = "intUpdatedBy")
	private Integer updatedBy;

	@Column(name = "vchNotificationModule", nullable = false)
	private String notificationModule;

	@Column(name = "vchNotificationType")
	private String notificationType;
	
	@Column(name = "vchPath")
	private String vchPath;
	
	@Transient
	private Integer compAppId;
	
	@Transient
	private Integer intProcessId;
	
	@Transient
	private Integer linkId;
	
	@Transient
	private Integer stageNo;
	
	@Transient
	private Integer labelId;
	@Transient
	private Integer appRoleId;
	@Transient
	private Integer intAction;
	
	
	
	
}
