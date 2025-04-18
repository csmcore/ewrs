package app.ewarehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_application_of_conformity_form_one_a_docs")
public class ApplicationOfConformityFormOneADocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long intAocFoaDocsId;

    private Long intFormOneaDocId;
    private Integer intWhLocationId;
    private String vchCompanyId;
    private String fileId;
    private String fileName;
    private Integer documentId;
    private String documentName;
    private String format;
    private Integer size;
    private Boolean isMandetory;
    private String typeName;
    private Integer intCreatedBy;

    @Column(name = "dtmCreatedOn")
    private LocalDateTime dtmCreatedOn;

    private Integer intUpdatedBy;

    @Column(name = "dtmUpdatedOn")
    private LocalDateTime dtmUpdatedOn;

    private Boolean bitDeletedFlag = false;
}

