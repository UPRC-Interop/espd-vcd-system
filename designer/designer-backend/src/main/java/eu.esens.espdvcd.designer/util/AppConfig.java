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
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;

public enum AppConfig {
    INSTANCE;

    private static final String defaultConfigFile = "defaults.conf";
    private final Config appConfig;

    AppConfig(){
        Config defaultConfig = ConfigFactory.parseResources(defaultConfigFile);
        appConfig = ConfigFactory.load()
                .withFallback(defaultConfig)
                .resolve();
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public boolean isDebugEnabled() {
        return appConfig.getBoolean("espd.designer.debug.enabled");
    }

    public int getServerPort() {
        return appConfig.getInt("espd.designer.port");
    }

    public EULanguageCodeEnum getDefaultLang() {
        return appConfig.getEnum(EULanguageCodeEnum.class, "espd.designer.lang");
    }

    public boolean isValidatorEnabled() {
        return appConfig.getBoolean("espd.designer.validator.enabled");
    }

    public boolean isArtefactDumpingEnabled() {
        return appConfig.getBoolean("espd.designer.artefactDump.enabled");
    }

    public String dumpIncomingArtefactsLocation() {
        return appConfig.getString("espd.designer.artefactDump.location");
    }

    public boolean isCORSEnabled() {
        return appConfig.getBoolean("espd.designer.CORS.enabled");
    }

    public boolean isEnchancedSecurityEnabled() {
        return appConfig.getBoolean("espd.designer.enchancedSecurity.enabled");
    }

    public boolean isFramingAllowed() {
        return appConfig.getBoolean("espd.designer.allowFraming");
    }

    public boolean isSelectionCriteriaPreselected() {
        return appConfig.getBoolean("espd.designer.criteria.selection.selected");
    }

    public int getMaxFileSize() {
        return appConfig.getInt("espd.designer.maxFileSizeUpload");
    }
}
