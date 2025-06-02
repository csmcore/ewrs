package app.ewarehouse.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_pledging_discharge_upload_docs")
public class PledgingDischargeUploadDocs {

	@Id
	@GeneratedValue(generator = "pledging_discharge_udoc_id_gen")
	@GenericGenerator(name = "pledging_discharge_udoc_id_gen", type = app.ewarehouse.util.CustomIdGenerator.class, parameters = {
			@org.hibernate.annotations.Parameter(name = "tableName", value = "t_pledging_discharge_upload_docs"),
			@org.hibernate.annotations.Parameter(name = "idName", value = "vchPdUploadDocsId") })
    @Column(name = "vchPdUploadDocsId", nullable = false, length = 20)
    private String vchPdUploadDocsId;

	@ManyToOne
    @JoinColumn(name = "pledging_discharge_receipt_id", referencedColumnName = "pledging_discharge_receipt_id", nullable = true)
    private PledgingDischargeDepositorWarehouse pledgingDischargeDepositorWarehouse;

    @Column(name = "bank_statement_upload", length = 255)
    private String bankStatementUpload;

    @Column(name = "passport_photo_upload", length = 255)
    private String passportPhotoUpload;

    @Column(name = "latest_id_or_passport_upload", length = 255)
    private String latestIdOrPassportUpload;

    @Column(name = "address_proof_upload", length = 255)
    private String addressProofUpload;

    @Column(name = "warehouse_receipt_upload", length = 255)
    private String warehouseReceiptUpload;

    @Column(name = "pin_certificate_upload", length = 255)
    private String pinCertificateUpload;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private Timestamp dtmCreatedOn;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn")
    private Timestamp dtmUpdatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;

    @Column(name = "bitDraftStatus")
    private Boolean bitDraftStatus = false;
}
