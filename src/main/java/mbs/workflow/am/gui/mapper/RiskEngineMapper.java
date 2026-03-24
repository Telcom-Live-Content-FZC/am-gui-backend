package mbs.workflow.am.gui.mapper;

import mbs.workflow.am.gui.entity.RiskConfigEntity;
import mbs.workflow.am.gui.model.RiskConfig;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RiskEngineMapper {
    public RiskConfig toModel(RiskConfigEntity entity) {
        if (entity == null) return null;
        return RiskConfig.builder()
                .tenantId(entity.getTenantId())
                .monitorWindowHours(entity.getMonitorWindowHours())
                .isEngineEnabled(entity.getIsEngineEnabled() == 1)
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public RiskConfigEntity toEntity(RiskConfig model) {
        if (model == null) return null;
        RiskConfigEntity entity = new RiskConfigEntity();
        entity.setTenantId(model.getTenantId());
        entity.setMonitorWindowHours(model.getMonitorWindowHours());
        entity.setIsEngineEnabled(model.isEngineEnabled() ? 1 : 0);
        entity.setUpdatedBy(model.getUpdatedBy());
        return entity;
    }
}
