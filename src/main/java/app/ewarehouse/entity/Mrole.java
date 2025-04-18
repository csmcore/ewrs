package app.ewarehouse.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Table(name="m_admin_role")
@Entity
public class Mrole {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="intId")
private Integer intId;
@Column(name="`vchRoleName`")
private String txtRoleName;
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

//@ManyToMany(mappedBy = "roles")
//private List<ComplaintType> complaints;


}
