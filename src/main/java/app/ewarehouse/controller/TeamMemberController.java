package app.ewarehouse.controller;

import app.ewarehouse.service.TeamMemberService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin/team-member")
public class TeamMemberController {

    private static final Logger logger = LoggerFactory.getLogger(TeamMemberController.class);

    @Autowired
    private TeamMemberService teamMemberService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<String> addEdit(@RequestBody String teamMember) throws JsonProcessingException {
        logger.info("Inside createOrUpdate method of TeamMemberController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(teamMemberService.save(teamMember), objectMapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id) throws JsonProcessingException {
        logger.info("Inside getById method of TeamMemberController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(teamMemberService.getById(id), objectMapper));
    }

    @GetMapping
    public ResponseEntity<String> getAll() throws JsonProcessingException {
        logger.info("Inside getAll method of TeamMemberController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(teamMemberService.getAll(), objectMapper));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id) throws JsonProcessingException {
        logger.info("Inside delete method of TeamMemberController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(teamMemberService.deleteById(id), objectMapper));
    }

    @GetMapping("/paginated")
    public ResponseEntity<String> getAllPaginated(Pageable pageable) throws JsonProcessingException {
        logger.info("Inside getAllPaginated method of TeamMemberController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(teamMemberService.getAll(pageable), objectMapper));
    }
}


