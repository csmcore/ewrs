package app.ewarehouse.dto;



import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public class CommonCaptcha {

	private String id;
	private String text;
	private String result;
	private String imageData; 
	
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public CommonCaptcha() {
		
	}

	 public CommonCaptcha(String id, String text, BufferedImage image, String result) {
	        this.id = id;
	        this.text = text;
	        this.result = result;
	        this.imageData = encodeImageToBase64(image);
	    }
	
	  public String getImageData() {
	        return imageData;
	    }

	    public void setImageData(String imageData) {
	        this.imageData = imageData;
	    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	private String encodeImageToBase64(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpeg", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode image to Base64", e);
        }
    }

}
