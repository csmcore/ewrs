package app.ewarehouse.dto;

import app.ewarehouse.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TakeActionRequest <T> {
    private Status action;
    private Integer officerRole;
    private Integer userId;
    private T data;
}
