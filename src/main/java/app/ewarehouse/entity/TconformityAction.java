package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_conformity_action")
public class TconformityAction {

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "intId")
	    private Integer intId;

	    @Column(name = "Remark")
	    private String txtRemark;
	    
	    @Column(name = "Role")
	    private Integer Role;
	        
	    @ManyToOne
	    @JoinColumn(name = "vchParticularOfApplicantsId")
	    private ApplicationOfConformity applicationConformity;
	    
	    @Column(name = "vchInspectorFilePath")
	    private String txtInspectorFilePath;
	    
	    @Column(name="intCreatedBy")
		private Integer intCreatedBy;
		
		@Column(name = "intUpdatedBy")
		private Integer intUpdatedBy;
		
		@Column(name = "dtmCreatedOn")
		@CreationTimestamp
		private Date dtmCreatedOn;
		
		@Column(name = "stmUpdatedOn")
		@UpdateTimestamp
		private Date stmUpdatedOn;
		
		@Column(name = "bitDeletedFlag")
		private Boolean bitDeletedFlag = false;
}
