package app.ewarehouse.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_application_fee_config")
public class ApplicationFeeConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicationFeeId")
    private Integer applicationFeeId;

    @Column(name = "applicationType", nullable = false, length = 150)
    private String applicationType;

    @Column(name = "applicationTypeAliasName", length = 20)
    private String applicationTypeAliasName;

    @Column(name = "paymentValue")
    private Double paymentValue;

    @Column(name = "intCreatedBy", nullable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn")
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean deletedFlag = false;
}

