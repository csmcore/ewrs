package app.ewarehouse.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@Table(name = "t_complaint_mgmt_insp_schedule_docs")
public class ComplaintMgmtInspScheduleDocs implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3023966310096493662L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docId", nullable = false)
    private Integer docId;
	@Column(name = "intParentId", nullable = false)
    private Integer parentId;
    @Column(name = "docName", length = 200, nullable = false)
    private String docName;

    @Column(name = "docPath", length = 500, nullable = false)
    private String docPath;

    @Column(name = "createdBy", nullable = false)
    private Integer createdBy;

    @Column(name = "updatedBy", nullable = false)
    private Integer updatedBy;

    @Column(name = "createdOn", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "updatedOn", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean bitDeletedFlag;
}
