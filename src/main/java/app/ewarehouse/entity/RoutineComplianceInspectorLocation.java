package app.ewarehouse.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_routine_compliance_inspector_location")
public class RoutineComplianceInspectorLocation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_location_id")
    private Long intLocationId;

    @Column(name = "vch_storage_facility")
    private String vchStorageFacility;

    @Column(name = "int_plot_number")
    private String intPlotNumber;

    @Column(name = "vchr_road")
    private String vchrRoad;

    @Column(name = "vch_location")
    private String vchLocation;

    @Column(name = "int_county")
    private Integer intCounty;

    @Column(name = "int_sub_county")
    private Integer intSubCounty;

    @Column(name = "int_ward")
    private Integer intWard;

    @Column(name = "vch_longitude")
    private String vchLongitude;

    @Column(name = "vch_latitude")
    private String vchLatitude;

    @OneToMany(mappedBy = "routineComplianceInspectorLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RoutineComplianceInspectorAgriculturalCommodity> agriculturalCommodities;

    @Column(name = "vch_managing_company")
    private String vchManagingCompany;

    @Column(name = "vch_po_box")
    private String vchPoBox;

    @Column(name = "vch_postal_code")
    private String vchPostalCode;

    @Column(name = "bit_ownership_type")
    private Boolean bitOwnershipType;

    @Column(name = "vch_key_contact_person1")
    private String vchKeyContactPerson1;

    @Column(name = "vch_alternate_contact2")
    private String vchAlternateContact2;

    @Column(name = "vch_function_position")
    private String vchFunctionPosition;

    @Column(name = "vch_mobile_phone1")
    private String vchMobilePhone1;

    @Column(name = "vch_alternative_phone2")
    private String vchAlternativePhone2;

    @Column(name = "vch_landline_phone")
    private String vchLandlinePhone;

    @Column(name = "vch_email1")
    private String vchEmail1;

    @Column(name = "vch_alternative_email2")
    private String vchAlternativeEmail2;

    @Column(name = "bit_type_of_entity_lim")
    private Boolean bitTypeOfEntityLim;

    @Column(name = "bit_type_of_entity_sole")
    private Boolean bitTypeOfEntitySole;

    @Column(name = "bit_type_of_entity_cooperative")
    private Boolean bitTypeOfEntityCooperative;

    @Column(name = "bit_type_of_entity_trust")
    private Boolean bitTypeOfEntityTrust;

    @Column(name = "bit_type_of_entity_cbo")
    private Boolean bitTypeOfEntityCBO;

    @Column(name = "bit_type_of_entity_partnership")
    private Boolean bitTypeOfEntityPartnership;

    @Column(name = "bit_type_of_entity_society")
    private Boolean bitTypeOfEntitySociety;

    @Column(name = "bit_type_of_entity_other")
    private Boolean bitTypeOfEntityOther;
    
    @Column(name = "vch_enity_type_other")
    private String vchEnityTypeOther;

    @Column(name = "dec_hours_operation_day")
    private Integer decHoursOperationDay;

    @Column(name = "dec_hours_operation_night")
    private Integer decHoursOperationNight;
    
    @Column(name = "int_employees_male_day")
    private Integer intEmployeesMaleDay;

    @Column(name = "int_employees_male_night")
    private Integer intEmployeesMaleNight;

    @Column(name = "int_employees_female_day")
    private Integer intEmployeesFemaleDay;

    @Column(name = "int_employees_female_night")
    private Integer intEmployeesFemaleNight;

    @Column(name = "is_storage_facility")
    private Boolean isStorageFacility;
    
    @Column(name = "comments")
    private String comments;
    

    @Column(name = "vch_warehouse_owner")
    private String vchWarehouseOwner;

    @Column(name = "vch_warehouse_po_box")
    private String vchWarehousePoBox;

    @Column(name = "vch_warehouse_postal_code")
    private String vchWarehousePostalCode;

    @Column(name = "vch_warehouse_telephone")
    private String vchWarehouseTelephone;

    @Column(name = "vch_warehouse_email")
    private String vchWarehouseEmail;

    @Column(name = "vch_warehouse_remarks")
    private String vchWarehouseRemarks;

    @Column(name = "bit_valid_certificate")
    private Boolean bitValidCertificate;

    @Column(name = "vch_valid_certificate_comment")
    private String vchValidCertificateComment;

    @Column(name = "bit_latest_cr12")
    private Boolean bitLatestCR12;

    @Column(name = "bit_kra_pin")
    private Boolean bitKRAPIN;

    @Column(name = "bit_tax_compliance_certificate")
    private Boolean bitTaxComplianceCertificate;

    @Column(name = "bit_good_conduct_manager")
    private Boolean bitGoodConductManager;

    @Column(name = "vch_good_conduct_manager_comment")
    private String vchGoodConductManagerComment;

    @Column(name = "bit_good_conduct_weighers")
    private Boolean bitGoodConductWeighers;

    @Column(name = "vch_good_conduct_weighers_comment")
    private String vchGoodConductWeighersComment;

    @Column(name = "bit_good_conduct_graders")
    private Boolean bitGoodConductGraders;

    @Column(name = "vch_good_conduct_graders_comment")
    private String vchGoodConductGradersComment;

    @Column(name = "bit_valid_warehouse_license")
    private Boolean bitValidWarehouseLicense;

    @Column(name = "vch_valid_warehouse_license_comment")
    private String vchValidWarehouseLicenseComment;

    @Column(name = "bit_demonstrate_performance_bond")
    private Boolean bitDemonstratePerformanceBond;

    @Column(name = "vch_demonstrate_performance_bond_comment")
    private String vchDemonstratePerformanceBondComment;

    @Column(name = "bit_business_plan")
    private Boolean bitBusinessPlan;

    @Column(name = "vch_business_plan_comment")
    private String vchBusinessPlanComment;

    @Column(name = "bit_service_charges_displayed")
    private Boolean bitServiceChargesDisplayed;

    @Column(name = "vch_service_charges_displayed_comment")
    private String vchServiceChargesDisplayedComment;

    @Column(name = "bit_certified_graders")
    private Boolean bitCertifiedGraders;

    @Column(name = "vch_certified_graders_comment")
    private String vchCertifiedGradersComment;
    
    @Column(name = "bit_capital_financial_investment")
    private Boolean bitCapitalFinancialInvestment;

    @Column(name = "vch_capital_financial_investment_comment")
    private String vchCapitalFinancialInvestmentComment;

    @Column(name = "bit_financial_capacity")
    private Boolean bitFinancialCapacity;

    @Column(name = "vch_financial_capacity_comment")
    private String vchFinancialCapacityComment;

    @Column(name = "bit_qualified_weighers")
    private Boolean bitQualifiedWeighers;

    @Column(name = "vch_qualified_weighers_comment")
    private String vchQualifiedWeighersComment;

    @Column(name = "bit_qualified_graders")
    private Boolean bitQualifiedGraders;

    @Column(name = "vch_qualified_graders_comment")
    private String vchQualifiedGradersComment;

    @Column(name = "bit_qualified_operations_manager")
    private Boolean bitQualifiedOperationsManager;

    @Column(name = "vch_qualified_operations_manager_comment")
    private String vchQualifiedOperationsManagerComment;

    @Column(name = "bit_qualified_it_clerks")
    private Boolean bitQualifiedITClerks;

    @Column(name = "vch_qualified_it_clerks_comment")
    private String vchQualifiedITClerksComment;

    @Column(name = "bit_qualified_warehouse_clerks")
    private Boolean bitQualifiedWarehouseClerks;

    @Column(name = "vch_qualified_warehouse_clerks_comment")
    private String vchQualifiedWarehouseClerksComment;

    @Column(name = "bit_health_certificates")
    private Boolean bitHealthCertificates;

    @Column(name = "vch_health_certificates_comment")
    private String vchHealthCertificatesComment;

    @Column(name = "bit_health_safety_policies")
    private Boolean bitHealthSafetyPolicies;

    @Column(name = "vch_health_safety_policies_comment")
    private String vchHealthSafetyPoliciesComment;

//	@Override
//	public String toString() {
//		return "RoutineComplianceInspectorLocation [intLocationId=" + intLocationId + ", vchStorageFacility="
//				+ vchStorageFacility + ", intPlotNumber=" + intPlotNumber + ", vchrRoad=" + vchrRoad + ", vchLocation="
//				+ vchLocation + ", intCounty=" + intCounty + ", intSubCounty=" + intSubCounty + ", intWard=" + intWard
//				+ ", vchLongitude=" + vchLongitude + ", vchLatitude=" + vchLatitude + ", agriculturalCommodities="
//				+ agriculturalCommodities + ", vchManagingCompany=" + vchManagingCompany + ", vchPoBox=" + vchPoBox
//				+ ", vchPostalCode=" + vchPostalCode + ", bitOwnershipType=" + bitOwnershipType
//				+ ", vchKeyContactPerson1=" + vchKeyContactPerson1 + ", vchAlternateContact2=" + vchAlternateContact2
//				+ ", vchFunctionPosition=" + vchFunctionPosition + ", vchMobilePhone1=" + vchMobilePhone1
//				+ ", vchAlternativePhone2=" + vchAlternativePhone2 + ", vchLandlinePhone=" + vchLandlinePhone
//				+ ", vchEmail1=" + vchEmail1 + ", vchAlternativeEmail2=" + vchAlternativeEmail2
//				+ ", bitTypeOfEntityLim=" + bitTypeOfEntityLim + ", bitTypeOfEntitySole=" + bitTypeOfEntitySole
//				+ ", bitTypeOfEntityCooperative=" + bitTypeOfEntityCooperative + ", bitTypeOfEntityTrust="
//				+ bitTypeOfEntityTrust + ", bitTypeOfEntityCBO=" + bitTypeOfEntityCBO + ", bitTypeOfEntityPartnership="
//				+ bitTypeOfEntityPartnership + ", bitTypeOfEntitySociety=" + bitTypeOfEntitySociety
//				+ ", bitTypeOfEntityOther=" + bitTypeOfEntityOther + ", decHoursOperationDay=" + decHoursOperationDay
//				+ ", decHoursOperationNight=" + decHoursOperationNight + ", intEmployeesMaleDay=" + intEmployeesMaleDay
//				+ ", intEmployeesMaleNight=" + intEmployeesMaleNight + ", intEmployeesFemaleDay="
//				+ intEmployeesFemaleDay + ", intEmployeesFemaleNight=" + intEmployeesFemaleNight
//				+ ", storageFacilityType=" + storageFacilityType + ", comments=" + comments + ", vchWarehouseOwner="
//				+ vchWarehouseOwner + ", vchWarehousePoBox=" + vchWarehousePoBox + ", vchWarehousePostalCode="
//				+ vchWarehousePostalCode + ", vchWarehouseTelephone=" + vchWarehouseTelephone + ", vchWarehouseEmail="
//				+ vchWarehouseEmail + ", vchWarehouseRemarks=" + vchWarehouseRemarks + ", bitValidCertificate="
//				+ bitValidCertificate + ", vchValidCertificateComment=" + vchValidCertificateComment
//				+ ", bitLatestCR12=" + bitLatestCR12 + ", bitKRAPIN=" + bitKRAPIN + ", bitTaxComplianceCertificate="
//				+ bitTaxComplianceCertificate + ", bitGoodConductManager=" + bitGoodConductManager
//				+ ", vchGoodConductManagerComment=" + vchGoodConductManagerComment + ", bitGoodConductWeighers="
//				+ bitGoodConductWeighers + ", vchGoodConductWeighersComment=" + vchGoodConductWeighersComment
//				+ ", bitGoodConductGraders=" + bitGoodConductGraders + ", vchGoodConductGradersComment="
//				+ vchGoodConductGradersComment + ", bitValidWarehouseLicense=" + bitValidWarehouseLicense
//				+ ", vchValidWarehouseLicenseComment=" + vchValidWarehouseLicenseComment
//				+ ", bitDemonstratePerformanceBond=" + bitDemonstratePerformanceBond
//				+ ", vchDemonstratePerformanceBondComment=" + vchDemonstratePerformanceBondComment
//				+ ", bitBusinessPlan=" + bitBusinessPlan + ", vchBusinessPlanComment=" + vchBusinessPlanComment
//				+ ", bitServiceChargesDisplayed=" + bitServiceChargesDisplayed + ", vchServiceChargesDisplayedComment="
//				+ vchServiceChargesDisplayedComment + ", bitCertifiedGraders=" + bitCertifiedGraders
//				+ ", vchCertifiedGradersComment=" + vchCertifiedGradersComment + ", bitCapitalFinancialInvestment="
//				+ bitCapitalFinancialInvestment + ", vchCapitalFinancialInvestmentComment="
//				+ vchCapitalFinancialInvestmentComment + ", bitFinancialCapacity=" + bitFinancialCapacity
//				+ ", vchFinancialCapacityComment=" + vchFinancialCapacityComment + ", bitQualifiedWeighers="
//				+ bitQualifiedWeighers + ", vchQualifiedWeighersComment=" + vchQualifiedWeighersComment
//				+ ", bitQualifiedGraders=" + bitQualifiedGraders + ", vchQualifiedGradersComment="
//				+ vchQualifiedGradersComment + ", bitQualifiedOperationsManager=" + bitQualifiedOperationsManager
//				+ ", vchQualifiedOperationsManagerComment=" + vchQualifiedOperationsManagerComment
//				+ ", bitQualifiedITClerks=" + bitQualifiedITClerks + ", vchQualifiedITClerksComment="
//				+ vchQualifiedITClerksComment + ", bitQualifiedWarehouseClerks=" + bitQualifiedWarehouseClerks
//				+ ", vchQualifiedWarehouseClerksComment=" + vchQualifiedWarehouseClerksComment
//				+ ", bitHealthCertificates=" + bitHealthCertificates + ", vchHealthCertificatesComment="
//				+ vchHealthCertificatesComment + ", bitHealthSafetyPolicies=" + bitHealthSafetyPolicies
//				+ ", vchHealthSafetyPoliciesComment=" + vchHealthSafetyPoliciesComment + "]";
//	}
    
    
    


}
