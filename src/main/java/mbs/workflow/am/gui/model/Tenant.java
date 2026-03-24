package mbs.workflow.am.gui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {
    private Long id;
    private String name;
    private String issuer;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<TenantKeyRegistry> keys;
    private List<TenantConfig> configs;
}
