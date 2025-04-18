package app.ewarehouse.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ConformityCertificateDto {

	private String certificateId;
	
	private String companyName;
	
	private String countyName;
	
	private String postalCode;
	
	private String lrNumber;
	
	private String approverName;
	
	private String sealPath;
	
	private String applicationId;
	
	private String applicantName;
	
	private LocalDate dateOfIssue;
	
	private LocalDate validFrom;
	
	private LocalDate validTo;
	
	private String fee;
}
