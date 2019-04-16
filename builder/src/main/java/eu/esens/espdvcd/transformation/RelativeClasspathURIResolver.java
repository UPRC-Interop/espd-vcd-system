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
package eu.esens.espdvcd.transformation;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.net.URL;
import java.util.MissingResourceException;

import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

/**
 * <ul>
 * <li>Dieser URIResolver loest die relative positionierten XSD, die per '<xsl:import href="xxx.xsl"/> integriert werden, auf und laedt diese mit dem
 * Classloader.</li>
 * <li>Dies ist notwendig, wenn die Dateien als "file://..." geladen werden und somit z.B. aus den Tests nicht gefunden wird.</li>
 * </ul>
 */
public class RelativeClasspathURIResolver implements URIResolver {

    private final static String BASE_PATH = "eu/esens/espdvcd/";
    private final static String SLASH = "/";
    private final static String DOT_SLASH = "./";
    private static final String NAVIGATE_TO_PARENT = "../";

    @Override
    public Source resolve(String href, String base) {

        String packageWithFileRef = substringAfter(base, BASE_PATH);

        String packageReference = substringBeforeLast(packageWithFileRef, SLASH);

        final String resourceName = getResourceName(packageReference, href);
        URL resource = getClass().getClassLoader().getResource(resourceName);
        if (resource == null) {
            throw new MissingResourceException("Ressource konnte nicht geladen werden", getClass().getCanonicalName(), resourceName);
        }
        return new StreamSource(resource.toExternalForm());
    }

    private String getResourceName(String packageReference, String href) {

        String fileNameResult = href;
        String packageReferenceResult = packageReference;

        while (fileNameResult.contains(NAVIGATE_TO_PARENT)) {
            packageReferenceResult = substringBeforeLast(packageReferenceResult, SLASH);
            fileNameResult = substringAfter(fileNameResult, NAVIGATE_TO_PARENT);
        }
        while (fileNameResult.contains(DOT_SLASH)) {
            fileNameResult = substringAfter(fileNameResult, DOT_SLASH);
        }
        return BASE_PATH.concat(packageReferenceResult).concat(SLASH).concat(fileNameResult);
    }
}
