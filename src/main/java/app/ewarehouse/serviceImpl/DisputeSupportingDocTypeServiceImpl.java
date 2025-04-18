package app.ewarehouse.serviceImpl;

import app.ewarehouse.dto.DisputeSupportingDocumentTypeResponse;
import app.ewarehouse.repository.DisputeSupportingDocTypeRepository;
import app.ewarehouse.service.DisputeSupportingDocTypeService;
import app.ewarehouse.util.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DisputeSupportingDocTypeServiceImpl implements DisputeSupportingDocTypeService {

    @Autowired
    DisputeSupportingDocTypeRepository disputeSupportingDocTypeRepository;

    @Override
    public List<DisputeSupportingDocumentTypeResponse> getAllDisputeSupportingDocumentType() {
        log.info("Inside getAllDisputeSupportingDocumentType method of getAllDisputeSupportingDocumentType");
        try {
            return disputeSupportingDocTypeRepository.findAllDisputeSupportingDocumentType(false).stream()
                    .map(Mapper::disputeSupportingDocumentTypeToResponseDTO)
                    .toList();
        } catch (Exception e) {
            log.error("Error occurred in getAllDisputeSupportingDocumentType method of getAllDisputeSupportingDocumentType: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch docTypes");
        }

    }
}

