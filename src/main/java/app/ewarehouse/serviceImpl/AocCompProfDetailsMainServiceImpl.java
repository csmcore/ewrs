package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.AocCompProfDetailsMainDTO;
import app.ewarehouse.dto.AocCompProfDirectorDetDTO;
import app.ewarehouse.dto.AocCompProfSignSealDTO;
import app.ewarehouse.dto.AocCompProfileDetDTO;
import app.ewarehouse.entity.AocCompProfDirectorDetails;
import app.ewarehouse.entity.AocCompProfSignSeal;
import app.ewarehouse.entity.AocCompProfileDetails;
import app.ewarehouse.repository.AocCompProfDirectorDetRepository;
import app.ewarehouse.repository.AocCompProfSignSealRepository;
import app.ewarehouse.repository.AocCompProfileDetRepository;
import app.ewarehouse.service.AocCompProfDetailsMainService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.Mapper;

@Service
public class AocCompProfDetailsMainServiceImpl implements AocCompProfDetailsMainService {

	private static final String STATUS = "status";
	private static final String AN_UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred: ";
	private static final String ERROR = "error";

	@Autowired
	private AocCompProfileDetRepository profileRepository;

	@Autowired
	private AocCompProfDirectorDetRepository directorRepository;

	@Autowired
	private AocCompProfSignSealRepository signSealRepository;

	@Autowired
	ObjectMapper om;

	@Override
	@Transactional
	public JSONObject saveCompanyDetails(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			AocCompProfDetailsMainDTO companyDetailsDTO = om.readValue(decodedData, AocCompProfDetailsMainDTO.class);
			
			if(companyDetailsDTO.getCompanyProfile().getProfDetId() != null) {
			Optional<AocCompProfileDetails> content = profileRepository.findById(companyDetailsDTO.getCompanyProfile().getProfDetId());
			if(content.isPresent() && !companyDetailsDTO.getCompanyProfile().getTypeOfEntity().equals(content.get().getTypeOfEntity())) {
					directorRepository.deleteAllByProfileId(content.get().getProfDetId());
				}
			}
			
			// Save company profile
			AocCompProfileDetails profile = Mapper.toEntity(companyDetailsDTO);
			profile.setCreatedBy(companyDetailsDTO.getUserId());
			if (companyDetailsDTO.getCompanyProfile().getProfDetId() != null) {
				profile.setProfDetId(companyDetailsDTO.getCompanyProfile().getProfDetId());
			} else {
				profile.setProfDetId(getId("COP"));
			}
			profileRepository.save(profile);
			
			
			
			// Save directors
			if(!companyDetailsDTO.getDirectors().isEmpty()) {
			for (AocCompProfDirectorDetDTO directorDTO : companyDetailsDTO.getDirectors()) {
				AocCompProfDirectorDetails director = Mapper.toEntity(directorDTO);
				if (directorDTO.getDirectorId() != null) {
					director.setDirectorId(directorDTO.getDirectorId());
				} else {
					director.setDirectorId(getId("DIR"));
				}
				director.setCreatedBy(companyDetailsDTO.getUserId());
				director.setProfDet(profile);
				directorRepository.save(director);
			}
			}
			// Save sign/seal
			AocCompProfSignSeal signSeal = Mapper.toEntity(companyDetailsDTO.getSignSeal());
			if (companyDetailsDTO.getSignSeal().getSignSealId() != null) {
				signSeal.setSignSealId(companyDetailsDTO.getSignSeal().getSignSealId());
			} else {
				signSeal.setSignSealId(getId("SIN"));
			}
			signSeal.setCreatedBy(companyDetailsDTO.getUserId());
			signSeal.setProfDet(profile);
			signSealRepository.save(signSeal);
			json.put(STATUS, 200);
		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}

	@Override
	@Transactional(readOnly = true)
	public AocCompProfDetailsMainDTO getCompanyDetails(String profDetId) {
		AocCompProfDetailsMainDTO companyDetailsDTO = new AocCompProfDetailsMainDTO();

		// Get company profile
		Optional<AocCompProfileDetails> profileOptional = profileRepository.findById(profDetId);
		if (profileOptional.isPresent()) {
			AocCompProfileDetails profile = profileOptional.get();
			AocCompProfileDetDTO profileDTO = new AocCompProfileDetDTO();
			BeanUtils.copyProperties(profile, profileDTO);
			companyDetailsDTO.setCompanyProfile(profileDTO);

			// Get directors
			List<AocCompProfDirectorDetails> directors = directorRepository.findByProfDetAndDeletedFlagFalse(profile);
			if(!directors.isEmpty()) {
			List<AocCompProfDirectorDetDTO> directorDTOs = directors.stream().map(director -> {
				AocCompProfDirectorDetDTO directorDTO = new AocCompProfDirectorDetDTO();
				BeanUtils.copyProperties(director, directorDTO);
				directorDTO.setProfDetId(profile.getProfDetId());
				return directorDTO;
			}).collect(Collectors.toList());
			companyDetailsDTO.setDirectors(directorDTOs);
			}

			// Get sign/seal
			AocCompProfSignSeal signSeal = signSealRepository.findByProfDet(profile);
			if (signSeal != null) {
				AocCompProfSignSealDTO signSealDTO = new AocCompProfSignSealDTO();
				BeanUtils.copyProperties(signSeal, signSealDTO);
				signSealDTO.setProfDetId(profile.getProfDetId());
				companyDetailsDTO.setSignSeal(signSealDTO);
			}
		}
		return companyDetailsDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AocCompProfDetailsMainDTO> getAllCompanyDetails() {
		List<AocCompProfDetailsMainDTO> allCompanyDetailsDTOs = new ArrayList<>();

		// Get all company profiles
		List<AocCompProfileDetails> allProfiles = profileRepository.findAll();
		for (AocCompProfileDetails profile : allProfiles) {
			AocCompProfDetailsMainDTO companyDetailsDTO = new AocCompProfDetailsMainDTO();
			AocCompProfileDetDTO profileDTO = new AocCompProfileDetDTO();
			BeanUtils.copyProperties(profile, profileDTO);
			companyDetailsDTO.setCompanyProfile(profileDTO);

			// Get directors
			List<AocCompProfDirectorDetails> directors = directorRepository.findByProfDetAndDeletedFlagFalse(profile);
			List<AocCompProfDirectorDetDTO> directorDTOs = directors.stream().map(director -> {
				AocCompProfDirectorDetDTO directorDTO = new AocCompProfDirectorDetDTO();
				BeanUtils.copyProperties(director, directorDTO);
				directorDTO.setProfDetId(profile.getProfDetId());
				return directorDTO;
			}).collect(Collectors.toList());
			companyDetailsDTO.setDirectors(directorDTOs);

			// Get sign/seal
			AocCompProfSignSeal signSeal = signSealRepository.findByProfDet(profile);
			if (signSeal != null) {
				AocCompProfSignSealDTO signSealDTO = new AocCompProfSignSealDTO();
				BeanUtils.copyProperties(signSeal, signSealDTO);
				signSealDTO.setProfDetId(profile.getProfDetId());
				companyDetailsDTO.setSignSeal(signSealDTO);
			}

			allCompanyDetailsDTOs.add(companyDetailsDTO);
		}
		return allCompanyDetailsDTOs;
	}

	private String getId(String idName) {
		String dbCurrentId = switch (idName) {
		case "COP" -> profileRepository.getId();
		case "DIR" -> directorRepository.getId();
		case "SIN" -> signSealRepository.getId();
		default -> null;
		};
		if (dbCurrentId == null) {
			return idName + "100000";
		} else {
			Integer idNum = Integer.parseInt(dbCurrentId.substring(3, dbCurrentId.length()));
			AtomicInteger seq = new AtomicInteger(idNum);
			int nextVal = seq.incrementAndGet();
			return idName + nextVal;
		}

	}

	// get the data on the basis of user id
	@Override
	public List<AocCompProfDetailsMainDTO> getCompanyProfileDataByUserId(Integer userId) {
		// Get all company profiles by userId (createdBy)
		List<AocCompProfileDetails> profiles = profileRepository.findByCreatedBy(userId);

		// Map each profile to AocCompProfDetailsMainDTO
		return profiles.stream().map(profile -> {
			AocCompProfDetailsMainDTO companyDetailsDTO = new AocCompProfDetailsMainDTO();

			// Copy profile details
			AocCompProfileDetDTO profileDTO = new AocCompProfileDetDTO();
			BeanUtils.copyProperties(profile, profileDTO);
			companyDetailsDTO.setCompanyProfile(profileDTO);
			companyDetailsDTO.setUserId(profile.getCreatedBy());

			// Get directors
			List<AocCompProfDirectorDetails> directors = directorRepository.findByProfDetAndDeletedFlagFalse(profile);
			List<AocCompProfDirectorDetDTO> directorDTOs = directors.stream().map(director -> {
				AocCompProfDirectorDetDTO directorDTO = new AocCompProfDirectorDetDTO();
				BeanUtils.copyProperties(director, directorDTO);
				directorDTO.setProfDetId(profile.getProfDetId());
				return directorDTO;
			}).collect(Collectors.toList());
			companyDetailsDTO.setDirectors(directorDTOs);

			// Get sign/seal
			AocCompProfSignSeal signSeal = signSealRepository.findByProfDet(profile);
			if (signSeal != null) {
				AocCompProfSignSealDTO signSealDTO = new AocCompProfSignSealDTO();
				BeanUtils.copyProperties(signSeal, signSealDTO);
				signSealDTO.setProfDetId(profile.getProfDetId());
				companyDetailsDTO.setSignSeal(signSealDTO);
			}

			return companyDetailsDTO;
		}).collect(Collectors.toList());
	}

	@Override
	public Page<AocCompProfDetailsMainDTO> getCompanyProfileDataByUserId(Integer userId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AocCompProfileDetails> profilesPage = profileRepository.findByCreatedBy(userId, pageable);

		// Convert the Page<AocCompProfileDetails> to Page<AocCompProfDetailsMainDTO>
		return profilesPage.map(profile -> {
			AocCompProfDetailsMainDTO companyDetailsDTO = new AocCompProfDetailsMainDTO();

			// Copy profile details
			AocCompProfileDetDTO profileDTO = new AocCompProfileDetDTO();
			BeanUtils.copyProperties(profile, profileDTO);
			companyDetailsDTO.setCompanyProfile(profileDTO);
			companyDetailsDTO.setUserId(profile.getCreatedBy());

			// Get directors
			List<AocCompProfDirectorDetails> directors = directorRepository.findByProfDetAndDeletedFlagFalse(profile);
			List<AocCompProfDirectorDetDTO> directorDTOs = directors.stream().map(director -> {
				AocCompProfDirectorDetDTO directorDTO = new AocCompProfDirectorDetDTO();
				BeanUtils.copyProperties(director, directorDTO);
				directorDTO.setProfDetId(profile.getProfDetId());
				return directorDTO;
			}).collect(Collectors.toList());
			companyDetailsDTO.setDirectors(directorDTOs);

			// Get sign/seal
			AocCompProfSignSeal signSeal = signSealRepository.findByProfDet(profile);
			if (signSeal != null) {
				AocCompProfSignSealDTO signSealDTO = new AocCompProfSignSealDTO();
				BeanUtils.copyProperties(signSeal, signSealDTO);
				signSealDTO.setProfDetId(profile.getProfDetId());
				companyDetailsDTO.setSignSeal(signSealDTO);
			}

			return companyDetailsDTO;
		});
	}

	@Override
	public Integer getCountProfileDetails(Integer userId) {
		return profileRepository.getCountProfileDetails(userId);
	}

	@Override
	public JSONObject removeDirector(String directorId) {
		JSONObject response = new JSONObject();
		directorRepository.markAsDeleted(directorId);
		response.put("status", "Director Removed");
		return response;
	}

}
