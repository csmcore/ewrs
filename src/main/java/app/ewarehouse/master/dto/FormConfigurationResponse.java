package app.ewarehouse.master.dto;

import lombok.Data;

@Data
public class FormConfigurationResponse {

	private FormConfigurationBean formConfigurationBean;
	
	private String status;
	
	private String message;
	
}
