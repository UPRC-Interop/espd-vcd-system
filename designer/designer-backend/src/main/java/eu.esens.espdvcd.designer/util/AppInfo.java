/**
 * Copyright 2016-2019 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
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
