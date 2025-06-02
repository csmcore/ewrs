package app.ewarehouse.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import app.ewarehouse.entity.Seasonality;
import app.ewarehouse.repository.SeasonalityRepository;
import app.ewarehouse.service.SeasonalityService;

@Service
public class SeasonalityServiceImpl implements SeasonalityService {
	
	@Autowired
	private SeasonalityRepository seasonalityRepository;

	@Override
	public boolean save(Seasonality seasonality) {
		try {
            seasonalityRepository.save(seasonality);
            return true;
        } catch (Exception e) {
            return false;
        }
	}

	@Override
	public List<Seasonality> getAll() {
		 return seasonalityRepository.findAll();
	}

	@Override
	public Seasonality getById(Integer id) {
		java.util.Optional<Seasonality> optionalSeasonality = seasonalityRepository.findById(id);
        return optionalSeasonality.orElse(null);
	}
}
