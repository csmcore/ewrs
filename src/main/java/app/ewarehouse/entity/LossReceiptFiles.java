package app.ewarehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_loss_receipt_files")
public class LossReceiptFiles {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intId")
    private Integer id;

	@Column(name = "vchFilePath")
    private String filePath;
	
	@Column(name = "bitDeletedFlag")
	private Boolean bitDeletedFlag = false;
	
	@Column(name = "intLossReceiptId")
	private Integer lossReceiptId;
}
