package eu.esens.espdvcd.validator.schematron;

import com.google.common.io.Files;
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
    // private File espdArtefact;
    private List<SchematronResourceSCH> schList;
    private List<SchematronResourceXSLT> xsltList;

    private ESPDSchematronValidator(Builder b) {
        // this.espdArtefact = b.espdArtefact;
        this.schList = b.schList;
        this.xsltList = b.xsltList;
        validateXMLViaAllSCH(b.espdArtefact);
        validateXMLViaAllXSLT(b.espdArtefact);
    }

    private void validateXMLViaAllSCH(File espdArtefact) {

        schList.forEach(sch -> {
            try {
                applyResultToValidationMessages(createValidationResult(espdArtefact, sch));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        });
    }

    private void validateXMLViaAllXSLT(File espdArtefact) {

        xsltList.forEach(xslt -> {
            try {
                applyResultToValidationMessages(createValidationResult(espdArtefact, xslt));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        });
    }

    private SchematronOutputType createValidationResult(File xmlArtefact, SchematronResourceXSLT xslt) throws Exception {
        return xslt.applySchematronValidationToSVRL(new StreamSource(new FileInputStream(xmlArtefact)));
    }

    private SchematronOutputType createValidationResult(File xmlArtefact, SchematronResourceSCH sch) throws Exception {
        return sch.applySchematronValidationToSVRL(new StreamSource(new FileInputStream(xmlArtefact)));
    }

    private void applyResultToValidationMessages(SchematronOutputType svrl) {

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

        private List<SchematronResourceSCH> schList;
        private List<SchematronResourceXSLT> xsltList;
        private File espdArtefact;

        public Builder(File espdArtefact) {
            this.espdArtefact = espdArtefact;
            schList = new ArrayList<>();
            xsltList = new ArrayList<>();
        }

        /**
         * Add a schematron file (.sch, .xslt), which will be used
         * during validation process of an ESPD XML Artefact.
         *
         * @param path The schematron file path
         * @return
         */
        public Builder addSchematron(String path) {
            if (path != null) {
                addIfValidOrThrowException(path);
            }
            return this;
        }

        private void addIfValidOrThrowException(String path) {

            switch (Files.getFileExtension(path)) {

                case "sch":
                    schList.add(createSchematronResourceSCH(path));
                    break;

                case "xslt":
                    xsltList.add(createSchematronResourceXSLT(path));
                    break;

                default:
                    throw new IllegalArgumentException("Error... Invalid schematron file extension."
                            + " Valid extensions are .sch, .xslt");
            }

        }

        private SchematronResourceSCH createSchematronResourceSCH(String schPath) {

            final SchematronResourceSCH sch = SchematronResourceSCH.fromClassPath(schPath);
            sch.setParameters(createDefaultParams());
            sch.setURIResolver(createTaxonomyURIResolver());

            if (!sch.isValidSchematron()) {
                throw new IllegalArgumentException("Error... Invalid Schematron: " + schPath);
            }

            return sch;
        }

        private SchematronResourceXSLT createSchematronResourceXSLT(String xsltPath) {

            final SchematronResourceXSLT xslt = SchematronResourceXSLT.fromClassPath(xsltPath);
            xslt.setParameters(createDefaultParams());
            xslt.setURIResolver(createTaxonomyURIResolver());

            if (!xslt.isValidSchematron()) {
                throw new IllegalArgumentException("Error... Invalid Schematron: " + xsltPath);
            }

            return xslt;
        }

        private Map<String, Object> createDefaultParams() {
            final Map<String, Object> params = new HashMap<>();
            params.put("allow-foreign", "true");
            return params;
        }

        private ClasspathURIResolver createTaxonomyURIResolver() {
            ClasspathURIResolver resolver = new ClasspathURIResolver();
            resolver.addResource("rules/v2/eu/ESPDRequest-2.0.2/xsl/ESPD-CriteriaTaxonomy-REGULATED.V2.0.2.xml");
            resolver.addResource("rules/v2/eu/ESPDRequest-2.0.2/xsl/ESPD-CriteriaTaxonomy-SELFCONTAINED.V2.0.2.xml");
            return resolver;
        }

        public ESPDSchematronValidator build() {
            return new ESPDSchematronValidator(this);
        }

    }

}
