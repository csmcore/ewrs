package app.ewarehouse.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import app.ewarehouse.dto.AocCompProfDetailsMainDTO;
import app.ewarehouse.dto.AocCompProfDirectorDetDTO;
import app.ewarehouse.dto.AocCompProfSignSealDTO;
import app.ewarehouse.dto.AocComplianceCollarterDirectorDTO;
import app.ewarehouse.dto.AocComplianceCollarterSignSealDTO;
import app.ewarehouse.dto.ApplicationCertificateOfCollateralDto;
import app.ewarehouse.dto.ApplicationFeeResponseDto;
import app.ewarehouse.dto.ApplicationOfConformityDTO;
import app.ewarehouse.dto.ApplicationOfConformityPaymentDetailsDTO;
import app.ewarehouse.dto.ApplicationOfConformitySupportingDocumentsDTO;
import app.ewarehouse.dto.ApplicationOfConformityWarehouseOperatorViabilityDTO;
import app.ewarehouse.dto.BuyerDepositorResponse;
import app.ewarehouse.dto.BuyerPaymentInvoiceResponse;
import app.ewarehouse.dto.CollateralCvUploadDTO;
import app.ewarehouse.dto.CollateralManagerMainFormDTO;
import app.ewarehouse.dto.CompanyLocationResponse;
import app.ewarehouse.dto.CompanyResponse;
import app.ewarehouse.dto.ComplaintTypeResponse;
import app.ewarehouse.dto.ComplaintmanagementResponse;
import app.ewarehouse.dto.ComplaintsResponse;
import app.ewarehouse.dto.CountryResponse;
import app.ewarehouse.dto.CountyResponse;
import app.ewarehouse.dto.DepositorResponse;
import app.ewarehouse.dto.DisputeCategoryResponse;
import app.ewarehouse.dto.DisputeDeclarationResponse;
import app.ewarehouse.dto.DisputeSupportingDocumentTypeResponse;
import app.ewarehouse.dto.GraderWeigherExperienceDTO;
import app.ewarehouse.dto.GraderWeigherMainFormDTO;
import app.ewarehouse.dto.InspectorDTO;
import app.ewarehouse.dto.InspectorSuspensionComplaintResponse;
import app.ewarehouse.dto.LoginTrailDTO;
import app.ewarehouse.dto.LotMasterResponseDto;
import app.ewarehouse.dto.MFinalOperatorLicenseResponse;
import app.ewarehouse.dto.MsplitReceiptResponse;
import app.ewarehouse.dto.OPLApplicationStatusDTO;
import app.ewarehouse.dto.OPLFormDetailsDTO;
import app.ewarehouse.dto.OperatorLicenceApprovalResponseDto;
import app.ewarehouse.dto.OperatorLicenceDTO;
import app.ewarehouse.dto.PaymentStatusViewResponseDTO;
import app.ewarehouse.dto.PledgingDischargeWarehouseReceiptResponse;
import app.ewarehouse.dto.SalesApplicationMainResponse;
import app.ewarehouse.dto.SalesApplicationTabOneDTO;
import app.ewarehouse.dto.SalesApplicationTabThreeDTO;
import app.ewarehouse.dto.SalesApplicationTabTwoDTO;
import app.ewarehouse.dto.SendNotificationDto;
import app.ewarehouse.dto.SubCountyResponse;
import app.ewarehouse.dto.TsplitReceiptResponse;
import app.ewarehouse.dto.WardResponse;
import app.ewarehouse.dto.WarehouseApplicantResponse;
import app.ewarehouse.dto.WarehouseParticularsResponse;
import app.ewarehouse.dto.WarehouseReceiptResponse;
import app.ewarehouse.dto.receiveCommodityResponse;
import app.ewarehouse.entity.AocCompProfDirectorDetails;
import app.ewarehouse.entity.AocCompProfSignSeal;
import app.ewarehouse.entity.AocCompProfileDetails;
import app.ewarehouse.entity.AocComplianceCollarterDirector;
import app.ewarehouse.entity.AocComplianceCollarterSignSeal;
import app.ewarehouse.entity.ApplicationCertificateOfCollateral;
import app.ewarehouse.entity.ApplicationFeeConfig;
import app.ewarehouse.entity.ApplicationOfConformity;
import app.ewarehouse.entity.ApplicationOfConformityParticularOfApplicants;
import app.ewarehouse.entity.ApplicationOfConformityPaymentDetails;
import app.ewarehouse.entity.ApplicationOfConformitySupportingDocuments;
import app.ewarehouse.entity.ApplicationOfConformityWarehouseOperatorViability;
import app.ewarehouse.entity.BuyerDepositor;
import app.ewarehouse.entity.BuyerPaymentInvoice;
import app.ewarehouse.entity.CollateralCVUploadedDetails;
import app.ewarehouse.entity.Company;
import app.ewarehouse.entity.CompanyLocation;
import app.ewarehouse.entity.ComplaintType;
import app.ewarehouse.entity.Complaint_managment;
import app.ewarehouse.entity.Complaints;
import app.ewarehouse.entity.Country;
import app.ewarehouse.entity.County;
import app.ewarehouse.entity.Depositor;
import app.ewarehouse.entity.DisputeCategory;
import app.ewarehouse.entity.DisputeDeclaration;
import app.ewarehouse.entity.DisputeSupportingDocumentType;
import app.ewarehouse.entity.GraderWeigherExperience;
import app.ewarehouse.entity.GraderWeigherForm;
import app.ewarehouse.entity.Inspector;
import app.ewarehouse.entity.InspectorSuspensionComplaint;
import app.ewarehouse.entity.LoginTrail;
import app.ewarehouse.entity.LotMaster;
import app.ewarehouse.entity.MFinalOperatorLicense;
import app.ewarehouse.entity.MsplitReceiptMain;
import app.ewarehouse.entity.NotificationEntity;
import app.ewarehouse.entity.OperatorLicence;
import app.ewarehouse.entity.PaymentMethod;
import app.ewarehouse.entity.PledgingDischargeMain;
import app.ewarehouse.entity.SalesApplicationMain;
import app.ewarehouse.entity.SalesApplicationTabOne;
import app.ewarehouse.entity.SalesApplicationTabThree;
import app.ewarehouse.entity.SalesApplicationTabTwo;
import app.ewarehouse.entity.SubCounty;
import app.ewarehouse.entity.TreceiveCommodity;
import app.ewarehouse.entity.TsplitReceipt;
import app.ewarehouse.entity.TwarehouseReceipt;
import app.ewarehouse.entity.Ward;
import app.ewarehouse.entity.WarehouseApplicant;
import app.ewarehouse.entity.WarehouseParticulars;
import app.ewarehouse.master.dto.ComplaintStatusResponseDto;
import app.ewarehouse.master.entity.ComplaintStatusMaster;

public class Mapper {

    public static BuyerDepositorResponse mapToBuyerResponse(BuyerDepositor buyer) {
        return BuyerDepositorResponse.builder()
                .intId(buyer.getIntId())
                .txtName(buyer.getTxtName())
                .txtPassportNo(buyer.getTxtPassportNo())
                .txtPassportPath(buyer.getTxtPassportPath())
                .intCentralRegistryIdentifier(buyer.getIntCentralRegistryIdentifier())
                .txtPostalAddress(buyer.getTxtPostalAddress())
                .txtTelephoneNumber(buyer.getTxtTelephoneNumber())
                .txtEmailAddress(buyer.getTxtEmailAddress())
                .txtWard(buyer.getTxtWard())
                .subCounty(mapToSubCountyResponse(buyer.getSubCounty()))
                .intBankAccNo(buyer.getIntBankAccNo())
                .bank(buyer.getBank())
                .txtBankProofPath(buyer.getTxtBankProofPath())
                .intBusinessRegNo(buyer.getIntBusinessRegNo())
                .txtBusinessRegCertPath(buyer.getTxtBusinessRegCertPath())
                .dtmCreatedOn(buyer.getDtmCreatedOn())
                .stmUpdatedOn(buyer.getStmUpdatedOn())
                .intCreatedBy(buyer.getIntCreatedBy())
                .intUpdatedBy(buyer.getIntUpdatedBy())
                .enmStatus(buyer.getEnmStatus().name())
                .enmRegistrationType(buyer.getEnmRegistrationType().name())
                .intApprovedBy(buyer.getIntApprovedBy())
                .buyerDepositorType(buyer.getBuyerDepositorType())
                .vchAccountHolderName(buyer.getVchAccountHolderName())
                .vchBranchName(buyer.getVchBranchName())
                .build();
    }

    public static SubCountyResponse mapToSubCountyResponse(SubCounty subCounty) {
        if (subCounty == null) {
            return null;
        }
        return SubCountyResponse.builder()
                .intId(subCounty.getIntId())
                .txtSubCountyName(subCounty.getTxtSubCountyName())
                .county(mapToCountyResponse(subCounty.getCounty()))
                .dtmCreatedOn(subCounty.getDtmCreatedOn())
                .stmUpdatedOn(subCounty.getStmUpdatedOn())
                .intCreatedBy(subCounty.getIntCreatedBy())
                .intUpdatedBy(subCounty.getIntUpdatedBy())
                .bitDeletedFlag(subCounty.getBitDeletedFlag())
                .build();
    }

    public static CountyResponse mapToCountyResponse(County county) {
        if (county == null) {
            return null;
        }
        return CountyResponse.builder()
                .id(county.getId())
                .name(county.getName())
                .country(mapToCountryResponse(county.getCountry()))
                .build();
    }

    public static CountryResponse mapToCountryResponse(Country country) {
        if (country == null) {
            return null;
        }
        return CountryResponse.builder()
                .countryId(country.getCountryId())
                .countryName(country.getCountryName())
                .countryCode(country.getCountryCode())
                .build();
    }

    public static CountyResponse mapToCountyResponseWithSubCounties(County county) {
        if (county == null) {
            return null;
        }
        return CountyResponse.builder()
                .id(county.getId())
                .name(county.getName())
                .bitDeletedFlag(county.getBitDeletedFlag())
                .country(mapToCountryResponse(county.getCountry()))
                .subCounties(county.getSubCounties().stream()
                        .map(Mapper::mapToSubCountyResponseWithoutCounty)
                        .collect(Collectors.toList()))
                .build();
    }

    public static SubCountyResponse mapToSubCountyResponseWithoutCounty(SubCounty subCounty) {
        if (subCounty == null) {
            return null;
        }
        return SubCountyResponse.builder()
                .intId(subCounty.getIntId())
                .txtSubCountyName(subCounty.getTxtSubCountyName())
                .wards(subCounty.getWards().stream().map(Mapper::mapToWardResponseWithoutSubCounty).collect(Collectors.toList()))
                .dtmCreatedOn(subCounty.getDtmCreatedOn())
                .stmUpdatedOn(subCounty.getStmUpdatedOn())
                .intCreatedBy(subCounty.getIntCreatedBy())
                .intUpdatedBy(subCounty.getIntUpdatedBy())
                .build();
    }

    public static InspectorSuspensionComplaintResponse mapToComplaintResponse(InspectorSuspensionComplaint inspectorSuspensionComplaint) {
        if (inspectorSuspensionComplaint == null) {
            return null;
        }
        return InspectorSuspensionComplaintResponse.builder()
                .complaintId(inspectorSuspensionComplaint.getComplaintId())
                .complainantName(inspectorSuspensionComplaint.getComplainantName())
                .complainantContactNumber(inspectorSuspensionComplaint.getComplainantContactNumber())
                .complainantEmail(inspectorSuspensionComplaint.getComplainantEmail())
                .complainantAddress(inspectorSuspensionComplaint.getComplainantAddress())
                .dateOfIncident(inspectorSuspensionComplaint.getDateOfIncident())
                .locationOfIncident(inspectorSuspensionComplaint.getLocationOfIncident())
                .complaintType(inspectorSuspensionComplaint.getComplaintType())
                .descriptionOfIncident(inspectorSuspensionComplaint.getDescriptionOfIncident())
                .supportingDocument(inspectorSuspensionComplaint.getSupportingDocument())
                .status(inspectorSuspensionComplaint.getStatus().name())
                .remark(inspectorSuspensionComplaint.getRemark())
                .ceoRemarks(inspectorSuspensionComplaint.getCeoRemarks())
                .oicRemarks(inspectorSuspensionComplaint.getOicRemarks())
                .ceo2Remarks(inspectorSuspensionComplaint.getCeo2Remarks())
                .approverRemarks(inspectorSuspensionComplaint.getApproverRemarks())
                .actionTakenBy(inspectorSuspensionComplaint.getActionTakenBy())
                .build();
    }

    public static CountryResponse mapToCountryDto(Country country) {
        if (country == null) {
            return null;
        }
        return CountryResponse.builder()
                .countryId(country.getCountryId())
                .countryName(country.getCountryName())
                .countryCode(country.getCountryCode())
                .isActive(country.getIsActive())
                .counties(country.getCounties().stream().map(county -> CountyResponse.builder()
                        .id(county.getId())
                        .name(county.getName())
                        .build()
                ).toList()).build();
    }

    public static WarehouseApplicantResponse mapToWarehouseApplicantResponse(WarehouseApplicant warehouseApplicant) {
        return WarehouseApplicantResponse.builder()
                .vchApplicantId(warehouseApplicant.getVchApplicantId())
                .vchFirstName(warehouseApplicant.getVchFirstName())
                .vchLastName(warehouseApplicant.getVchLastName())
                .vchMiddleName(warehouseApplicant.getVchMiddleName())
                .vchPassportNo(warehouseApplicant.getVchPassportNo())
                .vchPhoneNo(warehouseApplicant.getVchPhoneNo())
                .company(mapToCompanyResponse(warehouseApplicant.getCompany()))
                .warehouseParticulars(mapToWarehouseParticularsResponse(warehouseApplicant.getWarehouseParticulars()))
                .vchEmail(warehouseApplicant.getVchEmail())
                .vchPaymentMethod(warehouseApplicant.getVchPaymentMethod())
                .bitDeclaration(warehouseApplicant.getBitDeclaration())
                .enmStatus(warehouseApplicant.getEnmStatus())
                .intVerifiedById(warehouseApplicant.getIntVerifiedById())
                .vchVerificationRemark(warehouseApplicant.getVchVerificationRemark())
                .dtmCreatedOn(warehouseApplicant.getDtmCreatedOn())
                .stmUpdatedOn(warehouseApplicant.getStmUpdatedOn())
                .intCreatedBy(warehouseApplicant.getIntCreatedBy())
                .intUpdatedBy(warehouseApplicant.getIntUpdatedBy())
                .intApprovedBy(warehouseApplicant.getIntApprovedBy())
                .vchFinanceRemarks(warehouseApplicant.getFinanceRemarks())
                .vchVerificationRemarks(warehouseApplicant.getVerificationRemarks())
                .inspector(mapToInspectorDTO(warehouseApplicant.getInspector()))
                .vchManagerRcRemarks(warehouseApplicant.getManagerRcRemarks())
                .vchUploadInspectionPlan(warehouseApplicant.getUploadInspectionPlan())
                .dtmDateTimeInspection(convertToDate(warehouseApplicant.getDateTimeInspection()))
                .vchInspectorRemarks(warehouseApplicant.getInspectorRemarks())
                .vchManagerRccRemarks(warehouseApplicant.getManagerRccRemarks())
                .vchFoodCropsRemarks(warehouseApplicant.getFoodCropsRemarks())
                .build();
    }

    private static Date convertToDate(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static InspectorDTO mapToInspectorDTO(Inspector inspector) {
        if (inspector == null) {
            return null;
        }

        return InspectorDTO.builder()
                .id(inspector.getId())
                .name(inspector.getName())
                .email(inspector.getEmail())
                .mobileNumber(inspector.getMobileNumber())
                .status(inspector.getStatus())
                .build();
    }

    public static WarehouseParticularsResponse mapToWarehouseParticularsResponse(WarehouseParticulars warehouseParticulars) {
        if (warehouseParticulars == null) {
            return null;
        }
        return WarehouseParticularsResponse.builder()
                .intWareHouseParticularsId(warehouseParticulars.getIntWareHouseParticularsId())
                .vchWarehouseName(warehouseParticulars.getVchWarehouseName())
                .vchOperatorName(warehouseParticulars.getVchOperatorName())
                .vchOperatorLicenseNo(warehouseParticulars.getVchOperatorLicenseNo())
                .vchNameAndBuildingLocation(warehouseParticulars.getVchNameAndBuildingLocation())
                .vchStreetName(warehouseParticulars.getVchStreetName())
                .ward(mapToWardResponse(warehouseParticulars.getWard()))
                .vchOperatorsInsurance(warehouseParticulars.getVchOperatorsInsurance())
                .vchPolicyNo(warehouseParticulars.getVchPolicyNo())
                .build();
    }

    public static WardResponse mapToWardResponse(Ward ward) {
        if (ward == null) {
            return null;
        }
        return WardResponse.builder()
                .intId(ward.getIntId())
                .vchWardName(ward.getVchWardName())
                .subCounty(mapToSubCountyResponse(ward.getSubCounty()))
                .build();
    }

    public static WardResponse mapToWardResponseWithoutSubCounty(Ward ward) {
        if (ward == null) {
            return null;
        }
        return WardResponse.builder()
                .intId(ward.getIntId())
                .vchWardName(ward.getVchWardName())
                .build();
    }

    public static CompanyResponse mapToCompanyResponse(Company company) {
        if (company == null) {
            return null;
        }
        return CompanyResponse.builder()
                .intCompanyId(company.getIntCompanyId())
                .vchName(company.getVchName())
                .vchRegistrationNo(company.getVchRegistrationNo())
                .dtmEstablishmentDate(company.getDtmEstablishmentDate())
                .legalStatus(company.getLegalStatus())
                .vchKraPin(company.getVchKraPin())
                .vchEmail(company.getVchEmail())
                .companyLocation(mapToCompanyLocationResponse(company.getCompanyLocation()))
                .build();
    }

    public static CompanyLocationResponse mapToCompanyLocationResponse(CompanyLocation companyLocation) {
        if (companyLocation == null) {
            return null;
        }
        return CompanyLocationResponse.builder()
                .intId(companyLocation.getIntId())
                .intPostalCode(companyLocation.getIntPostalCode())
                .vchPostalAddress(companyLocation.getVchPostalAddress())
                .vchBuildingName(companyLocation.getVchBuildingName())
                .vchStreetName(companyLocation.getVchStreetName())
                .vchPlotNo(companyLocation.getVchPlotNo())
                .ward(mapToWardResponse(companyLocation.getWard()))
                .vchLocation(companyLocation.getVchLocation())
                .vchSubLocation(companyLocation.getVchSubLocation())
                .vchTown(companyLocation.getVchTown())
                .vchVillage(companyLocation.getVchVillage())
                .build();
    }

    public static ApplicationOfConformitySupportingDocuments toEntity(ApplicationOfConformitySupportingDocumentsDTO dto) {
        if (dto == null) {
            return null;
        }

        ApplicationOfConformitySupportingDocuments entity = new ApplicationOfConformitySupportingDocuments();

        entity.setId(dto.getSupportDocId());

        ApplicationOfConformityParticularOfApplicants aocpa = new ApplicationOfConformityParticularOfApplicants();
        aocpa.setId(dto.getIntParticularOfApplicantsId());
        entity.setParticularOfApplicants(aocpa);
        processDocument(dto.getBusinessPlanPath(), dto.getBusinessPlanPathFetchFromDb(),
                entity::setBusinessPlanPath, "AOC_BUSINESS_PLAN_",
                FolderAndDirectoryConstant.AOC_SUPPORTING_DOCS_FOLDER);

        processDocument(dto.getTaxComplianceCertificatePath(), dto.getTaxComplianceCertificatePathFetchFromDb(),
                entity::setTaxComplianceCertificatePath, "AOC_TAX_COMPLIANCE_",
                FolderAndDirectoryConstant.AOC_SUPPORTING_DOCS_FOLDER);

        processDocument(dto.getGoodConductCertificatePath(), dto.getGoodConductCertificatePathFetchFromDb(),
                entity::setGoodConductCertificatePath, "AOC_GOOD_CONDUCT_",
                FolderAndDirectoryConstant.AOC_SUPPORTING_DOCS_FOLDER);

        processDocument(dto.getIdentificationProofPath(), dto.getIdentificationProofPathFetchFromDb(),
                entity::setIdentificationProofPath, "AOC_IDENTIFICATION_PROOF_",
                FolderAndDirectoryConstant.AOC_SUPPORTING_DOCS_FOLDER);

        processDocument(dto.getInsurancePolicyPath(), dto.getInsurancePolicyPathFetchFromDb(),
                entity::setInsurancePolicyPath, "AOC_INSURANCE_POLICY_",
                FolderAndDirectoryConstant.AOC_SUPPORTING_DOCS_FOLDER);

        processDocument(dto.getLicenseForTheWarehousePath(), dto.getLicenseForTheWarehousePathFetchFromDb(),
                entity::setLicenseForTheWarehousePath, "AOC_LICENSE_FOR_WAREHOUSE_",
                FolderAndDirectoryConstant.AOC_SUPPORTING_DOCS_FOLDER);

        processDocument(dto.getCertificateOfRegistrationPath(), dto.getCertificateOfRegistrationPathFetchFromDb(),
                entity::setCertificateOfRegistrationPath, "AOC_CERTIFICATE_OF_REGISTRATION_",
                FolderAndDirectoryConstant.AOC_SUPPORTING_DOCS_FOLDER);

        entity.setIntCreatedBy(dto.getUserId());
        entity.setBitDraftStatusFlag(dto.getDraftStatus());

        return entity;
    }

    private static void processDocument(String path, Boolean fetchFromDb, Consumer<String> setter, String filePrefix, String folder) {
        if (Boolean.FALSE.equals(fetchFromDb) && path != null) {
            byte[] decodedBytes = Base64.getDecoder().decode(path.getBytes());
            String uploadedPath = DocumentUploadutil.uploadFileByte(filePrefix + System.currentTimeMillis(), decodedBytes, folder);
            setter.accept(uploadedPath);
        } else {
            setter.accept(path);
        }
    }

    public static ApplicationOfConformitySupportingDocumentsDTO toDto(ApplicationOfConformitySupportingDocuments entity) {
        if (entity == null) {
            return null;
        }

        ApplicationOfConformitySupportingDocumentsDTO dto = new ApplicationOfConformitySupportingDocumentsDTO();
        dto.setBusinessPlanPath(entity.getBusinessPlanPath());
        dto.setTaxComplianceCertificatePath(entity.getTaxComplianceCertificatePath());
        dto.setGoodConductCertificatePath(entity.getGoodConductCertificatePath());
        dto.setIdentificationProofPath(entity.getIdentificationProofPath());
        dto.setInsurancePolicyPath(entity.getInsurancePolicyPath());
        dto.setLicenseForTheWarehousePath(entity.getLicenseForTheWarehousePath());
        dto.setCertificateOfRegistrationPath(entity.getCertificateOfRegistrationPath());

        return dto;
    }

    public static ApplicationOfConformityWarehouseOperatorViability toEntity(ApplicationOfConformityWarehouseOperatorViabilityDTO dto) {
        if (dto == null) {
            return null;
        }

        ApplicationOfConformityWarehouseOperatorViability entity = new ApplicationOfConformityWarehouseOperatorViability();
        if (dto.getViabilityId() != null) {
            entity.setId(dto.getViabilityId());
        }
        ApplicationOfConformityParticularOfApplicants aocpa = new ApplicationOfConformityParticularOfApplicants();
        aocpa.setId(dto.getIntParticularOfApplicantsId());
        entity.setParticularOfApplicants(aocpa);
        processDocument(dto.getCompanyRegistrationCertificatePath(),
                dto.getCompanyRegistrationCertificatePathFetchFromDb(),
                entity::setCompanyRegistrationCertificatePath,
                "AOC_COMPANY_REGISTRATION_");

        processDocument(dto.getBusinessPermitPath(),
                dto.getBusinessPermitPathFetchFromDb(),
                entity::setBusinessPermitPath,
                "AOC_BUSINESS_PERMIT_");

        processDocument(dto.getBusinessStrategicPlanPath(),
                dto.getBusinessStrategicPlanPathFetchFromDb(),
                entity::setBusinessStrategicPlanPath,
                "AOC_BUSINESS_STRATEGIC_PLAN_");

        processDocument(dto.getLeaseAgreementPath(),
                dto.getLeaseAgreementPathFetchFromDb(),
                entity::setLeaseAgreementPath,
                "AOC_LEASE_AGREEMENT_");

        processDocument(dto.getTitleDeedPath(),
                dto.getTitleDeedPathFetchFromDb(),
                entity::setTitleDeedPath,
                "AOC_TITLE_DEED_");

        processDocument(dto.getAssetRegister(),
                dto.getAssetRegisterFetchFromDb(),
                entity::setAssetRegister,
                "AOC_ASSET_REGISTER_");

        processDocument(dto.getRecommendationLettersPath(),
                dto.getRecommendationLettersPathFetchFromDb(),
                entity::setRecommendationLettersPath,
                "AOC_RECOMMENDATION_LETTER_");

        processDocument(dto.getCertificationPath(),
                dto.getCertificationPathFetchFromDb(),
                entity::setCertificationPath,
                "AOC_CERTIFICATION_");

        processDocument(dto.getBusinessOpportunity(),
                dto.getBusinessOpportunityFetchFromDb(),
                entity::setBusinessOpportunity,
                "AOC_BUSINESS_OPPORTUNITY_");

        processDocument(dto.getCustomerBasePath(),
                dto.getCustomerBasePathFetchFromDb(),
                entity::setCustomerBasePath,
                "AOC_CUSTOMER_BASE_");

        processDocument(dto.getProductionOfTargetAreasPath(),
                dto.getProductionOfTargetAreasPathFetchFromDb(),
                entity::setProductionOfTargetAreasPath,
                "AOC_PRODUCTION_OF_TARGET_AREAS_");

        processDocument(dto.getSupplyVolumesPath(),
                dto.getSupplyVolumesPathFetchFromDb(),
                entity::setSupplyVolumesPath,
                "AOC_SUPPLY_VOLUMES_");

        processDocument(dto.getFinancialProjectionsPath(),
                dto.getFinancialProjectionsPathFetchFromDb(),
                entity::setFinancialProjectionsPath,
                "AOC_FINANCIAL_PROJECTIONS_");

        processDocument(dto.getAuditedFinancialReportPath(),
                dto.getAuditedFinancialReportPathFetchFromDb(),
                entity::setAuditedFinancialReportPath,
                "AOC_AUDITED_FINANCIAL_REPORT_");

        processDocument(dto.getFireInsurancePolicyPath(),
                dto.getFireInsurancePolicyPathFetchFromDb(),
                entity::setFireInsurancePolicyPath,
                "AOC_FIRE_INSURANCE_POLICY_");

        processDocument(dto.getTheftInsurancePolicyPath(),
                dto.getTheftInsurancePolicyPathFetchFromDb(),
                entity::setTheftInsurancePolicyPath,
                "AOC_THEFT_INSURANCE_POLICY_");

        processDocument(dto.getNaturalCalamityInsurancePolicyPath(),
                dto.getNaturalCalamityInsurancePolicyPathFetchFromDb(),
                entity::setNaturalCalamityInsurancePolicyPath,
                "AOC_NATURAL_CALAMITY_INSURANCE_POLICY_");

        processDocument(dto.getPerformanceBondPath(),
                dto.getPerformanceBondPathFetchFromDb(),
                entity::setPerformanceBondPath,
                "AOC_PERFORMANCE_BOND_");

        processDocument(dto.getBusinessContinuityPath(),
                dto.getBusinessContinuityPathFetchFromDb(),
                entity::setBusinessContinuityPath,
                "AOC_BUSINESS_CONTINUITY_");

        entity.setIntCreatedBy(dto.getUserId());
        entity.setBitDraftStatusFlag(dto.getDraftStatus());
        return entity;
    }

    private static void processDocument(String path, Boolean fetchFromDb, Consumer<String> setter, String filePrefix) {
        processDocument(path, fetchFromDb, setter, filePrefix, FolderAndDirectoryConstant.AOC_OPERATOR_VIABILITY_FOLDER);
    }

    public static ApplicationOfConformityWarehouseOperatorViabilityDTO toDto(ApplicationOfConformityWarehouseOperatorViability entity) {
        if (entity == null) {
            return null;
        }

        ApplicationOfConformityWarehouseOperatorViabilityDTO dto = new ApplicationOfConformityWarehouseOperatorViabilityDTO();
        dto.setCompanyRegistrationCertificatePath(entity.getCompanyRegistrationCertificatePath());
        dto.setBusinessPermitPath(entity.getBusinessPermitPath());
        dto.setBusinessStrategicPlanPath(entity.getBusinessStrategicPlanPath());
        dto.setLeaseAgreementPath(entity.getLeaseAgreementPath());
        dto.setTitleDeedPath(entity.getTitleDeedPath());
        dto.setAssetRegister(entity.getAssetRegister());
        dto.setRecommendationLettersPath(entity.getRecommendationLettersPath());
        dto.setCertificationPath(entity.getCertificationPath());
        dto.setBusinessOpportunity(entity.getBusinessOpportunity());
        dto.setCustomerBasePath(entity.getCustomerBasePath());
        dto.setProductionOfTargetAreasPath(entity.getProductionOfTargetAreasPath());
        dto.setSupplyVolumesPath(entity.getSupplyVolumesPath());
        dto.setFinancialProjectionsPath(entity.getFinancialProjectionsPath());
        dto.setAuditedFinancialReportPath(entity.getAuditedFinancialReportPath());
        dto.setFireInsurancePolicyPath(entity.getFireInsurancePolicyPath());
        dto.setTheftInsurancePolicyPath(entity.getTheftInsurancePolicyPath());
        dto.setNaturalCalamityInsurancePolicyPath(entity.getNaturalCalamityInsurancePolicyPath());
        dto.setPerformanceBondPath(entity.getPerformanceBondPath());
        dto.setBusinessContinuityPath(entity.getBusinessContinuityPath());

        return dto;
    }

    public static ApplicationOfConformityPaymentDetails toEntity(ApplicationOfConformityPaymentDetailsDTO dto) {
        if (dto == null) {
            return null;
        }

        ApplicationOfConformityPaymentDetails entity = new ApplicationOfConformityPaymentDetails();
        entity.setPaymentType(dto.getPaymentType());
        return entity;
    }

    // Convert Entity to DTO
    public static ApplicationOfConformityPaymentDetailsDTO toDto(ApplicationOfConformityPaymentDetails entity) {
        if (entity == null) {
            return null;
        }

        ApplicationOfConformityPaymentDetailsDTO dto = new ApplicationOfConformityPaymentDetailsDTO();
        dto.setPaymentType(entity.getPaymentType());
        return dto;
    }

    public static DisputeDeclarationResponse mapToDisputeDeclarationResponse(DisputeDeclaration disputeDeclaration) {
        if (disputeDeclaration == null) {
            return null;
        }

        return DisputeDeclarationResponse.builder()
                .disputeId(disputeDeclaration.getDisputeId())
                .disputantName(disputeDeclaration.getDisputantName())
                .contactNumber(disputeDeclaration.getContactNumber())
                .email(disputeDeclaration.getEmail())
                .subCounty(Mapper.mapToSubCountyResponse(disputeDeclaration.getSubCounty()))
                .address(disputeDeclaration.getAddress())
                .dateOfOccurrence(disputeDeclaration.getDateOfOccurrence())
                .locationOfOccurrence(disputeDeclaration.getLocationOfOccurrence())
                .disputeCategory(disputeDeclaration.getDisputeCategory())
                .descriptionOfDispute(disputeDeclaration.getDescriptionOfDispute())
                .otherPartyName(disputeDeclaration.getOtherPartyName())
                .otherPartyNo(disputeDeclaration.getOtherPartyNo())
                .relationship(disputeDeclaration.getRelationship())
                .supportingDocuments(disputeDeclaration.getSupportingDocuments())
                .desiredResolution(disputeDeclaration.getDesiredResolution())
                .ceoApproval(disputeDeclaration.getCeoApproval())
                .committeeDetails(disputeDeclaration.getCommitteeDetails())
                .oicApproval(disputeDeclaration.getOicApproval())
                .oicLegalTwo(disputeDeclaration.getOicLegalTwo())
                .remarks(disputeDeclaration.getRemarks())
                .status(disputeDeclaration.getStatus())
                .intCurrentRole(disputeDeclaration.getIntCurrentRole())
                .intCurrentStage(disputeDeclaration.getIntCurrentStage())
                .createdAt(disputeDeclaration.getCreatedAt())
                .updatedAt(disputeDeclaration.getUpdatedAt())
                .intCreatedBy(disputeDeclaration.getIntCreatedBy())
                .intUpdatedBy(disputeDeclaration.getIntUpdatedBy())
                .build();
    }

    public static ComplaintTypeResponse mapToComplaintTypeResponse(ComplaintType
                                                                           complaint) {
        return ComplaintTypeResponse.builder()
                .complaintId(complaint.getComplaintId())
                .complaintType(complaint.getComplaintType())
                .complaintStatus(complaint.getComplaintStatus())
                .isActive(complaint.getIsActive())
                .build();
    }


//		 public static DepositorResponse mapToDepositorResponse(Depositor depositor) {
//    return DepositorResponse.builder()
//            .intId(depositor.getIntId())
//            .txtName(depositor.getTxtName())
//            .txtPassportNo(depositor.getTxtPassportNo())
//            .txtPassportPath(depositor.getTxtPassportPath())
//            .intCentralRegistryIdentifier(depositor.getIntCentralRegistryIdentifier())
//            .txtPostalAddress(depositor.getTxtPostalAddress())
//            .txtTelephoneNumber(depositor.getTxtTelephoneNumber())
//            .txtEmailAddress(depositor.getTxtEmailAddress())
//            .txtWard(depositor.getTxtWard())
//            .subCounty(mapToSubCountyResponse(depositor.getSubCounty()))
//            .intBankAccNo(depositor.getIntBankAccNo())
//            .txtBankName(depositor.getTxtBankName())
//            .txtBankProofPath(depositor.getTxtBankProofPath())
//            .intBusinessRegNo(depositor.getIntBusinessRegNo())
//            .txtBusinessRegCertPath(depositor.getTxtBusinessRegCertPath())
//            .dtmCreatedOn(depositor.getDtmCreatedOn())
//            .stmUpdatedOn(depositor.getStmUpdatedOn())
//            .intCreatedBy(depositor.getIntCreatedBy())
//            .intUpdatedBy(depositor.getIntUpdatedBy())
//            .enmStatus(depositor.getEnmStatus().name())
//            .intApprovedBy(depositor.getIntApprovedBy())
//            .build();
//}


    public static ComplaintsResponse mapToComplaintsResponse(Complaints complaints) {
        if (complaints == null) {
            return null;
        }

        ComplaintsResponse dto = new ComplaintsResponse();
        dto.setComplaintFor(complaints.getComplaintFor().name());
        dto.setWarehouseOperatorName((complaints.getWarehouseoperator() == null) ? null : complaints.getWarehouseoperator().getVchOperatorName()); // Assuming WarehouseParticulars has a getName() method
        dto.setComplainertelephone(complaints.getComplainertelephone());
        dto.setComplaineremail(complaints.getComplaineremail());
        dto.setComplaintCategory(complaints.getComplaintCategory().getCategoryName()); // Assuming ComplaintsCategory has a getName() method
        dto.setDateOfIncident(complaints.getDateOfIncident());
        dto.setRemark((complaints.getRemark() == null) ? null : complaints.getRemark());
        dto.setStatus(complaints.getStatus());
        dto.setSupportingDocument(complaints.getSupportingDocument());
        dto.setComplaintDescription(complaints.getComplaintDescription());
        dto.setSpecificAllegations(complaints.getSpecificAllegations());
        dto.setComplaintid(complaints.getComplaintId());
        return dto;
    }


//    public static ComplaintTypeResponse mapToComplaintTypeResponse(ComplaintType
//                                                                           complaint) {
//        return ComplaintTypeResponse.builder()
//                .complaintId(complaint.getComplaintId())
//                .complaintType(complaint.getComplaintType())
//                .complaintStatus(complaint.getComplaintStatus())
//                .build();
//    }

    public static DepositorResponse mapToDepositorResponse(Depositor depositor) {
        return DepositorResponse.builder()
                .intId(depositor.getIntId())
                .txtName(depositor.getTxtName())
                .txtPassportNo(depositor.getTxtPassportNo())
                .txtPassportPath(depositor.getTxtPassportPath())
                .intCentralRegistryIdentifier(depositor.getIntCentralRegistryIdentifier())
                .txtPostalAddress(depositor.getTxtPostalAddress())
                .txtTelephoneNumber(depositor.getTxtTelephoneNumber())
                .txtEmailAddress(depositor.getTxtEmailAddress())
                .txtWard(depositor.getTxtWard())
                .subCounty(mapToSubCountyResponse(depositor.getSubCounty()))
                .intBankAccNo(depositor.getIntBankAccNo())
                .txtBankName(depositor.getTxtBankName())
                .txtBankProofPath(depositor.getTxtBankProofPath())
                .intBusinessRegNo(depositor.getIntBusinessRegNo())
                .txtBusinessRegCertPath(depositor.getTxtBusinessRegCertPath())
                .dtmCreatedOn(depositor.getDtmCreatedOn())
                .stmUpdatedOn(depositor.getStmUpdatedOn())
                .intCreatedBy(depositor.getIntCreatedBy())
                .intUpdatedBy(depositor.getIntUpdatedBy())
                .enmStatus(depositor.getEnmStatus().name())
                .intApprovedBy(depositor.getIntApprovedBy())
                .build();
    }

    public static DisputeCategoryResponse disputeCategoryToResponseDTO(DisputeCategory disputeCategory) {
        if (disputeCategory == null) {
            return null;
        }

        DisputeCategoryResponse disputeCategoryResponse = new DisputeCategoryResponse();
        disputeCategoryResponse.setDisputeCategoryId(disputeCategory.getDisputeCategoryId());
        disputeCategoryResponse.setDisputeCategoryName(disputeCategory.getDisputeCategoryName());

        return disputeCategoryResponse;
    }

    public static DisputeSupportingDocumentTypeResponse disputeSupportingDocumentTypeToResponseDTO(DisputeSupportingDocumentType disputeSupportingDocumentType) {
        if (disputeSupportingDocumentType == null) {
            return null;
        }

        DisputeSupportingDocumentTypeResponse disputeSupportingDocumentTypeResponse = new DisputeSupportingDocumentTypeResponse();
        disputeSupportingDocumentTypeResponse.setSupportingDocTypeId(disputeSupportingDocumentType.getSupportingDocTypeId());
        disputeSupportingDocumentTypeResponse.setSupportingDocTypeName(disputeSupportingDocumentType.getSupportingDocTypeName());

        return disputeSupportingDocumentTypeResponse;
    }

    public static AocCompProfileDetails toEntity(AocCompProfDetailsMainDTO companyDetailsDTO) {
        if (companyDetailsDTO == null) {
            return null;
        }
        AocCompProfileDetails aoc = new AocCompProfileDetails();
        aoc.setApplicantName(companyDetailsDTO.getCompanyProfile().getApplicantName());
        aoc.setMobileNumber(companyDetailsDTO.getCompanyProfile().getMobileNumber());
        aoc.setEmail(companyDetailsDTO.getCompanyProfile().getEmail());
        aoc.setPostalAddress(companyDetailsDTO.getCompanyProfile().getPostalAddress());
        aoc.setPostalCode(companyDetailsDTO.getCompanyProfile().getPostalCode());
        aoc.setTown(companyDetailsDTO.getCompanyProfile().getTown());
        aoc.setTelephone(companyDetailsDTO.getCompanyProfile().getTelephone());
        aoc.setWebsite(companyDetailsDTO.getCompanyProfile().getWebsite());
        aoc.setTypeOfEntity(companyDetailsDTO.getCompanyProfile().getTypeOfEntity());
        aoc.setCompany(companyDetailsDTO.getCompanyProfile().getCompany());
        aoc.setCompanyRegNo(companyDetailsDTO.getCompanyProfile().getCompanyRegNo());
        aoc.setKraPin(companyDetailsDTO.getCompanyProfile().getKraPin());
        return aoc;
    }

    public static AocCompProfDirectorDetails toEntity(AocCompProfDirectorDetDTO directorDTO) {
        if (directorDTO == null) {
            return null;
        }else {
        AocCompProfDirectorDetails directorData = new AocCompProfDirectorDetails();
        directorData.setDirectorName(directorDTO.getDirectorName());
        directorData.setNationalityId(directorDTO.getNationalityId());
        if(directorDTO.getPosition() == null || directorDTO.getPosition().isBlank()) {
        	directorData.setPosition(null);
        }else {
        	directorData.setPosition(directorDTO.getPosition());
        }
        if(directorDTO.getPassportNo() == null || directorDTO.getPassportNo().isBlank()) {
        	directorData.setPassportNo(null);
        }else {
        	
        	directorData.setPassportNo(directorDTO.getPassportNo());
        }
        processDocument(directorDTO.getWorkPermitPath(), directorDTO.getFetchFromDb(), directorData::setWorkPermitPath, "COMP_DIRECTOR_WORKPERMIT_", FolderAndDirectoryConstant.COMPANY_DETAILS_FOLDER);
        return directorData;
        }
    }

    public static AocCompProfSignSeal toEntity(AocCompProfSignSealDTO signSealDto) {
        if (signSealDto == null) {
            return null;
        }
        AocCompProfSignSeal signSeal = new AocCompProfSignSeal();
        signSeal.setSignName(signSealDto.getSignName());
        signSeal.setSignTitle(signSealDto.getSignTitle());
        processDocument(signSealDto.getSignPath(), signSealDto.getSignPathfetchFromDb(), signSeal::setSignPath, "COMP_DET_SIGN_", FolderAndDirectoryConstant.COMPANY_DETAILS_FOLDER);
        processDocument(signSealDto.getSealPath(), signSealDto.getSealPathfetchFromDb(), signSeal::setSealPath, "COMP_DET_SEAL_", FolderAndDirectoryConstant.COMPANY_DETAILS_FOLDER);
        return signSeal;
    }

    public static receiveCommodityResponse mapToReceiveCommodityResponse(TreceiveCommodity commodity) {
        receiveCommodityResponse dto = receiveCommodityResponse.builder()
                .txtReceiveCId(commodity.getTxtReceiveCId())
                .depositor(mapToBuyerResponse(commodity.getDepositor()))
                .commodity(commodity.getCommodity())
                .intQuantity(commodity.getIntQuantity())
                .seasonality(commodity.getSeasonality())
                .txtGrade(commodity.getTxtGrade())
                .txtLotNo(commodity.getTxtLotNo())
                .warehouseId(commodity.getWarehouseId())
                .intCreatedBy(commodity.getIntCreatedBy())
                .intUpdatedBy(commodity.getIntUpdatedBy())
                .dtmCreatedAt(commodity.getDtmCreatedAt())
                .stmUpdatedAt(commodity.getStmUpdatedAt())
                .bitDeleteFlag(commodity.getBitDeleteFlag())
                .status(String.valueOf(commodity.getStatus()))
                .build();

        if (dto.getDtmCreatedAt() != null) {
            long daysDifference = DateTimeUtil.timeDiff(new Date(), dto.getDtmCreatedAt(), "Days");
            dto.setTotalDays((int) daysDifference);
        }
        return dto;
    }

    public static WarehouseReceiptResponse mapToWarehouseReceiptResponse(TwarehouseReceipt commodity) {
        return WarehouseReceiptResponse.builder()
                .txtWarehouseReceiptId(commodity.getTxtWarehouseReceiptId())
                .receiveCommodity(mapToReceiveCommodityResponse(commodity.getReceiveCommodity()))
                .txtRemark(commodity.getTxtRemark())
                .bitDeleteFlag(commodity.getBitDeleteFlag())
                .intCreatedBy(commodity.getIntCreatedBy())
                .intUpdatedBy(commodity.getIntUpdatedBy())
                .dtmCreatedOn(commodity.getDtmCreatedOn())
                .stmUpdatedAt(commodity.getStmUpdatedAt())
                .intIndex(commodity.getIntIndex())
                .status(commodity.getStatus().name())
                .intIndex(commodity.getIntIndex())
                .build();
    }

    public static PledgingDischargeWarehouseReceiptResponse mapToPledgingDischargeWarehouseReceiptResponse(PledgingDischargeMain entity) {
        if (entity == null) {
            return null;
        }

        return PledgingDischargeWarehouseReceiptResponse.builder()
                .pledgingDischargeId(entity.getPledgingDischargeId())
                .buyer(mapToBuyerResponse(entity.getDepositorWarehouse().getBuyer()))
                .warehouseReceiptResponse(mapToWarehouseReceiptResponse(entity.getDepositorWarehouse().getWarehouseReceipt()))
                .loanPurpose(entity.getLoanApp().getLoanPurpose())
                .bank(entity.getLoanApp().getBank())
                .scheme(entity.getLoanApp().getScheme())
                .proposedCreditAmount(entity.getLoanApp().getProposedCreditAmount())
                .tenureOfLoanRepayment(entity.getLoanApp().getTenureOfLoanRepayment())
                .area(entity.getResidential().getArea())
                .plotNo(entity.getResidential().getPlotNo())
                .streetName(entity.getResidential().getStreetName())
                .lengthOfStayAtPresentAddress(entity.getResidential().getLengthOfStayAtPresentAddress())
                .nearestMarket(entity.getResidential().getNearestMarket())
                .currentPoliceStation(entity.getResidential().getCurrentPoliceStation())
                .yearsOfStay(entity.getResidential().getYearsOfStay())
                .gender(entity.getResidential().getGender())
                .maritalStatus(entity.getResidential().getMaritalStatus())
                .religion(entity.getResidential().getReligion())
                .isEmployed(entity.getResidential().getIsEmployed())
                .employmentDetails(entity.getResidential().getEmploymentDetails())
                .nameOfAccountHolder(entity.getBankDetails().getNameOfAccountHolder())
                .accountNo(entity.getBankDetails().getAccountNo())
                .bankBranchName(entity.getBankDetails().getBankBranchName())
                .swiftCodes(entity.getBankDetails().getSwiftCodes())
                .bankStatementUpload(entity.getUploadDocs().getBankStatementUpload())
                .passportPhotoUpload(entity.getUploadDocs().getPassportPhotoUpload())
                .latestIdOrPassportUpload(entity.getUploadDocs().getLatestIdOrPassportUpload())
                .addressProof(entity.getUploadDocs().getAddressProofUpload())
                .warehouseReceiptUpload(entity.getUploadDocs().getWarehouseReceiptUpload())
                .pinCertificate(entity.getUploadDocs().getPinCertificateUpload())
                .build();
    }

    public static OperatorLicenceDTO convertToDto(OperatorLicence operatorLicence) {
        OperatorLicenceDTO dto = new OperatorLicenceDTO();
        dto.setId(operatorLicence.getId());
        dto.setBusinessName(operatorLicence.getBusinessName());
        dto.setBusinessRegNumber(operatorLicence.getBusinessRegNumber());
        dto.setBusinessEntityType(operatorLicence.getBusinessEntityType());
        dto.setBusinessAddress(operatorLicence.getBusinessAddress());
        dto.setEmailAddress(operatorLicence.getEmailAddress());
        dto.setPhoneNumber(operatorLicence.getPhoneNumber());
        dto.setKraPin(operatorLicence.getKraPin());
        dto.setPhysicalAddressWarehouse(operatorLicence.getPhysicalAddressWarehouse());
        dto.setWarehouseSize(operatorLicence.getWarehouseSize());
        dto.setGoodsStored(operatorLicence.getGoodsStored());
        dto.setStorageCapacity(operatorLicence.getStorageCapacity());
        dto.setSecurityMeasures(operatorLicence.getSecurityMeasures());
        dto.setWasteDisposalMethods(operatorLicence.getWasteDisposalMethods());
        dto.setDeclaration(operatorLicence.getDeclaration());
        dto.setPaymentMethod(operatorLicence.getPaymentMethod());
        dto.setStatus(operatorLicence.getStatus());
        dto.setAction(operatorLicence.getAction());
        dto.setForwardedTo(operatorLicence.getForwardedTo());
        dto.setFiles(operatorLicence.getFiles());
        dto.setRemarks(operatorLicence.getRemarks());
        dto.setVchApplicationNo(operatorLicence.getVchApplicationNo());
        return dto;
    }

    public static ApplicationOfConformityDTO MapToApplicationOfConformityDto(ApplicationOfConformity entity) {
        ApplicationOfConformityDTO dto = new ApplicationOfConformityDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static MFinalOperatorLicenseResponse mapToMFinalOperatorLicenseResponse(MFinalOperatorLicense entity) {
        return MFinalOperatorLicenseResponse.builder()
                .vchLicenseNo(entity.getVchLicenseNo())
                .operatorLicenceApplication(convertToDto(entity.getOperatorLicenceApplication()))
                .applicationOfConformity(MapToApplicationOfConformityDto(entity.getApplicationOfConformity()))
                .enmStatus(entity.getEnmStatus().name())
                .bitDeletedFlag(entity.getBitDeletedFlag())
                .dtmCreatedOn(entity.getDtmCreatedOn())
                .intCreatedBy(entity.getIntCreatedBy())
                .intUpdatedBy(entity.getIntUpdatedBy())
                .stmUpdatedOn(entity.getStmUpdatedOn())
                .intApprovedBy(entity.getIntApprovedBy())
                .build();
    }

    public static TsplitReceiptResponse mapToSplitReceiptResponse(TsplitReceipt entity) {
        return TsplitReceiptResponse.builder()
                .splitId(entity.getSplitId())
                .splitReceiptNumber(entity.getSplitReceiptNo())
                .qty(entity.getQtyInEachLot())
                .msplitReceiptResponse(mapToMsplitReceiptResponse(entity.getSplitReceipt()))
                .lotNo(entity.getLotNo())
                .bitDeleteFlag(entity.getBitDeleteFlag())
                .dtmCreatedOn(entity.getDtmCreatedOn())
                .intCreatedBy(entity.getIntCreatedBy())
                .intUpdatedBy(entity.getIntUpdatedBy())
                .stmUpdatedOn(entity.getStmUpdatedAt())
                .build();
    }

    public static TsplitReceiptResponse mapToSplitReceiptResponseWithoutMainResponse(TsplitReceipt entity) {
        return TsplitReceiptResponse.builder()
                .splitId(entity.getSplitId())
                .splitReceiptNumber(entity.getSplitReceiptNo())
                .qty(entity.getQtyInEachLot())
                .msplitReceiptResponse(null)
                .lotNo(entity.getLotNo())
                .bitDeleteFlag(entity.getBitDeleteFlag())
                .dtmCreatedOn(entity.getDtmCreatedOn())
                .intCreatedBy(entity.getIntCreatedBy())
                .intUpdatedBy(entity.getIntUpdatedBy())
                .stmUpdatedOn(entity.getStmUpdatedAt())
                .build();
    }

    public static MsplitReceiptResponse mapToMsplitReceiptResponse(MsplitReceiptMain entity) {
        return MsplitReceiptResponse.builder()
                .txtSplitApplicationId(entity.getTxtSplitApplicationId())
                .warehouseReceipt(mapToWarehouseReceiptResponse(entity.getWarehouseReceipt()))
                .totalQuantity(entity.getTotalQuantity())
                .surrenderReceiptCheck(entity.getSurrenderReceipt())
                .totalLotNo(entity.getTotalLotNo())
                .status(entity.getStatus().name())
                .receiptName(entity.getDocumentName())
                .oldWarehouseReceiptPath(entity.getOldWarehouseReceipt())
              //  .splitIdReceipt(entity.getSplits().stream().map(Mapper::mapToSplitReceiptResponseWithoutMainResponse).collect(Collectors.toList()))
                .bitDeleteFlag(entity.getBitDeleteFlag())
                .dtmCreatedAt(entity.getDtmCreatedAt())
                .intCreatedBy(entity.getIntCreatedBy())
                .intUpdatedBy(entity.getIntUpdatedBy())
                .stmUpdatedAt(entity.getStmUpdatedAt())
                .build();

    }


    public static BuyerPaymentInvoiceResponse mapToBuyerPaymentInvoiceResponse(BuyerPaymentInvoice entity) {
        return BuyerPaymentInvoiceResponse.builder()
                .buyerResponse(mapToBuyerResponse(entity.getBuyer()))
                .waReceiptResponse(mapToWarehouseReceiptResponse(entity.getWarehouseReceipt()))
                .price(entity.getPrice().toString())
                .contractAgreement(entity.getContractAgreementFileUrl())
                .build();
    }

    public static ComplaintmanagementResponse mapToComplaintmanagementResponse(Complaint_managment entity) {
        if (entity == null) {
            return null;
        }

        return ComplaintmanagementResponse.builder()
                .intId(entity.getIntId())
                .txtrAddress(entity.getTxtrAddress())
                .bitDeletedFlag(entity.getBitDeletedFlag())
//               .chkDeclaration(entity.getChkDeclaration())
                .selNameofCollateralManager(entity.getSelNameofCollateralManager())
                .rdoComplaintAgainst(entity.getRdoComplaintAgainst())
                .selTypeofComplain(entity.getSelTypeofComplain())
                .txtContactNumber(entity.getTxtContactNumber())
                .selCounty(entity.getSelCounty())
                .selCountyofWarehouse(entity.getSelCountyofWarehouse())
                .txtrDescriptionofIncident(entity.getTxtrDescriptionofIncident())
                .dtmCreatedOn(entity.getDtmCreatedOn())
                .txtEmailAddress(entity.getTxtEmailAddress())
                .txtDateofIncident(entity.getTxtDateofIncident())
                .selNameofInspector(entity.getSelNameofInspector())
                .intCreatedBy(entity.getIntCreatedBy())
                .intInsertStatus(entity.getIntInsertStatus())
                .intUpdatedBy(entity.getIntUpdatedBy())
                .selNameofGrader(entity.getSelNameofGrader())
             //   .selWarehouseShopName(entity.getSelWarehouseShopName())
                .stmUpdatedOn(entity.getStmUpdatedOn())
                .selSubCounty(entity.getSelSubCounty())
                .selSubCountyofWarehouse(entity.getSelSubCountyofWarehouse())
                .txtFullName(entity.getTxtFullName())
                .txtWarehouseOperator(entity.getTxtWarehouseOperator())
                .vchActionOnApplication(entity.getVchActionOnApplication())
//                .actionCondition(entity.getActionCondition())
                .complaintNo(entity.getComplaintNo())
                .notingCount(entity.getNotingCount())
                .tinStatus(entity.getTinStatus())
                .intProcessId(entity.getIntProcessId())
                .resubmitStatus(entity.getResubmitStatus())
                .resubmitCount(entity.getResubmitCount())
                .pendingATUser(entity.getPendingATUser())
                .selCountyVal(entity.getSelCountyVal())
                .selSubCountyVal(entity.getSelSubCountyVal())
                .applicationStatus(entity.getApplicationStatus())
                .storageCapacity("---")
                .allotment("---")
                .selWarehouseShopNameVal(entity.getSelWarehouseShopName())
                .build();
    }

    public static SalesApplicationTabOneDTO mapToSalesApplicationTabOneDTO(SalesApplicationTabOne entity){
        return SalesApplicationTabOneDTO.builder()
                .applicationId(entity.getApplicationId())
                .bitDeleteFlag(entity.getBitDeleteFlag())
                .applicationId(entity.getApplicationId())
                .stmUpdatedAt(entity.getStmUpdatedAt())
                .id(entity.getId())
                .intUpdatedBy(entity.getIntUpdatedBy())
                .intCreatedBy(entity.getIntCreatedBy())
                .dtmCreatedOn(entity.getDtmCreatedOn())
                .build();
    }
    public static SalesApplicationTabThreeDTO mapToSalesApplicationTabThreeDTO(SalesApplicationTabThree entity){
        return SalesApplicationTabThreeDTO.builder()
                .applicationId(entity.getApplicationId())
                .bitDeleteFlag(entity.getBitDeleteFlag())
                .isDeclared(entity.getIsDeclared())
                .id(entity.getId())
                .dateOfIncident(entity.getDateOfIncident())
                .intUpdatedBy(entity.getIntUpdatedBy())
                .intCreatedBy(entity.getIntCreatedBy())
                .dtmCreatedAt(entity.getDtmCreatedAt())
                .stmUpdatedAt(entity.getStmUpdatedAt())
                .uploadContractAgreement(entity.getUploadContractAgreement())
                .build();
    }
    public static SalesApplicationTabTwoDTO mapToSalesApplicationTabTwoDTO(SalesApplicationTabTwo entity){
        return  SalesApplicationTabTwoDTO.builder()
                .salesApplicationId(entity.getSalesApplicationId())
                .applicationId(entity.getApplicationId())
                .otherSaleReason(entity.getOtherSaleReason())
                .reasonForSale(entity.getReasonForSale())
                .priceOfReceipt(entity.getPriceOfReceipt())
                .uploadProofOfOwnership(entity.getUploadProofOfOwnership())
                .uploadWarehouseReceipt(entity.getUploadWarehouseReceipt())
                .bitDeleteFlag(entity.getBitDeleteFlag())
                .intUpdatedBy(entity.getIntUpdatedBy())
                .intCreatedBy(entity.getIntCreatedBy())
                .dtmCreatedAt(entity.getDtmCreatedAt())
                .stmUpdatedAt(entity.getStmUpdatedAt())
                .build();
    }

    public static SalesApplicationMainResponse mapToSalesApplicationMainResponse(SalesApplicationMain entity){
        return SalesApplicationMainResponse.builder()
                .salesApplicationMainId(entity.getSalesApplicationMainId())
                .applicationStatus(entity.getApplicationStatus())
                .salesApplicationTabOneDTO(mapToSalesApplicationTabOneDTO(entity.getIdtabone()))
                .salesApplicationTabTwoDTO(mapToSalesApplicationTabTwoDTO(entity.getIdtabtwo()))
                .salesApplicationTabThreeDTO(mapToSalesApplicationTabThreeDTO(entity.getIdtabthree()))
                .build();
    }
    
    
    
    
    public static SendNotificationDto mapSendNotificationDtoResponse(NotificationEntity notificationDto){
    	return  SendNotificationDto.builder()
    		.authNotificationNo(notificationDto.getAuthNotificationNo())
			.fromAuthId(notificationDto.getFromAuthId())
			.toAuthId(notificationDto.getToAuthId())
			.notification(notificationDto.getNotification())
			.notificationType(notificationDto.getNotificationType())
			.notificationModule(notificationDto.getNotificationModule())
			.bitReadStatus(notificationDto.getReadStatus())
			.vchPath(notificationDto.getVchPath()).build();
    }
    
    
    
    public static NotificationEntity mapSendNotificationDtoToNotificationEntity(SendNotificationDto notificationDto){
    	NotificationEntity notificationEntity = new NotificationEntity();
    	notificationEntity.setAuthNotificationNo(notificationDto.getAuthNotificationNo());
    	notificationEntity.setFromAuthId(notificationDto.getFromAuthId());
    	notificationEntity.setToAuthId(notificationDto.getToAuthId());
    	notificationEntity.setNotification(notificationDto.getNotification());
    	notificationEntity.setNotificationType(notificationDto.getNotificationType());
    	notificationEntity.setNotificationModule(notificationDto.getNotificationModule());
    	notificationEntity.setReadStatus(notificationDto.getBitReadStatus());
    	notificationEntity.setVchPath(notificationDto.getVchPath());
    	return notificationEntity;
    }
    
    
    /**
     * Complaint Status DTO
     */
    
    public static ComplaintStatusResponseDto mapToComplaintStatusDto(ComplaintStatusMaster data) {
    	if (data == null) {
            return null;
        }
        return ComplaintStatusResponseDto.builder()
        		.intId(data.getIntId()).vchComplaintStatusName(data.getVchComplaintStatusName())
                .isActive(data.getBitDeletedFlag())
                .build();
    	
    }
    
    /**
     * Payment Method DTO
     */
    public static PaymentStatusViewResponseDTO mapToPaymentTypeViewResponse(PaymentMethod data) {
    	if (data == null) {
            return null;
        }
        return PaymentStatusViewResponseDTO.builder()
        		.intId(data.getIntId()).txtPaymentMethod(data.getTxtPaymentMethod()).txtDescription(data.getTxtDescription())
        		.bitDeletedFlag(data.getBitDeletedFlag())
                .build();
    }
    
    /**
     * Login Trail DTO
     */
    
    public static LoginTrailDTO mapToLoginTrailDto(LoginTrail data) {
    	if (data == null) {
            return null;
        }
        return LoginTrailDTO.builder()
        		.userName(data.getUser() != null ? data.getUser().getTxtFullName() : "--")
        		.roleName(data.getRole() != null ? data.getRole().getTxtRoleName() : "--").action(data.getAction()).datetime(data.getDateTime())
        		.ipAddress(data.getIpAddress()).longitude(data.getLongitude()).latitude(data.getLatitude())
        		.os(data.getOs()).deviceName(data.getDeviceName()).enmStatus(data.getEnmStatus())
                .build();
    	
    }
    
    // Application Fee Config
    public static ApplicationFeeResponseDto mapToApplicationFeesDto(ApplicationFeeConfig data) {
    	if(data == null) {
    		return null;
    	}
    	return ApplicationFeeResponseDto.builder()
    			.id(data.getApplicationFeeId()).applicationType(data.getApplicationType()).applicationFee(data.getPaymentValue()).build();
    }
    
    //Lot data
    public static LotMasterResponseDto mapToLotDto(LotMaster data) {
    	if(data == null) {
    		return null;
    	}
    	
    	return LotMasterResponseDto.builder().id(data.getLotId())
    			.typeOfLot(data.getTypeOfLot()).noOfBags(data.getNoOfBags()).kgPerBag(data.getKgPerBag()).build();
    }
    

  
    
    public static OperatorLicenceApprovalResponseDto mapToOperatorLicenceApprovalDto(Map<String, Object> record) {
        OperatorLicenceApprovalResponseDto dto = new OperatorLicenceApprovalResponseDto();
        
        dto.setVchOPlId((String) record.get("vchOPlId"));
        dto.setVchWarehouseName((String) record.get("vchWarehouseName"));
        dto.setVchWhOperatorName((String) record.get("vchWhOperatorName"));
        dto.setVchEmail((String) record.get("vchEmail"));
        dto.setVchMobileNumber((String) record.get("vchMobileNumber"));
        dto.setVchLrNumber((String) record.get("vchLrNumber"));
        dto.setCountyName((String) record.get("county_name"));
        dto.setVchSubCountyName((String) record.get("vchSubCountyName"));
        dto.setWardName((String) record.get("wardName"));
        dto.setUserName((String) record.get("userName"));
        dto.setVchFormOneCId((String) record.get("vchFormOneCId"));
        dto.setPaymentId(String.valueOf(record.get("paymentId")));
        dto.setEnmPaymentStatus((String)record.get("enmPaymentStatus"));
        dto.setVchApplicationStatus((String) record.get("vchApplicationStatus")); 
        dto.setVchApprovalStatus((String) record.get("vchApprovalStatus"));
        dto.setAppId((Integer)record.get("intOnlineServiceId"));
        dto.setProcessId((Integer)record.get("intProcessId"));
        dto.setStageNo((Integer)record.get("intStageNo"));
        dto.setPendingAt(Integer.parseInt(record.get("intPendingAt").toString()));        
        dto.setLabelId((Integer)record.get("intLabelId"));
        dto.setVchWareHouseId((String)record.get("vchWareHouseId"));
        dto.setVchCompanyId((String)record.get("vchCompanyId"));
        dto.setVchOplicenceId("-");
        dto.setIcmStatus((String)record.get("icmStatus")); 
        return dto;
    
    }

    // Grader-Weigher Registration Form Save
    public static GraderWeigherForm toEntity(GraderWeigherMainFormDTO graderWeigherDetailsDTO) {
        if (graderWeigherDetailsDTO == null) {
            return null;
        }
        GraderWeigherForm gwData = new GraderWeigherForm();
        gwData.setDesignationType(graderWeigherDetailsDTO.getGraderweigher().getDesignationType());
        gwData.setFullName(graderWeigherDetailsDTO.getGraderweigher().getFullName());
        gwData.setDateOfBirth(graderWeigherDetailsDTO.getGraderweigher().getDateOfBirth());
        gwData.setGender(graderWeigherDetailsDTO.getGraderweigher().getGender());
        gwData.setMobileNo(graderWeigherDetailsDTO.getGraderweigher().getMobileNo());
        gwData.setEmailId(graderWeigherDetailsDTO.getGraderweigher().getEmailId());
        gwData.setAlternateContactNo(graderWeigherDetailsDTO.getGraderweigher().getAlternateContactNo());
        gwData.setAddress(graderWeigherDetailsDTO.getGraderweigher().getAddress());
        gwData.setEmployeeId(graderWeigherDetailsDTO.getGraderweigher().getEmployeeId());
        gwData.setStartDate(graderWeigherDetailsDTO.getGraderweigher().getStartDate());
        gwData.setEndDate(graderWeigherDetailsDTO.getGraderweigher().getEndDate());
        gwData.setGovtIssuedId(graderWeigherDetailsDTO.getGraderweigher().getGovtIssuedId());
        //gwData.setIdType(graderWeigherDetailsDTO.getGraderweigher().getIdType());
        gwData.setIdNo(graderWeigherDetailsDTO.getGraderweigher().getIdNo());
        gwData.setExpiryDate(graderWeigherDetailsDTO.getGraderweigher().getExpiryDate());
        
        //gwData.setUploadedCertificatePath(graderWeigherDetailsDTO.getGraderweigher().getUploadedCertificatePath());
       // processDocument(graderWeigherDetailsDTO.getGraderweigher().getUploadedCertificatePath(), graderWeigherDetailsDTO.getGraderweigher().getFetchFromDb(), gwData::setUploadedCertificatePath, "GRADER_WEIGHER_REGISTRATION", FolderAndDirectoryConstant.GRADER_WEIGHER);
        return gwData;
    }

	public static GraderWeigherExperience toEntity(GraderWeigherExperienceDTO experienceDto) {
		if(experienceDto==null) {
			return null;
		}
		
		GraderWeigherExperience gwexperinece=new GraderWeigherExperience();
		
		gwexperinece.setExperience(experienceDto.getExperience());
		gwexperinece.setUploadedCertificatePath(experienceDto.getUploadedCertificatePath());
	    processDocument(experienceDto.getUploadedCertificatePath(), experienceDto.getFetchFromDb(), gwexperinece::setUploadedCertificatePath, "GRADER_WEIGHER_REGISTRATION", FolderAndDirectoryConstant.GRADER_WEIGHER);
		
		
		return gwexperinece;
	}
    

	
    public static OPLApplicationStatusDTO mapOPLApplicationStatusDTO(Map<String, Object> map) {
    	OPLApplicationStatusDTO dto = OPLApplicationStatusDTO.builder()
    	 .intLicenceSno((Integer) map.get("intLicenceSno"))		
         .vchOPlId((String) map.get("vchOPLAPPId"))
         .vchWarehouseName((String) map.get("vchWarehouseName"))
         .vchWhOperatorName((String) map.get("vchWhOperatorName"))
         .vchEmail((String) map.get("vchEmail"))
         .vchMobileNumber((String) map.get("vchMobileNumber"))
         .vchLrNumber((String) map.get("vchLrNumber"))
         .countyName((String) map.get("county_name"))
         .vchSubCountyName((String) map.get("vchSubCountyName"))
         .wardName((String) map.get("wardName"))
         .userName((String) map.get("userName"))
         .vchFormOneCId((String) map.get("vchFormOneCId"))
         .paymentId((Integer) map.get("paymentId"))
         .enmPaymentStatus((String) map.get("enmPaymentStatus"))
         .vchApplicationStatus((String) map.get("vchApplicationStatus"))
         .vchApprovalStatus((String) map.get("vchApprovalStatus"))
         .vchWareHouseId((String) map.get("vchWareHouseId"))
         .vchCompanyId((String) map.get("vchCompanyId"))
         .bitLicenseGen((Boolean) map.get("bitLicenseGen"))
         .bitLicenceCertGen((Boolean) map.get("bitLicenceCertGen"))
         .vchApproverRemarks((String) map.get("vchApproverRemarks"))
         .vchOplicenceId((String) map.get("vchOplId"))
         .amountExpected((String) map.get("amountExpected"))
         .currency((String) map.get("currency"))
         .vchStreetName((String) map.get("vchStreetName")) 
         .vchBuilding((String) map.get("vchBuilding"))
         .vchCertId((String) map.get("vchCertId"))
         .bitLicenceCertGen((Boolean) map.get("bitCertGen"))
         .build(); 
       
    	 return dto;
    }
    

    
    public static OPLFormDetailsDTO convertToFormDTO(Map<String, Object> map) {
    	
    	OPLFormDetailsDTO dto = OPLFormDetailsDTO.builder()
                .vchOPlId((String) map.get("vchOPlId"))
                .vchWarehouseName((String) map.get("vchWarehouseName"))
                .vchWhOperatorName((String) map.get("vchWhOperatorName"))
                .vchEmail((String) map.get("vchEmail"))
                .vchMobileNumber((String) map.get("vchMobileNumber"))
                .vchLrNumber((String) map.get("vchLrNumber"))
                .countyName((String) map.get("county_name"))
                .vchSubCountyName((String) map.get("vchSubCountyName"))
                .wardName((String) map.get("wardName"))
                .userName((String) map.get("userName"))
                .vchFormOneCId((String) map.get("vchFormOneCId"))
                .paymentId((Integer)map.get("paymentId"))
                .enmPaymentStatus((String) map.get("enmPaymentStatus"))
                .vchApplicationStatus((String) map.get("vchApplicationStatus"))
                .vchApprovalStatus((String) map.get("vchApprovalStatus"))
                .intOnlineServiceId((Integer) map.get("intOnlineServiceId"))
                .intStageNo((Integer) map.get("intStageNo"))
                .intPendingAt(Integer.parseInt(map.get("intPendingAt").toString()))
                .intProcessId((Integer) map.get("intProcessId"))
                .intLabelId((Integer) map.get("intLabelId"))
                .vchWareHouseId((String) map.get("vchWareHouseId"))
                .vchCompanyId((String) map.get("vchCompanyId"))
                .appPaymentStatus((String) map.get("appPaymentStatus"))
                .bitLicenseGen((Boolean) map.get("bitLicenseGen"))
                .bitLicenceCertGen((Boolean) map.get("bitLicenceCertGen"))
                .vchApproverRemarks((String) map.get("vchApproverRemarks"))
                .vchOplicenceId((String) map.get("vchOplId"))
                .amountExpected((String) map.get("amountExpected"))
                .currency((String) map.get("currency"))
                .vchStreetName((String) map.get("vchStreetName"))
                .vchBuilding((String) map.get("vchBuilding"))
                .vchCertId((String) map.get("vchCertId"))
                .build();
    	
    	return dto;
    }

    public static ApplicationCertificateOfCollateral toEntity(CollateralManagerMainFormDTO collateralDetailsDTO) {
		
		 if (collateralDetailsDTO == null) {
		        return null;
		    }

		    ApplicationCertificateOfCollateral entity = new ApplicationCertificateOfCollateral();

		    entity.setFullName(collateralDetailsDTO.getCollateralProfile().getFullName() != null ? collateralDetailsDTO.getCollateralProfile().getFullName() : "");
		    entity.setMobileNo(collateralDetailsDTO.getCollateralProfile().getMobileNo() != null ? collateralDetailsDTO.getCollateralProfile().getMobileNo() : "");
		    entity.setEmail(collateralDetailsDTO.getCollateralProfile().getEmail() != null ? collateralDetailsDTO.getCollateralProfile().getEmail() : "");
		    entity.setPostalCode(collateralDetailsDTO.getCollateralProfile().getPostalCode() != null ? collateralDetailsDTO.getCollateralProfile().getPostalCode() : "");
		    entity.setPostalAddress(collateralDetailsDTO.getCollateralProfile().getPostalAddress() != null ? collateralDetailsDTO.getCollateralProfile().getPostalAddress() : "");
		    entity.setTown(collateralDetailsDTO.getCollateralProfile().getTown() != null ? collateralDetailsDTO.getCollateralProfile().getTown() : "");
		    entity.setTelephoneNo(collateralDetailsDTO.getCollateralProfile().getTelephoneNo() != null ? collateralDetailsDTO.getCollateralProfile().getTelephoneNo() : "");
		    entity.setWebsite(collateralDetailsDTO.getCollateralProfile().getWebsite() != null ? collateralDetailsDTO.getCollateralProfile().getWebsite() : "");
		    entity.setEntityType(collateralDetailsDTO.getCollateralProfile().getEntityType() != null ? collateralDetailsDTO.getCollateralProfile().getEntityType() : "");
		    entity.setCompanyName(collateralDetailsDTO.getCollateralProfile().getCompanyName() != null ? collateralDetailsDTO.getCollateralProfile().getCompanyName() : "");
		    entity.setCompanyRegNo(collateralDetailsDTO.getCollateralProfile().getCompanyRegNo() != null ? collateralDetailsDTO.getCollateralProfile().getCompanyRegNo() : "");
		    entity.setKraPin(collateralDetailsDTO.getCollateralProfile().getKraPin() != null ? collateralDetailsDTO.getCollateralProfile().getKraPin() : "");

		    // Boolean fields default to false if null
		   // entity.setInspector(collateralDetailsDTO.getCollateralProfile().getInspector() != null ? collateralDetailsDTO.getCollateralProfile().getInspector() : false);
		  //  entity.setCertificateCommittee(collateralDetailsDTO.getCollateralProfile().getCertificateCommittee() != null ? collateralDetailsDTO.getCollateralProfile().getCertificateCommittee() : false);
		  //  entity.setDefer(collateralDetailsDTO.getCollateralProfile().getDefer() != null ? collateralDetailsDTO.getCollateralProfile().getDefer() : false);

		    // Default integer fields to 0 if null
		    //entity.setActionStatus(collateralDetailsDTO.getCollateralProfile().getActionStatus() != null ? collateralDetailsDTO.getCollateralProfile().getActionStatus() : 0);

		    return entity;
	}

	public static AocComplianceCollarterDirector toEntity(AocComplianceCollarterDirectorDTO collateralDirectorDTO) {
		if (collateralDirectorDTO == null) {
           return null;
       }else {
       	AocComplianceCollarterDirector directorData = new AocComplianceCollarterDirector();
            directorData.setDirectorName(collateralDirectorDTO.getDirectorName() != null ?collateralDirectorDTO.getDirectorName(): "");
            directorData.setNationalityId(collateralDirectorDTO.getNationalityId()!=null ? collateralDirectorDTO.getNationalityId() : 0);
       	 directorData.setPosition(collateralDirectorDTO.getPosition() != null ?collateralDirectorDTO.getPosition():null );
            directorData.setPassportNo(collateralDirectorDTO.getPassportNo() !=null ? collateralDirectorDTO.getPassportNo() : null);
           processDocument(collateralDirectorDTO.getWorkPermitPath(), collateralDirectorDTO.getFetchFromDb(), directorData::setWorkPermitPath, "COLLATERAL_DIRECTOR_WORKPERMIT_", FolderAndDirectoryConstant.COLLATERAL_MANAGER_FOLDER);
           return directorData;
      }
	}

	public static AocComplianceCollarterSignSeal toEntity(AocComplianceCollarterSignSealDTO signSealDto) {
		
		 if (signSealDto == null) {
	            return null;
	        }
		 else {
		    AocComplianceCollarterSignSeal signSeal = new AocComplianceCollarterSignSeal();
	        signSeal.setSignName(signSealDto.getSignName()!=null ?signSealDto.getSignName() : "" );
	        signSeal.setSignTitle(signSealDto.getSignTitle() != null ? signSealDto.getSignTitle() : "");
	        processDocument(signSealDto.getSignPath(), signSealDto.getSignPathfetchFromDb(), signSeal::setSignPath, "COLLATERAL_DET_SIGN_", FolderAndDirectoryConstant.COLLATERAL_MANAGER_FOLDER);
	        processDocument(signSealDto.getSealPath(), signSealDto.getSealPathfetchFromDb(), signSeal::setSealPath, "COLLATERAL_DET_SEAL_", FolderAndDirectoryConstant.COLLATERAL_MANAGER_FOLDER);
	        processDocument(signSealDto.getApplicationPath(), signSealDto.getApplicationPathfetchFromDb(), signSeal::setApplicationPath, "COLLATERAL_DET_APPLICATION_", FolderAndDirectoryConstant.COLLATERAL_MANAGER_FOLDER);
	        processDocument(signSealDto.getTaxCertificatePath(), signSealDto.getTaxCertificatePathfetchFromDb(), signSeal::setTaxCertificatePath, "COLLATERAL_DET_TAX_CERTIFICATE_", FolderAndDirectoryConstant.COLLATERAL_MANAGER_FOLDER);
	        processDocument(signSealDto.getIncorporationPath(), signSealDto.getIncorporationPathfetchFromDb(), signSeal::setIncorporationPath, "COLLATERAL_DET_INCORPORATION_", FolderAndDirectoryConstant.COLLATERAL_MANAGER_FOLDER);
	        processDocument(signSealDto.getPersonalDetailsPath(), signSealDto.getPersonalDetailsPathfetchFromDb(), signSeal::setPersonalDetailsPath, "COLLATERAL_DET_PRESONAL_DETAILS_", FolderAndDirectoryConstant.COLLATERAL_MANAGER_FOLDER);
	        processDocument(signSealDto.getProofWhPath(), signSealDto.getProofWhPathfetchFromDb(), signSeal::setProofWhPath, "COLLATERAL_DET_WHPROOF_", FolderAndDirectoryConstant.COLLATERAL_MANAGER_FOLDER);
	        
	        return signSeal;
		 }
	}

	public static ApplicationCertificateOfCollateralDto toDto(ApplicationCertificateOfCollateral savedCollateralDetails) {
		        if (savedCollateralDetails == null) {
		            return null;
		        }

		        ApplicationCertificateOfCollateralDto dto = new ApplicationCertificateOfCollateralDto();
		        dto.setApplicationId(savedCollateralDetails.getApplicationId());
		        dto.setFullName(savedCollateralDetails.getFullName());
		        dto.setMobileNo(savedCollateralDetails.getMobileNo());
		        dto.setEmail(savedCollateralDetails.getEmail());
		        dto.setPostalCode(savedCollateralDetails.getPostalCode());
		        dto.setPostalAddress(savedCollateralDetails.getPostalAddress());
		        dto.setTown(savedCollateralDetails.getTown());
		        dto.setTelephoneNo(savedCollateralDetails.getTelephoneNo());
		        dto.setWebsite(savedCollateralDetails.getWebsite());
		        dto.setEntityType(savedCollateralDetails.getEntityType());	        
		        dto.setCompanyName(savedCollateralDetails.getCompanyName());
		        dto.setCompanyRegNo(savedCollateralDetails.getCompanyRegNo());
		        dto.setKraPin(savedCollateralDetails.getKraPin());
		        dto.setActionStatus(savedCollateralDetails.getActionStatus());

		        return dto;
	}
	
	public static AocComplianceCollarterDirectorDTO toDto(AocComplianceCollarterDirector directorDetails) {
       if (directorDetails == null) {
           return null;
       }

       AocComplianceCollarterDirectorDTO dto = new AocComplianceCollarterDirectorDTO();
       dto.setDirectorId(directorDetails.getDirectorId());
       dto.setApplicationId(directorDetails.getApplicationId() != null ? directorDetails.getApplicationId().getApplicationId() : null);
       dto.setDirectorName(directorDetails.getDirectorName());
       dto.setNationalityId(directorDetails.getNationalityId());
       dto.setPassportNo(directorDetails.getPassportNo());
       dto.setPosition(directorDetails.getPosition());
       dto.setWorkPermitPath(directorDetails.getWorkPermitPath());
       dto.setFetchFromDb(true);

       return dto;
}

	public static AocComplianceCollarterSignSealDTO toDto(AocComplianceCollarterSignSeal savedSignSealDetails) {
		if (savedSignSealDetails == null) {
           return null;
       }

       AocComplianceCollarterSignSealDTO dto = new AocComplianceCollarterSignSealDTO();
       dto.setSignSealId(savedSignSealDetails.getSignSealId());
       dto.setApplicationId(savedSignSealDetails.getApplicationId() != null ? savedSignSealDetails.getApplicationId().getApplicationId() : null);
       dto.setSignName(savedSignSealDetails.getSignName());
       dto.setSignTitle(savedSignSealDetails.getSignTitle());
       dto.setSignPath(savedSignSealDetails.getSignPath());
       dto.setSealPath(savedSignSealDetails.getSealPath());
       dto.setSignPathfetchFromDb(true);
       dto.setSealPathfetchFromDb(true);

       return dto;
	}

	public static CollateralCVUploadedDetails toEntity(CollateralCvUploadDTO uploadCvDTO) {
		if (uploadCvDTO == null) {
	           return null;
	       }else {
	       	CollateralCVUploadedDetails cvUploaded = new CollateralCVUploadedDetails();
	       		cvUploaded.setTechnicalStaffName(uploadCvDTO.getTechnicalStaffName() != null ? uploadCvDTO.getTechnicalStaffName():"");
	           processDocument(uploadCvDTO.getUploadCvPath(), uploadCvDTO.getFetchFromDbOne(), cvUploaded::setUploadCvPath, "COLLATERAL_UPLOADED_CV", FolderAndDirectoryConstant.COLLATERAL_MANAGER_FOLDER);
	           return cvUploaded;
	      }
		
	}
	
	

    
}