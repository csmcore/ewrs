package app.ewarehouse.entity;


import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Table(name="supporting_document")
@Entity
public class Supporting_document {
@Column(name="bitDeletedFlag", columnDefinition="BIT")
private Boolean bitDeletedFlag=false;

@Column(name="dtmCreatedOn")
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
@CreationTimestamp
private Timestamp dtmCreatedOn;

@Column(name="intCreatedBy")
private Integer intCreatedBy;

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="intId")
private Integer intId;

@Column(name="intInsertStatus")
private Integer intInsertStatus=1;

private Integer intParentId;
@Column(name="intUpdatedBy")
private Integer intUpdatedBy;

@Column(name="`name_document`")
private String amtxtDocumentName;

@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
@Column(name="stmUpdatedOn")
@UpdateTimestamp
private Timestamp stmUpdatedOn;

@Column(name="`uplod_doc_path`")
private String amfileUploadDocuments;
}