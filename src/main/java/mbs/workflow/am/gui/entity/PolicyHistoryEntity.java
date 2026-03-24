package mbs.workflow.am.gui.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBLSIGNALPOLICYHISTORY")
public class PolicyHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TENANT_ID", length = 50)
    private String tenantId;

    @Column(name = "POLICY_ID", nullable = false)
    private Long policyId;

    @Column(name = "FIELD_CHANGED", length = 50) // e.g., "EXPECTED_VALUE", "RISK_WEIGHT"
    private String fieldChanged;

    @Column(name = "OLD_VALUE", length = 250)
    private String oldValue;

    @Column(name = "NEW_VALUE", length = 250)
    private String newValue;

    @Column(name = "CHANGED_BY", length = 100) // The Admin's Username
    private String changedBy;

    @Column(name = "CHANGED_AT", nullable = false)
    private LocalDateTime changedAt;

}
