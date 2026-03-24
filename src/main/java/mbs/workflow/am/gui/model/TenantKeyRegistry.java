package mbs.workflow.am.gui.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantKeyRegistry {

    private Long id;
    private String kid;
    private String algorithm;
    private String publicKey;
    private String privateKeyEncrypted;
    private LocalDateTime expiresAt;
    private String status;
}
