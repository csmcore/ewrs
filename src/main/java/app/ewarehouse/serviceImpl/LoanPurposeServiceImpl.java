package app.ewarehouse.serviceImpl;

import app.ewarehouse.entity.MLoanPurpose;
import app.ewarehouse.repository.LoanPurposeRepository;
import app.ewarehouse.service.LoanPurposeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanPurposeServiceImpl implements LoanPurposeService {
    private final LoanPurposeRepository loanPurposeRepository;
    @Override
    public List<MLoanPurpose> getAll() {
        return loanPurposeRepository.findAll();
    }
}
