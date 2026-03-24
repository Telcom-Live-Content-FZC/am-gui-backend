package mbs.am.gui.mapper;

import mbs.am.gui.dto.TenantDto;
import mbs.am.gui.entity.TenantConfigEntity;
import mbs.am.gui.entity.TenantEntity;
import mbs.am.gui.entity.TenantKeyRegistryEntity;
import mbs.am.gui.model.Tenant;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TenantMapper {

    @Inject
    private TenantKeyRegistryMapper tenantKeyRegistryMapper;

    @Inject
    private TenantConfigMapper tenantConfigMapper;

    // ========================
    // ENTITY -> MODEL
    // ========================
    public Tenant fromEntity(TenantEntity entity) {
        if (entity == null) return null;

        return Tenant.builder()
                .id(entity.getId())
                .name(entity.getName())
                .issuer(entity.getIssuer())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .keys(tenantKeyRegistryMapper.fromEntities(entity.getKeys()))
                .configs(tenantConfigMapper.fromEntities(entity.getConfigs()))
                .build();
    }

    public List<Tenant> fromEntities(List<TenantEntity> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    // ========================
    // MODEL -> ENTITY
    // ========================
    public TenantEntity toEntity(Tenant model) {
        if (model == null) return null;

        TenantEntity entity = TenantEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .issuer(model.getIssuer())
                .status(model.getStatus() != null ? model.getStatus() : "ACTIVE")
                .build();

        if (model.getKeys() != null) {
            List<TenantKeyRegistryEntity> keyEntities = tenantKeyRegistryMapper.toEntities(model.getKeys());
            keyEntities.forEach(k -> k.setTenant(entity)); // set parent
            entity.setKeys(keyEntities);
        }

        if (model.getConfigs() != null) {
            List<TenantConfigEntity> configEntities = tenantConfigMapper.toEntities(model.getConfigs());
            configEntities.forEach(c -> c.setTenant(entity)); // set parent
            entity.setConfigs(configEntities);
        }

        return entity;
    }

    public List<TenantEntity> toEntities(List<Tenant> models) {
        if (models == null) return Collections.emptyList();
        return models.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // ========================
    // MODEL -> DTO
    // ========================
    public TenantDto toDto(Tenant model) {
        if (model == null) return null;

        return TenantDto.builder()
                .id(model.getId())
                .name(model.getName())
                .issuer(model.getIssuer())
                .status(model.getStatus())
                .keys(tenantKeyRegistryMapper.toDtos(model.getKeys()))
                .configs(tenantConfigMapper.toDtos(model.getConfigs()))
                .build();
    }

    public List<TenantDto> toDtos(List<Tenant> models) {
        if (models == null) return Collections.emptyList();
        return models.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ========================
    // DTO -> MODEL
    // ========================
    public Tenant fromDto(TenantDto dto) {
        if (dto == null) return null;

        return Tenant.builder()
                .id(dto.getId())
                .name(dto.getName())
                .issuer(dto.getIssuer())
                .status(dto.getStatus())
                .keys(tenantKeyRegistryMapper.fromDtos(dto.getKeys()))
                .configs(tenantConfigMapper.fromDtos(dto.getConfigs()))
                .build();
    }

    public List<Tenant> fromDtos(List<TenantDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
    }
}
