package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "otp", schema = "ewrsdevdb")
@Getter
@Setter
public class OTP {
	
	@Id
	@Column(name = "otpid", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int otpId;
	@Column(name = "otp", nullable = false)
	private int generatedOtp;
	@Column(name = "email", nullable = false)
	private String email;
	@Column(name = "mobile")
	private String mobile;
	@Column(name = "ip_address")
	private String ipAddress;
	@Column(name = "edt")
	private Date edt;
	@Column(name = "ludt")
	private Date ludt;
	@Column(name = "flag")
	private int flag;

}
