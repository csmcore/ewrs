package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@Entity
@Data
@Table(name="dyn_language_list")
public class ManageLanguage implements  Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intId;
	private String vchLanguageName;
	private String vchLanguageCode;
	private String vchAliasName;
	private Integer bitDefaultFlag;
	private Integer tinStatus;
	@CreationTimestamp
	private Date stmCreatedOn;
	@UpdateTimestamp
	private Date dtmUpdatedOn;
	private Integer intCreatedBy;
	private Integer intUpdatedBy;
	private Boolean bitDeletedFlag=false;

	

}
