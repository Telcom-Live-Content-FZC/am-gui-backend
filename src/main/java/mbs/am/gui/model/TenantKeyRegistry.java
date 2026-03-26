package mbs.am.gui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantKeyRegistry {

    private Long id;
    private Long tenantId;
    private String kid;
    private String algorithm;
    private String publicKey;
    private String privateKeyEncrypted;
    private java.security.PrivateKey cachedPrivateKey;
    private LocalDateTime expiresAt;
    private String status;
}
