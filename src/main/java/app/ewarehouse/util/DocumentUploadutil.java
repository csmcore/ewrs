package app.ewarehouse.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.tika.Tika;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DocumentUploadutil {

	@Value("${tempUpload.path}")
	private String tempUploadPath1;
	@Value("${finalUpload.path}")
	private  String finalUploadPath1;
	
	
	private static String finalUploadPath;
	private static String tempUploadPath;

    @PostConstruct
    public void init() {
    	tempUploadPath=tempUploadPath1;
    	finalUploadPath = finalUploadPath1;
    }
	
	private String uploadFile(String uniqueName, MultipartFile multipartFile, String imagePathFolder) {
		String fileUrl = null;
		String todayDate = DateTimeUtility.getTodayDate();
		String docPath = imagePathFolder + "/" + todayDate;
		//Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
		//Path file_directory_path = Paths.get(root.toString(),"src", "main", "resources", "uploadedDocuments");
		

		
		if (multipartFile!=null) {
				
			File file = new File(finalUploadPath+docPath);
			
			Path path = Paths.get(finalUploadPath +docPath);
			
			if (!Files.exists(path) && !Files.isDirectory(path)) {
				try {
					Files.createDirectories(path);
				} catch (IOException e) {
					log.info("Inside getById method of Document UploadImpl"+ e.getMessage());

				}
			}
			try {
				String name = multipartFile.getOriginalFilename();
				byte[] bytes = multipartFile.getBytes();
				boolean isExists = file.exists();
				if (!file.setExecutable(true, false)) {
		            log.warn("Failed to set executable permission on file: {}", file.getAbsolutePath());
		        }
		        if (!file.setReadable(true, false)) {
		            log.warn("Failed to set readable permission on file: {}", file.getAbsolutePath());
		        }
		        if (!file.setWritable(true, false)) {
		            log.warn("Failed to set writable permission on file: {}", file.getAbsolutePath());
		        }
				String extension = name.substring(name.lastIndexOf(".") + 1);
				String fileName = uniqueName + "." + extension;
				try(FileOutputStream fos = new FileOutputStream(file + "/" + fileName)){
				fos.write(bytes);
				}
				fileUrl = file.getPath() + "/" + fileName;
			} catch (Exception e) {
				log.error("DocumentUploadutil:uploadFile()",e);
			}

					
	}

	

		return fileUrl;
	}
	

	public static String uploadFileByte(String uniquefileName, byte[] multipartFileByteArr, String imagePathFolder) {
		String fileUrl = null;
		String todayDate = DateTimeUtility.getTodayDate();
		String docPath = imagePathFolder + "/" + todayDate;
		System.out.println(finalUploadPath);
		Path file_directory_path = Paths.get(finalUploadPath);
		File file = new File(file_directory_path + "/" + docPath);
		
		try {
			
			
			boolean isExists = file.exists();
			if (!file.setExecutable(true, false)) {
	            log.warn("Failed to set executable permission on file: {}", file.getAbsolutePath());
	        }
	        if (!file.setReadable(true, false)) {
	            log.warn("Failed to set readable permission on file: {}", file.getAbsolutePath());
	        }
	        if (!file.setWritable(true, false)) {
	            log.warn("Failed to set writable permission on file: {}", file.getAbsolutePath());
	        }
			if (!isExists) {
				file.mkdirs();
			}
			
			    String MimeType = new Tika().detect(multipartFileByteArr);
			    String[]extension_arr=MimeType.split("/");
			    long filelength = multipartFileByteArr.length;
			    if("application/pdf".equals(MimeType)) {
			    	
			    	if(filelength>0) {
			    		String extension = extension_arr[1];
						String fileName = uniquefileName + "." + extension;
						try(FileOutputStream fos = new FileOutputStream(file + "/" + fileName)){
						fos.write(multipartFileByteArr);
						}
						fileUrl = docPath + "/" + fileName;
			    	}
			    	else {
			    		return "Document is not correct";
			    	}
			    }
			    else if("image/jpeg".equals(MimeType)||"image/jpg".equals(MimeType)||"image/png".equals(MimeType)) {
			    	
			    	if(filelength>0) {
				    	String extension = extension_arr[1];
				    	String fileName = uniquefileName + "." + extension;
						try(FileOutputStream fos = new FileOutputStream(file + "/" + fileName)){
						InputStream is = new ByteArrayInputStream(multipartFileByteArr);
						       BufferedImage image = ImageIO.read(is);
						       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						ImageIO.write(Scalr.resize(image, Method.ULTRA_QUALITY, 1000, Scalr.OP_ANTIALIAS), extension.toUpperCase(), byteArrayOutputStream);
	
						fos.write(byteArrayOutputStream.toByteArray());
						}
						fileUrl = docPath + "/" + fileName;
			    	}
			    	else {
			    		return "File is not available";
			    	}
			    }
			    else {
			    	fileUrl= "File is not available";
			    }
			    
		} catch (Exception e) {
			log.info("Error: {}", e.getMessage());
		}

		return fileUrl;
	}
	
	
	
}
