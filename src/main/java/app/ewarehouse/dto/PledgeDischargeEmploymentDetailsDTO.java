package app.ewarehouse.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class PledgeDischargeEmploymentDetailsDTO {
	private Integer id;
	private String payrollNo;
    private String nameOfCurrentEmployer;
    private Integer noOfYearsWithEmployer;
    private BigDecimal currentSalaryPM;
    private BigDecimal monthlyExpenditure;
    private String terms;
    private String contract;
    private String selfEmployed;
    private String workPhysicalAddress;
    private String telephoneOffice;
    private String position;
    private String department;
    private String employmentTerms;
    @Email
    private String employerEmail;
}
