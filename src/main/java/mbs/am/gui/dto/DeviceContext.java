package mbs.am.gui.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceContext {

    @SerializedName("device-id")
    private String deviceId;
    private String os;
    @SerializedName("os-version")
    private String osVersion;
    @SerializedName("patch-level")
    private String patchLevel;
    private boolean isRooted;
}
