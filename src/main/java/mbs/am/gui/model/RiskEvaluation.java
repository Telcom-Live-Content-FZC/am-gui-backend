package mbs.am.gui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RiskEvaluation {

    private String requestId;
    private Long tenantId;
    private String deviceId;
    private String signingKey;
    private Integer totalRiskScore;
    private String appVersion;
    private String finalAction;

    @Builder.Default
    private List<RiskEvaluationDetail> details = new ArrayList<>();

    public void addDetail(RiskEvaluationDetail detail) {
        if (this.details == null) {
            this.details = new ArrayList<>();
        }
        this.details.add(detail);
    }

}
