package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity @Getter @Setter @ToString
@Table(name = "t_inspector")
public class Inspector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "intId", unique = true)
    private String intId;
    
    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "mobileNumber")
    private String mobileNumber;

    @Column(name = "address")
    private String address;
    
    @Column(name = "dtmCreatedOn")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date dtmCreatedOn;

    @Column(name = "stmUpdatedOn")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date stmUpdatedOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
//    private Status status = Status.Pending;
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "forwarded_to")
    private Stakeholder forwardedTo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "forwarded_by")
    private Stakeholder forwardedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_action")
//    private Action currentAction = Action.Pending;
    private Action currentAction ;

    @OneToMany(mappedBy = "inspector", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CheckList> checklist;

    @OneToMany(mappedBy = "inspector", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Remark> remarks;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_user_id", referencedColumnName = "intId")
    private Tuser adminUser;
}