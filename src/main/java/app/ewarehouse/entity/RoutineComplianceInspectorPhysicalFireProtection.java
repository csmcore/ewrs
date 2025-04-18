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
@Table(name = "t_rout_com_physical_fire_protection")
public class RoutineComplianceInspectorPhysicalFireProtection {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_physical_fire_protection_id")
    private Long intPhysicalFireProtectionId;

    @Column(name = "bit_lockable_access_points")
    private Boolean bitLockableAccessPoints;

    @Column(name = "vch_lockable_access_points_comments")
    private String vchLockableAccessPointsComments;

    @Column(name = "bit_employee_only_access")
    private Boolean bitEmployeeOnlyAccess;

    @Column(name = "vch_employee_only_access_comments")
    private String vchEmployeeOnlyAccessComments;

    @Column(name = "bit_burglar_protection")
    private Boolean bitBurglarProtection;

    @Column(name = "bit_burglar_alarm_company")
    private Boolean bitBurglarAlarmCompany;

    @Column(name = "bit_watchmen_station", nullable = false)
    private Boolean bitWatchmenStation;

    @Column(name = "int_watchmen_day_shift", nullable = false)
    private Integer intWatchmenDayShift;

    @Column(name = "int_watchmen_night_shift", nullable = false)
    private Integer intWatchmenNightShift;

    @Column(name = "bit_clocking_station")
    private Boolean bitClockingStation;

    @Column(name = "bit_police_nearby", nullable = false)
    private Boolean bitPoliceNearby;

    @Column(name = "dec_police_distance_km", nullable = false)
    private Double decPoliceDistanceKM;

    @Column(name = "bit_cctv_surveillance")
    private Boolean bitCCTVSurveillance;

    @Column(name = "bit_night_lights")
    private Boolean bitNightLights;

    @Column(name = "bit_emergency_alarm")
    private Boolean bitEmergencyAlarm;

}
