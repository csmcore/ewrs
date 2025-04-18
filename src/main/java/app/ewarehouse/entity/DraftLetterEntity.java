package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name="t_dynamic_letter_config")
public class DraftLetterEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intConfigId;
	private Integer intLetterConfigId;
	private String vchLetterTitle;
	private String txtLetterContent;
	private Integer intCreatedBy=0;
	@CreationTimestamp
	private Date dtmCreatedOn;
	private Boolean bitDeletedFlag=false;
	private Integer intUpdatedBy=0;
	@UpdateTimestamp
	private Date dtmUpdatedOn;
	private Integer intProfileId;
	private Integer intApplicationId;
	private String vchLetterNo;
	private Integer vchGenerateStatus;
	
	
}
