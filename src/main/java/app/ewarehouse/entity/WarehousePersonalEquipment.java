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
@Table(name = "t_rout_com_warehouse_personal_equipment")
public class WarehousePersonalEquipment {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_warehouse_personal_equipment_id")
    private Long intWareHousePersonalEquipmentId;

    @Column(name = "bit_necessary_equipment_available")
    private Boolean bitNecessaryEquipmentAvailable;

    @Column(name = "vch_necessary_equipment_comments")
    private String vchNecessaryEquipmentComments;

    @Column(name = "bit_drinking_water_available")
    private Boolean bitDrinkingWaterAvailable;

    @Column(name = "vch_drinking_water_available")
    private String vchDrinkingWaterAvailable;

    @Column(name = "bit_essential_ppe_available")
    private Boolean bitEssentialPPEAvailable;

    @Column(name = "vch_essential_ppe_comments")
    private String vchEssentialPPEComments;

    @Column(name = "bit_training_and_maintenance_adequate")
    private Boolean bitTrainingAndMaintenanceAdequate;

    @Column(name = "vch_training_and_maintenance_comments")
    private String vchTrainingAndMaintenanceComments;

    @Column(name = "bit_bobcats")
    private Boolean bitBobcats;

    @Column(name = "bit_pallets_dunnage")
    private Boolean bitPalletsDunnage;

    @Column(name = "bit_gunny_bags")
    private Boolean bitGunnyBags;

    @Column(name = "bit_forklifts_handfork")
    private Boolean bitForkliftsHandfork;

    @Column(name = "bit_plastic_sheets")
    private Boolean bitPlasticSheets;

    @Column(name = "bit_drier")
    private Boolean bitDrier;

    @Column(name = "bit_hooks")
    private Boolean bitHooks;

    @Column(name = "bit_tarpaulin")
    private Boolean bitTarpaulin;

    @Column(name = "bit_sampling_spikes")
    private Boolean bitSamplingSpikes;

    @Column(name = "bit_weighing_scales")
    private Boolean bitWeighingScales;

    @Column(name = "bit_elevators")
    private Boolean bitElevators;

    @Column(name = "bit_slieves")
    private Boolean bitSlieves;

    @Column(name = "bit_moisture_meter")
    private Boolean bitMoistureMeter;

    @Column(name = "bit_elisa_kit_meter")
    private Boolean bitElisaKitMeter;

}
