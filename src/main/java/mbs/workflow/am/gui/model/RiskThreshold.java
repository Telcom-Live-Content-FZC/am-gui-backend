package mbs.workflow.am.gui.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskThreshold {

    private Long id;
    private String tenantId;
    private String name;
    private Integer minScore;
    private Integer maxScore;
    private String action;
    private Integer priority;
    private Integer status;
}
