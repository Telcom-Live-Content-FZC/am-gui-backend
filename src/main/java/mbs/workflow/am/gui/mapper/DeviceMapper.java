package mbs.workflow.am.gui.mapper;

import mbs.workflow.am.gui.dto.DeviceRegistrationRequest;
import mbs.workflow.am.gui.entity.DeviceEntity;
import mbs.workflow.am.gui.model.Device;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DeviceMapper {

    public Device toModel(DeviceRegistrationRequest request) {
        if (request == null) return null;

        return Device.builder()
                .deviceId(request.getDeviceId())
                .manufacturer(request.getManufacturer())
                .model(request.getModel())
                .os(request.getOs())
                .version(request.getVersion())
                .rooted(request.isRooted())
                .label(request.getLabel())
                .isAttestationEnabled(request.isAttestationEnabled())
                .build();
    }

    public Device toModel(DeviceEntity entity) {
        if (entity == null) return null;
        return Device.builder()
                .id(entity.getId())
                .deviceId(entity.getDeviceId())
                .manufacturer(entity.getManufacturer())
                .model(entity.getModel())
                .os(entity.getOs())
                .version(entity.getOsVersion())
                .rooted("YES".equals(entity.getRooted()))
                .label(entity.getLabel())
                .isAttestationEnabled(entity.getAttestationEnabled() == 1)
                .build();
    }
    public DeviceEntity toEntity(Device model) {
        if (model == null) return null;
        DeviceEntity entity = new DeviceEntity();
         entity.setId(model.getId());
        entity.setDeviceId(model.getDeviceId());
        entity.setManufacturer(model.getManufacturer());
        entity.setModel(model.getModel());
        entity.setOs(model.getOs());
        entity.setOsVersion(model.getVersion());
        entity.setRooted(model.isRooted() ? "YES" : "NO");
        entity.setLabel(model.getLabel());
        entity.setAttestationEnabled(model.isAttestationEnabled() ? 1 : 0);
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
                .rooted(dto.isRooted() ? "YES" : "NO")
                .label(dto.getLabel())
                .attestationEnabled(dto.isAttestationEnabled() ? 1 : 0)
                .build();
    }
}
