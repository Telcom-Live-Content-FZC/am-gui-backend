package mbs.workflow.am.gui.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class PolicySaveRequest implements Serializable {

    @SerializedName("tenant-id")
    private String tenantId;
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
    private Integer status;
    @SerializedName("updated-by")
    private String updatedBy;
}
