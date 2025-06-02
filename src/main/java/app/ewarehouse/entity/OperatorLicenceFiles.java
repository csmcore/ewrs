package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity @Getter @Setter @ToString(exclude = "operatorLicence")
@Table(name = "t_operator_licence_files")
public class OperatorLicenceFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_path")
    private String filePath;
    @Column(name = "created_by")
    private String createdBy;
    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
    
    @Column(name = "file_type")
    private String type;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "operator_licence_id")
    private OperatorLicence operatorLicence;
}
