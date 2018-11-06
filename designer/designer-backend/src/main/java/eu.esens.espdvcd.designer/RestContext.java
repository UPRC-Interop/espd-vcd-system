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
package eu.esens.espdvcd.designer;

import eu.esens.espdvcd.designer.endpoint.Endpoint;
import spark.Service;

import java.util.logging.Logger;

public class RestContext {

    private static final Logger logger = Logger.getLogger(RestContext.class.getName());

    private final Service spark;

    private final String basePath;

    public RestContext(String basePath, Service spark) {
        this.basePath = basePath;
        this.spark = spark;
    }

    public void addEndpoint(Endpoint endpoint) {

        endpoint.configure(spark, basePath);
        logger.info("REST endpoints registered for "+ endpoint.getClass().getName());
    }

    public void addEndpointWithPath(Endpoint endpoint, String path) {

        endpoint.configure(spark, basePath + path);
        logger.info("REST endpoints registered for "+ endpoint.getClass().getName());
    }

}
