package app.ewarehouse.dto;

import app.ewarehouse.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesApplicationMainResponse {
        private String salesApplicationMainId;
        private SalesApplicationTabOneDTO salesApplicationTabOneDTO;
        private SalesApplicationTabTwoDTO salesApplicationTabTwoDTO;
        private SalesApplicationTabThreeDTO salesApplicationTabThreeDTO;
        private ApplicationStatus applicationStatus;
        private Integer intCreatedBy;
        private Integer intUpdatedBy;
        private Date dtmCreatedOn;
        private Date stmUpdatedAt;
        private Boolean bitDeleteFlag;
}
