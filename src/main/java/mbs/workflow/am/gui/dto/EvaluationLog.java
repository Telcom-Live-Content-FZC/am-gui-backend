package mbs.workflow.am.gui.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationLog {
    private long id;
    @SerializedName("evaluation-time")
    private LocalDateTime evaluationTime;
    @SerializedName("device-id")
    private String deviceId;
    @SerializedName("final-action")
    private String finalAction;
    @SerializedName("total-risk-score")
    private Integer totalRiskScore;
}
