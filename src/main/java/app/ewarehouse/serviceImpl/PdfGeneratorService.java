package app.ewarehouse.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfGeneratorService {

    public ResponseEntity<String> generatePdf(Map<String, Object> requestData) throws Exception {
        // Get the user's Downloads folder path
        String downloadsDir = System.getProperty("user.home") + File.separator + "Downloads";
        String filePath = downloadsDir + File.separator + "Inspection_Report.pdf";

        // Create a new document
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Add Title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Inspection & Location Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Process Inspection Data
        if (requestData.containsKey("inspection")) {
            document.add(new Paragraph("Inspection Details", titleFont));
            document.add(createTable((Map<String, Object>) requestData.get("inspection")));
        }

        // Process Location Data
        if (requestData.containsKey("location")) {
            document.add(new Paragraph("Location Details", titleFont));
            document.add(createTable((Map<String, Object>) requestData.get("location")));
        }

        document.close();

        return ResponseEntity.ok("PDF saved at: " + filePath);
    }

    private PdfPTable createTable(Map<String, Object> data) throws DocumentException {
        PdfPTable table = new PdfPTable(3); // 3 columns: S.No, Field Name, Value
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 5, 7}); // Column widths

        // Table Header
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        addCell(table, "S.No", headerFont, Element.ALIGN_CENTER);
        addCell(table, "Field Name", headerFont, Element.ALIGN_CENTER);
        addCell(table, "Value", headerFont, Element.ALIGN_CENTER);

        // Table Data
        Font dataFont = new Font(Font.FontFamily.HELVETICA, 11);
        int serialNo = 1;

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.equals("agriculturalCommodities") && value instanceof List) {
                List<Map<String, Object>> commodities = (List<Map<String, Object>>) value;
                int commoditySize = commodities.size();

                // Merge Serial Number and Field Name for all commodities
                PdfPCell serialCell = new PdfPCell(new Phrase(String.valueOf(serialNo), dataFont));
                serialCell.setRowspan(commoditySize);
                serialCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                serialCell.setPadding(5);

                PdfPCell fieldNameCell = new PdfPCell(new Phrase(key, dataFont));
                fieldNameCell.setRowspan(commoditySize);
                fieldNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                fieldNameCell.setPadding(5);

                table.addCell(serialCell);
                table.addCell(fieldNameCell);

                // Add the first commodity
                table.addCell(new PdfPCell(new Phrase(String.valueOf(commodities.get(0).get("name")), dataFont)));

                // Add remaining commodities in new rows (without S.No and Field Name)
                for (int i = 1; i < commoditySize; i++) {
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(commodities.get(i).get("name")), dataFont)));
                }

                serialNo++; // Increment serial number

            } else {
                addCell(table, String.valueOf(serialNo++), dataFont, Element.ALIGN_CENTER);
                addCell(table, key, dataFont, Element.ALIGN_LEFT);
                addCell(table, String.valueOf(value), dataFont, Element.ALIGN_LEFT);
            }
        }

        return table;
    }

    private void addCell(PdfPTable table, String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        table.addCell(cell);
    }
}




//import java.io.FileOutputStream;
//import java.nio.file.Paths;
//import java.util.Iterator;
//
//import org.json.JSONObject;
//import org.springframework.stereotype.Service;
//
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//
//@Service
//public class PdfGeneratorService {
//
//    public String generateDynamicPDF(JSONObject jsonData) {
//        try {
//            // Get the Downloads folder path
//            String userHome = System.getProperty("user.home");
//            String downloadPath = Paths.get(userHome, "Downloads", "warehouse_report.pdf").toString();
//
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream(downloadPath));
//            document.open();
//
//            // Title
//            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
//            Paragraph title = new Paragraph("Warehouse Report", titleFont);
//            title.setAlignment(Element.ALIGN_CENTER);
//            document.add(title);
//            document.add(new Paragraph("\n")); // Line break
//
//            // Loop through JSON and generate a table
//            for (String key : jsonData.keySet()) {
//                JSONObject section = jsonData.getJSONObject(key);
//                Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
//                document.add(new Paragraph(key.toUpperCase(), sectionFont));
//                document.add(new Paragraph("\n"));
//
//                // Dynamic Table
//                PdfPTable table = new PdfPTable(2);
//                table.setWidthPercentage(100);
//                table.setSpacingBefore(10f);
//                table.setSpacingAfter(10f);
//
//                // Add Table Headers
//                Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
//                PdfPCell header1 = new PdfPCell(new Phrase("Field", headerFont));
//                PdfPCell header2 = new PdfPCell(new Phrase("Value", headerFont));
//                header1.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                header2.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                table.addCell(header1);
//                table.addCell(header2);
//
//                // Add Table Data Dynamically
//                for (Iterator<String> it = section.keys(); it.hasNext(); ) {
//                    String fieldKey = it.next();
//                    Object value = section.get(fieldKey);
//
//                    table.addCell(new Phrase(fieldKey));
//                    table.addCell(new Phrase(value.toString()));
//                }
//
//                document.add(table);
//            }
//
//            document.close();
//            return "PDF saved successfully at: " + downloadPath;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error generating PDF";
//        }
//    }
//}




