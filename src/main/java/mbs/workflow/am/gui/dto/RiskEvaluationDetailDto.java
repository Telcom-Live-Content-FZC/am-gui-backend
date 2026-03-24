package mbs.workflow.am.gui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @SerializedName("policy-name")
    private String policyName;
    @SerializedName("score")
    private Integer appliedScore;
}
