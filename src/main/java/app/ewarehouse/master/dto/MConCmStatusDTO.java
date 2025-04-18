package app.ewarehouse.master.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MConCmStatusDTO {
    private Integer statusId;
    private String status;
    private String statusDescription;

    public MConCmStatusDTO(Integer statusId, String status, String statusDescription) {
        this.statusId = statusId;
        this.status = status;
        this.statusDescription = statusDescription;
    }
}