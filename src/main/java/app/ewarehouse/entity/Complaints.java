package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
@Table(name = "t_complaints")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Complaints {

    @Id
    @GeneratedValue(generator = "complainer-custom-id")
    @GenericGenerator(
            name = "complainer-custom-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_complaints"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "complaint_id")
            })
    @Column(name = "complaint_id")
    private String complaintId;

    @Enumerated(EnumType.STRING)
    @Column(name = "complaint_for", nullable = false)
    private ComplaintFor complaintFor;
    
    @ManyToOne
    @JoinColumn(name = "warehouse_operator_id")
    private WarehouseParticulars warehouseoperator;
    
    @Column(name="complainer_telephone")
    private Integer complainertelephone;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @Column(name="complainer_email")
    private String complaineremail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_category_id")
    private ComplaintsCategory complaintCategory;

    @Column(name = "date_of_incident", nullable = false)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfIncident;
    
    @Column(name = "remark")
    private String remark;

    @Column(name = "supporting_document")
    private String supportingDocument;

    @Column(name = "complaint_description", nullable = false, columnDefinition = "TEXT")
    private String complaintDescription;

    @Column(name = "specific_allegations", columnDefinition = "TEXT")
    private String specificAllegations;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private Date dtmCreatedAt;

    @UpdateTimestamp
    @Column(name = "stmUpdatedOn")
    private Date stmUpdatedAt;

    @Column(name = "bitDeleteFlag")
    private Boolean bitDeleteFlag = false;

}
