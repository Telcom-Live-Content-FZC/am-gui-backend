package mbs.workflow.am.gui.entity;
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

    @Column(name = "PACKAGE_NAME", nullable = false, length = 50)
    private String packageName;

    @Column(name = "MIN_VERSION", length = 25)
    private String minVersion;

    @Column(name = "MAX_VERSION", length = 25)
    private String maxVersion;

    @Column(name = "SIGNATURE", length = 250)
    private String signature;

    @Column(name = "PLAY_INTEGRITY_ENABLED")
    private Integer playIntegrityEnabled; // 1 = Enabled, 0 = Disabled

    @Column(name = "STATUS", length = 10)
    private String status; // 'ACTIVE' or 'INACTIVE'
}
