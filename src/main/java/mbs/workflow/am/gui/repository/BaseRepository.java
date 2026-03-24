package mbs.workflow.am.gui.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

public abstract class BaseRepository<E, ID> {

    @PersistenceContext(unitName = "AttestationPU")
    protected EntityManager em;

    private final Class<E> entityClass;

    protected BaseRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public E save(E entity) {
        E result = em.merge(entity);
        em.flush();
        return result;
    }

    public Optional<E> findById(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }
}