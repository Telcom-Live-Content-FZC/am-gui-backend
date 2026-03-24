package mbs.workflow.am.gui.mapper;

import mbs.workflow.am.gui.domain.RiskEvaluation;
import mbs.workflow.am.gui.dto.RiskEvaluationResponse;
import mbs.workflow.am.gui.entity.RiskEvaluationDetailEntity;
import mbs.workflow.am.gui.entity.RiskEvaluationEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.stream.Collectors;

@ApplicationScoped
public class RiskEvaluationMapper {

    @Inject
    private RiskEvaluationDetailMapper detailMapper;

    public RiskEvaluationEntity toEntity(RiskEvaluation model) {
        if (model == null) {
            return null;
        }
        RiskEvaluationEntity entity = new RiskEvaluationEntity();
        entity.setRequestId(model.getRequestId());
        entity.setTenantId(model.getTenantId());
        entity.setDeviceId(model.getDeviceId());
        entity.setTotalRiskScore(model.getTotalRiskScore());
        entity.setAppVersion(model.getAppVersion());
        entity.setFinalAction(model.getFinalAction());

        if (model.getDetails() != null) {
            model.getDetails().forEach(detailModel -> {
                RiskEvaluationDetailEntity detailEntity = detailMapper.toEntity(detailModel);
                detailEntity.setEvaluation(entity);
                entity.addDetail(detailEntity);
            });
        }
        return entity;
    }

    public RiskEvaluationResponse toResponse(RiskEvaluation model) {
        if (model == null) return null;

        return RiskEvaluationResponse.builder()
                .requestId(model.getRequestId())
                .deviceId(model.getDeviceId())
                .finalAction(model.getFinalAction())
                .totalRiskScore(model.getTotalRiskScore())
                .triggeredRules(model.getDetails().stream()
                        .map(detailMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
}