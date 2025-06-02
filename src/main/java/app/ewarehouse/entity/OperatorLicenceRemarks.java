package app.ewarehouse.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = "operatorLicence")
@Table(name = "t_operator_licence_remarks")
public class OperatorLicenceRemarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "operator_licence_id", nullable = false)
    private OperatorLicence operatorLicence;

    @Enumerated(EnumType.STRING)
    @Column(name = "stakeholder", nullable = false)
    private Stakeholder stakeholder;

    @Column(name = "remark", nullable = false, length = 500)
    private String remark;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Date createdOn;
}
