package mbs.workflow.am.gui.mapper;


import mbs.workflow.am.gui.domain.RiskEvaluationDetail;
import mbs.workflow.am.gui.dto.RiskEvaluationDetailDto;
import mbs.workflow.am.gui.entity.RiskEvaluationDetailEntity;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class RiskEvaluationDetailMapper {

    public RiskEvaluationDetailEntity toEntity(RiskEvaluationDetail model) {
        if (model == null) return null;

        RiskEvaluationDetailEntity entity = new RiskEvaluationDetailEntity();
        entity.setPolicyName(model.getPolicyName());
        entity.setAppliedScore(model.getAppliedScore());
        return entity;
    }

    public RiskEvaluationDetailDto toDto(RiskEvaluationDetail model) {
        if (model == null) return null;

        return RiskEvaluationDetailDto.builder()
                .policyName(model.getPolicyName())
                .appliedScore(model.getAppliedScore())
                .build();
    }
}
