package app.ewarehouse.dto;

import java.util.List;

import lombok.Data;

@Data
public class AocCompProfDetailsMainDTO {
	private Integer userId;
	private AocCompProfileDetDTO companyProfile;
    private List<AocCompProfDirectorDetDTO> directors;
    private AocCompProfSignSealDTO signSeal;
}
