package app.ewarehouse.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.ewarehouse.serviceImpl.PdfGeneratorService;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfGeneratorService pdfGeneratorService;

    public PdfController(PdfGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }
    
    @PostMapping("/generate")
    public ResponseEntity<String> generatePdf(@RequestBody Map<String, Object> requestData) throws Exception {
        return pdfGeneratorService.generatePdf(requestData);
    }

//    @PostMapping("/generate")
//    public ResponseEntity<String> generatePdf(@RequestBody String jsonData) {
//        JSONObject jsonObject = new JSONObject(jsonData);
//        String message = pdfGeneratorService.generateDynamicPDF(jsonObject);
//        return ResponseEntity.ok(message);
//    }
}
