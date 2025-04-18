package app.ewarehouse.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import app.ewarehouse.dto.EntityTypeDto;
import app.ewarehouse.entity.EntityType;
import app.ewarehouse.repository.EntityTypeRepository;
import app.ewarehouse.service.EntityTypeService;
@Service
public class EntityTypeServiceImpl implements EntityTypeService {

	private static final Logger logger = LoggerFactory.getLogger(EntityTypeServiceImpl.class);

    private final EntityTypeRepository entityTypeRepository;

    public EntityTypeServiceImpl(EntityTypeRepository entityTypeRepository) {
        this.entityTypeRepository = entityTypeRepository;
    }

    @Override
    public List<EntityType> getAllEntityTypes() {
        logger.info("Fetching all entity types");
        return entityTypeRepository.findAll();
    }

    @Override
    public Optional<EntityType> getEntityTypeById(Long id) {
        logger.info("Fetching entity type with ID: {}", id);
        return entityTypeRepository.findById(id);
    }

    @Override
    public EntityType createEntityType(EntityTypeDto entityTypeDto) {
        logger.info("Creating new entity type: {}", entityTypeDto);
        EntityType entityType = new EntityType();
        entityType.setVchEntityTypeName(entityTypeDto.getVchEntityTypeName());
        return entityTypeRepository.save(entityType);
    }

    @Override
    public EntityType updateEntityType(Long id, EntityTypeDto entityTypeDto) {
        logger.info("Updating entity type with ID: {}", id);
        Optional<EntityType> optionalEntityType = entityTypeRepository.findById(id);
        if (optionalEntityType.isPresent()) {
            EntityType savedEntityType = optionalEntityType.get();
            savedEntityType.setVchEntityTypeName(entityTypeDto.getVchEntityTypeName());
            logger.info("Updated entity type: {}", savedEntityType);
            return entityTypeRepository.save(savedEntityType);
        } else {
            logger.warn("Entity type with ID: {} not found", id);
            return null;
        }
    }

    @Override
    public void changeEntityTypeStatus(Long id) {
        logger.info("Deleting entity type with ID: {}", id);
        
    }

	@Override
	public List<EntityType> getEntityTypes() {
		logger.info("Fetching entity types");
        return entityTypeRepository.findByBitDeletedFlagFalse();
	}

}
