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
public class RiskConfig {

    private Long tenantId;
    private Integer monitorWindowHours;
    private boolean isEngineEnabled;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
