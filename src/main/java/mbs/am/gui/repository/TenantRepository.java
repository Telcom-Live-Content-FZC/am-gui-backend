package mbs.am.gui.repository;

import mbs.am.gui.entity.TenantEntity;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Stateless
public class TenantRepository extends BaseRepository<TenantEntity, Long> {

    public TenantRepository() {
        super(TenantEntity.class);
    }

    public List<TenantEntity> findAll() {
        List<TenantEntity> tenants = em.createQuery(
                        "SELECT t FROM TenantEntity t ORDER BY t.id", TenantEntity.class)
                .getResultList();

        initializeBatch(tenants);
        return tenants;
    }

    public List<TenantEntity> findAll(int offset, int limit) {
        List<TenantEntity> tenants = em.createQuery(
                        "SELECT t FROM TenantEntity t ORDER BY t.id", TenantEntity.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        initializeBatch(tenants);
        return tenants;
    }

    @Override
    public Optional<TenantEntity> findById(Long id) {
        try {
            TenantEntity tenant = em.createQuery(
                            "SELECT t FROM TenantEntity t LEFT JOIN FETCH t.keys WHERE t.id = :id", TenantEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();

            tenant = em.createQuery(
                            "SELECT t FROM TenantEntity t LEFT JOIN FETCH t.configs WHERE t = :tenant", TenantEntity.class)
                    .setParameter("tenant", tenant)
                    .getSingleResult();

            return Optional.of(tenant);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<TenantEntity> findByName(String name) {
        try {
            TenantEntity tenant = em.createQuery(
                            "SELECT t FROM TenantEntity t WHERE t.name = :name", TenantEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();

            fetchCollections(tenant);
            return Optional.of(tenant);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<TenantEntity> findByIssuer(String issuer) {
        try {
            TenantEntity tenant = em.createQuery(
                            "SELECT t FROM TenantEntity t WHERE t.issuerUri = :issuer", TenantEntity.class)
                    .setParameter("issuer", issuer)
                    .getSingleResult();

            fetchCollections(tenant);
            return Optional.of(tenant);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private void initializeBatch(List<TenantEntity> tenants) {
        if (tenants != null && !tenants.isEmpty()) {
            fetchCollections(tenants.get(0));
        }
    }

    private void fetchCollections(TenantEntity tenant) {
        if (tenant != null) {
            tenant.getKeys().size();
            tenant.getConfigs().size();
        }
    }
}