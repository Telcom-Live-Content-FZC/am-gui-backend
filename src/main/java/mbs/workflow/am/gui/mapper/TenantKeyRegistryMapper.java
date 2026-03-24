package mbs.workflow.am.gui.mapper;

import mbs.workflow.am.gui.dto.TenantKeyRegistryDto;
import mbs.workflow.am.gui.entity.TenantKeyRegistryEntity;
import mbs.workflow.am.gui.model.TenantKeyRegistry;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TenantKeyRegistryMapper {

    // ========================
    // MODEL -> ENTITY
    // ========================
    public TenantKeyRegistryEntity toEntity(TenantKeyRegistry model) {
        if (model == null) return null;

        return TenantKeyRegistryEntity.builder()
                .id(model.getId())
                .kid(model.getKid())
                .algorithm(model.getAlgorithm())
                .publicKey(model.getPublicKey())
                .privateKey(model.getPrivateKeyEncrypted())
                .expiresAt(model.getExpiresAt())
                .status(model.getStatus())
                .build();
    }

    public List<TenantKeyRegistryEntity> toEntities(List<TenantKeyRegistry> models) {
        if (models == null) return Collections.emptyList();
        return models.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // ========================
    // ENTITY -> MODEL
    // ========================
    public TenantKeyRegistry fromEntity(TenantKeyRegistryEntity entity) {
        if (entity == null) return null;

        return TenantKeyRegistry.builder()
                .id(entity.getId())
                .kid(entity.getKid())
                .algorithm(entity.getAlgorithm())
                .publicKey(entity.getPublicKey())
                .privateKeyEncrypted(entity.getPrivateKey()) // keep internal
                .expiresAt(entity.getExpiresAt())
                .status(entity.getStatus())
                .build();
    }

    public List<TenantKeyRegistry> fromEntities(List<TenantKeyRegistryEntity> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    // ========================
    // DTO -> MODEL
    // ========================
    public TenantKeyRegistry fromDto(TenantKeyRegistryDto dto) {
        if (dto == null) return null;

        return TenantKeyRegistry.builder()
                .id(dto.getId())
                .kid(dto.getKid())
                .algorithm(dto.getAlgorithm())
                .publicKey(dto.getPublicKey())
                .status(dto.getStatus())
                .build();
    }

    public List<TenantKeyRegistry> fromDtos(List<TenantKeyRegistryDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
    }

    // ========================
    // MODEL -> DTO
    // ========================
    public TenantKeyRegistryDto toDto(TenantKeyRegistry model) {
        if (model == null) return null;

        return TenantKeyRegistryDto.builder()
                .id(model.getId())
                .kid(model.getKid())
                .algorithm(model.getAlgorithm())
                .publicKey(model.getPublicKey())
                .status(model.getStatus())
                // privateKeyEncrypted deliberately excluded
                .build();
    }

    public List<TenantKeyRegistryDto> toDtos(List<TenantKeyRegistry> models) {
        if (models == null) return Collections.emptyList();
        return models.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}