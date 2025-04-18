package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "t_company")
@DynamicUpdate
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intCompanyId")
    private Integer intCompanyId;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "vchName")
    private String vchName;

    @NotBlank(message = "Registration number is mandatory")
    @Column(name = "vchRegistrationNo")
    private String vchRegistrationNo;

    @NotNull(message = "Establishment date is mandatory")
    @Column(name = "dtmEstablishmentDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dtmEstablishmentDate;

    @ManyToOne
    @JoinColumn(name = "intLegalStatusId")
    private LegalStatus legalStatus;

    @NotBlank(message = "KRA PIN is mandatory")
    @Column(name = "vchKraPin")
    private String vchKraPin;

    @Email(message = "Email address is invalid")
    @Column(name = "vchEmail")
    private String vchEmail;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intCompanyLocationId")
    private CompanyLocation companyLocation;

    @Column(name = "dtmCreatedOn")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date dtmCreatedOn;

    @Column(name = "stmUpdatedOn")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date stmUpdatedOn;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;

    @Column(name = "intApprovedBy")
    private Integer intApprovedBy;
}
