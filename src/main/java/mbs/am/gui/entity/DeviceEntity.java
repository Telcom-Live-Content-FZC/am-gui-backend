package mbs.am.gui.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "TBLDEVICES", uniqueConstraints = {
        @UniqueConstraint(name = "UK_DEVICE_TENANT", columnNames = {"TENANT_ID", "DEVICE_ID"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TENANT_ID", nullable = false)
    private Long tenantId;

    @Column(name = "DEVICE_ID", nullable = false, length = 100)
    private String deviceId;

    @Column(name = "MANUFACTURER", length = 250)
    private String manufacturer;

    @Column(name = "MODEL", length = 250)
    private String model;

    @Column(name = "OS", length = 50)
    private String os;

    @Column(name = "OSVERSION", length = 50)
    private String osVersion;

    @Column(name = "ROOTED", length = 1)
    @Builder.Default
    private String rooted = "N";

    @Column(name = "LABEL", length = 250)
    private String label;

    @Column(name = "ATTESTATION_ENABLED", nullable = false)
    @Builder.Default
    private Integer attestationEnabled = 1;

    @Column(name = "STATUS", length = 20)
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