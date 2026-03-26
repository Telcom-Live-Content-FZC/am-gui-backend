package mbs.am.gui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name = "TBLDEVICESKEY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceKeyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SECRETKEY", nullable = false, length = 250)
    private String secretKey;

    @Column(name = "STATUS", nullable = false)
    @Builder.Default
    private Integer status = 1;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVICE_ID_FK", nullable = false, unique = true)
    private DeviceEntity device;
}