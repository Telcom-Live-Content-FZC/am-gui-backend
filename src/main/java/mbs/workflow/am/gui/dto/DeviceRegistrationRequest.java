package mbs.workflow.am.gui.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mbs.softpos.common.dto.Threat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRegistrationRequest {

    @SerializedName("device-id")
    @Expose
    private String deviceId;
    private String os;
    @SerializedName("os-version")
    private String version;
    @SerializedName("patch-level")
    private String patchLevel;
    private String manufacturer;
    private String model;
    @Builder.Default
    private boolean attestationEnabled = true;
    @Builder.Default
    private boolean rooted = false;
    private String label;
    @Builder.Default
    private String status = "ACTIVE";


}
