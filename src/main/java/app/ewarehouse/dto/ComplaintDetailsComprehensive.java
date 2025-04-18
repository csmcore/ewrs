package app.ewarehouse.dto;

import app.ewarehouse.entity.ComplaintObservationResponse;
import app.ewarehouse.entity.OnlineServiceApprovalNotings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintDetailsComprehensive {

        private ComplaintmanagementResponse complaintManagementResponse;
        private OnlineServiceApprovalNotings approvalNotings;
        private List<ComplaintObservationResponse> observationResponses;

}
