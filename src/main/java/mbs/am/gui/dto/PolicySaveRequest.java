package mbs.am.gui.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class PolicySaveRequest implements Serializable {

    @SerializedName("tenant-id")
    private Long tenantId;
    private String version;
    @SerializedName("policy-name")
    private String key;
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
    @SerializedName("updated-by")
    private String updatedBy;
}
