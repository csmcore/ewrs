package app.ewarehouse.service;

import app.ewarehouse.dto.TeamMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeamMemberService {
    Integer save(String teamMember);
    TeamMemberDTO getById(Integer Id);
    List<TeamMemberDTO> getAll();
    Page<TeamMemberDTO> getAll(Pageable pageable);
    Integer deleteById(Integer id);
}
