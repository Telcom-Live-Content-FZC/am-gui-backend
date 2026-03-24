package mbs.workflow.am.gui.mapper;

import mbs.workflow.am.gui.dto.TenantConfigDto;
import mbs.workflow.am.gui.entity.TenantConfigEntity;
import mbs.workflow.am.gui.model.TenantConfig;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TenantConfigMapper {

    // ========================
    // MODEL -> ENTITY
    // ========================
    public TenantConfigEntity toEntity(TenantConfig model) {
        if (model == null) return null;

        return TenantConfigEntity.builder()
                .id(model.getId())
                .key(model.getKey())
                .value(model.getValue())
                .build();
    }

    public List<TenantConfigEntity> toEntities(List<TenantConfig> models) {
        if (models == null) return Collections.emptyList();

        return models.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // ========================
    // ENTITY -> MODEL
    // ========================
    public TenantConfig fromEntity(TenantConfigEntity entity) {
        if (entity == null) return null;

        return TenantConfig.builder()
                .id(entity.getId())
                .key(entity.getKey())
                .value(entity.getValue())
                .build();
    }

    public List<TenantConfig> fromEntities(List<TenantConfigEntity> entities) {
        if (entities == null) return Collections.emptyList();

        return entities.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    // ========================
    // DTO -> MODEL
    // ========================
    public TenantConfig fromDto(TenantConfigDto dto) {
        if (dto == null) return null;

        return TenantConfig.builder()
                .id(dto.getId())
                .key(dto.getKey())
                .value(dto.getValue())
                .build();
    }

    public List<TenantConfig> fromDtos(List<TenantConfigDto> dtos) {
        if (dtos == null) return Collections.emptyList();

        return dtos.stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
    }

    // ========================
    // MODEL -> DTO
    // ========================
    public TenantConfigDto toDto(TenantConfig model) {
        if (model == null) return null;

        return TenantConfigDto.builder()
                .id(model.getId())
                .key(model.getKey())
                .value(model.getValue())
                .build();
    }

    public List<TenantConfigDto> toDtos(List<TenantConfig> models) {
        if (models == null) return Collections.emptyList();

        return models.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
