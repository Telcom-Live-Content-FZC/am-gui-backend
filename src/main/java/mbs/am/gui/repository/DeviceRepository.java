package mbs.am.gui.repository;


import mbs.am.gui.entity.DeviceEntity;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class DeviceRepository extends BaseRepository<DeviceEntity, Long> {

    public DeviceRepository() {
        super(DeviceEntity.class);
    }

    public List<DeviceEntity> findAll() {
        return em.createQuery("SELECT d FROM DeviceEntity d ORDER BY d.id ASC", DeviceEntity.class)
                .getResultList();
    }
    public List<DeviceEntity> findAllByTenant(Long tenantId) {
        return em.createQuery(
                        "SELECT d FROM DeviceEntity d WHERE d.tenantId = :tenantId ORDER BY d.id ASC",
                        DeviceEntity.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    public Optional<DeviceEntity> findByDeviceId(String deviceId) {
        return em.createQuery(
                        "SELECT d FROM DeviceEntity d WHERE d.deviceId = :deviceId",
                        DeviceEntity.class)
                .setParameter("deviceId", deviceId)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }
    public Optional<DeviceEntity> findByTenantAndDeviceId(Long tenantId , String deviceId) {
        return em.createQuery(
                        "SELECT d FROM DeviceEntity d " +
                                "LEFT JOIN FETCH d.deviceKey " +
                                "WHERE d.deviceId = :deviceId AND d.tenantId = :tenantId",
                        DeviceEntity.class)
                .setParameter("deviceId", deviceId)
                .setParameter("tenantId", tenantId)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }

    public void updateDeviceStatus(Long tenantId, String deviceId, String status, boolean isRooted) {
        findByTenantAndDeviceId(tenantId, deviceId).ifPresent(entity -> {
            if (status != null && !status.isEmpty()) {
                entity.setStatus(status);
            }
            entity.setRooted(isRooted ? "Y" : "N");
            this.save(entity);
        });
    }
}