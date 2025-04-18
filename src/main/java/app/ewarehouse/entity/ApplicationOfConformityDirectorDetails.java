package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "t_application_of_conformity_director_details")
public class ApplicationOfConformityDirectorDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intDirectorDetailsId")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "intParticularOfApplicantsId")
    @JsonBackReference
   // @JsonIgnore
    private ApplicationOfConformityParticularOfApplicants particularOfApplicants;

    @Column(name = "vchDirectorName")
    private String directorName;

    @ManyToOne
    @JoinColumn(name = "intNationalityId", referencedColumnName = "intNationalityId")
    private Nationality nationality;

    @Column(name = "vchPassportNumber")
    private String passportNumber;

    @Column(name = "vchUploadWorkPermitPath")
    private String uploadWorkPermitPath;

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
	
}
