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
package eu.esens.espdvcd.model;

/**
 * Created by Ulf Lotzmann on 05/03/2016.
 */
public class CompulsoryCriterion extends SelectableCriterion {

    private static final long serialVersionUID = -3642428067998066099L;

    public CompulsoryCriterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference) {
        super(ID, typeCode, name, description, legislationReference);
        selected = true;
    }

    /**
     *
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = true;
    }
}
