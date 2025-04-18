package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
@Data
@Table(name="m_admin_groups")
@Entity
public class Mgroups {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="intId")
private Integer intId;

@Column(name="`vchGroupName`")
private String txtGroupName;
@Column(name="`vchAliasName`")
private String txtAliasName;
@Column(name="intCreatedBy")
private Integer intCreatedBy;

@Column(name="intUpdatedBy")
private Integer intUpdatedBy;

@Column(name="dtmCreatedOn")
@CreationTimestamp
private Date dtmCreatedOn;

@Column(name="stmUpdatedOn")
@UpdateTimestamp
private Date stmUpdatedOn;

@Column(name="bitDeletedFlag")
private Boolean bitDeletedFlag=false;


}
