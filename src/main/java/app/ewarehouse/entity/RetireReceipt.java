package app.ewarehouse.entity;

import java.time.LocalTime;
import java.util.Date;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_retire_receipt")
@DynamicUpdate
public class RetireReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
    @Column(name = "date_of_activity")
    private Date dateOfActivity; 
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "tm_activity_time", columnDefinition = "TIME")
    private Date timeOfActivity;
    private Boolean declaration;
    private String remark;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_receipt_id", referencedColumnName = "vchWarehouseReceiptId")
    private TwarehouseReceipt warehouseReceipt;
    @Column(name = "txt_passport_path")
    private String txtPassportPath;
}
