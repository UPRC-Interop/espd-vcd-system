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
package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.EmbeddedDocumentModelType;

import java.io.Serializable;

/**
 * This interface is in charge to provide {@link EmbeddedDocument} data.
 *
 */
public interface EmbeddedDocument extends Serializable {

    /**
     * @return The ID of the {@link EmbeddedDocument}.
     */
    Long getId();


    /**
     * @return The ID of the parent owner ({@link ESPDResponse} or {@link VCDResponse}).
     */
    Long getParentId();

    /**
     * @param parentId
     */
    void setParentId(Long parentId);

    /**
     * @return The filename of the document
     * uniquely identifies document in the {@link ESPDResponse} or {@link VCDResponse}.
     */
    String getFileName();

    /**
     * @return The URI of the document
     * uniquely identifies document in the {@link ESPDResponse} or {@link VCDResponse}.
     */
    String getUri();

    /**
     * @return The file size of the document.
     */
    Long getSize();


    /**
     * @return The model type of the embedded document.
     */
    EmbeddedDocumentModelType getModelType();
}
