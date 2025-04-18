package app.ewarehouse.dto;

import app.ewarehouse.entity.CheckList;
import app.ewarehouse.entity.Remark;
import app.ewarehouse.entity.Stakeholder;
import app.ewarehouse.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectorDTO {

    private Integer id;
    private String intId;
    private String name;
    private String email;
    private String mobileNumber;
    private Status status;
    private String address;
    private List<CheckList> checkList;
    private List<Remark> remarks;
    private Stakeholder forwardedTo;
    private Stakeholder forwardedBy;
    private Date dtmCreatedOn;
    private Date stmUpdatedOn;
}
