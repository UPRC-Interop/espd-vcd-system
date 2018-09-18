/**
 * Copyright 2016-2018 University of Piraeus Research Center
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
package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.requirement.RequirementGroup;
import java.util.List;

/**
 * Created by Ulf Lotzmann on 05/03/2016.
 */
public class SelectableCriterion extends Criterion {

    private static final long serialVersionUID = -4642428067998066099L;

    protected boolean selected;
    
    public SelectableCriterion() {
        super();
    }

    public SelectableCriterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference) {
        super(ID, typeCode, name, description, legislationReference);
        selected = false;
    }
    
    public SelectableCriterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference, List<RequirementGroup> requirementGroups) {
        super(ID, typeCode, name, description, legislationReference, requirementGroups);
        selected = false;
    }

    public SelectableCriterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference, List<RequirementGroup> requirementGroups, boolean selected) {
        super(ID, typeCode, name, description, legislationReference, requirementGroups);
        this.selected = selected;
    }

     /**
     *
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
       
}
