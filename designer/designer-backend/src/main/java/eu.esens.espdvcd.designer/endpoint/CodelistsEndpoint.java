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
package eu.esens.espdvcd.designer.endpoint;

import eu.esens.espdvcd.designer.service.CodelistsService;
import eu.esens.espdvcd.designer.util.ErrorResponse;
import spark.Service;

public class CodelistsEndpoint extends Endpoint {

    private final CodelistsService service;
    private final String CODELIST_ERROR = "Codelist not found.";

    public CodelistsEndpoint(CodelistsService service) {
        this.service = service;
    }

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath + "/codelists", () -> {
            spark.get("/:codelist", ((request, response) -> {
                response.header("Content-Type", "application/json");
                try {
                    return WRITER.writeValueAsString(service.getCodelist(request.params("codelist")));
                } catch (IllegalArgumentException e) {
                    response.status(404);
                    return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(404, CODELIST_ERROR).build());
                }
            }));

            spark.get("/:codelist/:lang", (request, response) -> {
                response.header("Content-Type", "application/json");
                try {
                    return WRITER.writeValueAsString(service.getTranslatedCodelist(request.params("codelist"), request.params("lang")));
                } catch (IllegalArgumentException e) {
                    response.status(404);
                    return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(404, CODELIST_ERROR).build());
                } catch (UnsupportedOperationException e) {
                    response.status(406);
                    return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(404, "Translation is not supported for V1 Codelists.").build());
                }
            });

            spark.get("/", (request, response) -> {
                response.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(service.getAvailableCodelists());
            });

            spark.get("", (request, response) -> {
                response.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(service.getAvailableCodelists());
            });
        });
    }
}
