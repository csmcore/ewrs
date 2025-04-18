package app.ewarehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "t_Inspection_MemberOPL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InspectionMemberOPL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intId")
    private Integer id;

    @Column(name = "intInspMemberUserId")
    private Integer inspMemberUserId;

    @Column(name = "intInspMemberRoleId")
    private Integer inspMemberRoleId;

    @Column(name = "vchInspMemberName", length = 45)
    private String inspMemberName;

    @Column(name = "intTeamLeadId")
    private Integer teamLeadId;

    @Column(name = "dtmCreateOn", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createOn = LocalDateTime.now();

    @Column(name = "dtmUpdatedOn")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedOn = LocalDateTime.now();

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;
}
