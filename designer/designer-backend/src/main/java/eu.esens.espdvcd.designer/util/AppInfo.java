/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        info = new PlatformInfo(getAppVersion(), getEdmVersion(), getAppRevision(), getAppName(), getBuildTime());
    }

    public static AppInfo getInstance() {
        return INSTANCE;
    }

    public String getAppVersion() {
        return appConfig.getString("eu.esens.espdvcd.build.app-version");
    }

    public String getEdmVersion() {
        return appConfig.getString("eu.esens.espdvcd.build.edm-version");
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

    public static class PlatformInfo {
        private final String appVersion;
        private final String edmVersion;
        private final String revision;
        private final String name;
        private final String buildTime;

        public PlatformInfo(String appVersion, String edmVersion, String revision, String name, String buildTime) {
            this.appVersion = appVersion;
            this.edmVersion = edmVersion;
            this.revision = revision;
            this.name = name;
            this.buildTime = buildTime;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public String getEdmVersion() {
            return edmVersion;
        }

        public String getRevision() {
            return revision;
        }

        public String getName() {
            return name;
        }

        public String getBuildTime() {
            return buildTime;
        }
    }
}
