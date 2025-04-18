package app.ewarehouse.master.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pravat.behera
 * @Date 24-03-2025 12:40
 * @Description  Type master bean class for extranal response and request data bind  
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeMasterBean {

	private Integer typeId;
    private String type;
    private String description;
    private String createdBy;
    @JsonIgnore
    private Date createdOn;
    private String updatedBy;
    @JsonIgnore
    private Date updatedOn;
    private Boolean deletedFlag;
	
}
