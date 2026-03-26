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

    public List<SignalPolicyEntity> findPolicies(Long tenantId, String version, Integer status) {
        String jpql = "SELECT p FROM SignalPolicyEntity p " +
                "JOIN FETCH p.catalog " +
                "WHERE (:tenantId IS NULL OR p.tenantId = :tenantId) " +
                "AND (:version IS NULL OR p.version = :version) " +
                "AND (:status IS NULL OR p.status = :status) " +
                "ORDER BY p.tenantId ASC, p.priority ASC";

        return em.createQuery(jpql, SignalPolicyEntity.class)
                .setParameter("tenantId", tenantId)
                .setParameter("version", (version != null && !version.trim().isEmpty()) ? version : null)
                .setParameter("status", status)
                .getResultList();
    }

    public List<SignalPolicyEntity> findByVersion(Long tenantId, String version) {
        return em.createQuery("SELECT p FROM SignalPolicyEntity p WHERE p.tenantId = :tenantId AND p.version = :version", SignalPolicyEntity.class)
                .setParameter("tenantId", tenantId)
                .setParameter("version", version)
                .getResultList();
    }

    public Optional<SignalPolicyEntity> findByIdAndTenant(Long id, Long tenantId) {
        try {
            SignalPolicyEntity entity = em.createQuery(
                            "SELECT p FROM SignalPolicyEntity p " +
                                    "JOIN FETCH p.catalog " +
                                    "WHERE p.id = :id AND (:tenantId IS NULL OR p.tenantId = :tenantId)", SignalPolicyEntity.class)
                    .setParameter("id", id)
                    .setParameter("tenantId", tenantId)
                    .getSingleResult();
            return Optional.of(entity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    public SignalPolicyEntity saveWithCatalog(SignalPolicyEntity entity, String key) {
        if (key != null) {
            SignalCatalogEntity catalogRef = em.getReference(SignalCatalogEntity.class, key);
            entity.setCatalog(catalogRef);
        }

        return this.save(entity);
    }
    public void updateStatusByVersion(Long tenantId, String version, Integer status) {
        em.createQuery("UPDATE SignalPolicyEntity p SET p.status = :status, p.updatedAt = :now " +
                        "WHERE p.tenantId = :tenantId AND p.version = :version")
                .setParameter("status", status)
                .setParameter("tenantId", tenantId)
                .setParameter("version", version)
                .setParameter("now", java.time.LocalDateTime.now())
                .executeUpdate();
    }
    public void archiveCurrentActive(Long tenantId) {
        em.createQuery("UPDATE SignalPolicyEntity p SET p.status = 0, p.updatedAt = :now " +
                        "WHERE p.tenantId = :tenantId AND p.status = 1")
                .setParameter("tenantId", tenantId)
                .setParameter("now", java.time.LocalDateTime.now())
                .executeUpdate();
    }

    public Optional<Integer> getVersionStatus(Long tenantId, String version) {
        try {
            Integer status = em.createQuery(
                            "SELECT p.status FROM SignalPolicyEntity p " +
                                    "WHERE p.tenantId = :tenantId AND p.version = :version", Integer.class)
                    .setParameter("tenantId", tenantId)
                    .setParameter("version", version)
                    .setMaxResults(1)
                    .getSingleResult();
            return Optional.of(status);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void insertHistory(PolicyHistoryEntity historyEntity) {
        em.persist(historyEntity);
        em.flush(); // Ensure visibility or immediate constraint failure
    }
}