package app.ewarehouse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiptDealFinalDto {

   private String sales_application_id; 
   private String new_owner_id;
   private String depositor_id;
   private boolean status;
	
}
