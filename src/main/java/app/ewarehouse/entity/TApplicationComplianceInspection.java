package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "t_application_compliance_inspection")
public class TApplicationComplianceInspection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer intId;

    @Column(name = "dtmInspectionDate")
    private Date dtmInspectionDate;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "tmInspectionTime", columnDefinition = "TIME")
    private Date tmInspectionTime;

    @Column(name = "dtmEndInspectionDate")
    private Date dtmEndInspectionDate;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "tmEndInspectionTime", columnDefinition = "TIME")
    private Date tmEndInspectionTime;

    @Column(name = "vchReportFilePath")
    private String vchReportFilePath;

    @Column(name = "vchRemarks")
    private String vchRemarks;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedAt")
    private Date dtmCreatedAt;

    @UpdateTimestamp
    @Column(name = "stmUpdatedAt")
    private Date stmUpdatedAt;

    @Column(name = "bitDeleteFlag")
    private Boolean bitDeleteFlag = false;
}
