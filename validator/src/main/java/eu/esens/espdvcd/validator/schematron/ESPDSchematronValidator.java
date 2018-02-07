package eu.esens.espdvcd.validator.schematron;

import com.helger.commons.state.ESuccess;
import com.helger.jaxb.IJAXBWriter;
import com.helger.schematron.svrl.SVRLMarshaller;
import com.helger.schematron.xslt.SchematronResourceSCH;
import eu.esens.espdvcd.validator.ArtifactValidator;
import jdk.internal.util.xml.impl.Input;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author konstantinos
 */
public class ESPDSchematronValidator implements ArtifactValidator {

    private static final String ERROR_INVALID_SCHEMATRON = "Invalid Schematron";
    private List<String> validationMessages = new LinkedList<>();
//    private static Map<String, SchematronResourceSCH> schematronResourceCache = new LinkedHashMap<>();

    public ESPDSchematronValidator(InputStream is, String schPath) {
        // validateXMLViaXSLTSchematron(is, schPath);
        validateXMLViaXSLTSchematronFull(is, schPath);
    }

    private void validateXMLViaXSLTSchematron(InputStream is, String schPath) {
//        final SchematronResourceSCH schematron = Optional.ofNullable(schematronResourceCache.get(schPath))
//                .orElseGet(() -> loadAndCacheSch(schPath));
        final SchematronResourceSCH schematron = SchematronResourceSCH.fromFile(schPath);

        if (!schematron.isValidSchematron()) {
            throw new IllegalArgumentException(ERROR_INVALID_SCHEMATRON);
        }

        try {
            boolean valid = schematron.getSchematronValidity(new StreamSource(is)).isValid();

            if (!valid) {
                validationMessages.add("invalid");
            }

        } catch (Exception e) {
            validationMessages.add(e.getMessage());
        }
    }

    private void validateXMLViaXSLTSchematronFull(InputStream is, String schPath) {
//        final SchematronResourceSCH schematron = Optional.ofNullable(schematronResourceCache.get(schPath))
//                .orElseGet(() -> loadAndCacheSch(schPath));
        final SchematronResourceSCH schematron = SchematronResourceSCH.fromFile(schPath);

        if (!schematron.isValidSchematron()) {
            throw new IllegalArgumentException(ERROR_INVALID_SCHEMATRON);
        }

        try {
            SchematronOutputType svrl = schematron.applySchematronValidationToSVRL(new StreamSource(is));
            new SVRLMarshaller().write(svrl, System.out).isSuccess();

        } catch (Exception e) {
            e.printStackTrace();
            validationMessages.add(e.getMessage());
        }
    }

//    private SchematronResourceSCH loadAndCacheSch(String schPath) {
//        System.out.println("Caching " + schPath);
//        schematronResourceCache.put(schPath, SchematronResourceSCH.fromFile(schPath));
//        System.out.println(schPath + " cached");
//        return schematronResourceCache.get(schPath);
//    }

    @Override
    public boolean isValid() {
        return validationMessages.isEmpty();
    }

    @Override
    public List<String> getValidationMessages() {
        return validationMessages;
    }

    @Override
    public List<String> getValidationMessagesFiltered(String keyWord) {
        return validationMessages.stream().filter(s -> s.contains(keyWord)).collect(Collectors.toList());
    }

}
