package app.ewarehouse.service;

import app.ewarehouse.entity.Seasonality;

import java.util.List;

public interface SeasonalityService {
	
	boolean save(Seasonality seasonality);

    List<Seasonality> getAll();

    Seasonality getById(Integer id);

}
