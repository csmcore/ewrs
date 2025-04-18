package app.ewarehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "t_rout_com_it_system")
@Data
public class RoutineComplianceInspectorITSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_it_system_id")
    private Long intItSystemId;

    @Column(name = "bit_open_to_integration")
    private Boolean bitOpenToIntegration;

    @Column(name = "vch_open_to_integration_comments", length = 255)
    private String vchOpenToIntegrationComments;

    @Column(name = "vch_warehouse_management_comments", length = 255)
    private String vchWarehouseManagementComments;

    @Column(name = "vch_operating_system_software", length = 255)
    private String vchOperatingSystemSoftware;

    @Column(name = "vch_ram", length = 50)
    private String vchRAM;

    @Column(name = "vch_processor", length = 50)
    private String vchProcessor;

    @Column(name = "vch_generation", length = 50)
    private String vchGeneration;

    @Column(name = "vch_storage", length = 50)
    private String vchStorage;

    @Column(name = "vch_computer_type", length = 50)
    private String vchComputerType;

    @Column(name = "bit_hosting_details_clod")
    private Boolean bitHostingDetailsClod;

    @Column(name = "bit_hosting_details_drive")
    private Boolean bitHostingDetailsDrive;

    @Column(name = "vch_hosting_others", length = 255)
    private String vchHostingOthers;

    @Column(name = "bit_connected_to_mains_electricity")
    private Boolean bitConnectedToMainsElectricity;

    @Column(name = "vch_mains_electricity_comments", length = 255)
    private String vchMainsElectricityComments;

    @Column(name = "bit_has_power_backup")
    private Boolean bitHasPowerBackup;

    @Column(name = "vch_power_backup_comments", length = 255)
    private String vchPowerBackupComments;

    @Column(name = "bit_warehouse_manager_computer_literacy")
    private Boolean bitWarehouseManagerComputerLiteracy;

    @Column(name = "vch_manager_computer_literacy_comments", length = 255)
    private String vchManagerComputerLiteracyComments;

    @Column(name = "bit_data_entry_staff_computer_literacy")
    private Boolean bitDataEntryStaffComputerLiteracy;

    @Column(name = "vch_data_entry_staff_comments", length = 255)
    private String vchDataEntryStaffComments;

    @Column(name = "bit_has_fixed_internet")
    private Boolean bitHasFixedInternet;

    @Column(name = "vch_internet_comments", length = 255)
    private String vchInternetComments;

    @Column(name = "vch_gsm_connectivity", length = 50)
    private String vchGSMConnectivity;

    @Column(name = "bit_disaster_recovery_backup")
    private Boolean bitDisasterRecoveryBackup;

    @Column(name = "vch_disaster_recovery_comments", length = 255)
    private String vchDisasterRecoveryComments;

    @Column(name = "bit_logical_security_in_place")
    private Boolean bitLogicalSecurityInPlace;

    @Column(name = "vch_logical_security_comments", length = 255)
    private String vchLogicalSecurityComments;
}
