package app.ewarehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "m_company_warehouse_details")
public class CompanyWarehouseDetails {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intCompanyWhId")
    private Integer comWhId;  // Primary Key with Auto Increment

    @Column(name = "vchCompanyId")
    private String companyId;

    @Column(name = "vchWarehouseId")
    private String warehouseId;

    @Column(name = "vchCompanyName")
    private String companyName;

    @Column(name = "vchWarehouseName")
    private String warehouseName;
    
    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;

}
