package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@Entity
@Table(name="t_set_authority")
public class TSetAuthority implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Integer intSetAuthId;
	private Integer intHierarchyId;
	private Integer intProcessId;
	private Integer intProjectId;
	private Integer intSubParamC1;
	private Integer intSubParamC2; 
	private String vchSubStage; 
	private Integer tinStageNo;
	private Integer intRoleId;
	private Integer intPrimaryAuth; 
	private Integer tinTimeLine;
	private Integer tinSignature;
	private Integer intSignatoryAuth;
	private String vchAuthTypes; 
	private Integer tinDelegateStatus;
	private Date dtmCreatedOn;
	private Date stmUpdatedOn; 
	private Integer intCreatedBy;
	private Integer intUpdatedBy;
	private Byte bitDeletedFlag; 
	private Integer intATAProcessId;
	private Integer intApprovingAuth; 
	@Column(length = Integer.MAX_VALUE)
	private Integer intSetAuthLinkId;
	@Column(length = Integer.MAX_VALUE)
	private Integer intSetLetterLinkId;
	private Integer tinAutoEscalate;
	private Integer tinDemandNotePaymentStatus; 
	private String vchCtrlName;
	private int intLabelId;
	private Integer tinType; 
	private String jsnApprovalDocument; 
	private String vchPcategoryName;
	private String jsnVerifyDocument;
	private String jsnCalcDetails;
	private Integer tinCalcStatus;
	private String vchParentNode;
	private Integer tinCurrentNode;
	private String vchDynFilter;
	private String vchDynFilterCtrlId;
	private String vchMailSmsConfigIds;
	private String authLetters;


}
