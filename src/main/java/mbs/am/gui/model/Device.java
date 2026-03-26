package mbs.am.gui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    private Long id;
    private Long tenantId;
    private String deviceId;
    private String manufacturer;
    private String model;
    private String os;
    private String version;
    private boolean rooted;
    private String label;
    private boolean isAttestationEnabled;
    private Status status;
    private String key;
}
