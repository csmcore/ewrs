package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
@Data
@Table(name="m_admin_menulinks")
@Entity
public class Tmenulinks {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intId")
	private Integer intId;

	@Column(name = "`intParentLinkId`")
	private Integer selParentLink;
	@Transient
	private String selParentLinkVal;
	@Column(name = "`intLinkType`")
	private Integer selLinkType;
	@Transient
	private String selLinkTypeVal;
	@Column(name = "`vchLinkName`")
	private String txtLinkName;
	@Column(name = "`vchUrl`")
	private String txtURL;
	@Column(name = "`vchCssClass`")
	private String txtCSSClass;
	@Column(name = "`intslNo`")
	private Integer txtSerialNo;
	@Column(name = "intCreatedBy")
	private Integer intCreatedBy;
	
	private Integer intForApproval;
	private Integer intApplicableFor;

	@Column(name = "intUpdatedBy")
	private Integer intUpdatedBy;

	@Column(name = "dtmCreatedOn")
	@CreationTimestamp
	private Date dtmCreatedOn;
	private String vchViewUrl;
	@Column(name = "stmUpdatedOn")
	@UpdateTimestamp
	private Date stmUpdatedOn;
	@Column(name = "bitDeletedFlag")
	private Boolean bitDeletedFlag = false;
	@Column(name = "vchIconEnc")
	private String vchIconEnc;
	@Column(name = "vchIconFileType")
	private String vchIconFileType;
	
}
