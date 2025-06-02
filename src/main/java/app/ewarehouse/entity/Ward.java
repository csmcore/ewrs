package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "t_ward")
@DynamicUpdate
public class Ward implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intId")
    private Integer intId;

    @NotBlank(message = "Ward name is mandatory")
    @Column(name = "vchWardName")
    private String vchWardName;

    @ManyToOne
    @JoinColumn(name = "intSubCountyId")
    @JsonBackReference
    private SubCounty subCounty;

    @Column(name = "dtmCreatedOn")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date dtmCreatedOn;

    @Column(name = "stmUpdatedOn")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date stmUpdatedOn;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;

    @Column(name = "intApprovedBy")
    private Integer intApprovedBy;
}
