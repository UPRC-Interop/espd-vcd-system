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
import eu.esens.espdvcd.codelist.CodelistsV2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public enum AppConfig {
    INSTANCE;

    private static final String defaultConfigFile = "defaults.conf";
    private final Config appConfig;

    AppConfig(){
        Config defaultConfig = ConfigFactory.parseResources(defaultConfigFile);
        Path f = Paths.get("./application.properties");
        appConfig = ConfigFactory.parseFile(f.toFile())
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

    public String getDefaultCountry() {
        String country = appConfig.getString("espd.designer.country");
        if (CodelistsV2.CountryIdentification.containsId(country)) {
            return country;
        } else {
            Logger.getLogger(AppConfig.class.getName())
                    .warning(String.format("Country %s does not exist in the codelists. Defaulting to AD.", country));
            return "AD";
        }
    }

    public String getDefaultCurrency() {
        String currency = appConfig.getString("espd.designer.currency");
        if (CodelistsV2.Currency.containsId(currency)) {
            return currency;
        } else {
            Logger.getLogger(AppConfig.class.getName())
                    .warning(String.format("Currency %s does not exist in the codelists. Defaulting to EUR.", currency));
            return "EUR";
        }
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

    public boolean isPurelyNationalPreselected() {
        return appConfig.getBoolean("espd.designer.criteria.purelyNational.selected");
    }

    public boolean isReductionPreselected() {
        return appConfig.getBoolean("espd.designer.criteria.reduction.selected");
    }

    public int getMaxFileSize() {
        return appConfig.getInt("espd.designer.maxFileSizeUpload");
    }
}
