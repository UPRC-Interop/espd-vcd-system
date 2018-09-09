package eu.esens.espdvcd.validator.schematron;

import com.helger.schematron.xslt.SchematronResourceSCH;
import com.helger.schematron.xslt.SchematronResourceXSLT;
import eu.esens.espdvcd.validator.ArtefactValidator;
import eu.esens.espdvcd.validator.ValidationResult;
import org.oclc.purl.dsdl.svrl.FailedAssert;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class ESPDSchematronValidator implements ArtefactValidator {

    private static final Logger LOGGER = Logger.getLogger(ESPDSchematronValidator.class.getName());

    private List<ValidationResult> validationMessages = new LinkedList<>();
    private File espdArtefact;
    private Set<String> schematronPathSet;

//    public ESPDSchematronValidator(InputStream is, String schPath) {
//        validateXMLViaSchematronOld(is, schPath);
//    }

//    public ESPDSchematronValidator(File artefact, String... schPath) {
//        Arrays.asList(schPath).forEach(path -> validateXMLViaSchematronOld(artefact, path));
//    }

    private ESPDSchematronValidator(Builder b) {
        this.espdArtefact = b.espdArtefact;
        this.schematronPathSet = b.schematronPathSet;
        validateXMLViaXSLTSchematron();
    }

//    static boolean validateXMLViaXSLTSchematron(InputStream is, String schPath) {
//        final SchematronResourceSCH schematron = SchematronResourceSCH.fromClassPath(schPath);
//        final Map<String, Object> aParams = new HashMap<>();
//        aParams.put("allow-foreign", "true");
//        schematron.setParameters(aParams);
//        boolean isValid = false;
//
//        if (!schematron.isValidSchematron()) {
//            throw new IllegalArgumentException(ERROR_INVALID_SCHEMATRON);
//        }
//
//        try {
//            isValid = schematron.getSchematronValidity(new StreamSource(is)).isValid();
//        } catch (Exception e) {
//            Logger.getLogger(ESPDSchematronValidator.class.getName()).log(Level.SEVERE, e.getMessage(), e);
//        }
//
//        return isValid;
//    }

    private void validateXMLViaXSLTSchematron() {
//        schematronPathSet.forEach(schPath -> validateXMLViaSchematronOld(espdArtefact, schPath));
        schematronPathSet.forEach(schPath -> validateXMLViaXSLT(espdArtefact, schPath));
    }

//    /**
//     * Validating given file against the specified schematron
//     *
//     * @param artefact the espd request/response artefact provided by the specified file
//     * @param schPath  the schematron file path
//     */
//    private void validateXMLViaSchematronOld(File artefact, String schPath) {
//        try {
//            validateXMLViaSchematronOld(new FileInputStream(artefact), schPath);
//        } catch (FileNotFoundException e) {
//            Logger.getLogger(ESPDSchematronValidator.class.getName()).log(Level.SEVERE, e.getMessage(), e);
//        }
//    }

    private SchematronOutputType createSVRLForXSLTValidation(File xmlArtefact, String xsltPath) throws Exception {

        final SchematronResourceXSLT xslt = SchematronResourceXSLT.fromClassPath(xsltPath);
        final Map<String, Object> params = new HashMap<>();
        params.put("allow-foreign", "true");
        xslt.setParameters(params);
        xslt.setURIResolver(createTaxonomyURIResolver());

        if (!xslt.isValidSchematron()) {
            throw new IllegalArgumentException("Error... Invalid Schematron");
        }

        return xslt.applySchematronValidationToSVRL(new StreamSource(new FileInputStream(xmlArtefact)));
    }

    private SchematronOutputType createSVRLForSchematronValidation(File xmlArtefact, String schPath) throws Exception {

        final SchematronResourceSCH schematron = SchematronResourceSCH.fromClassPath(schPath);
        final Map<String, Object> params = new HashMap<>();
        params.put("allow-foreign", "true");
        schematron.setParameters(params);
        schematron.setURIResolver(createTaxonomyURIResolver());

        if (!schematron.isValidSchematron()) {
            throw new IllegalArgumentException("Error... Invalid Schematron");
        }

        return schematron.applySchematronValidationToSVRL(new StreamSource(new FileInputStream(xmlArtefact)));
    }

    private ClasspathURIResolver createTaxonomyURIResolver() {
        ClasspathURIResolver resolver = new ClasspathURIResolver();
        resolver.addResource("rules/v2/eu/ESPDRequest-2.0.2/xsl/ESPD-CriteriaTaxonomy-REGULATED.V2.0.2.xml");
        resolver.addResource("rules/v2/eu/ESPDRequest-2.0.2/xsl/ESPD-CriteriaTaxonomy-SELFCONTAINED.V2.0.2.xml");
        return resolver;
    }

    private void validateXMLViaXSLT(File xmlArtefact, String xsltPath) {
        try {
            validateXMLViaSVRL(createSVRLForXSLTValidation(xmlArtefact, xsltPath));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void validateXMLViaSchematron(File xmlArtefact, String schPath) {
        try {
            validateXMLViaSVRL(createSVRLForSchematronValidation(xmlArtefact, schPath));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void validateXMLViaSVRL(SchematronOutputType svrl) {

        try {

            svrl.getActivePatternAndFiredRuleAndFailedAssert()
                    .stream()
                    .filter(value -> value instanceof FailedAssert) // discard all others and keep FailedAssert objects
                    .map(failedAssertObject -> (FailedAssert) failedAssertObject) // convert the object stream to FailedAssert stream
                    // loop through all and create for each one a new validation result
                    .forEach(fa -> validationMessages.add(new ValidationResult.Builder(fa.getId(), fa.getLocation(), fa.getText())
                            .flag(fa.getFlag())
                            .test(fa.getTest())
                            .role(fa.getRole())
                            .build()));
            // Print SVRL
            // new SVRLMarshaller().write(svrl, System.out);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            validationMessages.add(new ValidationResult.Builder(String.valueOf(validationMessages.size()),
                    "(line 0, column 0)", e.getMessage())
                    .flag("fatal")
                    .build());
        }

    }

//    /**
//     * Validating given file against the specified schematron.
//     *
//     * @param xmlArtefact The espd request/response artefact provided by the specified file.
//     * @param schPath     The schematron file path.
//     */
//    private void validateXMLViaSchematronOld(File xmlArtefact, String schPath) {
//
//        final SchematronResourceSCH schematron = SchematronResourceSCH.fromClassPath(schPath);
//        final Map<String, Object> aParams = new HashMap<>();
//        aParams.put("allow-foreign", "true");
//        schematron.setParameters(aParams);
//        schematron.setURIResolver(createTaxonomyURIResolver());
//
//        if (!schematron.isValidSchematron()) {
//            throw new IllegalArgumentException("Error... Invalid Schematron");
//        }
//
//        try {
//            SchematronOutputType svrl = schematron.applySchematronValidationToSVRL(new StreamSource(new FileInputStream(xmlArtefact)));
//
//            svrl.getActivePatternAndFiredRuleAndFailedAssert()
//                    .stream()
//                    .filter(value -> value instanceof FailedAssert) // discard all others and keep FailedAssert objects
//                    .map(failedAssertObject -> (FailedAssert) failedAssertObject) // convert the object stream to FailedAssert stream
//                    // loop through all and create for each one a new validation result
//                    .forEach(fa -> validationMessages.add(new ValidationResult.Builder(fa.getId(), fa.getLocation(), fa.getText())
//                            .flag(fa.getFlag())
//                            .test(fa.getTest())
//                            .role(fa.getRole())
//                            .build()));
//            // Print SVRL
//            // new SVRLMarshaller().write(svrl, System.out);
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, e.getMessage(), e);
//            validationMessages.add(new ValidationResult.Builder(String.valueOf(validationMessages.size()),
//                    "(line 0, column 0)", e.getMessage())
//                    .flag("fatal")
//                    .build());
//        }
//    }


    /**
     * Provides validation result.
     *
     * @return true, if validation was successful
     */
    @Override
    public boolean isValid() {

        boolean errorFound = validationMessages.stream()
                .filter(r -> !"warning".equals(r.getFlag()))
                .findFirst()
                .isPresent();

        return !errorFound;
    }

    /**
     * Provides list of validation events.
     *
     * @return list of events where validation was not successful
     */
    @Override
    public List<ValidationResult> getValidationMessages() {
        return validationMessages;
    }

    /**
     * Provides filtered list of validation events.
     * <p>
     * Possible flags are: warning, error and fatal
     *
     * @param flag, for which the list entries are filtered
     * @return filtered list of validation events
     */
    @Override
    public List<ValidationResult> getValidationMessagesFiltered(String flag) {
        return validationMessages
                .stream()
                .filter(validationResult -> Optional.ofNullable(validationResult.getFlag()).equals(flag))
                .collect(Collectors.toList());
    }

    public static class Builder {

        Set<String> schematronPathSet;
        File espdArtefact;

        public Builder(File espdArtefact) {
            this.espdArtefact = espdArtefact;
            schematronPathSet = new LinkedHashSet<>(10);
        }

        public Builder addSchematron(String path) {
            if (path != null) {
                schematronPathSet.add(path);
            }
            return this;
        }

        public ESPDSchematronValidator build() {
            return new ESPDSchematronValidator(this);
        }

    }

}
