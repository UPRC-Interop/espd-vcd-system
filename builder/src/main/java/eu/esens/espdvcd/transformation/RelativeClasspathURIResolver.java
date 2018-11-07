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
