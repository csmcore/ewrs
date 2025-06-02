package app.ewarehouse.master.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FormConfigurationActualResponse {

	private Integer id;
	private Integer typeMaster;
	private String format;
	private Integer size;
	private String documentName;
	private Boolean isMandetory;
	private Boolean isActive;
	private Integer finalStatus;
	private String typeName;
}
