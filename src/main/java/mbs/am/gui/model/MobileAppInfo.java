package mbs.am.gui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MobileAppInfo {

    private Long id;
    private Long tenantId;
    private String packageName;
    private String minVersion;
    private String maxVersion;
    private String signature;
    private boolean isPlayIntegrityEnabled;
    private String status;

}
