package app.ewarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.ewarehouse.service.CommonUserWiseService;
import app.ewarehouse.util.CommonUtil;

@RestController
@RequestMapping("/api/common-user")
public class CommonUserWiseController {
	@Autowired
    private CommonUserWiseService commonUserWiseService;

    @PostMapping("/process")
    public ResponseEntity<String> processActivity(@RequestBody String data) {
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder
        		(commonUserWiseService.processActivityByUser(data)).toString());
    }

}
