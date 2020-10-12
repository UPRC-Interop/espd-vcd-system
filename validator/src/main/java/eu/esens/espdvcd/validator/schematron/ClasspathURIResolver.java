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
package eu.esens.espdvcd.validator.schematron;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.xml.transform.DefaultTransformURIResolver;
import com.helger.xml.transform.ResourceStreamSource;

import javax.annotation.Nullable;
import javax.xml.transform.Source;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClasspathURIResolver extends DefaultTransformURIResolver {

    private static final Logger LOGGER = Logger.getLogger(ClasspathURIResolver.class.getName());

    // Key = filename, value = fullPath
    private Map<String, String> resourceMap;

    public ClasspathURIResolver() {
        resourceMap = new HashMap<>();
    }

    public void addResource(String path) {
        resourceMap.put(Paths.get(path).getFileName().toString(), path);
    }

    @Nullable
    @Override
    protected Source internalResolve(String sHref, String sBase) {

        String path = resourceMap.containsKey(sHref)
                ? resourceMap.get(sHref)
                : sHref;

        final Source correctSource = new ResourceStreamSource(new ClassPathResource(path));
        LOGGER.log(Level.INFO, "URIResolver resolved " + sHref + " to " + correctSource);

        return correctSource;
    }

}
