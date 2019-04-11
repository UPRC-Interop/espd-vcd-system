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
package eu.esens.espdvcd.retriever.criteria.resource.enums;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import eu.esens.espdvcd.codelist.enums.internal.ContractingOperatorEnum;

public enum CriteriaConfig {

    INSTANCE;

    private static final String CONFIG_FILE = "criteria.conf";
    public final Config criteriaConfig;

    CriteriaConfig() {
        Config config = ConfigFactory.parseResources(CONFIG_FILE);
        criteriaConfig = ConfigFactory.load()
                .withFallback(config)
                .resolve();
    }

    public static CriteriaConfig getInstance() {
        return INSTANCE;
    }

    public boolean isCompulsory(ContractingOperatorEnum operator, String ID) {
        return criteriaConfig.getStringList(  operator.getTag() + ".compulsory").stream()
                .anyMatch(id -> id.equals(ID));
    }

    public boolean isPreSelected(ContractingOperatorEnum operator, String ID) {
        return criteriaConfig.getStringList(operator.getTag() + ".pre_selected").stream()
                .anyMatch(id -> id.equals(ID));
    }

}
