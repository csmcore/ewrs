package app.ewarehouse.serviceImpl;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.GraderWeigherExperienceDTO;
import app.ewarehouse.dto.GraderWeigherMainFormDTO;
import app.ewarehouse.dto.GraderWeigherRegFormDTO;
import app.ewarehouse.entity.GraderWeigherExperience;
import app.ewarehouse.entity.GraderWeigherForm;
import app.ewarehouse.entity.Mrole;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.repository.GraderWeigherExperienceRepository;
import app.ewarehouse.repository.GraderWeigherRegRepository;
import app.ewarehouse.repository.MroleRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.GraderWeigherRegService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.Mapper;

@Service
public class GraderWeigherRegServiceImpl implements GraderWeigherRegService {
	
	private static final String STATUS = "status";
	private static final String AN_UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred: ";
	private static final String ERROR = "error";
	
	@Autowired
	private GraderWeigherRegRepository graderWeigherRepository;
	
	@Autowired
	private GraderWeigherExperienceRepository experienceRepository;
	
	@Autowired
	ObjectMapper om;
	
	@Autowired
	private TuserRepository tuserRepository;
	
	@Autowired
	private MroleRepository mroleRepository;
	
	 private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	 private static final SecureRandom RANDOM = new SecureRandom();

	@Override
	public JSONObject saveGraderWeigherDetails(String data) {
		
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			GraderWeigherMainFormDTO graderWeigherDetailsDTO = om.readValue(decodedData,GraderWeigherMainFormDTO.class);
			
			// to save grader weigher registration form details
			GraderWeigherForm graderWeigherData=Mapper.toEntity(graderWeigherDetailsDTO);
			graderWeigherData.setCreatedBy(graderWeigherDetailsDTO.getUserId());
			if(graderWeigherDetailsDTO.getGraderweigher().getGraderWeigherId()!=null) {
				graderWeigherData.setGraderWeigherId(graderWeigherDetailsDTO.getGraderweigher().getGraderWeigherId());
				
			}else {
				graderWeigherData.setGraderWeigherId(generateId());
			}
			
			graderWeigherRepository.save(graderWeigherData);
			
			
			// to save add more data for experience details
			if(!graderWeigherDetailsDTO.getExperienceDetails().isEmpty()) {
				int i=1;
				for(GraderWeigherExperienceDTO experienceDto : graderWeigherDetailsDTO.getExperienceDetails()) {
					
					GraderWeigherExperience experienceData=Mapper.toEntity(experienceDto);
					
					if(experienceDto.getExperienceId()!=null) {
						experienceData.setExperienceId(experienceDto.getExperienceId());
						json.put(STATUS, 202);
					}else {
						 String dbCurrentId = experienceRepository.getId();
						 if(dbCurrentId == null) {
							 experienceData.setExperienceId("EXPERIENCE"+ (i));
						 }else {
							 int lastNumber = Integer.parseInt(dbCurrentId.replace("EXPERIENCE", ""));
						        i = lastNumber + 1;
						        experienceData.setExperienceId("EXPERIENCE"+ (i));
						 }
						
						json.put(STATUS, 200);
					}
					
					experienceData.setCreatedBy(graderWeigherDetailsDTO.getUserId());
					experienceData.setGraderWeigher(graderWeigherData);
					
					experienceRepository.save(experienceData);
					i=i+1;
				}
			}
			
			// save the register user in admin user table
			if(graderWeigherDetailsDTO.getGraderweigher().getGraderWeigherId()!=null) {
			Tuser user = tuserRepository.findByIntIdAndBitDeletedFlag(graderWeigherDetailsDTO.getUserId(), false);
			Integer roleID =mroleRepository.findByTxtRoleName(graderWeigherData.getDesignationType());
			//Optional<Tuser> existUser = tuserRepository.findBySelRole(roleID);
		    Tuser newTuser = new Tuser();
//		    if (existUser.isPresent()) {
//		    	newTuser.setIntId(existUser.get().getIntId());
//		    }
			newTuser.setTxtFullName(graderWeigherData.getFullName());
			newTuser.setSelGender(Integer.parseInt(graderWeigherData.getGender()));
			newTuser.setTxtMobileNo(graderWeigherData.getMobileNo());
			newTuser.setTxtEmailId(graderWeigherData.getEmailId());
			newTuser.setTxtAlternateMobileNumber(graderWeigherData.getAlternateContactNo());
			newTuser.setTxtDateOfJoining(graderWeigherData.getCreatedOn());
			newTuser.setTxtrAddress(graderWeigherData.getAddress());
				
			newTuser.setSelRole(roleID);	
			newTuser.setSelDesignation(7);
			newTuser.setSelEmployeeType(0);
			newTuser.setSelDepartment(1);
			newTuser.setSelGroup(0);
			newTuser.setSelCounty(user.getSelCounty());
			newTuser.setSelSubCounty(user.getSelSubCounty());
			newTuser.setSelWard(user.getSelWard());
			newTuser.setSelHierarchy(0);
			newTuser.setTxtUserId(graderWeigherData.getEmailId().substring(0, graderWeigherData.getEmailId().indexOf("@")));
			newTuser.setSelIcmRoleId(0);
			newTuser.setSelCLCRoleId(0);
			newTuser.setTxtPassword("6e95001cee8e57360ffd1ed3ca37035b413a3f198d71df367e3a47a3d822e4df");
			newTuser.setIntReportingAuth(0);
			newTuser.setChkPrevilege(3);
			tuserRepository.save(newTuser);
			}
			
		
		}catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			e.printStackTrace();			
			json.put(STATUS, 500);
		}
		return json;
	}

	@Override
	public Page<GraderWeigherMainFormDTO> getGraderWeigherDetails(int page, int size) {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdOn"));
		Page<GraderWeigherForm> graderWeigherPage = graderWeigherRepository.findAll(pageable);
		
		return graderWeigherPage.map(graderWeigherData ->{
			GraderWeigherMainFormDTO graderWeigherdetails = new GraderWeigherMainFormDTO();
			
			GraderWeigherRegFormDTO graderWeigherDto = new GraderWeigherRegFormDTO();
			
			BeanUtils.copyProperties(graderWeigherData, graderWeigherDto);
			graderWeigherdetails.setGraderweigher(graderWeigherDto);

			// Get experience
			List<GraderWeigherExperience> experienceList = experienceRepository.findByGraderWeigherAndDeletedFlagFalse(graderWeigherData);
			List<GraderWeigherExperienceDTO> experienceDTOs = experienceList.stream().map(exp -> {
				GraderWeigherExperienceDTO experienceDTO = new GraderWeigherExperienceDTO();
				BeanUtils.copyProperties(exp, experienceDTO);
				experienceDTO.setGraderWeigherId(graderWeigherData.getGraderWeigherId());
				return experienceDTO;
			}).collect(Collectors.toList());
			graderWeigherdetails.setExperienceDetails(experienceDTOs);
			
			return graderWeigherdetails;
		});
		
		
	}


	@Override
	@Transactional(readOnly = true)
	public GraderWeigherMainFormDTO getGraderWeigherDetailsById(String graderWeigherId) {
		GraderWeigherMainFormDTO graderWeigherDetailsDTO = new GraderWeigherMainFormDTO();

		// Get Grader-Weigher Details By id
		Optional<GraderWeigherForm> graderweigherOptional = graderWeigherRepository.findById(graderWeigherId);
		if (graderweigherOptional.isPresent()) {
			GraderWeigherForm graderWeigher = graderweigherOptional.get();
			GraderWeigherRegFormDTO graderWeigherDTO = new GraderWeigherRegFormDTO();
			BeanUtils.copyProperties(graderWeigher, graderWeigherDTO);
			graderWeigherDetailsDTO.setGraderweigher(graderWeigherDTO);

			// Get Experience details
			List<GraderWeigherExperience> expList = experienceRepository.findByGraderWeigherAndDeletedFlagFalse(graderWeigher);
			if(!expList.isEmpty()) {
			List<GraderWeigherExperienceDTO> experienceDTOs = expList.stream().map(exp -> {
				GraderWeigherExperienceDTO experienceDTO = new GraderWeigherExperienceDTO();
				BeanUtils.copyProperties(exp, experienceDTO);
				experienceDTO.setGraderWeigherId(graderWeigher.getGraderWeigherId());
				return experienceDTO;
			}).collect(Collectors.toList());
			graderWeigherDetailsDTO.setExperienceDetails(experienceDTOs);
			}
		}
		return graderWeigherDetailsDTO;
	}
	
	@Override
	public JSONObject removeExperience(String experienceId) {
		JSONObject response = new JSONObject();
		experienceRepository.markAsDeleted(experienceId);
		response.put("status", "Experience Removed");
		return response;
	}
	
	@Override
	public boolean duplicateCheck(String empId) {
		int count = graderWeigherRepository.countByEmployeeId(empId);
		System.err.println(count);
	    return count > 0;
	}
	
	
	// for unique id generate
	public static String generateId() {
        return randomDigit() + randomUppercaseLetter() + randomDigits(3) +
               randomUppercaseLetter() + randomDigit() + randomUppercaseLetters(3);
    }

    private static String randomDigit() {
        return String.valueOf(RANDOM.nextInt(10));
    }

    private static String randomUppercaseLetter() {
        return String.valueOf(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
    }

    private static String randomDigits(int count) {
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < count; i++) {
            digits.append(randomDigit());
        }
        return digits.toString();
    }

    private static String randomUppercaseLetters(int count) {
        StringBuilder letters = new StringBuilder();
        for (int i = 0; i < count; i++) {
            letters.append(randomUppercaseLetter());
        }
        return letters.toString();
    }
	

}
