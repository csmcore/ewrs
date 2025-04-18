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
@Table(name = "t_rout_com_goods_storage")
public class RoutineComplianceInspectorConditionOfGoodsStorage {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_condition_of_goods_storage_id")
    private Long intConditionOfGoodsStorageId;

    @Column(name = "vch_packing_material_comments")
    private String vchPackingMaterialComments;

    @Column(name = "bit_stack_condition")
    private Boolean bitStackCondition;

    @Column(name = "vch_stack_condition_comments")
    private String vchStackConditionComments;

    @Column(name = "bit_external_condition")
    private Boolean bitExternalCondition;

    @Column(name = "vch_external_condition_comments")
    private String vchExternalConditionComments;

    @Column(name = "vch_marks_on_bags_comments")
    private String vchMarksOnBagsComments;

    @Column(name = "bit_stocks_commingled")
    private Boolean bitStocksCommingled;

    @Column(name = "vch_stocks_commingled_comments")
    private String vchStocksCommingledComments;

    @Column(name = "bit_receiving_records")
    private Boolean bitReceivingRecords;

    @Column(name = "vch_receiving_records_comments")
    private String vchReceivingRecordsComments;

    @Column(name = "bit_quantity_analysis")
    private Boolean bitQuantityAnalysis;

    @Column(name = "vch_quantity_analysis_comments")
    private String vchQuantityAnalysisComments;

    @Column(name = "bit_pest_control_records")
    private Boolean bitPestControlRecords;

    @Column(name = "vch_pest_control_records_comments")
    private String vchPestControlRecordsComments;

    @Column(name = "bit_stock_records")
    private Boolean bitStockRecords;

    @Column(name = "vch_stock_records_comments")
    private String vchStockRecordsComments;

    @Column(name = "bit_weighing_records")
    private Boolean bitWeighingRecords;

    @Column(name = "vch_weighing_records_comments")
    private String vchWeighingRecordsComments;

    @Column(name = "bit_grain_monitoring_system")
    private Boolean bitGrainMonitoringSystem;

    @Column(name = "vch_grain_monitoring_system_comments")
    private String vchGrainMonitoringSystemComments;

    @Column(name = "bit_aeration_system")
    private Boolean bitAerationSystem;

    @Column(name = "vch_aeration_system_comments")
    private String vchAerationSystemComments;

    @Column(name = "bit_housekeeping_sanitation")
    private Boolean bitHousekeepingSanitation;

    @Column(name = "vch_housekeeping_sanitation_comments")
    private String vchHousekeepingSanitationComments;

    @Column(name = "bit_pest_control_program")
    private Boolean bitPestControlProgram;

    @Column(name = "vch_pest_control_program_comments")
    private String vchPestControlProgramComments;

    @Column(name = "bit_pcpb_list")
    private Boolean bitPCPBList;

    @Column(name = "vch_pcpb_list_comments")
    private String vchPCPBListComments;

    @Column(name = "bit_weighing_equipment_certified")
    private Boolean bitWeighingEquipmentCertified;

    @Column(name = "vch_weighing_equipment_certified_comments")
    private String vchWeighingEquipmentCertifiedComments;

    @Column(name = "vch_weighing_calibration_date")
    private String vchWeighingCalibrationDate;

    @Column(name = "vch_weighing_calibration_by")
    private String vchWeighingCalibrationBy;

    @Column(name = "bit_moisture_meter_certified")
    private Boolean bitMoistureMeterCertified;

    @Column(name = "vch_moisture_meter_certified_comments")
    private String vchMoistureMeterCertifiedComments;

    @Column(name = "bit_grading_scale_certified")
    private Boolean bitGradingScaleCertified;

    @Column(name = "vch_grading_scale_certified_comments")
    private String vchGradingScaleCertifiedComments;

    @Column(name = "bit_aflatoxin_testing_certified")
    private Boolean bitAflatoxinTestingCertified;

    @Column(name = "vch_aflatoxin_testing_certified_comments")
    private String vchAflatoxinTestingCertifiedComments;

}
