package mbs.am.gui.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TBLRISKEVALUATION")
public class RiskEvaluationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "REQUESTID", nullable = false, unique = true)
    private String requestId;

    @Column(name = "TENANT_ID", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "DEVICEID", nullable = false)
    private String deviceId;

    @Column(name = "TOTAL_RISK_SCORE")
    private Integer totalRiskScore;

    @Column(name = "EVALUATION_TIME", insertable = false, updatable = false)
    private LocalDateTime evaluationTime;

    @Column(name = "APP_VERSION")
    private String appVersion;

    @Column(name = "FINAL_ACTION")
    private String finalAction;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<RiskEvaluationDetailEntity> details = new ArrayList<>();

    public void addDetail(RiskEvaluationDetailEntity detail) {
        if (this.details == null) {
            this.details = new ArrayList<>();
        }
        this.details.add(detail);
        detail.setEvaluation(this);
    }
}
