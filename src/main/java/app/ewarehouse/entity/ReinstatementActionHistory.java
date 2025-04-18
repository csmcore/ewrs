package app.ewarehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_reinstatement_action_history")
public class ReinstatementActionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intActionHistoryId")
    private Integer intActionHistoryId;

    @Column(name = "reinstatementId", nullable = false, length = 20)
    private String reinstatementId;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    @Column(name = "dtmActionTaken", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dtmActionTaken;

    @Column(name = "roleId")
    private Integer roleId;

    @Column(name = "rollName", length = 20)
    private String rollName;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "userName", length = 20)
    private String userName;
    
    @Column(name = "ceoSuspendStatus")
    private Integer ceoSuspendStatus;

    
}

