package app.ewarehouse.master.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author pravat.behera
 * @Date 24-03-2025 12:44 PM
 * @Description Form Configuration bean class for extranal response and request
 *              data bind
 */
@Data
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class FormConfigurationBean {

	private Integer id;
	private Integer typeMaster;
	private String format;
	private Integer size;
	private String documentName;
	private Boolean isMandetory;
	private Boolean isActive;
	private Integer finalStatus;
	private String createdBy;
	private Date createdOn;
	private String updatedBy;
	private Date updatedOn;
	private Boolean deletedFlag;
	private Integer checkerStatus;
	private String checkerUpdatedBy;
	private Date checkerUpdatedOn;
	private String typeName;
	private String finalStatusValue;
	private Integer userId;
	private Integer statustype;
}
