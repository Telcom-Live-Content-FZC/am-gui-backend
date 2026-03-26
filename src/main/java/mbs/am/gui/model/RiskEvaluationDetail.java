package mbs.am.gui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RiskEvaluationDetail {

    private String policyName;
    private String version;
    private Integer appliedScore;

}
