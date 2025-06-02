package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_suspension_of_certificate_of_compliance")
public class TsuspensionOfCertificateOfCompliance {
	@Id
	@GeneratedValue(generator = "suspension-custom-id")
	@GenericGenerator(
			name = "suspension-custom-id",
			type = app.ewarehouse.util.CustomIdGenerator.class,
			parameters = {
					@org.hibernate.annotations.Parameter(name = "tableName", value = "t_suspension_of_certificate_of_compliance"),
					@org.hibernate.annotations.Parameter(name = "idName", value = "complaint_number")
			})
	    @Column(name = "complaint_number")
	    private String complaintNumber;


	@NotBlank(message = "Complainant Name is mandatory")
	    @Column(name = "complainant_name")
	    private String complainantName;

	@NotBlank(message = "Contact Number is mandatory")
	    @Column(name = "complainant_contact_number", unique = true)
	    private String complainantContactNumber;

	@Email(message = "Email address is invalid")
	    @Column(name = "complainant_email")
	    private String complainantEmail;

	@NotBlank(message = "Address is mandatory")
	    @Column(name = "complainant_address")
	    private String complainantAddress;

	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
	    @Column(name = "date_of_incident")
	    private Date dateOfIncident;

	@NotBlank(message = "Location Of Incident is mandatory")
	    @Column(name = "location_of_incident")
	    private String locationOfIncident;

	@NotBlank(message = "Complaint Type is mandatory")
	    @Column(name = "complaint_type")
	    private String complaintType;
	    
	    @Column(name = "other_complaint_name")
	    private String otherComplaintName;

	@NotBlank(message = "Description Of Incident is mandatory")
	    @Column(name = "description_of_incident")
	    private String descriptionOfIncident;

	@NotBlank(message = "Supporting Document is mandatory")
	    @Column(name = "supporting_document")
	    private String supportingDocument;

		@Column(name ="documentName")
		private String documentName;

	    @Column(name = "is_declared")
	    private Boolean isDeclared;

	    @Column(name = "is_deleted")
	    private Boolean isDeleted = false;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "status")
	    private Status status;

	    @Column(name = "remark")
	    private String remark;

	    @CreationTimestamp
	    @Column(name = "created_at")
	    private LocalDateTime createdAt;

	    @UpdateTimestamp
	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;

	    @Column(name = "intCurrentRole")
	    private Integer intCurrentRole;

		@Column(name = "intCurrentStage")
		private Integer intCurrentStage;

		@Column(name = "vchOicLegalRemark")
		private String vchOicLegalRemark;

		@Column(name = "vchInspectorRemark")
		private String vchInspectorRemark;

		@Column(name = "vchOicLegalTwoRemark")
		private String vchOicLegalTwoRemark;

		@Column(name = "vchInspectionDocument")
		private String vchInspectionDocument;

		@Column(name = "vchApproverRemark")
		private String vchApproverRemark;

		@Column(name = "vchCeoSecondRemark")
		private String vchCeoSecondRemark;

		@Column(name = "vchSuspensionConclusion")
		private String vchSuspensionConclusion;

}
