package app.ewarehouse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormOneCRequestDto {

	 private Integer userid;
	 private String warehouseId;
	 private String fileName;
	 private String uploadedFileName;
	 private String actionType;
	 private Integer roleid;
	 private String formOneCId;
	 private boolean draftEDit;
	 private Long docId;
}
