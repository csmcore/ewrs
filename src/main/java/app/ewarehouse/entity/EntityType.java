package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "m_master_entity_type")
public class EntityType {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long intEntityTypeId;

    @Column(name = "vchEntityTypeName", length = 50)
    private String vchEntityTypeName;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @Column(name = "dtmCreatedOn")
    @CreationTimestamp
    private LocalDateTime dtmCreatedOn;

    @Column(name = "stmUpdatedOn")
    @UpdateTimestamp
    private Instant stmUpdatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;
	
}
