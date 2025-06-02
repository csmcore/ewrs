package app.ewarehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "m_status_master")
public class StatusMaster  {
    
    @Id
    @Column(name = "statusid")
    private Integer statusId;

    @Column(name = "statuspresent", nullable = false)
    private String statusPresent;

    @Column(name = "description")
    private String description;
}