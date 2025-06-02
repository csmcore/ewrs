package app.ewarehouse.master.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**\
 * @author pravat.behera
 * @Date 24-03-2025 12:31 PM
 * @Description  This Table used for all master form document details 
 */

@Data
@Entity
@Table(name="m_from_configuration")
public class FormConfiguration {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "typeid")
	private TypeMaster typeMaster;
	
	@Column(name="format")
	private String format;
	
	@Column(name="size")
	private Integer size;
	
	@Column(name="docname")
	private String documentName;
	
	@Column(name = "ismandetory")
	private Boolean isMandetory;
	
	@Column(name = "activestatus")
	private Boolean isActive;
	
	@Column(name = "finalstatus")
	private Integer finalStatus;
	
	@Column(name = "createdby")
	private String createdBy;

	@Column(name = "createdon")
	private Date createdOn;

	@Column(name = "updatedby")
	private String updatedBy;

	@Column(name = "updatedon")
	private Date updatedOn;

	@Column(name = "deletedflag")
	private Boolean deletedFlag;
	
	@Column(name = "checkerstatus")
	private Integer checkerStatus;
	
	@Column(name = "checker_updated_by")
	private String checkerUpdatedBy;

	@Column(name = "checker_updated_on")
	private Date checkerUpdatedOn;
	
	@Column(name = "userid")
	private Integer userId;
	
	@Column(name="statustype")
	private Integer statusType;
	
	@Column(name="remark")
	private String remark;
	

}
