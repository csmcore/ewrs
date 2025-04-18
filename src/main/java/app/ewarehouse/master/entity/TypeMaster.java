package app.ewarehouse.master.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**\
 * @author pravat.behera
 * @Date 24-03-2025 12:20 PM
 * @Description  This Table used for form type master table 
 *               where we cab store all type form name like FORM 1A ,FORM 1B ,FORM 1C
 */
@Data
@Entity
@Table(name = "m_form_type")
public class TypeMaster {

	@Id
	@Column(name = "typeid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer typeId;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "description")
	private String description;

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

}
