package mbs.am.gui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TBLMOBILEAPPINFO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobileAppInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TENANT_ID", nullable = false)
    private Long tenantId;

    @Column(name = "PACKAGE_NAME", nullable = false, length = 100) // Updated length to 100
    private String packageName;

    @Column(name = "MIN_VERSION", length = 25)
    private String minVersion;

    @Column(name = "MAX_VERSION", length = 25)
    private String maxVersion;

    @Column(name = "SIGNATURE", length = 250)
    private String signature;

    @Column(name = "PLAY_INTEGRITY_ENABLED", nullable = false)
    @Builder.Default
    private Integer playIntegrityEnabled = 1; // 1 = Enabled, 0 = Disabled

    @Column(name = "STATUS", length = 20)
    @Builder.Default
    private String status = "ACTIVE"; // 'ACTIVE' or 'INACTIVE'
}