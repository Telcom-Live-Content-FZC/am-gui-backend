package mbs.workflow.am.gui.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBLSIGNALCATALOG")
@Getter
@Setter
public class SignalCatalogEntity implements Serializable {

    @Id
    @Column(name = "KEY", length = 50, nullable = false)
    private String key;

    @Column(name = "CATEGORY", nullable = false, length = 50)
    private String category;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}