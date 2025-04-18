package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_buyer_payment_invoice")
@Data
public class BuyerPaymentInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "invoiceNumber")
    private String invoiceNumber;

    private BigDecimal price;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "buyer_intId", referencedColumnName = "intId")
    private BuyerDepositor buyer;

    @OneToOne
    @JoinColumn(name = "warehouseReceipt_vchWarehouseReceiptId", referencedColumnName = "vchWarehouseReceiptId")
    private TwarehouseReceipt warehouseReceipt;

    private String contractAgreementFileUrl;

    @OneToOne
    @JoinColumn(name = "document_id", referencedColumnName = "docID")
    private Document document;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.InProgress;

}

