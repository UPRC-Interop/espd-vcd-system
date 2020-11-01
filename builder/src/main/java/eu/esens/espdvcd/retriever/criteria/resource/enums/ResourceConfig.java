/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria.resource.enums;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 12/10/2020.
 */
public enum ResourceConfig {

    INSTANCE;

    private final String CONFIG_FILE = "resource.conf";
    private final Config config;

    ResourceConfig() {
        Config config = ConfigFactory.parseResources(CONFIG_FILE);
        Path f = Paths.get("./" + CONFIG_FILE);
        this.config =
                ConfigFactory.parseFile(f.toFile())
                        .withFallback(config)
                        .resolve();
    }

    public boolean useProduction() {
        return getECertisConfig().getBoolean("use-production");
    }

    public boolean useBackwardsCompatibleECertis() {
        return !useProduction() && getAcceptanceConfig().getBoolean("use-backwards-compatible");
    }

    public String getECertisHost() {
        return useProduction()
                ? getProductionConfig().getString("host")
                : getAcceptanceConfig().getString("host");
    }

    /**
     * http or https
     *
     * @return
     */
    public String getECertisScheme() {
        return useProduction()
                ? getProductionConfig().getString("scheme")
                : getAcceptanceConfig().getString("scheme");
    }

    public String getECertisCriteriaPath() {
        return useProduction()
                ? getProductionConfig().getString("criteria")
                : getAcceptanceConfig().getString("criteria");
    }

    private Config getECertisJsonElement() {
        return useProduction()
                ? getProductionConfig().getConfig("jsonelements")
                : getAcceptanceConfig().getConfig("jsonelements");
    }

    public String getECertisCriterionJsonElement() {
        return getECertisJsonElement().getString("criterion");
    }

    public String getECertisIDJsonElement() {
        return getECertisJsonElement().getString("id");
    }

    public String getECertisValueJsonElement() {
        return getECertisJsonElement().getString("value");
    }

    public String getECertisNameJsonElement() {
        return getECertisJsonElement().getString("name");
    }

    public String getECertisDescriptionJsonElement() {
        return getECertisJsonElement().getString("description");
    }

    public String getTaxonomyRegulatedV210() {
        return getTaxonomyRegulatedConfig().getConfig("v210").getString("path");
    }

    public String getTaxonomySelfContainedV210() {
        return getTaxonomySelfContainedConfig().getConfig("v210").getString("path");
    }

    public String getESPDArtefactRegulatedV102() {
        return getESPDArtefactRegulatedConfig().getConfig("v102").getString("path");
    }

    public String getESPDArtefactRegulatedV210() {
        return getESPDArtefactRegulatedConfig().getConfig("v210").getString("path");
    }

    private Config getProductionConfig() {
        return getECertisConfig().getConfig("production");
    }

    private Config getAcceptanceConfig() {
        return getECertisConfig().getConfig("acceptance");
    }

    private Config getTaxonomyRegulatedConfig() {
        return getTaxonomyConfig().getConfig("regulated");
    }

    private Config getTaxonomySelfContainedConfig() {
        return getTaxonomyConfig().getConfig("selfcontained");
    }

    private Config getESPDArtefactRegulatedConfig() {
        return getESPDArtefactConfig().getConfig("regulated");
    }

    private Config getTaxonomyConfig() {
        return config.getConfig("taxonomy");
    }

    private Config getESPDArtefactConfig() {
        return config.getConfig("espdartefact");
    }

    private Config getECertisConfig() {
        return config.getConfig("ecertis");
    }

}
