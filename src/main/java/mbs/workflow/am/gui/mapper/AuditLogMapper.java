package mbs.workflow.am.gui.mapper;

import mbs.workflow.am.gui.entity.AuditLogEntity;
import mbs.workflow.am.gui.model.AuditLog;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class AuditLogMapper {

    public AuditLogEntity toEntity(AuditLog model) {
        if (model == null) return null;

        return AuditLogEntity.builder()
                .tenantId(model.getTenantId())
                .tableName(model.getTableName())
                .recordId(model.getRecordId())
                .action(model.getAction())
                .fieldChanged(model.getFieldChanged())
                .oldValue(model.getOldValue())
                .newValue(model.getNewValue())
                .changedBy(model.getChangedBy() != null ? model.getChangedBy() : "SYSTEM")
                .changedAt(LocalDateTime.now())
                .build();
    }
}
