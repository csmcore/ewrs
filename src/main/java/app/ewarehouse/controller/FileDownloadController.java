package app.ewarehouse.controller;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.apache.tika.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.FileDownloadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin("*")
@RequestMapping("/file")
@Slf4j
public class FileDownloadController {

	@Autowired
	ObjectMapper objectMapper;
	 @Autowired
	 private ApplicationContext applicationContext;
	 

	// @Value("${logging.file.Url}")

	 @Value("${logging.fileUrl}")
	 String log_file_Url;
	
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(HttpServletResponse response, HttpServletRequest request, @RequestParam("filePath") String filePath) throws IOException {
        log.info("Inside download method of FileDownloadController");
        String[] parts = filePath.split("/");
        String fileName = parts[parts.length - 1];
        return FileDownloadUtil.fileDownloadUtil(fileName, filePath, response, request);
    }
    
    @GetMapping("/getNames")
	public ResponseEntity<?> getFileNames() throws JsonProcessingException {
    	Path dirPath=null;
    
    		 dirPath = Paths.get(log_file_Url);
    	
		List<String> fileNames = null;
		try {
			fileNames = getFileNames(dirPath);
		} catch (IOException e) {
		}
		return ResponseEntity.ok(CommonUtil.encodedJsonResponse(fileNames, objectMapper));
	}
    
    public List<String> getFileNames(Path dirPath) throws IOException {
        try (Stream<Path> paths = Files.walk(dirPath)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(f->f.endsWith(".gz"))
                    .sorted((file1, file2) -> file2.compareTo(file1))
                    .toList();
        }
    }
    
    @GetMapping("/showPath")
    
    String showFilePath() {
    	
    	Path root=null;
    	Path dirPath=null;
    	try {
    		 
    		 
    		 
    		 dirPath = Paths.get(log_file_Url);
    	}
    	catch(Exception e) {
    		
    		return  e.toString();
    	}
    	
    	 // Access the project directory using ApplicationContext
        String appDirectory = applicationContext.getApplicationName();
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
    	//return root.toString()+"-----"+ dirPath.toString();
    	
    	return log_file_Url;
    			
    }
    
    
    @GetMapping("/toCheck")
    
    String toCheck() {
    	
    	return "Version-10004";  
    			
    }
    
    @GetMapping("/viewFile")
    public ResponseEntity<InputStreamResource> viewFile(HttpServletResponse response, HttpServletRequest request, @RequestParam("filePath") String filePath) throws IOException {
        log.info("Inside download method of FileDownloadController");
        String[] parts = filePath.split("/");
        String fileName = parts[parts.length - 1];
        return FileDownloadUtil.viewFileUtil(fileName, filePath, response, request);
    }
    
}
