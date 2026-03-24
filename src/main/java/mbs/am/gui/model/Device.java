package mbs.am.gui.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    private Long id;
    private String deviceId;
    private String os;
    private String patchLevel;
    private String version;
    private String manufacturer;
    private String model;
    private boolean rooted;
    private String key;
    private String label;
    private boolean isAttestationEnabled;
    private String status;

}
