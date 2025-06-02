package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name="t_set_workflow")
public class TSetWorkFlow implements Serializable {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)	//MySql
	private Integer workflowId;
	private Integer projectId;
	private Integer serviceId;
	@Column(length=10485760)
	private String canvasData;
	private Integer deletedFlag;
	private Integer createdBy;
	private Date createdOn;
	private Integer tinType;
	private String vchCtrlName;
	private Integer intLabelId;
	private String vchDynFilter;
	private String vchDynFilterCtrlId;
	private String vchMailSmsConfigIds;
}
