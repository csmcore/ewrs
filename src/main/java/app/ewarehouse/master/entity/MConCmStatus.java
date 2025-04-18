package app.ewarehouse.master.entity;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "m_con_cm_status")
@Data

public class MConCmStatus {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intStatusId")
    private Integer statusId;

    @Column(name = "vchStatus", length = 255, nullable = false)
    private String status;

    @Column(name = "vchStatusDescription", length = 500)
    private String statusDescription;

}
