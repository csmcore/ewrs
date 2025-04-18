package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_sales_application_main")
public class SalesApplicationMain {
    @Id
    @GeneratedValue(generator = "application-id")
    @GenericGenerator(
            name = "application-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_sales_application_main"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "sales_application_id")
            })
    @Column(name = "sales_application_id")
    private String salesApplicationMainId;

    @OneToOne
    @JoinColumn(name = "id_tab_one")
    private SalesApplicationTabOne idtabone;

    @OneToOne
    @JoinColumn(name = "id_tab_two")
    private SalesApplicationTabTwo idtabtwo;

    @OneToOne
    @JoinColumn(name = "id_tab_three")
    private SalesApplicationTabThree idtabthree;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicationStatus")
    private ApplicationStatus applicationStatus = ApplicationStatus.inProgress;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private Date dtmCreatedOn;

    @UpdateTimestamp
    @Column(name = "stmUpdatedOn")
    private Date stmUpdatedAt;

    @Column(name = "bitDeleteFlag")
    private Boolean bitDeleteFlag = false;
}
