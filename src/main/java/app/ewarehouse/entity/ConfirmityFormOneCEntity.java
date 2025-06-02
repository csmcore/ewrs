package app.ewarehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_confirmity_formOneC")
@Getter
@Setter
public class ConfirmityFormOneCEntity {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "intDocId")
	    private Long id;

	    @Column(name = "vchDocName", nullable = false)
	    private String docName;

	    @Column(name = "vchFileName", nullable = false)
	    private String fileName;

	    @Column(name = "vchWareHouseId", nullable = false)
	    private String wareHouseId;

	    @Column(name = "intCreatedBy", nullable = false)
	    private Integer createdBy;

	    @Column(name = "dtCreatedOn")
	    private LocalDateTime createdOn;

	    @Column(name = "bitDeletedFlag")
	    private Boolean deletedFlag;
	    
	    @Column(name = "vchFormOneCId")
	    private String formOneCId;
	    
}
