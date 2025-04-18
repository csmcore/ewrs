package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_routine_compliance_inspector_agricultural_commodities")
public class RoutineComplianceInspectorAgriculturalCommodity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agricultural_commodities_id")
    private Long agriculturalCommoditiesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "int_location_id", nullable = false)
    @JsonBackReference
    private RoutineComplianceInspectorLocation routineComplianceInspectorLocation;

    @Column(name = "vch_commodity_warehouse")
    private String vchCommodityWarehouse;

    @Column(name = "int_capacity")
    private Integer intCapacity;

    @Column(name = "dec_dimensions")
    private Double decDimensions;

    @Column(name = "int_harvest_period")
    private String intHarvestPeriod;
    
    @Column(name = "bitDeleteFlag",columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean bitDeleteFlag = false;
    
    @Override
    public String toString() {
        return "RoutineComplianceInspectorAgriculturalCommodity{" +
                "agriculturalCommoditiesId=" + agriculturalCommoditiesId +
                ", vchCommodityWarehouse='" + vchCommodityWarehouse + '\'' +
                ", intCapacity=" + intCapacity +
                ", decDimensions=" + decDimensions +
                ", intHarvestPeriod=" + intHarvestPeriod +
                ", bitDeleteFlag=" + bitDeleteFlag +
                '}';
    }

}
