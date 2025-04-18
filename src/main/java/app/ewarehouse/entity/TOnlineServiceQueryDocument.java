package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name="t_online_service_query_document")
public class  TOnlineServiceQueryDocument {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intAppDocId;
	
	private Integer intNotingId;
	
	private Integer intOnlineServiceId;
	
	private String vchDocumentName;
	
	private String vchDocumentFile;
	
	private Integer  intProfileId=0;
	
	private Integer intCreatedBy;
	@CreationTimestamp
	private Date stmCreatedOn;
	
	private Integer intUpdatedBy;
	@UpdateTimestamp
	private Date dtmUpdatedOn;
	
	private Boolean bitDeletedFlag=false;
	
	
}
