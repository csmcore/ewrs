package app.ewarehouse.serviceImpl;

import app.ewarehouse.dto.TeamMemberDTO;
import app.ewarehouse.entity.TeamMember;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.TeamMemberRepository;
import app.ewarehouse.service.TeamMemberService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.RoutineComplianceMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamMemberServiceImpl implements TeamMemberService {

    @Autowired
    private TeamMemberRepository teamMemberRepository;
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper om;
    private static final Logger logger = LoggerFactory.getLogger(TeamMemberServiceImpl.class);

    @Override
    public Integer save(String data) {
        logger.info("Inside save method of TeamMemberServiceImpl");

        String decodedData = CommonUtil.inputStreamDecoder(data);
        TeamMember teamMember;

        try {
            teamMember = om.readValue(decodedData, TeamMember.class);
        } catch (Exception e) {
            throw new CustomGeneralException("Invalid JSON data format");
        }

        System.out.println(teamMember);

        Set<ConstraintViolation<TeamMember>> violations = validator.validate(teamMember);
        if (!violations.isEmpty()) {
            throw new CustomGeneralException(violations);
        }

        return teamMemberRepository.save(teamMember).getIntTeamMemberId();
    }

    @Override
    public TeamMemberDTO getById(Integer id) {
        logger.info("Inside getById method of TeamMemberServiceImpl");
        TeamMember teamMember = teamMemberRepository.findByIntTeamMemberIdAndBitDeleteFlag(id, false);
        return RoutineComplianceMapper.mapTeamMemberToDtoWithoutRoutineCompliance(teamMember);
    }

    @Override
    public List<TeamMemberDTO> getAll() {
        logger.info("Inside getAll method of TeamMemberServiceImpl");
        List<TeamMember> teamMembersList = teamMemberRepository.findAllByBitDeleteFlag(false);
        return teamMembersList.stream()
                .map(RoutineComplianceMapper::mapTeamMemberToDtoWithoutRoutineCompliance)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TeamMemberDTO> getAll(Pageable pageable) {
        logger.info("Inside getAll pageable method of TeamMemberServiceImpl");
        Page<TeamMember> teamMemberPage = teamMemberRepository.findAllByBitDeleteFlag(false, pageable);

        List<TeamMemberDTO> teamMembersDTOList = teamMemberPage.getContent().stream()
                .map(RoutineComplianceMapper::mapTeamMemberToDtoWithoutRoutineCompliance)
                .collect(Collectors.toList());

        return new PageImpl<>(teamMembersDTOList, pageable, teamMemberPage.getTotalElements());
    }

    @Override
    public Integer deleteById(Integer id) {
        logger.info("Inside deleteById method of TeamMemberServiceImpl");
        TeamMember teamMember = teamMemberRepository.findByIntTeamMemberIdAndBitDeleteFlag(id, false);
        teamMember.setBitDeleteFlag(true);
        teamMemberRepository.save(teamMember);
        return teamMember.getIntTeamMemberId();
    }
}

