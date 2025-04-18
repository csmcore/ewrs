package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberDTO {

    private Integer intTeamMemberId;
    private String vchTeamMemberName;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Date dtmCreatedAt;
    private Date stmUpdatedAt;
    private Boolean bitDeleteFlag;
}
