package app.ewarehouse.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_employment_details")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "payroll_no")
    private String payrollNo;

    @Column(name = "contract")
    private String contract;

    @Column(name = "name_of_current_employer")
    private String nameOfCurrentEmployer;

    @Column(name = "no_of_years_with_employer")
    private Integer noOfYearsWithEmployer;

    @Column(name = "current_salary_pm")
    private BigDecimal currentSalaryPM;

    @Column(name = "monthly_expenditure")
    private BigDecimal monthlyExpenditure;

    @Column(name = "terms")
    private String terms;

    @Column(name = "work_physical_address")
    private String workPhysicalAddress;

    @Column(name = "telephone_office")
    private String telephoneOffice;

    @Column(name = "position")
    private String position;

    @Column(name = "self_employed")
    private String selfEmployed;

    @Column(name = "department")
    private String department;

    @Column(name = "employment_terms")
    private String employmentTerms;

    @Column(name = "employer_email")
    private String employerEmail;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    
    @OneToOne
    @JoinColumn(name = "vchPdResidentialId", referencedColumnName = "vchPdResidentialId")
    @JsonBackReference
    private PledgingDischargeResidential pledgingDischargeResidential;
}

