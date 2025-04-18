package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity @Getter @Setter @ToString @Builder @AllArgsConstructor @NoArgsConstructor
@Table(name="t_commodity_master")

public class Commodity  {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer Id;

	@Column(name="commoditytype")
	private Integer commodityType;
	
	@Column(name="name",unique = true)
	private String name;
	
	@Column(name = "commodityorigin")
	private String commodityOrigin;
	
	@Column(name="charges")
	private double charges;
	
	@ManyToOne
	@JoinColumn(name = "type_id")
	private CommodityType type;
	
	@Column(name = "commoditycode")
	private String commodityCode;

	@ManyToOne
	@JoinColumn(name = "seasonality_id")
	private Seasonality seasonality;

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
