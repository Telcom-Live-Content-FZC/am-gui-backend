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
public class MobileAppInfoResponse {
    private Long id;
    @SerializedName("tenant-id")
    private Long tenantId;
    @SerializedName("package-name")
    private String packageName;
    @SerializedName("min-version")
    private String minVersion;
    @SerializedName("max-version")
    private String maxVersion;
    private String signature;
    @SerializedName("play-integrity-enabled")
    private boolean playIntegrityEnabled;
    private String status;
    
}
