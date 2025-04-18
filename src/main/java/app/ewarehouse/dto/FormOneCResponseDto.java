package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FormOneCResponseDto {
	private String fileName;
    private String uploadedFileName;
    private String formOneCId;
    private Long doCId;
}
