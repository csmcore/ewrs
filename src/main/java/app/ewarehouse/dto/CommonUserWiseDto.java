package app.ewarehouse.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CommonUserWiseDto {
	
    //private JSONArray activityType; 
	private List<Map<String, Object>> activityType;
    private Integer categoryId;    
    private Integer subCategoryId; 
    private List<String> messageMode; 
    private String message;      
    private String phoneNumber;  
    private String email;
    private String activity;
    private String userName;
    

}
