package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "m_warehouse_user_emp_type")
public class WarehouseUserEmpType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intWarehouseEmpTypeId", nullable = false, unique = true)
    private Integer warehouseEmpTypeId;

    @Column(name = "vchWarehouseEmpTypeName", length = 100, nullable = false)
    private String warehouseEmpTypeName;

    @Column(name = "intRoleId", nullable = false)
    private Integer roleId;

    @Column(name = "intCreatedBy", nullable = false)
    private Integer createdBy;

    @Column(name = "dtmCreatedOn", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmUpdatedOn")
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean deletedFlag;
}
