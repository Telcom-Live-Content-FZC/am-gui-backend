package mbs.am.gui.mapper;

import mbs.am.gui.dto.MobileAppInfoRequest;
import mbs.am.gui.dto.MobileAppInfoResponse;
import mbs.am.gui.entity.MobileAppInfoEntity;
import mbs.am.gui.model.MobileAppInfo;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MobileAppMapper {
    public MobileAppInfo toModel(MobileAppInfoEntity entity) {
        if (entity == null) return null;
        return MobileAppInfo.builder()
                .id(entity.getId())
                .packageName(entity.getPackageName())
                .minVersion(entity.getMinVersion())
                .maxVersion(entity.getMaxVersion())
                .signature(entity.getSignature())
                .isPlayIntegrityEnabled(entity.getPlayIntegrityEnabled() != null && entity.getPlayIntegrityEnabled() == 1)
                .status(entity.getStatus())
                .build();
    }
    public MobileAppInfo toModel(MobileAppInfoRequest request) {
        if (request == null) return null;
        return MobileAppInfo.builder()
                .packageName(request.getPackageName())
                .minVersion(request.getMinVersion())
                .maxVersion(request.getMaxVersion())
                .signature(request.getSignature())
                .isPlayIntegrityEnabled(request.isPlayIntegrityEnabled())
                .status("ACTIVE")
                .build();
    }

    public MobileAppInfoEntity toEntity(MobileAppInfo model) {
        if (model == null) return null;
        return MobileAppInfoEntity.builder()
                .id(model.getId())
                .packageName(model.getPackageName())
                .minVersion(model.getMinVersion())
                .maxVersion(model.getMaxVersion())
                .signature(model.getSignature())
                .playIntegrityEnabled(model.isPlayIntegrityEnabled() ? 1 : 0)
                .status(model.getStatus())
                .build();
    }

    public MobileAppInfoResponse toResponse(MobileAppInfo model) {
        if (model == null) return null;
        return MobileAppInfoResponse.builder()
                .id(model.getId())
                .packageName(model.getPackageName())
                .minVersion(model.getMinVersion())
                .maxVersion(model.getMaxVersion())
                .signature(model.getSignature())
                .playIntegrityEnabled(model.isPlayIntegrityEnabled())
                .status(model.getStatus())
                .build();
    }
}
