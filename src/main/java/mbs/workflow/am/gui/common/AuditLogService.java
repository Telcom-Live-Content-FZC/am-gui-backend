package mbs.workflow.am.gui.common;

import lombok.extern.slf4j.Slf4j;
import mbs.workflow.am.gui.entity.AuditLogEntity;
import mbs.workflow.am.gui.mapper.AuditLogMapper;
import mbs.workflow.am.gui.repository.AuditLogRepository;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Stateless
public class AuditLogService {

    @Inject
    private AuditLogRepository repository;

    @Inject
    private AuditLogMapper mapper;

    @Asynchronous
    public void logChangesAsync(Object oldObj, Object newObj, String recordId, String user) {

        // 1. AUTOMATICALLY DETERMINE THE ACTION
        String action;
        if (oldObj == null && newObj != null) {
            action = "INSERT";
        } else if (oldObj != null && newObj == null) {
            action = "DELETE";
        } else if (oldObj != null && newObj != null) {
            action = "UPDATE";
        } else {
            return; // Both are null, nothing to log
        }

        // 2. Get the reference object (whichever one isn't null) to read annotations and fields
        Object referenceObj = (newObj != null) ? newObj : oldObj;

        // Automatically get Table Name
        String tableName = referenceObj.getClass().getSimpleName();
        if (referenceObj.getClass().isAnnotationPresent(Table.class)) {
            tableName = referenceObj.getClass().getAnnotation(Table.class).name();
        }

        // 3. Loop through fields using Reflection
        Field[] fields = referenceObj.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);

                // Ignore standard/system fields if don't want to audit
                if (field.getName().equalsIgnoreCase("id") ||
                        field.getName().equalsIgnoreCase("updatedAt") ||
                        java.util.Collection.class.isAssignableFrom(field.getType())) {
                    continue;
                }

                // Extract values (safely handling null objects for Inserts/Deletes)
                Object oldVal = (oldObj != null) ? field.get(oldObj) : null;
                Object newVal = (newObj != null) ? field.get(newObj) : null;

                // 4. Compare and Log
                if (!Objects.equals(oldVal, newVal)) {

                    String oldStr = oldVal != null ? String.valueOf(oldVal) : null;
                    String newStr = newVal != null ? String.valueOf(newVal) : null;

                    AuditLogEntity logEntity = AuditLogEntity.builder()
                            .tableName(tableName)
                            .recordId(recordId)
                            .action(action)
                            .fieldChanged(field.getName().toUpperCase())
                            .oldValue(oldStr)
                            .newValue(newStr)
                            .changedBy(user)
                            .changedAt(LocalDateTime.now())
                            .build();

                    repository.save(logEntity);
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
