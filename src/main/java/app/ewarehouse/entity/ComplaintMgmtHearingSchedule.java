package app.ewarehouse.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_complaint_mgmt_hearing_schedule")
public class ComplaintMgmtHearingSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hearingId")
    private Integer hearingId;

    @Column(name = "complaintId", nullable = false)
    private Integer complaintId;
    
    @Column(name = "reasonForHearing", nullable = false, length = 255)
    private String reasonForHearing;

    @Column(name = "dateOfHearing", nullable = false)
    private LocalDate dateOfHearing;

    @Column(name = "timeOfHearing", nullable = false)
    private LocalTime timeOfHearing;

    @Column(name = "documentsToBeProduced", nullable = false, length = 255)
    private String documentsToBeProduced;

    @Column(name = "remedialAction", nullable = false, length = 255)
    private String remedialAction;

    @Column(name = "isHearingScheduled", nullable = false)
    private Boolean isHearingScheduled;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @Column(name = "updatedDate")
    private LocalDateTime updatedDate;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;
}

