package mbs.am.gui.repository;

import mbs.am.gui.entity.PolicyHistoryEntity;
import mbs.am.gui.entity.SignalCatalogEntity;
import mbs.am.gui.entity.SignalPolicyEntity;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Stateless
public class SignalPolicyRepository extends BaseRepository<SignalPolicyEntity, Long> {


    public SignalPolicyRepository() {
        super(SignalPolicyEntity.class);
    }
    public List<SignalPolicyEntity> findAllCrossTenant() {
        return em.createQuery(
                        "SELECT p FROM SignalPolicyEntity p " +
                                "JOIN FETCH p.catalog " +
                                "ORDER BY p.tenantId ASC, p.priority ASC", SignalPolicyEntity.class)
                .getResultList();
    }

    public List<SignalPolicyEntity> findAllByTenant(String tenantId) {
        return em.createQuery(
                        "SELECT p FROM SignalPolicyEntity p " +
                                "JOIN FETCH p.catalog " +
                                "WHERE p.tenantId = :tenantId " +
                                "ORDER BY p.priority ASC", SignalPolicyEntity.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }
    public List<SignalPolicyEntity> findActiveByTenant(String tenantId) {
        return em.createQuery(
                        "SELECT p FROM SignalPolicyEntity p " +
                                "JOIN FETCH p.catalog " +
                                "WHERE p.tenantId = :tenantId AND p.status = 1 " +
                                "ORDER BY p.priority ASC", SignalPolicyEntity.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }
    public Optional<SignalPolicyEntity> findByIdAndTenant(Long id, String tenantId) {
        try {
            SignalPolicyEntity entity = em.createQuery(
                            "SELECT p FROM SignalPolicyEntity p " +
                                    "JOIN FETCH p.catalog " +
                                    "WHERE p.id = :id AND p.tenantId = :tenantId", SignalPolicyEntity.class)
                    .setParameter("id", id)
                    .setParameter("tenantId", tenantId)
                    .getSingleResult();
            return Optional.of(entity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    public SignalPolicyEntity saveWithCatalog(SignalPolicyEntity entity, String signalKey) {
        if (signalKey != null) {
            SignalCatalogEntity catalogRef = em.getReference(SignalCatalogEntity.class, signalKey);
            entity.setCatalog(catalogRef);
        }

        // Inherits the merge + flush logic from BaseRepository
        return this.save(entity);
    }
    public void insertHistory(PolicyHistoryEntity historyEntity) {
        em.persist(historyEntity);
        em.flush(); // Ensure visibility or immediate constraint failure
    }
}