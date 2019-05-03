/**
 * Copyright 2016-2019 University of Piraeus Research Center
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
package eu.esens.espdvcd.codelist.enums.internal;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum PropertyKeyConfigEnum {

    INSTANCE;

    private static final String CONFIG_FILE = "propertykey.conf";
    public final Config criteriaConfig;

    PropertyKeyConfigEnum() {
        Config config = ConfigFactory.parseResources(CONFIG_FILE);
        Path f = Paths.get("./" + CONFIG_FILE);
        criteriaConfig = ConfigFactory.parseFile(f.toFile())
                .withFallback(config)
                .resolve();
    }

    public static PropertyKeyConfigEnum getInstance() {
        return INSTANCE;
    }

    /**
     * Get criterion's name property key.
     *
     * @param ID The criterion ID
     * @return
     */
    public String getCriterionNamePropertyKey(String ID) {
        return criteriaConfig.getString("criteria." + ID + ".name");
    }

    /**
     * Get criterion's description property key.
     *
     * @param ID The criterion ID
     * @return
     */
    public String getCriterionDescriptionPropertyKey(String ID) {
        return criteriaConfig.getString("criteria." + ID + ".desc");
    }

    /**
     * Get requirement's description property key
     *
     * @param desc The requirement's description in english.
     * @return
     */
    public String getRequirementDescriptionPropertyKey(String desc) {
        return criteriaConfig.getString("rq-desc." + desc);
    }

}
