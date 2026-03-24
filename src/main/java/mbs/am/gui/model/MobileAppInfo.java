package mbs.am.gui.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MobileAppInfo {
    
    private Long id;
    private String packageName;
    private String minVersion;
    private String maxVersion;
    private String signature;
    private boolean isPlayIntegrityEnabled;
    private String status;
    
}
