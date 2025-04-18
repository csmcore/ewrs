package app.ewarehouse.service;

import app.ewarehouse.dto.EntityTypeDto;
import app.ewarehouse.entity.EntityType;

import java.util.List;
import java.util.Optional;

public interface EntityTypeService {
	List<EntityType> getAllEntityTypes();

    Optional<EntityType> getEntityTypeById(Long id);

    EntityType createEntityType(EntityTypeDto entityTypeDto);

    EntityType updateEntityType(Long id, EntityTypeDto entityTypeDto);

    void changeEntityTypeStatus(Long id);

	List<EntityType> getEntityTypes();
}
