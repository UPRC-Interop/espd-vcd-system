/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceConfig;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 12/10/2020.
 */
public class ResourceConfigTest {

    private static final Logger LOGGER = Logger.getLogger(ResourceConfigTest.class.getName());

    @Test
    public void testGetAllCriteriaURL() {

        if (ResourceConfig.INSTANCE.useProduction()) {
            Assert.assertEquals("https://ec.europa.eu/growth/tools-databases/ecertisrest/criteria",
                    ResourceConfig.INSTANCE.getECertisCriteriaURL());
        } else {
            Assert.assertEquals("https://webgate.acceptance.ec.europa.eu/growth/tools-databases/ecertisrest3/criteria",
                    ResourceConfig.INSTANCE.getECertisCriteriaURL());
        }
    }

    @Ignore
    @Test
    public void testGetResourcesPath() {

        LOGGER.info(ResourceConfig.INSTANCE.getTaxonomyRegulatedV210());
        LOGGER.info(ResourceConfig.INSTANCE.getTaxonomySelfContainedV210());
        LOGGER.info(ResourceConfig.INSTANCE.getESPDArtefactRegulatedV102());
        LOGGER.info(ResourceConfig.INSTANCE.getESPDArtefactRegulatedV210());

    }

}
