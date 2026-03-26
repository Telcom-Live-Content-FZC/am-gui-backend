package mbs.am.gui.repository;

import lombok.extern.jbosslog.JBossLog;
import mbs.am.gui.entity.SystemConfigEntity;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.Optional;

@JBossLog
@Stateless
public class SystemConfigRepository extends BaseRepository<SystemConfigEntity, Long> {

    // 1. Use a constant to guarantee all methods use the EXACT same cache
    private static final String CACHE_NAME = "systemConfigCache";

    public SystemConfigRepository() {
        super(SystemConfigEntity.class);
    }

    @CacheResult(cacheName = CACHE_NAME)
    public Optional<String> getByName(String name) {
        log.debugf("Fetching config from DB for key: %s", name);
        return em.createQuery(
                        "SELECT c.value FROM SystemConfigEntity c WHERE c.key = :key AND c.status = 1", String.class)
                .setParameter("key", name)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }

    public String getByNameOrDefault(String name, String defaultValue) {
        return getByName(name).orElse(defaultValue);
    }

    public long getByNameOrDefaultAsLongValue(String name, long defaultValue) {
        String val = getByNameOrDefault(name, String.valueOf(defaultValue));
        try {
            return Long.parseLong(val);
        } catch (NumberFormatException e) {
            log.warnf("Failed to parse config %s as Long. Using default: %d", name, defaultValue);
            return defaultValue;
        }
    }

    public int getByNameOrDefaultAsIntegerValue(String name, int defaultValue) {
        String val = getByNameOrDefault(name, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            log.warnf("Failed to parse config %s as Integer. Using default: %d", name, defaultValue);
            return defaultValue;
        }
    }

    public boolean getByNameOrDefaultAsBooleanValue(String name, boolean defaultValue) {
        String val = getByNameOrDefault(name, String.valueOf(defaultValue));
        return Boolean.parseBoolean(val);
    }

    @CacheRemoveAll(cacheName = CACHE_NAME)
    public void reloadCache() {
        log.infof("SystemConfig cache cleared.");
    }
}