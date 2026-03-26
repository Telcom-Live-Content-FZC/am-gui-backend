package mbs.am.gui.mapper;


import mbs.am.gui.dto.PolicyResponse;
import mbs.am.gui.dto.PolicySaveRequest;
import mbs.am.gui.entity.SignalCatalogEntity;
import mbs.am.gui.entity.SignalPolicyEntity;
import mbs.am.gui.model.SignalPolicy;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SignalPolicyMapper {

    public SignalPolicy toModel(SignalPolicyEntity entity) {
        if (entity == null) return null;
        return SignalPolicy.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .version(entity.getVersion())
                .key(entity.getCatalog() != null ? entity.getCatalog().getKey() : null)
                .description(entity.getCatalog() != null ? entity.getCatalog().getDescription() : null)
                .category(entity.getCatalog() != null ? entity.getCatalog().getCategory() : null)
                .dataType(entity.getDataType())
                .operator(entity.getOperator())
                .expectedValue(entity.getExpectedValue())
                .riskWeight(entity.getRiskWeight())
                .action(entity.getAction())
                .priority(entity.getPriority())
                .messageId(entity.getMessageId())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    public SignalPolicyEntity toEntity(SignalPolicy model) {
        if (model == null) return null;
        SignalPolicyEntity entity = new SignalPolicyEntity();
        entity.setId(model.getId());
        entity.setTenantId(model.getTenantId());
        entity.setVersion(model.getVersion());

        if (model.getKey() != null) {
            SignalCatalogEntity catalogRef = new SignalCatalogEntity();
            catalogRef.setKey(model.getKey());
            entity.setCatalog(catalogRef);
        }

        entity.setDataType(model.getDataType());
        entity.setOperator(model.getOperator());
        entity.setExpectedValue(model.getExpectedValue());
        entity.setRiskWeight(model.getRiskWeight());
        entity.setAction(model.getAction());
        entity.setPriority(model.getPriority());
        entity.setMessageId(model.getMessageId());
        entity.setStatus(model.getStatus());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        return entity;
    }

    public PolicyResponse toResponse(SignalPolicy model) {
        if (model == null) return null;

        return PolicyResponse.builder()
                .id(model.getId())
                .tenantId(model.getTenantId())
                .version(model.getVersion())
                .key(model.getKey())
                .description(model.getDescription())
                .dataType(model.getDataType())
                .operator(model.getOperator())
                .expectedValue(model.getExpectedValue())
                .riskWeight(model.getRiskWeight())
                .action(model.getAction())
                .priority(model.getPriority())
                .messageId(model.getMessageId())
                .status(model.getStatus())
                .build();
    }
    public SignalPolicy toModel(PolicySaveRequest request) {
        if (request == null) return null;

        return SignalPolicy.builder()
                .tenantId(request.getTenantId())
                .version(request.getVersion())
                .key(request.getKey())
                .dataType(request.getDataType())
                .operator(request.getOperator())
                .expectedValue(request.getExpectedValue())
                .riskWeight(request.getRiskWeight())
                .action(request.getAction())
                .priority(request.getPriority())
                .messageId(request.getMessageId())
                .status(request.getStatus())
                .updatedAt(null)
                .createdAt(null)
                .build();
    }
}
