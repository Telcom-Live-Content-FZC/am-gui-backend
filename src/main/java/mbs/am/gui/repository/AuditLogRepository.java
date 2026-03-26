package mbs.am.gui.repository;


import mbs.am.gui.entity.AuditLogEntity;
import mbs.am.gui.mapper.AuditLogMapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Stateless
public class AuditLogRepository {

    @PersistenceContext(unitName = "AttestationPU")
    private EntityManager em;

    @Inject
    private AuditLogMapper mapper;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void save(AuditLogEntity entity) {
        em.persist(entity);
    }


}
