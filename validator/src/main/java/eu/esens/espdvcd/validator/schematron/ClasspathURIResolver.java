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
