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
package eu.esens.espdvcd.retriever.criteria.enums;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 27/11/2020.
 */
public enum TypeCodeConfig {

    INSTANCE;

    private static final String CONFIG_FILE = "typecodes.conf";
    public final Config typeCodesConfig;

    TypeCodeConfig() {
        Config config = ConfigFactory.parseResources(CONFIG_FILE);
        Path f = Paths.get("./" + CONFIG_FILE);
        typeCodesConfig = ConfigFactory.parseFile(f.toFile())
                .withFallback(config)
                .resolve();
    }

    public List<String> getAllRegulatedV1TypeCodeList() {
        return new ArrayList<>(typeCodesConfig.getStringList("taxonomy" + ".v1"));

    }

}
