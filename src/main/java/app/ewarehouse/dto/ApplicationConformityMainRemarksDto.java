package app.ewarehouse.dto;

import java.time.LocalDate;

import app.ewarehouse.entity.AocRemarksStatus;
import lombok.Data;

@Data
public class ApplicationConformityMainRemarksDto {

	private String applicantId;
	
	private String applicantName;
	
	private String remark;
	
	private LocalDate dateOfRemark;
	
	private Integer userId;
	
	private String userName;
	
	private String roleName;
	
	private Integer roleId;
	
	private AocRemarksStatus applicationStatus;
	
	
	
}
