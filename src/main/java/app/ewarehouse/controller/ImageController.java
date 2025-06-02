package app.ewarehouse.controller;

import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
@CrossOrigin("*")
public class ImageController {
	
	@Value("${finalUpload.path}")
	private String finalUploadPath;

    @PostMapping("/fetch")
    public ResponseEntity<Resource> getImage(@RequestBody ImageRequest imageRequest) {
        try {
            String filePathStr = imageRequest.getFilePath();

            Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
            Path fileDirectoryPath = Paths.get(finalUploadPath);
            Path filePath = Paths.get(fileDirectoryPath.toString(), filePathStr).normalize();

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static class ImageRequest {
        private String filePath;

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }
}
