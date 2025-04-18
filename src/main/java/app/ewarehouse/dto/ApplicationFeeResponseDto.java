package app.ewarehouse.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationFeeResponseDto {
	private Integer id;
	private String applicationType;
	private Double applicationFee;
}
