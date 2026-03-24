package mbs.am.gui.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBLDEVICES_BK")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_seq")
    @SequenceGenerator(name = "device_seq", sequenceName = "TBLDEVICES_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "DEVICEID", nullable = false, length = 50, unique = true)
    private String deviceId;

    @Column(name = "MANUFACTURER", length = 250)
    private String manufacturer;

    @Column(name = "MODEL", length = 250)
    private String model;

    @Column(name = "OS", length = 250)
    private String os;

    @Column(name = "OSVERSION", length = 250)
    private String osVersion;

    @Column(name = "ROOTED", length = 25)
    @Builder.Default
    private String rooted = "NO";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    @Builder.Default
    private Date timestamp = new Date();

    @Column(name = "LABEL", length = 250)
    private String label;

    @Column(name = "ATTESTATION_ENABLED", nullable = false)
    @Builder.Default
    private Integer attestationEnabled = 1;

    @Column(name = "STATUS", length = 10)
    @Builder.Default
    private String status = "ACTIVE";

    @OneToOne(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private DeviceKeyEntity deviceKey;

    public void setDeviceKey(DeviceKeyEntity key) {
        this.deviceKey = key;
        if (key != null) {
            key.setDevice(this);
        }
    }
}
