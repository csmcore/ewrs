package app.ewarehouse.dto;

import app.ewarehouse.entity.ApplicationOfConformity;
import app.ewarehouse.util.Mapper;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicationOfConformityMapper {
	
//	 public static ApplicationOfConformityDTO toDto(ApplicationOfConformity entity) {
//		 ApplicationOfConformityDTO dto = new ApplicationOfConformityDTO();
//	        dto.setApplicationId(entity.getApplicationId());
//	        dto.setParticularsDTO(ApplicationOfConformityParticularOfApplicantsMapper.toDto(entity.getParticularOfApplicantsId()));
//	        dto.setSupportingDocumentsDTO(Mapper.toDto(entity.getSupportingDocumentId()));
//	        dto.setWarehouseOperatorDTO(Mapper.toDto(entity.getViabilityId()));
//	        dto.setPaymentDetailsDTO(Mapper.toDto(entity.getPaymentId()));
//	        dto.setEnmStatus(entity.getEnmStatus());
//	        dto.setIntCreatedBy(entity.getIntCreatedBy());
//	        dto.setIntUpdatedBy(entity.getIntUpdatedBy());
//	        dto.setDtmCreatedOn(entity.getDtmCreatedOn());
//	        dto.setStmUpdatedOn(entity.getStmUpdatedOn());
//	        dto.setBitDeletedFlag(entity.getBitDeletedFlag());
//	        return dto;
//		}
	 
//	public static List<ApplicationOfConformityDTO> toDtoList(List<ApplicationOfConformity> entities) {
//		return entities.stream()
//         .map(ApplicationOfConformityMapper::toDto)
//         .collect(Collectors.toList());
//	}

}
 