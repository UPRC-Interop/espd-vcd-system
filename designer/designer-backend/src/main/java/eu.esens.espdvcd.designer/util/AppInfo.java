package eu.esens.espdvcd.designer.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public enum AppInfo {
    INSTANCE;

    private static final String VERSION_FILE = "application.version";
    private final Config appConfig;
    private final PlatformInfo info;

    AppInfo() {
        appConfig = ConfigFactory.parseResources(VERSION_FILE);
        info = new PlatformInfo(getAppVersion(), getAppRevision(), getAppName(), getBuildTime());
    }

    public static AppInfo getInstance() {
        return INSTANCE;
    }

    public String getAppVersion() {
        return appConfig.getString("eu.esens.espdvcd.build.version");
    }

    public String getAppName() {
        return appConfig.getString("eu.esens.espdvcd.build.name");
    }

    public String getAppRevision() {
        return appConfig.getString("eu.esens.espdvcd.build.revision");
    }

    public String getBuildTime() {
        return appConfig.getString("eu.esens.espdvcd.build.time");
    }

    public PlatformInfo getInfo() {
        return info;
    }
}
