package mbs.am.gui.repository;


import mbs.am.gui.entity.SignalCatalogEntity;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class SignalCatalogRepository extends BaseRepository<SignalCatalogEntity, String> {

    public SignalCatalogRepository() {
        super(SignalCatalogEntity.class);
    }

    public List<SignalCatalogEntity> findAll() {
        return em.createQuery("SELECT c FROM SignalCatalogEntity c ORDER BY c.key ASC", SignalCatalogEntity.class)
                .getResultList();
    }

    public Optional<SignalCatalogEntity> findByKey(String key) {
        return findById(key);
    }

}