package app.ewarehouse.dto;

import java.util.Base64;

import app.ewarehouse.entity.ApplicationOfConformityDirectorDetails;
import app.ewarehouse.entity.ApplicationOfConformityParticularOfApplicants;
import app.ewarehouse.entity.County;
import app.ewarehouse.entity.Nationality;
import app.ewarehouse.entity.SubCounty;
import app.ewarehouse.entity.UnitOfMeasurement;
import app.ewarehouse.util.DocumentUploadutil;
import app.ewarehouse.util.FolderAndDirectoryConstant;

public class ApplicationOfConformityParticularOfApplicantsMapper {

	public static ApplicationOfConformityParticularOfApplicants toEntity(ApplicationOfConformityParticularOfApplicantsDTO dto) {
		ApplicationOfConformityParticularOfApplicants entity = new ApplicationOfConformityParticularOfApplicants();
		if(dto.getIntParticularOfApplicantsId() != null) {
			entity.setId(dto.getIntParticularOfApplicantsId());
		}
		entity.setApplicantFullName(dto.getApplicantFullName());
		entity.setPostalAddress(dto.getPostalAddress());
		entity.setPostalCode(dto.getPostalCode());
		entity.setTown(dto.getTown());
		entity.setTelephoneNumber(dto.getTelephoneNumber());
		entity.setMobilePhoneNumber(dto.getMobilePhoneNumber());
		entity.setEmail(dto.getEmail());
		entity.setWebsite(dto.getWebsite());
//		EntityType ent = new EntityType();
//		ent.setIntEntityTypeId(dto.getEntityTypeId());
//		entity.setEntityTypeId(ent);
		entity.setCompanyRegistrationNumber(dto.getCompanyRegistrationNumber());
		entity.setKraPin(dto.getKraPin());
		entity.setLrNumber(dto.getLrNumber());
		County county = new County();
		county.setId(dto.getCountyId());
		entity.setCountyId(county);
		SubCounty sc = new SubCounty();
		sc.setIntId(dto.getSubCountyId());
		entity.setSubCountyId(sc);
		entity.setWard(dto.getWard());
		entity.setStreetName(dto.getStreetName());
		entity.setBuilding(dto.getBuilding());
		entity.setTypeOfCommodities(dto.getTypeOfCommodities());
		entity.setStorageCapacity(dto.getStorageCapacity());
		entity.setAuthorizedSignName(dto.getAuthorizedSignName());
		entity.setAuthorizedSignTitle(dto.getAuthorizedSignTitle());
		entity.setDate(dto.getDate());
		if(!Boolean.TRUE.equals(dto.getSignFetchFromDb())) {
		byte[] decodeSign = Base64.getDecoder().decode(dto.getUploadSignPath().getBytes());
		entity.setUploadSignPath(DocumentUploadutil.uploadFileByte("AOC_SIGNATURE_"+System.currentTimeMillis(), decodeSign, FolderAndDirectoryConstant.AOC_PARTICULAR_FOLDER));
		}else {
			entity.setUploadSignPath(dto.getUploadSignPath());
		}
		if(!Boolean.TRUE.equals(dto.getSealFetchFromDb())) {
		byte[] decodeSeal = Base64.getDecoder().decode(dto.getUploadCompanySealPath().getBytes());
		entity.setUploadCompanySealPath(DocumentUploadutil.uploadFileByte("AOC_COMPANY_SEAL_"+System.currentTimeMillis(), decodeSeal, FolderAndDirectoryConstant.AOC_PARTICULAR_FOLDER));
		}else {
			entity.setUploadCompanySealPath(dto.getUploadCompanySealPath());
		}
		entity.setIntCreatedBy(dto.getUserId());
		entity.setShop(dto.getShop());
		UnitOfMeasurement uu = new UnitOfMeasurement();
		uu.setUnitId(dto.getUnit());
		entity.setUnitId(uu);
		entity.setBitDraftStatusFlag(dto.getDraftStatus());
		return entity;
	}
	public static ApplicationOfConformityParticularOfApplicantsDTO toDto(ApplicationOfConformityParticularOfApplicants entity) {
		ApplicationOfConformityParticularOfApplicantsDTO dto = new ApplicationOfConformityParticularOfApplicantsDTO();

		dto.setApplicantFullName(entity.getApplicantFullName());
		dto.setPostalAddress(entity.getPostalAddress());
		dto.setPostalCode(entity.getPostalCode());
		dto.setTown(entity.getTown());
		dto.setTelephoneNumber(entity.getTelephoneNumber());
		dto.setMobilePhoneNumber(entity.getMobilePhoneNumber());
		dto.setEmail(entity.getEmail());
		dto.setWebsite(entity.getWebsite());
		dto.setEntityTypeId(entity.getEntityTypeId().getIntEntityTypeId());
		dto.setCompanyRegistrationNumber(entity.getCompanyRegistrationNumber());
		dto.setKraPin(entity.getKraPin());
		dto.setLrNumber(entity.getLrNumber());
		dto.setCountyId(entity.getCountyId().getId());
		dto.setSubCountyId(entity.getSubCountyId().getIntId());
		dto.setWard(entity.getWard());
		dto.setStreetName(entity.getStreetName());
		dto.setBuilding(entity.getBuilding());
		//dto.setTypeOfCommodities(entity.getTypeOfCommodities());
		dto.setStorageCapacity(entity.getStorageCapacity());
		dto.setAuthorizedSignName(entity.getAuthorizedSignName());
		dto.setAuthorizedSignTitle(entity.getAuthorizedSignTitle());
		dto.setDate(entity.getDate());
//		byte[] decodeSign = Base64.getDecoder().decode(entity.getUploadSignPath().getBytes());
//		byte[] decodeSeal = Base64.getDecoder().decode(entity.getUploadCompanySealPath().getBytes());
//		dto.setUploadSignPath(new DocumentUploadutil().uploadFileByte(dto.getActualFileSign().substring(0, dto.getActualFileSign().lastIndexOf('.')), decodeSign, FolderAndDirectoryConstant.AOC_PARTICULAR_FOLDER));
//		dto.setUploadCompanySealPath(new DocumentUploadutil().uploadFileByte(dto.getActualFileSeal().substring(0, dto.getActualFileSeal().lastIndexOf('.')), decodeSeal, FolderAndDirectoryConstant.AOC_PARTICULAR_FOLDER));
		return dto;
	}
	
	
	public static ApplicationOfConformityDirectorDetails toEntityDirector(ApplicationOfConformityDirectorDetailsDTO dto) {
		ApplicationOfConformityDirectorDetails entity = new ApplicationOfConformityDirectorDetails();
		if(dto.getDirectorDetailId() != null) {
			entity.setId(dto.getDirectorDetailId());
		}
		entity.setDirectorName(dto.getDirectorName());
		ApplicationOfConformityParticularOfApplicants acp=new ApplicationOfConformityParticularOfApplicants();
		acp.setId(dto.getParticularOfApplicantsId());
		entity.setParticularOfApplicants(acp);
		entity.setPassportNumber(dto.getPassportNumber());
		Nationality nationality = new Nationality();
		nationality.setIntNationalityId(dto.getNationalityId());
		entity.setNationality(nationality);
		if(!Boolean.TRUE.equals(dto.getFetchFromDb())) {
		byte[] decode = Base64.getDecoder().decode(dto.getUploadWorkPermitPath().getBytes());
		entity.setUploadWorkPermitPath(DocumentUploadutil.uploadFileByte("AOC_WORK_PERMIT_"+System.currentTimeMillis(),decode,FolderAndDirectoryConstant.AOC_PARTICULAR_FOLDER));
		}else {
			entity.setUploadWorkPermitPath(dto.getUploadWorkPermitPath());
		}
		return entity;
	}
	
	public static ApplicationOfConformityDirectorDetailsDTO toDtoDirector(ApplicationOfConformityDirectorDetails entity) {
		ApplicationOfConformityDirectorDetailsDTO dto = new ApplicationOfConformityDirectorDetailsDTO();
		dto.setDirectorName(entity.getDirectorName());
		dto.setParticularOfApplicantsId(entity.getParticularOfApplicants().getId());
		dto.setPassportNumber(entity.getPassportNumber());
		byte[] decode = Base64.getDecoder().decode(entity.getUploadWorkPermitPath().getBytes());
		//dto.setUploadWorkPermitPath(DocumentUploadutil.uploadFileByte(dto.getActualFilePermit().substring(0, dto.getActualFilePermit().lastIndexOf('.')),decode,FolderAndDirectoryConstant.AOC_PARTICULAR_FOLDER));
		return dto;
	}
	
	
//	public static ApplicationOfConformityCommodityType toEntityCommodityType(AocTypeOfCommodityDto ct) {
//		ApplicationOfConformityCommodityType entity = new ApplicationOfConformityCommodityType();
//		if(ct.getTocId() != null) {
//			entity.setAocCommodityTypeId(ct.getTocId());
//		}
//		ApplicationOfConformityParticularOfApplicants acp=new ApplicationOfConformityParticularOfApplicants();
//		acp.setId(ct.getParticularOfApplicantsId());
//		entity.setParticularOfApplicants(acp);
//		Commodity cc = new Commodity();
//		cc.setId(ct.getId());
//		entity.setCommodityType(cc);
//		return entity;
//	}
	 
}
