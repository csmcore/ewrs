package app.ewarehouse.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "t_grader_weigher_reg")
public class GraderWeigherForm {
	    
	    @Id
	    @Column(name = "vchgraderweigherId",length = 20)
	    private String graderWeigherId;

	    @Column(name = "vchdesignationtype")
	    private String designationType;
	    
	    @Column(name = "vchFullName")
	    private String fullName;
	    
	    @Column(name = "dateofbirth")
	    private String dateOfBirth;
	    
	    @Column(name = "vchGender")
	    private String gender;
	    
	    @Column(name = "vchMobileno")
	    private String mobileNo;
	    
	    @Column(name = "vchEmail")
	    private String emailId;
	    
	    @Column(name = "vchAlternateMobileNo")
	    private String alternateContactNo;
	    
	    @Column(name = "vchAddress")
	    private String address;
	    
	    @Column(name = "intEmpId")
	    private String employeeId;
	    
	    @Column(name = "vchStartDate")
	    private String startDate;
	    
	    @Column(name = "vchEndDate")
	    private String endDate;
	    
	    @Column(name = "govtIssuedId")
	    private String govtIssuedId;
	    
//	    @Column(name = "vchIdType")
//	    private String idType;
	    
	    @Column(name = "vchIdNo")
	    private String idNo;
	    
	    @Column(name = "vchExpiryDate")
	    private String expiryDate;
	    
//	    @Column(name = "uploadedDocument")
//	    private String uploadedCertificatePath;
	   
	    @Column(name = "intCreatedBy")
	    private Integer createdBy;

	    @Column(name = "dtmCreatedOn", nullable = false, updatable = false)
	    @Temporal(TemporalType.TIMESTAMP)
	    @CreationTimestamp
	    private Date createdOn;

	    @Column(name = "intUpdatedBy")
	    private Integer updatedBy;

	    @Column(name = "dtmUpdatedOn")
	    @Temporal(TemporalType.TIMESTAMP)
	    @UpdateTimestamp
	    private Date updatedOn;

	    @Column(name = "bitDeletedFlag")
	    private Boolean deletedFlag = false;

}
