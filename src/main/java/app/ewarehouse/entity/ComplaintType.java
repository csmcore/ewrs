/**
 * 
 */
package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name = "m_master_complaint_type")
public class ComplaintType implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "complaint_id")
	private Integer complaintId;

	@Column(name = "complaint_type")
	private String complaintType;

	@Column(name = "description")
	private String complaintStatus;

	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "is_active")
	private Boolean isActive;

	 @ManyToMany
	    @JoinTable(
	        name = "m_master_complaint_type_roles",
	        joinColumns = @JoinColumn(name = "intComplaintId"),
	        inverseJoinColumns = @JoinColumn(name = "intRoleId")
	    )
	private List<Mrole> roles;
}
