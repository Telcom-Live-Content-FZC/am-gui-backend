package mbs.am.gui.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    private String tenantId;
    private String tableName;
    private String recordId;
    private String action;
    private String fieldChanged;
    private String oldValue;
    private String newValue;
    private String changedBy;
}
