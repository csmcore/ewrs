package app.ewarehouse.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.CommonCaptcha;
import app.ewarehouse.service.CommonCaptchaGenerateService;
import app.ewarehouse.util.CommonUtil;

@RestController
@CrossOrigin("*")
public class CommonCaptchaGenerateController {
	
	@Autowired
	private CommonCaptchaGenerateService captchaService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(CommonCaptchaGenerateController.class);

	@GetMapping("/generateCaptcha")
    public ResponseEntity<?> generateCaptcha() {
		logger.info("Execute generateCaptcha() Method ..!!");
		try {
			CommonCaptcha captcha = captchaService.generateCaptcha();
			if(captcha != null) {
				return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(captcha)).toString());
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			return new ResponseEntity<>("An error occurred while generating captcha : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

}