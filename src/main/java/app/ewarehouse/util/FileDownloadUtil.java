
package app.ewarehouse.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tika.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FileDownloadUtil {
	
	@Value("${tempUpload.path}")
	private  String tempUploadPath1;
	@Value("${finalUpload.path}")
	private  String finalUploadPath1;

//	@Value("${logging.file.Url}")
//	String log_file_Url1;

	@Value("${logging.fileUrl}")
	  String log_file_Url1;

	 
		private static String finalUploadPath;
		private static String tempUploadPath;
		private static String log_file_Url;

	    @PostConstruct
	    public void init() {
	    	
	        tempUploadPath=tempUploadPath1;
	       finalUploadPath = finalUploadPath1;
	       log_file_Url = log_file_Url1;
	    }
	private FileDownloadUtil() {}
	public static String filePathTestUrl="";
	private static final String STRING = "/";
	private static final Logger logger = LoggerFactory.getLogger(FileDownloadUtil.class);
	public static ResponseEntity<InputStreamResource> fileDownloadUtil(String filename, String regdFilePath, HttpServletResponse response, HttpServletRequest request)
			throws IOException {

		byte[] data = null;
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		
		Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
		
		// Uploaded Documents
		Path file_directory_path = Paths.get(finalUploadPath);
		// Log Files
		Path file_directory_path2 = Paths.get(log_file_Url);
		filePathTestUrl=file_directory_path.toString();
		Path filePath = Paths.get(file_directory_path + "/" + regdFilePath);
		File file = new File(filePath.toString());
		
		String extension = regdFilePath.substring(regdFilePath.lastIndexOf(".") + 1);
			if (!file.exists()) {
				filePath = Paths.get(file_directory_path2 + "/" + regdFilePath);
				file = new File(filePath.toString());
				if (!file.exists())
					filePath = Paths.get(finalUploadPath,"download.png");
			} else {

				extension = filename.split("\\.")[1];
			}

		
		data = Files.readAllBytes(filePath);
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=Doc_" + currentDateTime + filename;
		response.setHeader(headerKey, headerValue);
	
		 if("pdf".equals(extension)){
			 
			response.setContentType("application/pdf");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
	        InputStreamResource resource = new InputStreamResource(inputStream);
	        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(resource);
			
		 }else if("xls".equals(extension)){
			 
			response.setContentType("application/vnd.ms-excel");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
	        InputStreamResource resource = new InputStreamResource(inputStream);
	        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
			
		 }else if("xlsx".equals(extension)){
			 
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
	        InputStreamResource resource = new InputStreamResource(inputStream);
	        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
			
		 }else if("png".equals(extension)){
			 
			response.setContentType("image/png");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
	        InputStreamResource resource = new InputStreamResource(inputStream);
	        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
			
		 }else if("jpeg".equals(extension)) {
			 
			response.setContentType("image/jpeg");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
	        InputStreamResource resource = new InputStreamResource(inputStream);
	        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
			
		 } else if("jpg".equals(extension)) {
			
			 response.setContentType("image/jpg");
			 ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		        InputStreamResource resource = new InputStreamResource(inputStream);
		        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
		 }
		 else if("log".equals(extension)) {
			
			 response.setContentType("text/plain");
			 ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		        InputStreamResource resource = new InputStreamResource(inputStream);
		        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(resource);
		 }
		 else if("gz".equals(extension)) {
			
			 response.setContentType("application/gzip");
			 ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		        InputStreamResource resource = new InputStreamResource(inputStream);
		        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		 }
		 else {
			 response.setContentType("image/png");
			 ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		        InputStreamResource resource = new InputStreamResource(inputStream);
		        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource); 
		 }
		
	}
	
	public static ResponseEntity<InputStreamResource> viewFileUtil(String filename, String regdFilePath, HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		if(regdFilePath == null || StringUtils.isBlank(regdFilePath)) {
			return null;
		}
		byte[] data = null;
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		
		Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
		
		// Uploaded Documents
		Path file_directory_path = Paths.get(tempUploadPath, "tempfile");
		filePathTestUrl=file_directory_path.toString();
		Path filePath = Paths.get(file_directory_path + "/" + regdFilePath);
		File file = new File(filePath.toString());
		
		String extension = regdFilePath.substring(regdFilePath.lastIndexOf(".") + 1);
			if (!file.exists()) {
				return null;
			} else {
				extension = filename.split("\\.")[1];
			}

		data = Files.readAllBytes(filePath);
	
		 if("pdf".equals(extension)){
			 
			response.setContentType("application/pdf");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
	        InputStreamResource resource = new InputStreamResource(inputStream);
	        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(resource);
			
		 }else if("xls".equals(extension)){
			 
			response.setContentType("application/vnd.ms-excel");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
	        InputStreamResource resource = new InputStreamResource(inputStream);
	        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
			
		 }else if("xlsx".equals(extension)){
			 
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
	        InputStreamResource resource = new InputStreamResource(inputStream);
	        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
			
		 }else if("png".equals(extension)){
			 
			response.setContentType("image/png");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
	        InputStreamResource resource = new InputStreamResource(inputStream);
	        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
			
		 }else if("jpeg".equals(extension)) {
			 
			response.setContentType("image/jpeg");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
	        InputStreamResource resource = new InputStreamResource(inputStream);
	        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
			
		 } else if("jpg".equals(extension)) {
			
			 response.setContentType("image/jpg");
			 ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		        InputStreamResource resource = new InputStreamResource(inputStream);
		        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
		 }
		 else if("log".equals(extension)) {
			
			 response.setContentType("text/plain");
			 ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		        InputStreamResource resource = new InputStreamResource(inputStream);
		        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(resource);
		 }
		 else if("gz".equals(extension)) {
			
			 response.setContentType("application/gzip");
			 ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		        InputStreamResource resource = new InputStreamResource(inputStream);
		        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		 }
		 return null;

	}

}
