package mbs.workflow.am.gui.repository;

import mbs.workflow.am.gui.entity.RiskThresholdEntity;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class RiskThresholdRepository extends BaseRepository<RiskThresholdEntity, Long> {

    public RiskThresholdRepository() {
        super(RiskThresholdEntity.class);
    }

    public List<RiskThresholdEntity> findActiveByTenant(String tenantId) {
        return em.createQuery(
                        "SELECT t FROM RiskThresholdEntity t " +
                                "WHERE t.tenantId = :tenantId AND t.status = 1 " +
                                "ORDER BY t.priority ASC", RiskThresholdEntity.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

}