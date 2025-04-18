package app.ewarehouse.serviceImpl;

import app.ewarehouse.dto.DisputeCategoryResponse;
import app.ewarehouse.repository.DisputeCategoryRepository;
import app.ewarehouse.service.DisputeCategoryService;
import app.ewarehouse.util.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DisputeCategoryServiceImpl implements DisputeCategoryService {

    @Autowired
    private DisputeCategoryRepository disputeCategoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<DisputeCategoryResponse> getAllDisputeCategories() {
        log.info("Inside getAllDisputeCategories method of DisputeCategoryServiceImpl");

        try {
            return disputeCategoryRepository.findAllDisputeCategories(false)
                    .stream()
                    .map(Mapper::disputeCategoryToResponseDTO)
                    .toList();
        } catch (Exception e) {
            log.error("Error occurred in getAllDisputeCategories method of DisputeCategoryServiceImpl: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch complaints");
        }

    }
}
