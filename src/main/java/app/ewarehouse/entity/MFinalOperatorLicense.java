package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "m_final_operator_license_master")
public class MFinalOperatorLicense implements Serializable {

    @Id
    @GeneratedValue(generator = "operator-license-custom-id")
    @GenericGenerator(
            name = "operator-license-custom-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "m_final_operator_license_master"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "vchLicenseNo")
            })
    @Column(name = "vchLicenseNo")
    private String vchLicenseNo;

    @OneToOne
    @JoinColumn(name = "vchApplicationId" ,referencedColumnName="vchApplicationNo" )
    private OperatorLicence operatorLicenceApplication;

    @ManyToOne
    @JoinColumn(name = "vchConformityId")
    private ApplicationOfConformity applicationOfConformity;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmStatus")
    private CreatedStatus enmStatus;

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

