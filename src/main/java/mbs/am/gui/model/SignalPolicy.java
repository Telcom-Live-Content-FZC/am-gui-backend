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
public class SignalPolicy {
    private Long id;
    private String tenantId;
    private String signalKey; // Flattened for easy access
    private String category;
    private String description;
    private String dataType;
    private String operator;
    private String expectedValue;
    private Integer riskWeight;
    private String action;
    private Integer priority;
    private Integer messageId;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


