package mbs.am.gui.mapper;

import mbs.am.gui.entity.RiskThresholdEntity;
import mbs.am.gui.model.RiskThreshold;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RiskThresholdMapper {

    public RiskThreshold toModel(RiskThresholdEntity entity) {
        if (entity == null) return null;
        return RiskThreshold.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .name(entity.getName())
                .minScore(entity.getMinScore())
                .maxScore(entity.getMaxScore())
                .action(entity.getAction())
                .priority(entity.getPriority())
                .status(entity.getStatus())
                .build();
    }
    public RiskThresholdEntity toEntity(RiskThreshold model) {
        if (model == null) return null;
        RiskThresholdEntity entity = new RiskThresholdEntity();
        entity.setId(model.getId());
        entity.setTenantId(model.getTenantId());
        entity.setName(model.getName());
        entity.setMinScore(model.getMinScore());
        entity.setMaxScore(model.getMaxScore());
        entity.setAction(model.getAction());
        entity.setPriority(model.getPriority());
        entity.setStatus(model.getStatus());
        return entity;
    }
}
