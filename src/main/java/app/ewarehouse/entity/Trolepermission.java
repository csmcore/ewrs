package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
@Data
@Table(name="m_admin_rolepermission")
@Entity
public class Trolepermission {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="intId")
private Integer intId;

@Column(name="`tinForPermission`")
private Integer selPermissionFor;
@Transient
private String selPermissionForVal;
@Column(name="`intRole`")
private Integer selSelectRole;
@Transient
private String selSelectRoleVal;

@Column(name="`intUserId`")
private Integer selSelectUser;

@Transient
private String selSelectUserVal;

@Column(name="intCreatedBy")
private Integer intCreatedBy;

@Column(name="intUpdatedBy")
private Integer intUpdatedBy;

@Column(name="dtmCreatedOn")
@CreationTimestamp
private Date dtmCreatedOn;

@Column(name="stmUpdatedOn")
@UpdateTimestamp
private Date stmUpdatedOn;

@Column(name="bitDeletedFlag")
private Boolean bitDeletedFlag=false;
@Column(name="vchLinkId")
private String vchLinkId;


@Column(name="tinManageRight")
private Integer intViewManageRight;

@Column(name="tinAddRight")
private Integer intadd;

@Column(name="tinEditRight")
private Integer intEditRight;

@Column(name="tinDeleteRight")
private Integer intDelete;

@Column(name="tinPublishRight")
private Integer publish;

@Column(name="tinAllRight")
private Integer intall;
}



