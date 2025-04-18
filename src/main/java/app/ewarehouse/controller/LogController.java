package app.ewarehouse.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {
	@GetMapping(value = "/viewLog")
	public ResponseEntity<FileSystemResource> viewLogFile() throws IOException {
			Path filePath = Paths.get("E:\\eWRS\\eWRS_backend\\ewrs_dev_backend\\src\\storage\\logs", "logFile.log");
			FileSystemResource fileResource = new FileSystemResource(filePath.toFile());
			String contentType = Files.probeContentType(filePath);
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=logFile.log")
	                .header(HttpHeaders.CONTENT_TYPE, contentType)
	                .body(fileResource);
	}
}
