package app.ewarehouse.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "m_charges_headers")
public class ChargesHeader {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(name = "intChargesHeader")
    private Integer intChargesHeader;
    
	@Column(name = "vchChargesHeader")
	private String vchChargesHeader;
	
	@Column(name = "bitDeletedFlag")
	private Boolean bitDeletedFlag = false;
	
	@ManyToOne
	@JoinColumn(name = "intUnitType")
	private UnitType intUnitType;

}
