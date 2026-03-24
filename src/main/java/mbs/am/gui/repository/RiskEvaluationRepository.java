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

    public Integer getTotalRiskForDevice(String deviceId, String tenantId, LocalDateTime windowStart) {
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

    public List<ActionAccount> getActionCounts(String tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        String jpql = "SELECT new mbs.workflow.am.dto.ActionAccount(e.finalAction, COUNT(e)) " +
                "FROM RiskEvaluationEntity e " +
                "WHERE e.tenantId = :tenantId " +
                "AND e.evaluationTime BETWEEN :startDate AND :endDate " +
                "GROUP BY e.finalAction";

        return em.createQuery(jpql, ActionAccount.class)
                .setParameter("tenantId", tenantId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    public List<TopThreat> getTopThreats(String tenantId, LocalDateTime startDate, LocalDateTime endDate, int limit) {
        String jpql = "SELECT new mbs.workflow.am.dto.TopThreat(d.policyName, c.description, COUNT(d)) " +
                "FROM RiskEvaluationDetailEntity d " +
                "JOIN d.evaluation e " +
                "JOIN SignalCatalogEntity c ON d.policyName = c.key " +
                "WHERE e.tenantId = :tenantId " +
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

    public List<EvaluationLog> getEvaluationLogs(String tenantId, String deviceId, String action, int offset, int limit) {
        List<String> conditions = new ArrayList<>();
        conditions.add("e.tenantId = :tenantId");

        boolean hasDevice = deviceId != null && !deviceId.isEmpty();
        boolean hasAction = action != null && !action.isEmpty();

        if (hasDevice) {
            conditions.add("e.deviceId = :deviceId");
        }
        if (hasAction) {
            conditions.add("e.finalAction = :action");
        }
        String jpql =
                "SELECT new mbs.workflow.am.dto.EvaluationLog(" +
                        "e.id, e.evaluationTime, e.deviceId, e.finalAction, e.totalRiskScore) " +
                        "FROM RiskEvaluationEntity e " +
                        "WHERE " + String.join(" AND ", conditions) + " " +
                        "ORDER BY e.evaluationTime DESC, e.id DESC";

        TypedQuery<EvaluationLog> query = em.createQuery(jpql, EvaluationLog.class)
                .setParameter("tenantId", tenantId);
        if (hasDevice) {
            query.setParameter("deviceId", deviceId);
        }
        if (hasAction) {
            query.setParameter("action", action);
        }

        return query
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Optional<RiskEvaluationEntity> getEvaluationById(long id, String tenantId) {
        String jpql = "SELECT e FROM RiskEvaluationEntity e WHERE e.id = :id AND e.tenantId = :tenantId";
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
        String jpql = "SELECT new mbs.workflow.am.dto.TriggeredRule(d.policyName, c.description, d.appliedScore) " +
                "FROM RiskEvaluationDetailEntity d " +
                "LEFT JOIN SignalCatalogEntity c ON d.policyName = c.key " +
                "WHERE d.evaluation.id = :evaluationId";

        return em.createQuery(jpql, TriggeredRule.class)
                .setParameter("evaluationId", evaluationId)
                .getResultList();
    }
}