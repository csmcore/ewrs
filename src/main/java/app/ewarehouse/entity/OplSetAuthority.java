package app.ewarehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "t_opl_set_authority")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OplSetAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intSetAuthId")
    private Integer setAuthId;

    @Column(name = "intProcessId", nullable = false)
    private Integer processId;

    @Column(name = "intProjectId", nullable = false)
    private Integer projectId;

    @Column(name = "vchSubStage", length = 255)
    private String subStage;

    @Column(name = "intStageNo")
    private Integer stageNo;

    @Column(name = "intRoleId")
    private Integer roleId;

    @Column(name = "intPrimaryAuth")
    private Integer primaryAuth;

    @Column(name = "tinSignature")
    private Byte signature;

    @Column(name = "intSignatoryAuth")
    private Integer signatoryAuth;

    @Column(name = "vchAuthTypes", length = 255)
    private String authTypes;

    @Column(name = "dtmCreatedOn", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdOn;

    @Column(name = "stmUpdatedOn")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedOn;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "intATAProcessId")
    private Integer ataProcessId;

    @Column(name = "intApprovingAuth")
    private Integer approvingAuth;

    @Column(name = "intSetAuthLinkId")
    private Integer setAuthLinkId;

    @Column(name = "intLabelId")
    private Integer labelId;

    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedOn = LocalDateTime.now();
    }
}
