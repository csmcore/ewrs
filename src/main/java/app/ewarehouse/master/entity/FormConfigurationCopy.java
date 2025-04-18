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
 * @Date 26-03-2025 12:31 PM
 * @Description  This Table used for copy of form configuration table data to used at edit and deleted record 
 */
@Data
@Entity
@Table(name="copy_from_configuration")
public class FormConfigurationCopy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="idcopy")
	private Integer idCopy;
	
	@Column(name="id",nullable = false)
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
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="statustype")
	private Integer statusType;
}
