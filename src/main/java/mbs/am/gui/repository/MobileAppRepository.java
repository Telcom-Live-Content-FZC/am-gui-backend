package mbs.am.gui.repository;


import mbs.am.gui.entity.MobileAppInfoEntity;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class MobileAppRepository extends BaseRepository<MobileAppInfoEntity, Long> {

    public MobileAppRepository() {
        super(MobileAppInfoEntity.class);
    }

    public List<MobileAppInfoEntity> findAll() {
        return em.createQuery("SELECT a FROM MobileAppInfoEntity a ORDER BY a.id ASC", MobileAppInfoEntity.class)
                .getResultList();
    }

    public List<MobileAppInfoEntity> findAllByTenant(Long tenantId) {
        return em.createQuery(
                        "SELECT a FROM MobileAppInfoEntity a WHERE a.tenantId = :tenantId ORDER BY a.id ASC",
                        MobileAppInfoEntity.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    public List<MobileAppInfoEntity> findByPackageName(String packageName) {
        return em.createQuery(
                        "SELECT a FROM MobileAppInfoEntity a WHERE a.packageName = :packageName", MobileAppInfoEntity.class)
                .setParameter("packageName", packageName)
                .getResultList();
    }

    public List<MobileAppInfoEntity> findByTenantAndPackageName(Long tenantId, String packageName) {
        String jpql = "SELECT a FROM MobileAppInfoEntity a " +
                "WHERE (:tenantId IS NULL OR a.tenantId = :tenantId) " +
                "AND (:packageName IS NULL OR a.packageName = :packageName) " +
                "ORDER BY a.id ASC";

        return em.createQuery(jpql, MobileAppInfoEntity.class)
                .setParameter("tenantId", tenantId)
                .setParameter("packageName", (packageName != null && !packageName.trim().isEmpty()) ? packageName : null)
                .getResultList();
    }
}