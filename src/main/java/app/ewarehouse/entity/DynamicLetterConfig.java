package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name="m_dyn_letter_config")
public class DynamicLetterConfig {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="intLetterConfigId")
	private Integer intLetterConfigId;
	@Column(name="intformId")
	private Integer intFormId;
	@Column(name="vchLetterName")
	private String txtLetterName;
	@Column(name="intLetterType")
	private Integer intLetterType;
	@Column(name="`txtLetterContent`")
	private String txtLetterContent;
	@Column(name="`intSignType`")
	private Integer signType;
	@Column(name="`tinSignTypeSts`")
	private Integer tinSignTypeStatus;
	private Integer intCreatedBy;
	@CreationTimestamp
	private Date dtmCreatedOn;
	private Boolean bitDeletedFlag=false;
	private Integer intUpdatedBy;
	@UpdateTimestamp
	private Date dtmUpdatedOn;
	@Column(name="`tinPublishStatus`")
	private Integer tinPublishStatus;
	
	 
}
	  
	 
	 
	 
	
	  
	 
	  
	 
	
	
