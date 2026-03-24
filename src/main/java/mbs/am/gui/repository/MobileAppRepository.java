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

    public List<MobileAppInfoEntity> findByPackageName(String packageName) {
        return em.createQuery(
                        "SELECT a FROM MobileAppInfoEntity a WHERE a.packageName = :packageName", MobileAppInfoEntity.class)
                .setParameter("packageName", packageName)
                .getResultList();
    }
}