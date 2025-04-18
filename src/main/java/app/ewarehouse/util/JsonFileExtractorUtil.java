package app.ewarehouse.util;

import app.ewarehouse.exception.CustomGeneralException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class JsonFileExtractorUtil {
    private static final Pattern BASE64_PATTERN = Pattern.compile("^(?:[A-Za-z0-9+/]{4})*" +
            "(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");

    private static final List<String> FILE_EXTENSIONS = List.of("pdf", "png", "jpg", "jpeg", "txt", "csv", "docx", "xlsx", "json", "xml");

    public static JsonNode processFileData(String decodedData, List<String> attributeNames) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;

        try {
            // Parse the JSON data to extract the image strings
            rootNode = mapper.readTree(decodedData);

            // Handle each attribute dynamically
            for (String attributeName : attributeNames) {
                if (rootNode.has(attributeName)) {
                    String value = rootNode.get(attributeName).asText();
                    String filePath = uploadFile(value);
                    ((ObjectNode) rootNode).put(attributeName, filePath);
                }
            }

        } catch (Exception e) {
            throw new CustomGeneralException("Invalid data format: " + e);
        }

        return rootNode;
    }

    private static boolean isBase64Encoded(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return BASE64_PATTERN.matcher(value).matches();
    }

    public static final Predicate<String> isFilePath = input -> {
        int lastIndex = input.lastIndexOf('.');
        if (lastIndex == -1 || lastIndex == input.length() - 1) {
            return false; // No extension found or last character is '.'
        }

        String extension = input.substring(lastIndex + 1).toLowerCase();
        return FILE_EXTENSIONS.stream().anyMatch(ext -> ext.equals(extension));
    };

    public static String uploadFile(String msg) {
        if (!isFilePath.test(msg)) {
            byte[] decode = Base64.getDecoder().decode(msg.getBytes());
            int currentYear = LocalDate.now().getYear();
            String uniqueFileName = currentYear + "_" + UUID.randomUUID().toString();

            String fullPath = DocumentUploadutil.uploadFileByte(uniqueFileName, decode, FolderAndDirectoryConstant.INSP_SUSPENSION_FOLDER);
            return fullPath.substring(fullPath.indexOf(FolderAndDirectoryConstant.INSP_SUSPENSION_FOLDER));
        }

        return msg;
    }

    public static String uploadFile(String msg, String fileFolder) {
        if (!isFilePath.test(msg)) {
            byte[] decode = Base64.getDecoder().decode(msg.getBytes());

            int currentYear = LocalDate.now().getYear();
            String uniqueFileName = currentYear + "_" + UUID.randomUUID().toString();

            String fullPath = DocumentUploadutil.uploadFileByte(uniqueFileName, decode, fileFolder);

            if (fullPath.contains("Document can't be upload")){
                throw new CustomGeneralException("Invalid file type. Only PDF, JPG, and JPEG are allowed.");
            }
            return fullPath.substring(fullPath.indexOf(fileFolder));
        }

        return msg;
    }

    public static String uploadFile(String msg, String fileName, String fileFolder) {
        if (!isFilePath.test(msg)) {
            byte[] decode = Base64.getDecoder().decode(msg.getBytes());

            String fullPath = DocumentUploadutil.uploadFileByte(fileName, decode, fileFolder);

            if (fullPath.contains("Document can't be upload")){
                throw new CustomGeneralException("Invalid file type. Only PDF, JPG, and JPEG are allowed.");
            }

            return fullPath.substring(fullPath.indexOf(fileFolder));
        }

        return msg;
    }

    public static <T> T processRequestData(String decodedData, List<String> attributeNames, Class<T> clazz) {
        JsonNode updatedNode = processFileData(decodedData, attributeNames);

        ObjectMapper mapper = new ObjectMapper();
        T t;

        try {
            // Deserialize the updated JSON data into the given object type
            t = mapper.treeToValue(updatedNode, clazz);
        } catch (Exception e) {
            throw new CustomGeneralException("Invalid data format: " + e);
        }

        return t;
    }
}
