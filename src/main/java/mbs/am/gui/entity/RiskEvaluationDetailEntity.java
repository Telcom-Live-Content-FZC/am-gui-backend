package mbs.am.gui.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TBLRISKEVALUATIONDETAIL")
public class RiskEvaluationDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVALUATION_ID", nullable = false)
    private RiskEvaluationEntity evaluation;

    @Column(name = "POLICY_NAME", nullable = false)
    private String policyName;

    @Column(name = "APPLIED_SCORE", nullable = false)
    private Integer appliedScore;
}
