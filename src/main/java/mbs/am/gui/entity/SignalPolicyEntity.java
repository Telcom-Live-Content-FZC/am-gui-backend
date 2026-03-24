package mbs.am.gui.entity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBLSIGNALPOLICY")
@Getter
@Setter
public class SignalPolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TENANT_ID", nullable = false, length = 50)
    private String tenantId;

    // Relationship back to the Catalog
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KEY", referencedColumnName = "KEY", nullable = false)
    private SignalCatalogEntity catalog;

    @Column(name = "DATA_TYPE", nullable = false, length = 20)
    private String dataType;

    @Column(name = "OPERATOR", nullable = false, length = 10)
    private String operator;

    @Column(name = "EXPECTED_VALUE", nullable = false, length = 250)
    private String expectedValue;

    @Column(name = "RISK_WEIGHT", nullable = false)
    private Integer riskWeight;

    @Column(name = "ACTION", nullable = false, length = 20)
    private String action;

    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    @Column(name = "MSGID", nullable = false)
    private Integer messageId;

    @Column(name = "STATUS", nullable = false)
    private Integer status;

    @Column(name = "UPDATED_AT", updatable = true)
    private LocalDateTime updatedAt;

    @Column(name = "CREATED_AT", insertable = false)
    private LocalDateTime createdAt;
}
