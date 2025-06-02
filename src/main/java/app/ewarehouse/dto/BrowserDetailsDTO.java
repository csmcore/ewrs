package app.ewarehouse.dto;

import lombok.Data;

@Data
public class BrowserDetailsDTO {
	private String browser;
    private String platform;
    private String language;
    private String screenResolution;
    private String timezone;
}
