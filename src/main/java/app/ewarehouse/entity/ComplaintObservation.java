package app.ewarehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "complaint_observation")
@Data
public class ComplaintObservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intIdobservation")
    private Integer intIdObservation;

    @Column(name = "vchObservation", nullable = false)
    private String vchObservation;

    @Column(name = "intLableId", nullable = false)
    private Integer intLableId;

    @Column(name = "intRoleId", nullable = false)
    private Integer intRoleId;

    @Column(name = "intStageId", nullable = false)
    private Integer intStageId;

 
}