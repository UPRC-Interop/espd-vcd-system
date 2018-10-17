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
package eu.esens.espdvcd.builder;

import java.io.File;
import java.net.URI;

/**
 * Created by Ulf Lotzmann on 01/07/2016.
 */
public class EvidenceHelper {

    /**
     * Checks if the provided URI references a local file, and in that case transforms the absolute path to an URI
     * with a local path of the following scheme:
     * file:/evidences/[parent folder name as specified in absolute path]/[file name]
     *
     * @param absoluteURI
     * @return URI of ASiC evidence
     */
    public static String transformEvidenceURIFromLocalResourceToASiCResource(String absoluteURI) {
        try {
            File document = new File(URI.create(absoluteURI));
            if (document.exists()) {
                String asicPath = absoluteURI.substring(absoluteURI.lastIndexOf("/", absoluteURI.lastIndexOf("/") - 1));
                return "file:/evidences".concat(asicPath);
            }
        } catch (Exception ex){
            // do nothing
        }
        return absoluteURI;
    }

    /**
     * Creates a path string to be used as entry name for the ASiC.
     *
     * @param absoluteURI
     * @return ASiC entry name
     */
    public static String getASiCResourcePath(String absoluteURI) {
        String asicPath = absoluteURI.substring(absoluteURI.lastIndexOf("/", absoluteURI.lastIndexOf("/") - 1));
        String result = URI.create("evidences".concat(asicPath)).getPath();
        return result.startsWith("/") ? result.substring(1) : result;

    }

}
