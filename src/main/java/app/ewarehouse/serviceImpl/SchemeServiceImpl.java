package app.ewarehouse.serviceImpl;

import app.ewarehouse.entity.MScheme;
import app.ewarehouse.repository.SchemeRepository;
import app.ewarehouse.service.SchemeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchemeServiceImpl implements SchemeService {
    private final SchemeRepository schemeRepository;

    @Override
    public List<MScheme> getAllSchemes() {
        log.info("Inside getAllSchemes method of SchemeServiceImpl");
        try {
            return schemeRepository.findAll();
        } catch (Exception e) {
            log.info("Error occurred in getAllSchemes method of SchemeServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
