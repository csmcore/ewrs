package app.ewarehouse.controller;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.AdminConfiguration;
import app.ewarehouse.master.service.AdminConfigService;
import app.ewarehouse.util.CommonUtil;


@RequestMapping("/admin")
@RestController
public class AdminController {
	
	
	private AdminConfigService adminConfigService;
	
    private ObjectMapper objectMapper;
    
    
	
	public AdminController(AdminConfigService adminConfigService, ObjectMapper objectMapper) {
		super();
		this.adminConfigService = adminConfigService;
		this.objectMapper = objectMapper;
	}

	@GetMapping("/test")
	public ResponseEntity<Map<String, Object>> sayHello(){
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Hii Admin");
		return ResponseEntity.ok(response);
	}
	
//	@PostMapping(value = "qrcode", produces = MediaType.IMAGE_PNG_VALUE)
//	public ResponseEntity<BufferedImage> barbecueEAN13Barcode(@RequestBody String text) throws Exception {
//
//		QRCodeWriter barcodeWriter = new QRCodeWriter();
//		BitMatrix bitMatrix = barcodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
//
//		return new ResponseEntity<>(MatrixToImageWriter.toBufferedImage(bitMatrix), HttpStatus.OK);
//	}

	@PostMapping("downloadReceipt")
	public ResponseEntity<Resource> generateQRCodePDF(@RequestBody String text) throws IOException, WriterException, DocumentException {
		// Generate QR code image
		byte[] qrCodeImageBytes = generateQRCodeImage(text, 300, 300);

		// Create PDF document with QR code
		ByteArrayOutputStream baos = createPDFDocumentWithQRCode(text, qrCodeImageBytes);

		// Prepare response entity
		ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=qrcode.pdf")
				.contentType(MediaType.APPLICATION_PDF).contentLength(baos.size()).body(resource);
	}

	@GetMapping("qrCodeImage")
	public ResponseEntity<Resource> generateQRCode(@RequestParam String text, @RequestParam int width, @RequestParam int height) throws IOException, WriterException {
		// Generate QR code image
		
		System.out.println(text);
		
		byte[] qrCodeImageBytes = generateQRCodeImage(text, width, height);

		// Prepare response entity
		ByteArrayResource resource = new ByteArrayResource(qrCodeImageBytes);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=qrcode.png")
				.contentType(MediaType.IMAGE_PNG)
				.contentLength(qrCodeImageBytes.length)
				.body(resource);
	}

	private byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		hints.put(EncodeHintType.MARGIN, 1); // Margin size

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, com.google.zxing.BarcodeFormat.QR_CODE, width, height, hints);

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bufferedImage.createGraphics();

		Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (bitMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}

		
		// bufferedImage=addWatermark(bufferedImage,"WRSC");
		
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", baos);
		baos.flush();
		byte[] imageBytes = baos.toByteArray();
		baos.close();

		return imageBytes;

	}

	private ByteArrayOutputStream createPDFDocumentWithQRCode(String text, byte[] qrCodeImageBytes)
			throws IOException, DocumentException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// Create PDF document using libraries like iText, Apache PDFBox, etc.
		// For simplicity, assuming you are using iText here
		Document document = new Document();
		PdfWriter.getInstance(document, baos);
		document.open();

		// Add content to the PDF document (you can add more content as needed)
		Paragraph paragraph = new Paragraph("QR Code Generated:");
		document.add(paragraph);

		// Embed QR code image in the PDF
		Image qrCodeImage = Image.getInstance(qrCodeImageBytes);
		document.add(qrCodeImage);

		document.close();

		return baos;
	}
	

    private BufferedImage addWatermark(BufferedImage qrImage, String watermarkText) {
        Graphics2D graphics = qrImage.createGraphics();

        // Set watermark properties
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        graphics.setComposite(alphaChannel);
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Arial", Font.BOLD, 30));

        // Calculate position for watermark
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int x = (qrImage.getWidth() - fontMetrics.stringWidth(watermarkText)) / 2;
        int y = qrImage.getHeight() - fontMetrics.getHeight();

        // Draw the watermark text
        graphics.drawString(watermarkText, x, y);
        graphics.dispose();

        return qrImage;
    }
    
    @PostMapping("/saveConfigurationDetails")
    public ResponseEntity<String> saveConfigurationDetails(@RequestBody String adminConfiguration)
    		throws JsonProcessingException {
    	
            String response = adminConfigService.saveConfigurationDetails(adminConfiguration);
           return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
    
    
    @GetMapping("/getAllConfigurationDetails")
    public ResponseEntity<?> getAllConfigurationDetails() throws JsonProcessingException {
            List<AdminConfiguration> configDetails = adminConfigService.getAllConfigurationDetails();
           return ResponseEntity.ok(CommonUtil.encodedJsonResponse(configDetails,objectMapper));
            
    }
    
    @GetMapping("/paginated")
	public ResponseEntity<?> getAllConfigurationPageable(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
			Pageable pageable = PageRequest.of(page, size);
			Page<AdminConfiguration> pageResult = adminConfigService.getAllConfigurationList(pageable);
			return ResponseEntity.ok(CommonUtil.encodedJsonResponse(pageResult,objectMapper));
	}
    
    @PostMapping("/updateConfigurationDetails/{configId}")
    public ResponseEntity<?> updateConfigurationDetails(@PathVariable Integer configId, @RequestBody String updatedConfigData) throws JsonProcessingException {
    	Integer isUpdated=0;
    	if(configId>1)
    		isUpdated=adminConfigService.updateConfigurationDetails(1,updatedConfigData);
    	else
    	    isUpdated = adminConfigService.updateConfigurationDetails(configId,updatedConfigData);
		return ResponseEntity.ok(CommonUtil.encodedJsonResponse(isUpdated,objectMapper));
    }
    
    
    @GetMapping("/getConfigurationById/{id}")
    public ResponseEntity<?> getConfigurationById(@PathVariable Integer id) throws JsonProcessingException {
    	AdminConfiguration adminConfig =adminConfigService.findConfigurationById(id);
		if (adminConfig != null) {
			return ResponseEntity.ok(CommonUtil.encodedJsonResponse(adminConfig,objectMapper));
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    	
    }
    
    @GetMapping("/getNoOfAttemptValue")
    public ResponseEntity<?> getNoOfAttemptValue() throws JsonProcessingException{
    		Map<String,Object> response1=adminConfigService.getNoOfAttemptAndIdleTimeValue();
    		Map<String,Object> response=new HashMap<>();
    		response.put("attemptNo", response1.get("noOfAttempt"));
    		response.put("idleTime", response1.get("idleTime"));
    	return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    }
    
    
    private <T> String buildJsonResponse(T response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ResponseDTO.<T>builder()
                        .status(200)
                        .result(response)
                        .build()
        );
    }
    
    
    @GetMapping("/getUserName/{id}")
    public ResponseEntity<?> getUserName(@PathVariable Integer id) throws JsonProcessingException {
    		String userName =adminConfigService.findUserNameById(id);
			return ResponseEntity.ok(CommonUtil.encodedJsonResponse(userName,objectMapper));
    }
	
	
}
