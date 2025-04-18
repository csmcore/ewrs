package app.ewarehouse.master.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CheckerRequestBean {

	private Integer formId;
	private String  createdBy;
	private Integer userId;
	private String remark;
	private Integer statusType;
	
}
