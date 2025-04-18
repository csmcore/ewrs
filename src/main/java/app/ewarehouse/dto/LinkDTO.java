package app.ewarehouse.dto;

import java.util.List;

import lombok.Data;

@Data
public class LinkDTO {

    private Integer intId;
    private String vchLinkName;
    private Integer intLinkType;
    private Integer parentLinkId;
    private Integer viewManageRight;
    private List<LinkDTO> subcategory;

    
}
