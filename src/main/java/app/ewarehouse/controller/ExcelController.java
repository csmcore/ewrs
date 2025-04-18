package app.ewarehouse.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import app.ewarehouse.util.CommonUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("api/excel")
public class ExcelController {
	

	
	@Autowired
	ObjectMapper objectMapper;
	
	   @GetMapping("/download-template")
	   public ResponseEntity<String> generateExcelTemplate() throws IOException {
		    try (Workbook workbook = new XSSFWorkbook()) {

		        Sheet sheet = workbook.createSheet("Template");

		        // Create font style for bold
		        Font boldFont = workbook.createFont();
		        boldFont.setBold(true);

		        // Create cell style with bold font
		        CellStyle boldStyle = workbook.createCellStyle();
		        boldStyle.setFont(boldFont);
		        boldStyle.setAlignment(HorizontalAlignment.CENTER);

		        // Create heading row
		        Row headingRow = sheet.createRow(0);
		        Cell headingCell = headingRow.createCell(0);
		        headingCell.setCellValue("User Lists");
		        headingCell.setCellStyle(boldStyle);
		        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 19));

		        // Create header row
		        Row headerRow = sheet.createRow(1);
		        String[] headers = {
		            "Full Name", "Gender", "Mobile No", "Email Id", "Alternate Mobile Number", "Date Of Joining",
		            "Address", "Department", "Role", "Designation","Employee Type","Group", "County", "Sub-County", "Ward", "User Id", "Password",
		             "Committee Members", "Privilege","Reporting Authorities"
		        };

		        for (int i = 0; i < headers.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(headers[i]);
		            cell.setCellStyle(boldStyle);
		        }

		        // Add a sample data row
		        Row dataRow = sheet.createRow(2);
		        String[] sampleData = {
		            "John Doe", "Male", "987654321", "johndoe@gmail.com", "876543210", "04/03/2025",
		            "123 Street, City", "warehouse", "Warehouse Operator", "Warehouse", "Employee Type", "Group One",
		            "Nairobi1", "Westlands", "Kitisuru", "John Doe","Admin@123","IC Member,CLC,WRSC", "Yes", "warehouse"
		        };

		        for (int i = 0; i < sampleData.length; i++) {
		            Cell cell = dataRow.createCell(i);
		            cell.setCellValue(sampleData[i]);
		        }

		        // Auto-size columns for better readability
		        for (int i = 0; i < headers.length; i++) {
		            sheet.autoSizeColumn(i);
		        }

		        // Write workbook to ByteArrayOutputStream
		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		        workbook.write(outputStream);

		        // Convert to Base64 String
		        String base64Excel = Base64.getEncoder().encodeToString(outputStream.toByteArray());

		        // Build JSON Response
		        Map<String, String> response = new HashMap<>();
		        response.put("fileName", "UserList.xlsx");
		        response.put("fileData", base64Excel);

		        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(response)).toString());
		    }
		}

}
