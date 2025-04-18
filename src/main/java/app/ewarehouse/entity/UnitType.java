package app.ewarehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "m_unit_type")
public class UnitType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(name = "intUnitType")
    private Integer intUnitType;
    
	@Column(name = "vchUnitType")
	private String vchUnitType;
    
	@Column(name = "bitDeletedFlag")
	private Boolean bitDeletedFlag = false;
	

}
