package app.ewarehouse.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity @Getter @Setter @ToString(exclude = "inspector")
@Table(name = "t_checklist")
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chkText")
    private String chkText;
    @Column(name = "isselected")
    private Boolean isSelected;
    @Column(name="file_path")
    private String filePath;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "inspector_id")
    private Inspector inspector;
}
