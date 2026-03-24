package mbs.workflow.am.gui.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RiskEvaluationDetail {

    private String policyName;
    private Integer appliedScore;
}
