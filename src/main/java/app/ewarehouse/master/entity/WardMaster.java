package app.ewarehouse.master.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import app.ewarehouse.entity.Tuser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "m_master_ward")
public class WardMaster {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intWardMasterId")
    private Integer intWardMasterId;

    @Column(name = "intCountyId", nullable = false)
    private Integer intCountyId;

    @Column(name = "intSubCountyId", nullable = false)
    private Integer intSubCountyId;
    
    @Column(name = "wardName", nullable = false)
    private String wardName;

    @Column(name = "dtmCreatedOn", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dtmCreatedOn;

    @Column(name = "stmUpdatedOn" ,columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedOn;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean bitDeletedFlag = false;
 

}
