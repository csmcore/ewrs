package app.ewarehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="m_temp_user")
public class TempUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "applicant_name")
	private String applicantName;
	
	@Column(name = "mobile")
	private String mobile;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "postal_address")
	private String postalAddress;
	
	@Column(name = "postal_code")
	private String postalCode;
	
	@Column(name = "town")
	private String town;
	
	@Column(name = "otp")
	private String otp;
	
	@Column(name = "company_id")
	private String companyId;
	
	@Column(name = "warehouse_id")
	private String warehouseId;
	
	@Column(name = "intEntityId")
	private Integer intEntityId;
	
	@Column(name = "intDepositorId")
	private Integer intDepositorId;
	
	@Column(name = "vchDepositorName")
	private String depositorName;
	
	@Column(name = "int_usertype")
	private Integer userType;
}
