package app.ewarehouse.controller;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;

import app.ewarehouse.service.DraftLetterService;
import app.ewarehouse.service.LetterConfigurationService;
import app.ewarehouse.service.TOnlineServiceApprovalService;
import app.ewarehouse.util.CommonUtil;
import jakarta.servlet.http.HttpServletResponse;
@RestController
@CrossOrigin("*")
public class DraftLetterController {

	@Value("${tempUpload.path}")
	private String fileDirectory;
	@Value("${queryFileUpload.path}")
	private String documentPathForTarget;
	@Autowired
	private DraftLetterService draftLetterService;

	@Autowired
	private LetterConfigurationService letterConfigurationService;

	@Autowired
	private TOnlineServiceApprovalService tOnlineServiceApprovalService;
	String data = "";
	JSONObject response = new JSONObject();

	private static final Logger logger = LoggerFactory.getLogger(DraftLetterController.class);

	@PostMapping("/saveLetterData")
	public ResponseEntity<String> viewDraftLetter(@RequestBody String requestParam) {

		logger.info("Inside viewDraftLetter method of LetterConfigurationController");

		JSONObject requestObj = new JSONObject(requestParam);
		if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"), requestObj.getString("REQUEST_TOKEN"))) {

			data = CommonUtil.inputStreamDecoder(requestParam);
			response = draftLetterService.saveDraftLetter(data);

		} else {
			logger.error("Inside  viewDraftLetter method of DraftLetterController  Token mismatch");

			response.put("status", 417);
			response.put("msg", "error");
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());

	}

	@PostMapping("/getLetterData")
	public ResponseEntity<String> getIdByName(@RequestBody String requestParam) {

		logger.info("Inside getIdByName method of DraftLetterController");

		JSONObject requestObj = new JSONObject(requestParam);

		if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"), requestObj.getString("REQUEST_TOKEN"))) {
			data = CommonUtil.inputStreamDecoder(requestParam);
			response = letterConfigurationService.getletterData(data);
		} else {
			logger.error("Inside  getIdByName method of DraftLetterController  Token mismatch");

			response.put("status", 417);
			response.put("msg", "error");
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());

	}

	@PostMapping("/getDraftLetterData")
	public ResponseEntity<String> getDraftLetterData(@RequestBody String requestParam) {

		logger.info("Inside getDraftLetterData method of DraftLetterController");

		JSONObject jsonob = new JSONObject(requestParam);
		if (CommonUtil.hashRequestMatch(jsonob.getString("REQUEST_DATA"), jsonob.getString("REQUEST_TOKEN"))) {
			data = CommonUtil.inputStreamDecoder(requestParam);
			JSONObject jsonobj = new JSONObject(data);
			response = draftLetterService.getLetterById(Integer.parseInt(jsonobj.getString("intConfigId")));

		} else {
			logger.error("Inside  getDraftLetterData method of DraftLetterController  Token mismatch");

			response.put("status", 417);
			response.put("msg", "error");
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@PostMapping("/generateStatus")
	public ResponseEntity<String> generateStatus(@RequestBody String requestParam) {

		logger.info("Inside getDraftLetterData method of DraftLetterController");

		JSONObject jsonob = new JSONObject(requestParam);
		if (CommonUtil.hashRequestMatch(jsonob.getString("REQUEST_DATA"), jsonob.getString("REQUEST_TOKEN"))) {
			data = CommonUtil.inputStreamDecoder(requestParam);
			JSONObject jsonobj = new JSONObject(data);
			response = draftLetterService
					.generateStatusByintConfigId(Integer.parseInt(jsonobj.getString("intConfigId")),Integer.parseInt(jsonobj.getString("formId")),Integer.parseInt(jsonobj.getString("roleId")));

		} else {
			logger.error("Inside  getDraftLetterData method of DraftLetterController  Token mismatch");

			response.put("status", 417);
			response.put("msg", "error");
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@PostMapping("/downloadLetterData")
	public ResponseEntity<byte[]> dowloadLetterData(@RequestBody String data, HttpServletResponse resp) {

		logger.info("Inside dowloadLetterData method of DraftLetterController");

		try {
			JSONObject json = new JSONObject(data);

			Integer intServiceId = Integer.parseInt(json.getString("intServiceId"));
			Integer intLetterConfigId = json.getInt("intLetterConfigId");

			String letterContent = draftLetterService.getLetterContentByIntConfigId(intLetterConfigId, intServiceId);
			System.out.println(letterContent);
			long currentStamp = System.currentTimeMillis();
			String file1 = "letter_" + currentStamp + ".pdf";
			System.out.println(letterContent);
			File f = null;
			f = new File(this.fileDirectory);
			if (!f.exists()) {
				f.mkdirs();
			}
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(f + "/" + "test" + ".html"))){
			bw.write(letterContent);
			}
			ConverterProperties properties = new ConverterProperties();
			MediaDeviceDescription description = MediaDeviceDescription.createDefault();
			HtmlConverter.convertToPdf(new File(this.fileDirectory + "test" + ".html"),
					new File(this.fileDirectory + file1 + ".pdf"), properties);
			Path delFilePath = Paths.get(this.fileDirectory, "test.html");
			Files.deleteIfExists(delFilePath);

			File file = new File(this.fileDirectory + File.separator + file1 + ".pdf");
			try(FileInputStream inputStream = new FileInputStream(file)){
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
			
			byte[] bytes = outputStream.toByteArray();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "letter.pdf");
			headers.setContentLength(bytes.length);
			return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
			}
			} catch (Exception e) {
			logger.error("Inside dowloadLetterData method of DraftLetterController error occur :", e);
		}
		return null;
	}

	@PostMapping("/downloadLetterDataByConfigId")
	public ResponseEntity<byte[]> downloadLetterDataByConfigId(@RequestBody String data, HttpServletResponse resp) {

		logger.info("Inside downloadLetterDataByConfigId method of DraftLetterController");

		try {
			JSONObject json = new JSONObject(data);
			String intConfigId = json.getString("intConfigId");
			String letterContent = draftLetterService.getIntLetterConfigIdByIntConfigId(Integer.parseInt(intConfigId));
			long currentStamp = System.currentTimeMillis();
			String file1 = "letter_" + currentStamp + ".pdf";
			System.out.println(letterContent);
			File f = null;
			f = new File(this.fileDirectory);
			if (!f.exists()) {
				f.mkdirs();
			}
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(f + "/" + "test" + ".html"))){
			bw.write(letterContent);
			}

			ConverterProperties properties = new ConverterProperties();
			MediaDeviceDescription description = MediaDeviceDescription.createDefault();
			HtmlConverter.convertToPdf(new File(this.fileDirectory + "test" + ".html"),
					new File(this.fileDirectory + file1 + ".pdf"), properties);
			Path delFilePath = Paths.get(this.fileDirectory, "test.html");
			Files.deleteIfExists(delFilePath);

			File file = new File(this.fileDirectory + File.separator + file1 + ".pdf");
			try(FileInputStream inputStream = new FileInputStream(file)){
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
			byte[] bytes = outputStream.toByteArray();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "letter.pdf");
			headers.setContentLength(bytes.length);
			return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
			}
			} catch (Exception e) {
			logger.error(
					"Inside downloadLetterDataByConfigId method of DraftLetterController error occur :",e);

		}
		return null;
	}

	@PostMapping("/uploadDocument")
	public ResponseEntity<?> uploadDraftDocument(@RequestBody String requestParam) {
		logger.info("Inside uploadDraftDocument method of DraftLetterController");
		
		JSONObject requestObj = new JSONObject(requestParam);
		if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"), requestObj.getString("REQUEST_TOKEN"))) {{
			data = CommonUtil.inputStreamDecoder(requestParam);
			JSONObject dataObject = new JSONObject(data);
			System.out.println("comming");
			JSONArray fileArray = dataObject.getJSONArray("file");
			for (int i = 0; i < fileArray.length(); i++) {
				String fileName = fileArray.getJSONObject(i).getString("fileName");
				File src = new File(fileDirectory + fileName);
				if (src.exists()) {
					File dest = new File(documentPathForTarget + fileName.trim());
					try {
						Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
						Files.delete(src.toPath());

					} catch (IOException e) {
						logger.error(
								"Inside uploadDraftDocument method of DraftLetterController error occur :",e);

						System.out.println("Inside Error");
						response.put("msg", "File Not Found");
						response.put("status", "400");
					}

				} else {
					response.put("msg", "error");
					response.put("status", "400");

				}

			}
			String viewpage=draftLetterService.saveDocumentByIds(fileArray.toString(), dataObject.getInt("formId"),
					dataObject.getInt("serviceId"),dataObject.getInt("roleId"));
			response.put("msg", "success");
			response.put("status", "200");
			response.put("viewpage", viewpage);
			
		}		
		

	}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}
	
	
	
	@PostMapping("/checkLength")
	public ResponseEntity<?> checkLength(@RequestBody String reqestParam){
		logger.info("Inside checkLength method of DraftLetterController");
		JSONObject jsonob = new JSONObject(reqestParam);
		if (CommonUtil.hashRequestMatch(jsonob.getString("REQUEST_DATA"), jsonob.getString("REQUEST_TOKEN"))) {
			data = CommonUtil.inputStreamDecoder(reqestParam);
			JSONObject jsb=new JSONObject(data);
			response=draftLetterService.generateStatuByRoleId(jsb.getInt("serviceId"));
		}
		
		return ResponseEntity.ok(response.toString());
	}
	
	@PostMapping("/saveInApprovalForWorkFolw")
	public ResponseEntity<?> saveInApprovalForWorkFolw(@RequestBody String reqestParam){
		logger.info("Inside saveInApprovalForWorkFolw method of DraftLetterController");
		JSONObject jsonob = new JSONObject(reqestParam);
		if (CommonUtil.hashRequestMatch(jsonob.getString("REQUEST_DATA"), jsonob.getString("REQUEST_TOKEN"))) {
			data = CommonUtil.inputStreamDecoder(reqestParam);
			
			response=draftLetterService.saveInApprovalForWorkFolw(data);
		}
		
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}
	
	
	
	
}
