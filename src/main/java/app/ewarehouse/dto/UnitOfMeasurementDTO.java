package app.ewarehouse.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UnitOfMeasurementDTO {

    private Integer unitId;
    private String unitName;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private Boolean deletedFlag;

    // Getters and Setters
}

