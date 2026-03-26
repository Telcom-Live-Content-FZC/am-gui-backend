package mbs.am.gui.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PolicyResponse {

    private Long id;
    @SerializedName("tenant-id")
    private Long tenantId;
    private String version;
    @SerializedName("policy-name")
    private String key;
    private String description;
    @SerializedName("data-type")
    private String dataType;
    private String operator;
    @SerializedName("expected-value")
    private String expectedValue;
    @SerializedName("risk-weight")
    private Integer riskWeight;
    private String action;
    private Integer priority;
    @SerializedName("message-id")
    private Integer messageId;
    private Integer status;

}
