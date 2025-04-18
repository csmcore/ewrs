package app.ewarehouse.controller;

import app.ewarehouse.dto.EntityTypeDto;
import app.ewarehouse.entity.EntityType;
import app.ewarehouse.service.EntityTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/master/entity-types")
public class EntityTypeController {
	private static final Logger logger = LoggerFactory.getLogger(EntityTypeController.class);

    private final EntityTypeService entityTypeService;

    public EntityTypeController(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<EntityType>> getAllEntityTypes() {
        logger.info("Fetching all entity types");
        List<EntityType> entityTypes = entityTypeService.getAllEntityTypes();
        return new ResponseEntity<>(entityTypes, HttpStatus.OK);
    }
    
    @GetMapping("/")
    public ResponseEntity<List<EntityType>> getEntityTypes() {
        logger.info("Fetching entity types");
        List<EntityType> entityTypes = entityTypeService.getEntityTypes();
        return new ResponseEntity<>(entityTypes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityType> getEntityTypeById(@PathVariable Long id) {
        logger.info("Fetching entity type with ID: {}", id);
        Optional<EntityType> entityType = entityTypeService.getEntityTypeById(id);
        return entityType.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> {
                    logger.warn("Entity type with ID: {} not found", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @PostMapping("/")
    public ResponseEntity<EntityType> createEntityType(@RequestBody EntityTypeDto entityTypeDto) {
        logger.info("Creating new entity type: {}", entityTypeDto);
        EntityType createdEntityType = entityTypeService.createEntityType(entityTypeDto);
        return new ResponseEntity<>(createdEntityType, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityType> updateEntityType(@PathVariable Long id, @RequestBody EntityTypeDto entityTypeDto) {
        logger.info("Updating entity type with ID: {}", id);
        EntityType updatedEntityType = entityTypeService.updateEntityType(id, entityTypeDto);
        if (updatedEntityType != null) {
            logger.info("Updated entity type: {}", updatedEntityType);
            return new ResponseEntity<>(updatedEntityType, HttpStatus.OK);
        } else {
            logger.warn("Entity type with ID: {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> changeEntityTypeStatus(@PathVariable Long id) {
        logger.info("Deleting entity type with ID: {}", id);
        entityTypeService.changeEntityTypeStatus(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
