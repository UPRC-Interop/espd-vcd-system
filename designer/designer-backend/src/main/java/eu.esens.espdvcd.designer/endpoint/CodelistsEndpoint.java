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
package eu.esens.espdvcd.designer.endpoint;

import eu.esens.espdvcd.designer.exception.LanguageNotExistsException;
import eu.esens.espdvcd.designer.service.CodelistsService;
import eu.esens.espdvcd.designer.util.Errors;
import eu.esens.espdvcd.designer.util.JsonUtil;
import spark.Service;

public class CodelistsEndpoint extends Endpoint {

    private final CodelistsService service;

    public CodelistsEndpoint(CodelistsService service) {
        this.service = service;
    }

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath + "/codelists", () -> {
            spark.get("/:codelist", ((request, response) -> {
                response.header("Content-Type", "application/json");
                try {
                    return service.getCodelist(request.params("codelist"));
                } catch (IllegalArgumentException e) {
                    response.status(404);
                    return Errors.codelistNotFoundError();
                }
            }), JsonUtil.json());

            spark.get("/:codelist/lang/:lang", (request, response) -> {
                response.header("Content-Type", "application/json");
                try {
                    return service.getTranslatedCodelist(request.params("codelist"), request.params("lang"));
                } catch (IllegalArgumentException e) {
                    response.status(404);
                    return Errors.codelistNotFoundError();
                } catch (UnsupportedOperationException e) {
                    response.status(406);
                    return Errors.notAcceptableError("Translation is not supported for V1 Codelists.");
                } catch (LanguageNotExistsException e){
                    response.status(404);
                    return Errors.notFoundError(e.getMessage());
                }
            }, JsonUtil.json());

            spark.get("", (request, response) -> {
                response.header("Content-Type", "application/json");
                return service.getAvailableCodelists();
            }, JsonUtil.json());
        });

        spark.after((req, res) -> res.type("application/json"));

    }
}
