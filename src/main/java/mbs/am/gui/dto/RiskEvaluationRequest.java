package mbs.am.gui.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class RiskEvaluationRequest {

    @Builder.Default
    private String tenantId = "1";
    private String requestId;
    private String deviceId;
    private String os;
    private String osVersion;
    private String patchLevel;
    private String appVersion;
    private boolean isRooted;
    private Map<String, Object> threatMap;

    private String certificate;
    private String dateTime;
}