package app.ewarehouse.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "m_company_warehouse_details")
public class BuyerWareHosueDetails {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intCompanyWhId")
    private Integer intCompanyWhId;  

    @Column(name = "vchCompanyId")
    private String vchCompanyId;

    @Column(name = "vchWarehouseId")
    private String vchWarehouseId;

    @Column(name = "vchCompanyName")
    private String vchCompanyName;

    @Column(name = "vchWarehouseName")
    private String vchWarehouseName;
    
    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;
    
//   @OneToMany(mappedBy = "wareHouseName", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//   private List<BuyerDepositorAndWareHouseOperator> depositorOperators;

}
