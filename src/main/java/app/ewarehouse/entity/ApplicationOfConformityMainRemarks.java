package app.ewarehouse.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "t_application_of_conformity_main_remarks")
public class ApplicationOfConformityMainRemarks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intRemarksId")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "vchApplicationId", referencedColumnName = "vchApplicationId", nullable = false)
	private ApplicationOfConformity applicationId;

	@Column(name = "vchRemarks")
	private String remarks;

	@Column(name = "dtDateOfRemarks")
	@Temporal(TemporalType.DATE)
	private LocalDate dateOfRemarks;

//    @Column(name = "vchUserId")
//    private Integer userId;
//
//    @Column(name = "vchRoleId")
//    private Integer roleId;

	@ManyToOne
	@JoinColumn(name = "vchUserId", referencedColumnName = "intId", nullable = false)
	private Tuser user;

	@ManyToOne
	@JoinColumn(name = "vchRoleId", referencedColumnName = "intId", nullable = false)
	private Mrole role;

	@Enumerated(EnumType.STRING)
	@Column(name = "enmApplicationStatus")
	private AocRemarksStatus applicationStatus;

	@Column(name = "bitDeletedFlag")
	private Boolean deletedFlag;
}
