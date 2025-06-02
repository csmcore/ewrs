package app.ewarehouse.dto;

import lombok.Data;

@Data
public class ApplicationFeeRequestDto {
	private Integer id;
	private String applicationType;
	private Double applicationFee;
}
