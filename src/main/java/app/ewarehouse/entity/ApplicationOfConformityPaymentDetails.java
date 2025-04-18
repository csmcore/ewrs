package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "t_application_of_conformity_payment_details")
public class ApplicationOfConformityPaymentDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intPaymentId")
	private Integer id;

	@Column(name = "vchPaymentType")
	private String paymentType;

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
	
	@Enumerated(EnumType.STRING)
    @Column(name = "enmPaymentStatus")
    private PaymentStatus enmPaymentStatus;
	
	
	@ManyToOne
    @JoinColumn(name = "intParticularOfApplicantsId" , referencedColumnName = "intParticularOfApplicantsId")
    private ApplicationOfConformityParticularOfApplicants particularOfApplicants;

}
