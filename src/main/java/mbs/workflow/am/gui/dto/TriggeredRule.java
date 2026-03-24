package mbs.workflow.am.gui.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriggeredRule {

    @SerializedName("policy-name")
    private String policyName;
    private String description;
    @SerializedName("applied-score")
    private Integer appliedScore;

}
