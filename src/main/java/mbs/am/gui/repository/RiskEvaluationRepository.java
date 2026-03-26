package mbs.am.gui.repository;


import mbs.am.gui.dto.ActionAccount;
import mbs.am.gui.dto.EvaluationLog;
import mbs.am.gui.dto.TopThreat;
import mbs.am.gui.dto.TriggeredRule;
import mbs.am.gui.entity.RiskEvaluationEntity;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class RiskEvaluationRepository extends BaseRepository<RiskEvaluationEntity, Long> {

    public RiskEvaluationRepository() {
        super(RiskEvaluationEntity.class);
    }

    @Override
    public RiskEvaluationEntity save(RiskEvaluationEntity entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    public Integer getTotalRiskForDevice(String deviceId, Long tenantId, LocalDateTime windowStart) {
        String jpql = "SELECT COALESCE(SUM(r.totalRiskScore), 0) FROM RiskEvaluationEntity r " +
                "WHERE r.deviceId = :deviceId " +
                "AND r.tenantId = :tenantId " +
                "AND r.evaluationTime >= :windowStart";

        Long total = em.createQuery(jpql, Long.class)
                .setParameter("deviceId", deviceId)
                .setParameter("tenantId", tenantId)
                .setParameter("windowStart", windowStart)
                .getSingleResult();

        return total != null ? Math.toIntExact(total) : 0;
    }

    public List<ActionAccount> getActionCounts(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        String jpql = "SELECT new mbs.am.gui.dto.ActionAccount(e.finalAction, COUNT(e)) " +
                "FROM RiskEvaluationEntity e " +
                "WHERE (:tenantId IS NULL OR e.tenantId = :tenantId) " +
                "AND e.evaluationTime BETWEEN :startDate AND :endDate " +
                "GROUP BY e.finalAction";

        return em.createQuery(jpql, ActionAccount.class)
                .setParameter("tenantId", tenantId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    public List<TopThreat> getTopThreats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, int limit) {
        String jpql = "SELECT new mbs.am.gui.dto.TopThreat(d.policyName, c.description, COUNT(d)) " +
                "FROM RiskEvaluationDetailEntity d " +
                "JOIN d.evaluation e " +
                "JOIN SignalCatalogEntity c ON d.policyName = c.key " +
                "WHERE (:tenantId IS NULL OR e.tenantId = :tenantId) " +
                "AND e.evaluationTime BETWEEN :startDate AND :endDate " +
                "GROUP BY d.policyName, c.description " +
                "ORDER BY COUNT(d) DESC";

        return em.createQuery(jpql, TopThreat.class)
                .setParameter("tenantId", tenantId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<EvaluationLog> getEvaluationLogs(Long tenantId, String deviceId, String action, int offset, int limit) {
        StringBuilder jpql = new StringBuilder(
                "SELECT new mbs.am.gui.dto.EvaluationLog(e.id, e.evaluationTime, e.deviceId, e.finalAction, e.totalRiskScore) " +
                        "FROM RiskEvaluationEntity e WHERE 1=1"
        );

        if (tenantId != null) jpql.append(" AND e.tenantId = :tenantId");
        if (deviceId != null && !deviceId.isEmpty()) jpql.append(" AND e.deviceId = :deviceId");
        if (action != null && !action.isEmpty()) jpql.append(" AND e.finalAction = :action");

        jpql.append(" ORDER BY e.evaluationTime DESC, e.id DESC");

        TypedQuery<EvaluationLog> query = em.createQuery(jpql.toString(), EvaluationLog.class);

        if (tenantId != null) query.setParameter("tenantId", tenantId);
        if (deviceId != null && !deviceId.isEmpty()) query.setParameter("deviceId", deviceId);
        if (action != null && !action.isEmpty()) query.setParameter("action", action);

        return query.setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    public Optional<RiskEvaluationEntity> getEvaluationById(long id, Long tenantId) {
        String jpql = "SELECT e FROM RiskEvaluationEntity e WHERE e.id = :id AND (:tenantId IS NULL OR e.tenantId = :tenantId)";
        try {
            return Optional.of(
                    em.createQuery(jpql, RiskEvaluationEntity.class)
                            .setParameter("id", id)
                            .setParameter("tenantId", tenantId)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<TriggeredRule> getTriggeredRulesForEvaluation(long evaluationId) {
        String jpql = "SELECT new mbs.am.gui.dto.TriggeredRule(d.policyName, c.description, d.appliedScore) " +
                "FROM RiskEvaluationDetailEntity d " +
                "LEFT JOIN SignalCatalogEntity c ON d.policyName = c.key " +
                "WHERE d.evaluation.id = :evaluationId";

        return em.createQuery(jpql, TriggeredRule.class)
                .setParameter("evaluationId", evaluationId)
                .getResultList();
    }
}