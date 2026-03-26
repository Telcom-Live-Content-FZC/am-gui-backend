package mbs.am.gui.mapper;

import lombok.extern.jbosslog.JBossLog;
import mbs.am.gui.common.KeyUtils;
import mbs.am.gui.dto.TenantKeyRegistryDto;
import mbs.am.gui.entity.TenantKeyRegistryEntity;
import mbs.am.gui.model.TenantKeyRegistry;
import mbs.am.gui.repository.SystemConfigRepository;
import mbs.softpos.common.EncryptionUtils;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@JBossLog
public class TenantKeyRegistryMapper {

    @Inject
    private EncryptionUtils encryptionUtils;

    @EJB
    private SystemConfigRepository systemConfig;

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

        TenantKeyRegistry model = TenantKeyRegistry.builder()
                .id(entity.getId())
                .tenantId(entity.getTenant() != null ? entity.getTenant().getId() : null)
                .kid(entity.getKid())
                .algorithm(entity.getAlgorithm())
                .publicKey(entity.getPublicKey())
                .privateKeyEncrypted(entity.getPrivateKey())
                .expiresAt(entity.getExpiresAt())
                .status(entity.getStatus())
                .build();

        // Decrypt the Private Key for the signing engine
        try {
            String masterEncKey = systemConfig.getByNameOrDefault("jwt.db.encryption.key", "DEFAULT_CHANGE_ME");
            String decrypted = encryptionUtils.decrypt(entity.getPrivateKey(), masterEncKey);

            // Assuming KeyUtils is a helper you have for parsing PKCS8/PEM
            model.setCachedPrivateKey(KeyUtils.parsePrivateKey(decrypted));

        } catch (Exception e) {
            log.errorf("CRITICAL: Failed to decrypt or parse JWT key for KID: %s", entity.getKid());
        }

        return model;
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