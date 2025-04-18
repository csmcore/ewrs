package app.ewarehouse.mapper;

import app.ewarehouse.dto.ApplicationCollateralDTO;
import app.ewarehouse.dto.receiveCommodityResponse;
import app.ewarehouse.entity.*;
import app.ewarehouse.util.DateTimeUtil;
import app.ewarehouse.util.Mapper;
import app.ewarehouse.util.RoutineComplianceMapper;

import java.util.Date;

public class ApplicationCollateralMapper {

    public static ApplicationCollateralDTO mapToDTO(TapplicationOfCertificateOfCompliance entity) {
        if (entity == null) {
            return null;
        }

        ApplicationCollateralDTO dto = ApplicationCollateralDTO.builder()
                .txtApplicationId(entity.getTxtApplicationId())
                .txtFullName(entity.getTxtFullName())
                .txtAddress(entity.getTxtAddress())
                .txtPostalCode(entity.getTxtPostalCode())
                .txtTown(entity.getTxtTown())
                .txtTelephoneNumber(entity.getTxtTelephoneNumber())
                .txtMobilePhoneNumber(entity.getTxtMobilePhoneNumber())
                .txtEmail(entity.getTxtEmail())
                .txtWebsite(entity.getTxtWebsite())
                .txtEntityType(entity.getTxtEntityType())
                .txtOtherEntityType(entity.getTxtOtherEntityType())
                .txtCompanyRegistrationNumber(entity.getTxtCompanyRegistrationNumber())
                .txtKraPin(entity.getTxtKraPin())
                .txtDirectorIDs(entity.getTxtDirectorIDs())
                .txtDirectorIDsName(entity.getTxtDirectorIDsName())
                .txtDirectorsPassportsName(entity.getTxtDirectorsPassportsName())
                .txtDirectorsPassports(entity.getTxtDirectorsPassports())
                .txtPhysicalAddress(entity.getTxtPhysicalAddress())
                .subCounty(entity.getSubCounty() != null ? Mapper.mapToSubCountyResponse(entity.getSubCounty()) : null)  // Assuming you have a method to convert SubCounty to SubCountyResponse
                .txtPhysicalTown(entity.getTxtPhysicalTown())
                .txtStreetName(entity.getTxtStreetName())
                .txtPaymentMode(entity.getTxtPaymentMode())
                .isDeclared(entity.getIsDeclared())
                .intCreatedBy(entity.getIntCreatedBy())
                .intUpdatedBy(entity.getIntUpdatedBy())
                .dtmCreatedOn(entity.getDtmCreatedOn())
                .stmUpdatedOn(entity.getStmUpdatedOn())
                .bitDeletedFlag(entity.getBitDeletedFlag())
                .status(entity.getStatus())
                .intCurrentRole(entity.getIntCurrentRole())
                .vchOicLegalRemark(entity.getVchOicLegalRemark())
                .vchOicFinRemark(entity.getVchOicFinRemark())
                .vchOicLegalTwoRemark(entity.getVchOicLegalTwoRemark())
                .inspector(RoutineComplianceMapper.mapInspectorToDtoWithoutRoutineCompliance(entity.getInspector()))
                .inspectionDetails(entity.getInspectionDetails())
                .vchOicLegalThreeRemark(entity.getVchOicLegalThreeRemark())
                .aproverDetails(entity.getAproverDetails())
                .ceoApprovalDetails(entity.getCeoApprovalDetails())
                .enmApprovalStatus(entity.getEnmApprovalStatus())
                .intApprovalStage(entity.getIntApprovalStage())
                .vchTransactionNo(entity.getVchTransactionNo())
                .vchChallanNo(entity.getVchChallanNo())
                .bitPaymentSuccess(entity.getBitPaymentSuccess())
                .collateralManagerStatus(entity.getCollateralManagerStatus())
                .timesSuspended(entity.getTimesSuspended())
                .build();

        if (dto.getDtmCreatedOn() != null) {
            long yearsDifference = DateTimeUtil.timeDiff(new Date(), dto.getDtmCreatedOn(), "Years");
            dto.setExperience((int) yearsDifference);
        }
        return dto;
    }
}
