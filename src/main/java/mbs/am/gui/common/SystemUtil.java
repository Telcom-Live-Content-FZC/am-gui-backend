package mbs.am.gui.common;

import mbs.am.gui.repository.SystemConfigRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.util.Optional;


public class SystemUtil {

    @Inject
    private SystemConfigRepository systemConfigRepo;

    @Inject
    private SystemConfigRepository configRepo;

    public static boolean isNotBlank(String str) {
        return str != null && str.trim().length() > 0;
    }
    public static boolean isBlank(String str) {
        return !isNotBlank(str);
    }

    /**
     * Returns the default string if the target string is null or whitespace.
     */
    public static String defaultIfBlank(String str, String defaultStr) {
        return isNotBlank(str) ? str : defaultStr;
    }
    public static Optional<Long> parseLongSafely(Object obj) {
        if (obj == null) return Optional.empty();

        if (obj instanceof Number) {
            return Optional.of(((Number) obj).longValue());
        }

        if (obj instanceof String) {
            String str = (String) obj;
            if (isBlank(str)) return Optional.empty();
            try {
                return Optional.of(Long.parseLong(str.trim()));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public static Optional<Integer> parseIntSafely(String str) {
        if (isBlank(str)) return Optional.empty();
        try {
            return Optional.of(Integer.parseInt(str.trim()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Centralized way to fetch config values.
     * Hides the repository implementation details from the rest of the app.
     */
    public String getAppConfig(String key, String defaultValue) {
        return configRepo.getByNameOrDefault(key, defaultValue);
    }

    public boolean getAppConfigAsBoolean(String key, boolean defaultValue) {
        return configRepo.getByNameOrDefaultAsBooleanValue(key, defaultValue);
    }

    /**
     * Safely resolves the working directory across different OS environments.
     */
    public  static String getWorkingDirectory() {
        String classpath = System.getProperty("java.class.path");
        if (isBlank(classpath)) {
            return new File(".").getAbsolutePath();
        }

        String firstEntry = classpath.split(File.pathSeparator)[0];
        File file = new File(firstEntry).getAbsoluteFile();

        File parent = file.getParentFile();
        return (parent != null) ? parent.getPath() : file.getPath();
    }

}
