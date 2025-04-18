package app.ewarehouse.dto;

import app.ewarehouse.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedStatusRequest {
	private String applicationId;
	private Status enmStatus;
}
