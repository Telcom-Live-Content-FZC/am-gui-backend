package mbs.am.gui.mapper;



import mbs.am.gui.dto.DeviceRegistrationRequest;
import mbs.am.gui.entity.DeviceEntity;
import mbs.am.gui.entity.DeviceKeyEntity;
import mbs.am.gui.model.Device;
import mbs.am.gui.model.Status;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DeviceMapper {

    public Device toModel(DeviceEntity entity) {
        if (entity == null) return null;

        return Device.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .deviceId(entity.getDeviceId())
                .manufacturer(entity.getManufacturer())
                .model(entity.getModel())
                .os(entity.getOs())
                .version(entity.getOsVersion())
                .rooted("Y".equals(entity.getRooted()))
                .label(entity.getLabel())
                .isAttestationEnabled(entity.getAttestationEnabled() == 1)
                .status(Status.fromValue(entity.getStatus()))
                .key(entity.getDeviceKey() != null ? entity.getDeviceKey().getSecretKey() : null)
                .build();
    }

    public DeviceEntity toEntity(Device model) {
        if (model == null) return null;

        DeviceEntity entity = new DeviceEntity();
        entity.setId(model.getId());
        entity.setTenantId(model.getTenantId());
        entity.setDeviceId(model.getDeviceId());
        entity.setManufacturer(model.getManufacturer());
        entity.setModel(model.getModel());
        entity.setOs(model.getOs());
        entity.setOsVersion(model.getVersion());
        entity.setRooted(model.isRooted() ? "Y" : "N");
        entity.setLabel(model.getLabel());
        entity.setAttestationEnabled(model.isAttestationEnabled() ? 1 : 0);
        entity.setStatus(model.getStatus().getValue());

        if (model.getKey() != null) {
            DeviceKeyEntity keyEntity = new DeviceKeyEntity();
            keyEntity.setSecretKey(model.getKey());
            keyEntity.setStatus(1); // Default active
            entity.setDeviceKey(keyEntity); // Uses the helper method to link both sides
        }
        return entity;
    }

    public DeviceEntity toEntity(DeviceRegistrationRequest dto) {
        if (dto == null) return null;
        return DeviceEntity.builder()
                .deviceId(dto.getDeviceId())
                .manufacturer(dto.getManufacturer())
                .model(dto.getModel())
                .os(dto.getOs())
                .osVersion(dto.getVersion())
                .rooted(dto.isRooted() ? "Y" : "N")
                .label(dto.getLabel())
                .attestationEnabled(dto.isAttestationEnabled() ? 1 : 0)
                .build();
    }
}
