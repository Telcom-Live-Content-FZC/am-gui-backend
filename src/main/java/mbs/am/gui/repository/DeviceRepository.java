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

}