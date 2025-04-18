package app.ewarehouse.dto;

import app.ewarehouse.entity.draftStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TapplicationOfCertificateDtoResponse {
    private draftStatus status;
    private Integer RoleId;
}
