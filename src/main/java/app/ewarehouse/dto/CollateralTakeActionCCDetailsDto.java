package app.ewarehouse.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CollateralTakeActionCCDetailsDto {
	
	 private Integer ccDetailsId;
	    private String applicationId;
	    private Integer allocateCCId;
	    private String supportingDocumentName;
	    private String remarkByCC;
	    private String supportingDocument;
	    private Boolean isEdit;
	    private String ccName; 
	  
}
