package app.ewarehouse.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_complaint_mgmt_insp_schedule")
public class ComplaintMgmtInspSchedule implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2925770786909701660L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspectionId", nullable = false)
    private Integer inspectionId;

    @Column(name = "complaintId", nullable = false)
    private Integer complaintId;

    @Column(name = "inspectorId", nullable = false)
    private Integer inspectorId;

    @Column(name = "inspectorName", length = 50, nullable = false)
    private String inspectorName;

    @Column(name = "inspectionDate", nullable = false)
    private LocalDate inspectionDate;

    @Column(name = "inspectionTime", nullable = false)
    private LocalTime inspectionTime;

    @Column(name = "inspectionStartRemarks", length = 200)
    private String inspectionStartRemarks;

    @Column(name = "inspectionCompleteDate")
    private LocalDate inspectionCompleteDate;

    @Column(name = "inspectionCompleteTime")
    private LocalTime inspectionCompleteTime;

    @Column(name = "inspectionCompleteRemarks", length = 200)
    private String inspectionCompleteRemarks;

    @Column(name = "docId")
    private Integer docId;

    @Column(name = "createdDate", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updatedDate", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean bitDeletedFlag = false;
    
    @Column(name = "intRoleId")
    private Integer intRoleId;
    
    @Column(name = "vchInspStatus")
    private String vchInspStatus;
    
    @Column(name = "bitTakeActionStatus")
    private Boolean bitTakeActionStatus = false;
    
}
