package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_sales_application_tab_two")
public class SalesApplicationTabTwo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_tab_two")
    private Integer salesApplicationId;

    @Column(name = "price_of_receipt", nullable = false)
    private Double priceOfReceipt;

    @Column(name = "reason_for_sale")
    private String reasonForSale;

    @Column(name = "upload_warehouse_receipt")
    private String uploadWarehouseReceipt;

    @Column(name ="upload_proof_of_ownership")
    private String uploadProofOfOwnership;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private Date dtmCreatedAt;

    @UpdateTimestamp
    @Column(name = "stmUpdatedOn")
    private Date stmUpdatedAt;

    @Column(name = "bitDeleteFlag")
    private Boolean bitDeleteFlag = false;

    @Column(name = "applicationId")
    private  String applicationId;

    @Column(name = "otherSaleReason")
    private  String otherSaleReason;



}
