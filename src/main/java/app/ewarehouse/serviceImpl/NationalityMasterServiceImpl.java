package app.ewarehouse.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import app.ewarehouse.dto.NationalityMasterDto;
import app.ewarehouse.entity.Nationality;
import app.ewarehouse.repository.NationalityMasterRepository;
import app.ewarehouse.service.NationalityMasterService;

@Service
public class NationalityMasterServiceImpl implements NationalityMasterService {

	private static final Logger logger = LoggerFactory.getLogger(NationalityMasterServiceImpl.class);

	private final NationalityMasterRepository nationalityRepository;

	public NationalityMasterServiceImpl(NationalityMasterRepository nationalityRepository) {
		this.nationalityRepository = nationalityRepository;
	}

	@Override
	public List<Nationality> getAllNationalities() {
		logger.info("Fetching all nationalities");
		return nationalityRepository.findAll();
	}

	@Override
	public Optional<Nationality> getNationalityById(Long id) {
		logger.info("Fetching nationality with ID: {}", id);
		return nationalityRepository.findById(id);
	}

	@Override
	public Nationality createNationality(NationalityMasterDto nationalityDto) {
		logger.info("Creating new nationality: {}", nationalityDto);
		Nationality nationality = new Nationality();
		nationality.setVchNationalityName(nationalityDto.getVchNationalityName());
		return nationalityRepository.save(nationality);
	}

	@Override
	public Nationality updateNationality(Long id, NationalityMasterDto nationalityDto) {
		logger.info("Updating nationality with ID: {}", id);
		Optional<Nationality> optionalNationalityMaster = nationalityRepository.findById(id);
		if (optionalNationalityMaster.isPresent()) {
			Nationality savedNationality = optionalNationalityMaster.get();
			savedNationality.setVchNationalityName(nationalityDto.getVchNationalityName());
			logger.info("Updated nationality: {}", savedNationality);
			return nationalityRepository.save(savedNationality);
		} else {
			logger.warn("Nationality with ID: {} not found", id);
			return null;
		}
	}

	@Override
	public void changeNationalityStatus(Long id) {
		logger.info("Changing status of nationality with ID: {}", id);
		nationalityRepository.changeNationalityStatus(id);
	}

	@Override
	public List<Nationality> getNationalities() {
		logger.info("Fetching all nationalities");
		return nationalityRepository.findByBitDeletedFlagFalse();
	}

}
