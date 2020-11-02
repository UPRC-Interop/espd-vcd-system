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
package eu.esens.espdvcd.model.requirement.response.evidence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Criterion evidence group
 *
 */
public class EvidenceGroup implements Serializable {

    private static final long serialVersionUID = -2279415611032396712L;

    private String ID;

    private List<Evidence> evidences;

    public EvidenceGroup(String ID) {
        this.ID = ID;
    }

    public EvidenceGroup(String ID, List<Evidence> evidences) {
        this.ID = ID;
        this.evidences = evidences;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public List<Evidence> getEvidences() {
        if (evidences == null) {
            evidences = new ArrayList<>();
        }
        return evidences;
    }

    public void setEvidences(List<Evidence> evidences) {
        this.evidences = evidences;
    }
}
