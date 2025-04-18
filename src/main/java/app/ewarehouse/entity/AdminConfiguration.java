package app.ewarehouse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "m_admin_config")
public class AdminConfiguration {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intConfigId;
	private String userName;
	private Integer noOfAttempt;
	private float idleTime;
	
	

}
