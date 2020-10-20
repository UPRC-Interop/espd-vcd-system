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
package eu.esens.espdvcd.codelist.enums.ecertis;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 17/10/2020.
 */
public enum ECertisDomainIdEnum {

    /**
     * DomainName: Classical
     * DomainDesc: Public procurement sub-domain
     * ParentDomain: 1000
     * ParentDomainName: Procurement
     *
     */
    D1100("1100");

    private String id;

    ECertisDomainIdEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public enum ECertisScenarioIdEnum {

        /**
         * ScenarioName: Official procedure regarding the purchase of public service contracts
         * ScenarioDesc: Official Belgian procedure regarding the purchase of public service contracts
         * ParentIdScenario: 1100
         * CoverageArea: Belgium
         *
         */
        S1340("1340"),
        /**
         * ScenarioName: Works
         * ScenarioDesc: Scenario for classical procurement
         * ParentIdScenario: 1300
         * CoverageArea: Italy
         *
         */
        S1363("1363"),
        /**
         * ScenarioName: Service
         * ScenarioDesc: Scenario for classical procurement
         * ParentIdScenario: 1100
         * CoverageArea: Italy
         *
         */
        S1364("1364"),
        /**
         * ScenarioName: Supply
         * ScenarioDesc: Scenario for classical procurement
         * ParentIdScenario: 1200
         * CoverageArea: Italy
         *
         */
        S1365("1365"),
        /**
         * ScenarioName: Works
         * ScenarioDesc: Scenario for classical procurement
         * ParentIdScenario: 1300
         * CoverageArea: Greece
         *
         */
        S1360("1360"),
        /**
         * ScenarioName: Supply
         * ScenarioDesc: Scenario for classical procurement
         * ParentIdScenario: 1200
         * CoverageArea: Greece
         *
         */
        S1362("1362"),
        /**
         * ScenarioName: Service
         * ScenarioDesc: Scenario for classical procurement
         * ParentIdScenario: -
         * CoverageArea: EU
         *
         */
        S1100("1100"),
        /**
         * ScenarioName: Supply
         * ScenarioDesc: Scenario for classical procurement
         * ParentIdScenario: -
         * CoverageArea: EU
         *
         */
        S1200("1200"),
        /**
         * ScenarioName: Works
         * ScenarioDesc: Scenario for classical procurement
         * ParentIdScenario: -
         * CoverageArea: EU
         *
         */
        S1300("1300"),
        /**
         * ScenarioName: Service
         * ScenarioDesc: Scenario for classical procurement
         * ParentIdScenario: 1100
         * CoverageArea: Greece
         *
         */
        S1361("1361");

        private String id;

        ECertisScenarioIdEnum(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

}
