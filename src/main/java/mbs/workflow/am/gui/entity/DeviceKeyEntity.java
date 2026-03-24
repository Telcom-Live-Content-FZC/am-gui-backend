package mbs.workflow.am.gui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "TBLDEVICESKEY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceKeyEntity {

    @Id
    @JoinColumn(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ID")
    private DeviceEntity device;

    @Column(name = "DEVICEID", length = 50)
    private String deviceId;

    @Column(name = "SECRETKEY", length = 250)
    private String secretKey;


}
