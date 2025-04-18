package app.ewarehouse.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import app.ewarehouse.master.entity.WardMaster;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Table(name = "m_admin_user")
@Entity
public class Tuser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intId")
	private Integer intId;
	@Column(name = "`vchFullName`")
	private String txtFullName;
	@Column(name = "`intGender`")
	private Integer selGender;
	@Transient
	private String selGenderVal;
	@Column(name = "`vchPhoto`")
	private String filePhoto;
	@Column(name = "`vchMobileNumber`")
	private String txtMobileNo;
	@Column(name = "`vchEmailId`")
	private String txtEmailId;
	@Column(name = "`vchAltrMobileNo`")
	private String txtAlternateMobileNumber;
	@Column(name = "`vchAddress`")
	private String txtrAddress;
	@Column(name = "`intRoleId`")
	private Integer selRole;
	@Transient
	private String selRoleVal;
	@Column(name = "`intDesignantion`")
	private Integer selDesignation;
	@Transient
	private String selDesignationVal;
	@Column(name = "`intEmployeeType`")
	private Integer selEmployeeType;
	@Transient
	private String selEmployeeTypeVal;
	@Column(name = "`intDepartment`")
	private Integer selDepartment;
	@Transient
	private String selDepartmentVal;
	@Column(name = "`intGroup`")
	private Integer selGroup;
	@Transient
	private String selGroupVal;
	@Column(name = "`intHierarchy`")
	private Integer selHierarchy;
	@Transient
	private String selHierarchyVal;
	
	@Column(name = "`intCountyId`")
	private Integer selCounty;
	@Transient
	private String selCountyVal;
	
	@Column(name = "`intSubCountyId`")
	private Integer selSubCounty;
	@Transient
	private String selSubCountyVal;
	
	@Column(name = "`intWardId`")
	private Integer selWard;
	@Transient
	private String selWardVal;
	
	@Column(name="`icmRoleId`")
	private Integer selIcmRoleId;
	
	@Column(name="`intCLCRoleId`")
	private Integer selCLCRoleId;
	
	@Column(name="`intWRSCRoleId`")
	private Integer selWRSCRoleId;
	
	@Column(name = "`vchLoginId`")
	private String txtUserId;
	@Column(name = "`vchPassword`")
	private String txtPassword;
	@Transient
	private String enPassword;
	@Column(name = "`intPrevilege`")
	private Integer chkPrevilege;
	@Transient
	private String chkPrevilegeVal;
	@Column(name = "intCreatedBy")
	private Integer intCreatedBy;

	@Column(name = "intUpdatedBy")
	private Integer intUpdatedBy;

	@Column(name = "dtmCreatedOn")
	@CreationTimestamp
	private Date dtmCreatedOn;

	@Column(name = "stmUpdatedOn")
	@UpdateTimestamp
	private Date stmUpdatedOn;

	@Column(name = "bitDeletedFlag")
	private Boolean bitDeletedFlag = false;

	@Column(name = "`vchDtOfJoining`")
	private Date txtDateOfJoining;
	@Transient
	private String txtConfirmPassword;

	@Column(name = "`intReportingAuth`")
	private Integer intReportingAuth;
	@Transient
	private String intReportingAuthVal;

	private LocalDateTime dtforgetpasswordstarttime;

   //@Column(name="vchWarehouse")
    //private String warehouseId;

	@Column(name = "vchWarehouse")
	private String warehouseId;

	@Column(name = "vchUserStatus")
	private String vchUserStatus;
	
	@Column(name = "bitIsTwoFactorAuthEnabled")
	private Boolean bitIsTwoFactorAuthEnabled = false;
	
	@Column(name = "bitIsSaveActivityLogEnabled")
	private Boolean bitIsSaveActivityLogEnabled = false;

	@Column(name = "isLocked")
	private boolean isLocked= false;
	
	@Column(name="intWhInternalUserId")
	private Integer intWhInternalUserId;
	
	@Column(name = "opl_susp_status")
	private boolean oplsuspStatus= false;
	
	@Column(name = "opc_susp_status")
	private boolean opcsuspStatus= false;
	
	@Column(name = "bitRevokeStatus")
	private boolean revokeStatus= false;
	
	
}