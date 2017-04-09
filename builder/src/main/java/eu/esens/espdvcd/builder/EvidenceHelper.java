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
