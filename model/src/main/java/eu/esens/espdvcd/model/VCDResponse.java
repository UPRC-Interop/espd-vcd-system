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

import eu.esens.espdvcd.model.types.VCDResponseModelType;

import java.io.Serializable;
import java.util.List;

/**
 * Not used.
 *
 */
public interface VCDResponse extends Serializable {
    /**
     * @return The ID of the {@link VCDResponse}.
     */
    Long getId();

    /**
     * Available model types are in VCDResponseModelType.
     *
     * @return the model type
     */
    VCDResponseModelType getModelType();

    /**
     * @param modelType
     */
    void setModelType(VCDResponseModelType modelType);

    /**
     * @return list of {@link EmbeddedDocument} of the {@link VCDResponse}.
     */
    List<EmbeddedDocument> getEmbeddedDocuments();
}
