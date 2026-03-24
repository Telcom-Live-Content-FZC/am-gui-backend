package mbs.workflow.am.gui.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBLTENANTKEYREGISTRY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantKeyRegistryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    private TenantEntity tenant;

    @Column(name = "KID", nullable = false, length = 100)
    private String kid;

    @Column(name = "ALGORITHM", length = 20)
    private String algorithm = "RS256";

    @Lob
    @Column(name = "PUBLIC_KEY")
    private String publicKey;

    @Lob
    @Column(name = "PRIVATE_KEY")
    private String privateKey;

    @Column(name = "EXPIRES_AT")
    private LocalDateTime expiresAt;

    @Column(name = "STATUS", length = 20)
    private String status = "ACTIVE";
}
