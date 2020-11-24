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
package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

/**
 * Quantity response (integer)
 *
 * (part of Period declaration fulfilment)
 *
 * Created by Ulf Lotzmann on 22/03/2016.
 */
public class QuantityIntegerResponse extends Response implements Serializable {

    private static final long serialVersionUID = 8321476080155936015L;

    /**
     * Criterion fulfillment quantity
     * <p>
     * UOM should be stated by using recommendation 20 v10 Declared quantity that fulfills this criterion.
     * <p>
     * Data type: Quantity<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-163<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Quantity<br>
     */
    private int quantity;

    public QuantityIntegerResponse() {
    }

    public QuantityIntegerResponse(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
