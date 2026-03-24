package mbs.am.gui.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationResponse {

    @SerializedName("request-id")
    private String requestId;
    @SerializedName("device-id")
    private String deviceId;

    @SerializedName("action")
    private String finalAction;      // ALLOWED, BLOCKED_DEVICE, QUARANTINE
    @SerializedName("risk-score")
    private Integer totalRiskScore;
    @SerializedName("threshold")
    private Integer thresholdUsed;
    @SerializedName("root-detected")
    private boolean rootDetected;

    @SerializedName("triggered-rules")
    private List<RiskEvaluationDetailDto> triggeredRules;

    @SerializedName("threat-signals")
    private Map<String, Object> integritySignals;
    @SerializedName("device-context")
    private DeviceContext deviceContext;

}
