package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "t_focus_area_item")
public class FocusAreaItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intId")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "vchFocusArea")
    private FocusArea focusArea;

    @Column(name = "vchFocusAreaItem")
    private String focusAreaItem;

    @Column(name = "bitFocusAreaItemValue")
    private Boolean focusAreaItemValue;

    @ManyToOne
    @JoinColumn(name = "intInspectionObjectivesId")
    private InspectionObjective inspectionObjective;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmCreatedAt")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date createdAt;

    @Column(name = "stmUpdatedAt")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date updatedAt;

    @Column(name = "bitDeleteFlag")
    private Boolean deleteFlag = false;
}
