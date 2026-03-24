package mbs.am.gui.repository;

import mbs.am.gui.entity.RiskConfigEntity;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class RiskConfigRepository extends BaseRepository<RiskConfigEntity, String> {


    public RiskConfigRepository() {
        super(RiskConfigEntity.class);
    }

    public List<RiskConfigEntity> findAll() {
        return em.createQuery("SELECT c FROM RiskConfigEntity c", RiskConfigEntity.class)
                .getResultList();
    }

    public Optional<RiskConfigEntity> findByTenantId(String tenantId) {
        return findById(tenantId);
    }

}