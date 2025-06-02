package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
@Data
@Table(name="warehouse_reg")
@Entity
public class Warehouse_reg {
@Column(name="`addr`")
private String txtaddress;
@Column(name="`appname`")
private String txtapplicantname;
@Column(name="bitDeletedFlag", columnDefinition="BIT")
private Boolean bitDeletedFlag=false;

@Column(name="dtmCreatedOn")
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

@Column(name="intUpdatedBy")
private Integer intUpdatedBy;

@Column(name="stmUpdatedOn")
@UpdateTimestamp
private Timestamp stmUpdatedOn;


}