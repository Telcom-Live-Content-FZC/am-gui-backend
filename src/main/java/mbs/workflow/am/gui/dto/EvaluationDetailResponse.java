package mbs.workflow.am.gui.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EvaluationDetailResponse {

    @SerializedName("evaluation-id")
    private String evaluationId;
    @SerializedName("evaluation-time")
    private LocalDateTime evaluationTime;
    @SerializedName("device-id")
    private String deviceId;
    @SerializedName("final-action")
    private String finalAction;
    @SerializedName("total-risk-score")
    private Integer totalRiskScore;

    @SerializedName("triggered-rules")
    private List<TriggeredRule> triggeredRules;


}
