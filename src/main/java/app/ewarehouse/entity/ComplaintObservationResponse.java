package app.ewarehouse.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "complaint_observation_response")
public class ComplaintObservationResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intIdObRes")
    private Integer id;

    @Column(name = "intObResRole")
    private Integer obResRole;

    @Column(name = "intObResStage")
    private Integer obResStage;

    @Column(name = "intObResAPId")
    private Integer obResAPId;

    @Column(name = "vchObResQuId", length = 45)
    private String obResQuId;

    @Column(name = "vchObRes", length = 45)
    private String obRes;

    @CreationTimestamp
    @Column(name = "edt")
    @Temporal(TemporalType.DATE)
    private Date edt;

    @UpdateTimestamp
    @Column(name = "ludt")
    @Temporal(TemporalType.DATE)
    private Date ludt;

    @Column(name = "intNotingId")
    private Integer notingId;

    @Column(name = "bitDeleteFlag")
    private Boolean deleteFlag = false;

    @Column(name = "intObResLableId", length = 45)
    private String obResLabelId;
}
