package mbs.am.gui.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationDetailDto {
    private String version;
    @SerializedName("policy-name")
    private String policyName;
    @SerializedName("score")
    private Integer appliedScore;
}
