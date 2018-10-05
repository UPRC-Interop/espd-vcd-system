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

import eu.esens.espdvcd.model.types.EmbeddedDocumentModelType;

/**
 * Not used.
 *
 */
public class EmbeddedDocumentImpl implements EmbeddedDocument {
    @Override
    public Long getId() {
        return null;
    }

    @Override
    public Long getParentId() {
        return null;
    }

    @Override
    public void setParentId(Long parentId) {

    }

    @Override
    public String getFileName() {
        return null;
    }

    @Override
    public String getUri() {
        return null;
    }

    @Override
    public Long getSize() {
        return null;
    }

    @Override
    public EmbeddedDocumentModelType getModelType() {
        return null;
    }
}
