/**
 * 
 */
package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="dyn_dynamic_keys")
public class DynamicKeys implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	private String keyName;
	private String keyDescription;
	private String keyReference;
	private LocalDateTime createdOn;
	private LocalDateTime updatedOn;
	private Boolean bitDeletedFlag=false;
	private Integer createdBy;
	private Integer updatedBy;
}
