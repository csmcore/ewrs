package app.ewarehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_opl_InspReportDocs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InspReportDoc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docId")
    private Integer docId;
    
    @Column(name = "docName", nullable = false)
    private String docName;
    
    @Column(name = "docPath", nullable = false)
    private String docPath;
    
    @Column(name = "createdUserId", nullable = false)
    private Integer createdUserId;
    
    @Column(name = "updatedBy")
    private Integer updatedBy;
    
    @Column(name = "createdOn", nullable = false)
    private LocalDateTime createdOn;
    
    @Column(name = "updatedOn")
    private LocalDateTime updatedOn;
    
    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean bitDeletedFlag;
    
    @Column(name = "intParentId")
    private Integer intParentId;
    
    @Column(name = "createdRoleId")
    private Integer createdRoleId;
    
    @Column(name = "vchInsObserId", nullable = false)
    private String vchInsObserId;
    
    
}

